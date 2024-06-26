package edu.northeastern.recipeasy.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import edu.northeastern.recipeasy.domain.ListItem;
import edu.northeastern.recipeasy.domain.Recipe;
import edu.northeastern.recipeasy.ItemRecyclerView.ListItemViewAdapter;
import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.utils.PhotosUtil;

public class AddRecipeActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView ingredientsRecyclerView;
    private ArrayList<ListItem> ingredientList;
    private ListItemViewAdapter ingredientAdapter;
    private RecyclerView recipeRecyclerView;
    private ArrayList<ListItem> recipeStepsList;
    private ListItemViewAdapter recipeAdapter;
    private ActivityResultLauncher<Uri> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ImageView dishPicture;
    private Button cameraButton;
    private Button galleryButton;
    private ProgressBar progressBar;
    private ScrollView scrollView;
    private Uri dishPictureUri;
    private static final int CAMERA_REQUEST_CODE = 101;
    private static final int GALLERY_REQUEST_CODE = 102;
    private boolean restoreIngredients = false;
    private boolean restoreRecipe = false;
    private boolean restorePicture = false;
    private String username;
    private Recipe newRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        if (savedInstanceState != null){
            if (savedInstanceState.containsKey("ingredientListSize")) {
                restoreIngredients = true;
            }
            if (savedInstanceState.containsKey("recipeStepsListSize")) {
                restoreRecipe = true;
            }
        }
        username = getIntent().getStringExtra("username");
        setUp();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddRecipeActivity.this);
                builder.setTitle("Go back?");
                builder.setMessage("Are you sure you want to go back? You will lose your progress on this recipe.");
                builder.setPositiveButton("YES", (dialog, which) -> finish());
                builder.setNegativeButton("NO", (dialog, which) -> dialog.cancel());
                builder.show();
            }
        });


    }

    private void setUp(){
        dishPicture = findViewById(R.id.pictureOfDishId);
        cameraButton = findViewById(R.id.cameraButtonId);
        galleryButton = findViewById(R.id.galleryButtonId);
        dishPictureUri = PhotosUtil.makeImageUri(getApplicationContext());
        progressBar = findViewById(R.id.progressBarID);
        scrollView = findViewById(R.id.scrollViewId);
        initializeIngredientsRecycler();
        initializeRecipeRecycler();
        initializeCamera();
        initializeGallery();
    }


    private void initializeIngredientsRecycler(){
        ingredientsRecyclerView = findViewById(R.id.ingredientsRecyclerViewId);
        ingredientsRecyclerView.setHasFixedSize(true);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientList = new ArrayList<>();
        if (!restoreIngredients){
            ingredientList.add(new ListItem("", "Ingredient"));
        }

        ingredientAdapter = new ListItemViewAdapter(ingredientList);
        ingredientsRecyclerView.setAdapter(ingredientAdapter);
    }

    private void initializeRecipeRecycler(){
        recipeRecyclerView = findViewById(R.id.recipeRecyclerViewId);
        recipeRecyclerView.setHasFixedSize(true);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeStepsList = new ArrayList<>();
        if (!restoreRecipe){
            recipeStepsList.add(new ListItem("", "Step"));
        }
        recipeAdapter = new ListItemViewAdapter(recipeStepsList);
        recipeRecyclerView.setAdapter(recipeAdapter);
    }

    @Override
    public void onClick(View v) {
        int clickedId = v.getId();
        if (clickedId == R.id.addNewIngredientButton) {
            addNewIngredient();
        } else if (clickedId == R.id.addNewRecipeStepButton) {
            addNewStep();
        } else if (clickedId == R.id.cameraButtonId) {
            clickedCameraButton();
        } else if (clickedId == R.id.galleryButtonId) {
            clickedGallery();
        } else if (clickedId == R.id.submitButtonId) {
            submitRecipe();
        }
    }

    public void addNewIngredient() {
        int position = ingredientList.size() - 1;
        ListItem lastItem = ingredientList.get(position);
        if (!lastItem.getItem().trim().isEmpty()) {
            ingredientList.add(new ListItem("", "Ingredient"));
            position = ingredientList.size() - 1;
            ingredientAdapter.notifyItemInserted(position);
            ingredientsRecyclerView.scrollToPosition(position);
        } else {
            showBlankListItemToast();
        }
    }

    public void addNewStep() {
        int position = recipeStepsList.size() - 1;
        ListItem lastItem = recipeStepsList.get(position);
        if (!lastItem.getItem().trim().isEmpty()) {
            recipeStepsList.add(new ListItem("", "Step"));
            position = recipeStepsList.size() - 1;
            recipeAdapter.notifyItemInserted(position);
            recipeRecyclerView.scrollToPosition(position);
        } else {
            showBlankListItemToast();
        }
    }

    private void showBlankListItemToast() {
        Toast.makeText(this, "Item cannot be left blank", Toast.LENGTH_SHORT).show();
    }

    public void clickedCameraButton() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            cameraLauncher.launch(dishPictureUri);
        }
    }

    public void clickedGallery() {
        if (Build.VERSION.SDK_INT < 33) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST_CODE);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryLauncher.launch(intent);
            }

        } else {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES}, GALLERY_REQUEST_CODE);
            } else {
                Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
                galleryLauncher.launch(intent);
            }
        }
    }

    private void setLoadingViewOn() {
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.INVISIBLE);
    }

    private void setLoadingViewOff() {
        progressBar.setVisibility(View.INVISIBLE);
        scrollView.setVisibility(View.VISIBLE);
    }

    public void submitRecipe() {
        this.setLoadingViewOn();
        String[] formedStrings = formListItemStrings();

        EditText dish = findViewById(R.id.editDishAddRecipe);
        RangeSlider calorieSlider = findViewById(R.id.rangeSliderCaloriesIdAddNewRecipe);
        EditText cookTime = findViewById(R.id.cookTimeId);
        EditText prepTime = findViewById(R.id.prepTimeId);
        EditText serving = findViewById(R.id.servingSizeIdAddNewRecipe);
        Spinner cuisineSpinner = findViewById(R.id.spinnerAddNewCuisine);
        CheckBox vegetarian = findViewById(R.id.vegetarianBoxId);
        CheckBox vegan = findViewById(R.id.veganBoxId);
        CheckBox glutenFree = findViewById(R.id.glutenFreeBoxId);

        String dishName = dish.getText().toString();
        String ingredients =  formedStrings[0];
        String recipeSteps = formedStrings[1];
        String cuisine = cuisineSpinner.getSelectedItem().toString();

        if (dishName.trim().isEmpty() || ingredients.trim().isEmpty() || recipeSteps.trim().isEmpty()) {
            Toast.makeText(this, "Dish Name, Ingredients, and Recipe Steps are required!", Toast.LENGTH_SHORT).show();
            this.setLoadingViewOff();
            return;
        }

        if (cuisine.equals("Select a Cuisine...")) {
            cuisine = "N/A";
        }

        int calories = Math.round(calorieSlider.getValues().get(0));

        int cookTimeMinutes = 0;
        if (!cookTime.getText().toString().isEmpty()) {
            cookTimeMinutes = Integer.parseInt(cookTime.getText().toString());
        }

        int prepTimeMinutes = 0;
        if (!prepTime.getText().toString().isEmpty()) {
            prepTimeMinutes = Integer.parseInt(prepTime.getText().toString());
        }

        int servingSizes = 0;
        if (!serving.getText().toString().isEmpty()) {
            servingSizes = Integer.parseInt(serving.getText().toString());
        }


        // create recipe object here with pictureURL as ""
         this.newRecipe = new Recipe(username, dishName, cuisine, prepTimeMinutes, cookTimeMinutes,
         servingSizes,vegetarian.isChecked(),vegan.isChecked(), glutenFree.isChecked(),
                 ingredients, recipeSteps, "", calories, 0);

         uploadPhotoAndRecipe();
    }

    private void uploadPhotoAndRecipe() {
        if (restorePicture) {
            Bitmap bitmap = PhotosUtil.uriToBitmap(dishPictureUri, getContentResolver());

            StorageReference imageRef = FirebaseStorage.getInstance().getReference()
                    .child("recipePics/" + username + "/" + new Date() + ".jpg");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            assert bitmap != null;
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] data = stream.toByteArray();

            UploadTask upload = imageRef.putBytes(data);
            upload.addOnFailureListener(exception -> {
                this.setLoadingViewOff();
                Snackbar.make(Objects.requireNonNull(this.getCurrentFocus()), "Photo Upload Failed", Snackbar.LENGTH_LONG)
                        .setAction("RETRY", v -> uploadPhotoAndRecipe()).show();
            }).addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String pictureUrl = uri.toString();
                    newRecipe.setPhotoPath(pictureUrl);
                    uploadRecipe(newRecipe);
                });
            });
        } else {
            uploadRecipe(newRecipe);
        }
    }


    private void uploadRecipe(Recipe recipe) {
        DatabaseReference recipesRef = FirebaseDatabase.getInstance().getReference().child("recipes");
        DatabaseReference newRecipeRef = recipesRef.push();
        updateUserRecipes(newRecipeRef.getKey());

        newRecipeRef.setValue(recipe)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddRecipeActivity.this, "Recipe Uploaded", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddRecipeActivity.this, "Failed to Upload Recipe", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUserRecipes(String recipeId) {
        DatabaseReference authorRef = FirebaseDatabase.getInstance().getReference().child("users").child(username);
        authorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // author doesn't exist
                    return;
                }
                DatabaseReference writtenRecipes = authorRef.child("recipeIdList").push();
                writtenRecipes.setValue(recipeId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private String[] formListItemStrings() {
        StringBuilder ingredients = new StringBuilder();
        StringBuilder recipeSteps = new StringBuilder();

        for (int i = 0; i < ingredientList.size(); i++) {
            ListItem item = ingredientList.get(i);
            if (!item.getItem().trim().equals("")) {
                ingredients.append(item.getItem()).append(";;");
            }
        }
        for (int i = 0; i < recipeStepsList.size(); i++) {
            ListItem stepItem = recipeStepsList.get(i);
            if (!stepItem.getItem().trim().equals("")) {
                recipeSteps.append(i + 1).append(". ")
                        .append(stepItem.getItem()).append(";;");
            }
        }
        return new String[]{ingredients.toString(), recipeSteps.toString()};
    }


    private void initializeCamera() {
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                result -> {
                    try {
                        if (result) {
                            dishPicture.setImageURI(null);
                            dishPicture.setImageURI(dishPictureUri);
                            galleryButton.setVisibility(View.GONE);
                            restorePicture = true;
                        }
                    } catch (Exception e) {
                    }
                }
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraLauncher.launch(dishPictureUri);
            } else {
                Toast.makeText(this, "Please allow camera permissions", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT < 33){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryLauncher.launch(intent);
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
                    galleryLauncher.launch(intent);
                }
            } else {
                Toast.makeText(this, "Please allow photo permissions", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void initializeGallery() {
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    try {
                        if (result.getResultCode() == RESULT_OK) {
                            dishPicture.setImageURI(null);
                            assert result.getData() != null;
                            dishPictureUri = result.getData().getData();
                            dishPicture.setImageURI(dishPictureUri);
                            cameraButton.setVisibility(View.GONE);
                            restorePicture = true;
                        }
                    } catch (Exception e) {
                    }
                }
        );
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (ingredientList.size() > 0){
            outState.putInt("ingredientListSize", ingredientList.size());
            for (int i = 0; i < ingredientList.size(); i++) {
                outState.putString("ingredient"+ i, ingredientList.get(i).getItem());
            }
        }
        if (recipeStepsList.size() > 0){
            outState.putInt("recipeStepsListSize", recipeStepsList.size());
            for (int i = 0; i < recipeStepsList.size(); i++) {
                outState.putString("recipeStep"+ i, recipeStepsList.get(i).getItem());
            }
        }
        if (restorePicture){
            outState.putString("dishPicture", dishPictureUri.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("ingredientListSize")) {
            if (ingredientList == null || ingredientList.size() <= 1) {
                int size = savedInstanceState.getInt("ingredientListSize");
                for (int i = 0; i < size; i++) {
                    String ingredient = savedInstanceState.getString("ingredient" + i);
                    ingredientList.add(new ListItem(ingredient, "Ingredient"));
                }
            }
        }
        if (savedInstanceState.containsKey("recipeStepsListSize")) {
            if (recipeStepsList == null || recipeStepsList.size() <= 1) {
                int size = savedInstanceState.getInt("recipeStepsListSize");
                for (int i = 0; i < size; i++) {
                    String recipeStep = savedInstanceState.getString("recipeStep" + i);
                    recipeStepsList.add(new ListItem(recipeStep, "Step"));
                }
            }
        }
        if (savedInstanceState.containsKey("dishPicture")) {
            dishPictureUri = Uri.parse(savedInstanceState.getString("dishPicture"));
            dishPicture.setImageURI(dishPictureUri);
        }
    }
}