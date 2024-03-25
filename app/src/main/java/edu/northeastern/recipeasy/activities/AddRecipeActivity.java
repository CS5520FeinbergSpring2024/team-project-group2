package edu.northeastern.recipeasy.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.slider.RangeSlider;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import edu.northeastern.recipeasy.domain.ListItem;
import edu.northeastern.recipeasy.ItemRecyclerView.ListItemViewAdapter;
import edu.northeastern.recipeasy.R;

public class AddRecipeActivity extends AppCompatActivity {
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
    private Uri dishPictureUri;
    private static final int CAMERA_REQUEST_CODE = 101;
    private static final int GALLERY_REQUEST_CODE = 102;
    private boolean restoreIngredients = false;
    private boolean restoreRecipe = false;
    private boolean restorePicture = false;

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
        setUp();
    }

    private void setUp(){
        dishPicture = findViewById(R.id.pictureOfDishId);
        cameraButton = findViewById(R.id.cameraButtonId);
        galleryButton = findViewById(R.id.galleryButtonId);
        dishPictureUri = makeUri();

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

    public void clickedAddNewStep(View view){
        int clickedId = view.getId();
        if (clickedId == R.id.addNewIngredientButton) {
            int position = ingredientList.size() - 1;
            ListItem lastItem = ingredientList.get(position);
            if (!lastItem.getItem().trim().isEmpty()) {
                ingredientList.add(new ListItem("", "Ingredient"));
                position = ingredientList.size() - 1;
                ingredientAdapter.notifyItemInserted(position);
                ingredientsRecyclerView.scrollToPosition(position);
            } else {
                blankListItem();
            }
        } else {
            int position = recipeStepsList.size() - 1;
            ListItem lastItem = recipeStepsList.get(position);
            if (!lastItem.getItem().trim().isEmpty()) {
                recipeStepsList.add(new ListItem("", "Step"));
                position = recipeStepsList.size() - 1;
                recipeAdapter.notifyItemInserted(position);
                recipeRecyclerView.scrollToPosition(position);
            } else {
                blankListItem();
            }
        }
    }


    private Uri makeUri() {
        File imageFile = new File(getApplicationContext().getFilesDir(), "pic.jpg");
        return FileProvider.getUriForFile(
                getApplicationContext(),
                "edu.northeastern.recipeasy.fileProvider",
                imageFile
        );
    }

    private void blankListItem() {
        Toast.makeText(this, "Item cannot be left blank", Toast.LENGTH_SHORT).show();
    }

    public void clickedCamera(View view) {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            cameraLauncher.launch(dishPictureUri);
        }
    }

    public void clickedGallery(View view) {
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

    public void submitRecipe(View view) {
        // TODO: which items are required, which arent?
        //TODO: make a recipe object and push to DB

        String[] formedStrings = formListItemStrings();

        EditText dish = findViewById(R.id.editDishAddRecipe);
        RangeSlider calorieSlider = findViewById(R.id.rangeSliderCaloriesIdAddNewRecipe);
        EditText cookTime = findViewById(R.id.cookTimeId);
        EditText prepTime = findViewById(R.id.prepTimeId);
        EditText serving = findViewById(R.id.servingSizeIdAddNewRecipe);
        Spinner cuisineSpinner = findViewById(R.id.spinnerAddNewCuisine);

        String dishName = dish.getText().toString();
        Log.w(" DISHNAME", " " + dishName);
        // String author = username;
        String ingredients =  formedStrings[0];
        Log.w("INGREDIENTS", " " +ingredients );
        int calories = Math.round(calorieSlider.getValues().get(0));
        Log.w("CALORIES", " " + calories);
        int cookTimeMinutes = 0;
        if (!cookTime.getText().toString().isEmpty()) {
            cookTimeMinutes = Integer.parseInt(cookTime.getText().toString());
        }
        Log.w("COOK TIME", " " + cookTimeMinutes);

        int prepTimeMinutes = 0;
        if (!prepTime.getText().toString().isEmpty()) {
            prepTimeMinutes = Integer.parseInt(prepTime.getText().toString());
        }
        Log.w("PREP TIME", " " + prepTimeMinutes);

        //TODO: decide how we want to do dietary. 3 separate booleans? a list?

        String recipeSteps = formedStrings[1];
        int servingSizes = 0;
        if (!serving.getText().toString().isEmpty()) {
            servingSizes = Integer.parseInt(serving.getText().toString());
        }
        Log.w("SERVINGS", " " + servingSizes );

        //TODO: figure out how to store a picture
        String cuisine = cuisineSpinner.getSelectedItem().toString();

        int likes = 0;
        int dislikes = 0;

        Log.w(" CUISINE", " " + cuisine );


    }

    private String[] formListItemStrings() {
        StringBuilder ingredients = new StringBuilder();
        StringBuilder recipeSteps = new StringBuilder();

        for (int i = 0; i < ingredientList.size(); i++) {
            ListItem item = ingredientList.get(i);
            ingredients.append(item.getItem()).append("\n");
        }
        for (int i = 0; i < recipeStepsList.size(); i++) {
            ListItem stepItem = recipeStepsList.get(i);
            recipeSteps.append(stepItem.getLabel())
                    .append(" ").append(i+1).append(": ")
                    .append(stepItem.getItem()).append("\n");
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

    //TODO: should all be saved or just the ones that disappear
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