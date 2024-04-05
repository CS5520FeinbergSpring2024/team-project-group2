package edu.northeastern.recipeasy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import edu.northeastern.recipeasy.DiscoverRecipeRecyclerView.DiscoverRecipeViewAdapter;
import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.RecipeRecyclerView.RecipeViewAdapter;
import edu.northeastern.recipeasy.UserRecyclerView.UserItemClickListener;
import edu.northeastern.recipeasy.UserRecyclerView.UserViewAdapter;
import edu.northeastern.recipeasy.domain.Recipe;
import edu.northeastern.recipeasy.domain.User;
import edu.northeastern.recipeasy.utils.DataUtil;
import edu.northeastern.recipeasy.utils.IUserFetchListener;
import edu.northeastern.recipeasy.utils.UserManager;

public class SearchActivity extends AppCompatActivity implements IUserFetchListener, NavigationBarView.OnItemSelectedListener {
    private RecyclerView userRecyclerView;
    private UserViewAdapter userAdapter;
    private RecipeViewAdapter recipeAdapter;
    private ArrayList<User> userList;
    private ArrayList<Recipe> recipeList;
    private ArrayList<User> originalUserList = new ArrayList<>();
    private ArrayList<Recipe> originalRecipeList = new ArrayList<>();
    private RecyclerView recipeRecyclerView;
    private User user;
    private TabLayout tabs;
    private SearchView search;
    private String username;
    private int position;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String filterText = "";
    private BottomNavigationView bottomNavigationView;
    private Menu menu;
    private MenuItem searchMenuItem;



    // api search stuff
    private RangeSlider calorieRange;
    private CheckBox glutenFree;
    private CheckBox vegetarian;
    private CheckBox vegan;
    private CheckBox peanutAllergy;
    private int minCalories;
    private int maxCalories;
    private EditText dishInput;
    private Spinner cuisineInput;
    private DiscoverRecipeViewAdapter discoverRecipeAdapter;
    private ArrayList<DiscoverRecipeItem> discoverRecipeList;
    private ArrayList<DiscoverRecipeItem> discoverOriginalRecipeList = new ArrayList<>();
    private RecyclerView discoverRecipeRecyclerView;
    private Handler apiHandler = new Handler();
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        fab = findViewById(R.id.fabId);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener((view)->openSearchInput());

        username = getIntent().getStringExtra("username");
        UserManager userManager = new UserManager();
        userManager.getUser(username, this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        tabs = findViewById(R.id.tabLayoutID);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);

        menu = bottomNavigationView.getMenu();
        searchMenuItem = menu.findItem(R.id.search_icon);
        searchMenuItem.setChecked(true);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();
                if (position == 0) {
                    search.setVisibility(View.VISIBLE);
                    setUpUserRecyclerView();
                    fab.setVisibility(View.GONE);
                } else if (position == 1) {
                    search.setVisibility(View.VISIBLE);
                    setUpRecipeRecyclerView();
                    fab.setVisibility(View.GONE);
                }

                else if (position == 2) {
                    search.setVisibility(View.GONE);
                    setUpDiscoverRecipeRecyclerView();
                    fab.setVisibility(View.VISIBLE);
                    openSearchInput();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        search = findViewById(R.id.searchBarID);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                filterText = newText;
                return false;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            if (position == 0) {
                loadAllUsers();
            } else if (position == 1) {
                loadAllRecipes();
            }
            else if (position == 2) {
                openSearchInput();

            }
        });

    }

    private void filterList(String text){
        if (position == 0) {
            filterUserList(text);
        } else if (position == 1) {
            filterRecipeList(text);
        }
    }

    private void filterUserList(String text) {
        ArrayList<User> filteredList = new ArrayList<>();
        for (User u : originalUserList) {
            if (u.getUsername().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(u);
            }
        }
        if (userList != null) {
            userList.clear();
            userList.addAll(filteredList);
            userAdapter.notifyDataSetChanged();
        }
    }

    private void filterRecipeList(String text) {
        ArrayList<Recipe> filteredList = new ArrayList<>();
        for (Recipe r : originalRecipeList) {
            if (r.getDishName().toLowerCase().contains(text.toLowerCase())
                    || r.getCuisine().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(r);
            }
        }
        if (recipeList != null) {
            recipeList.clear();
            recipeList.addAll(filteredList);
            recipeAdapter.notifyDataSetChanged();
        }
    }

    public void setUpUserRecyclerView() {
        userRecyclerView = findViewById(R.id.userRecyclerID);
        userRecyclerView.setHasFixedSize(true);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        userAdapter = new UserViewAdapter(userList, this);
        UserItemClickListener clickListener = position -> {
            User clickedUser = userList.get(position);
            Intent goProfile = new Intent(SearchActivity.this, ProfileActivity.class);
            goProfile.putExtra("currentUsername", username);
            goProfile.putExtra("profileUsername", clickedUser.getUsername());
            goProfile.putExtra("isCurrentUser", Objects.equals(username, clickedUser.getUsername()));
            startActivity(goProfile);
        };
        userAdapter.setOnUserClickListener(clickListener);
        userRecyclerView.setAdapter(userAdapter);
        loadAllUsers();
    }

    public void setUpRecipeRecyclerView() {
        recipeRecyclerView = findViewById(R.id.userRecyclerID);
        recipeRecyclerView.setHasFixedSize(true);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeList = new ArrayList<>();
        recipeAdapter = new RecipeViewAdapter(recipeList, this);
        recipeRecyclerView.setAdapter(recipeAdapter);
        loadAllRecipes();
    }

    private void loadAllRecipes(){
        new Thread(() -> {
            DataUtil.fetchAllRecipes(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Recipe> recipes = DataUtil.parseRecipes(dataSnapshot);
                    recipeList.clear();
                    originalRecipeList.clear();
                    recipeList.addAll(recipes);
                    originalRecipeList.addAll(recipes);
                    filterList(filterText);
                    new Handler(Looper.getMainLooper()).post(() -> recipeAdapter.notifyDataSetChanged());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }).start();
    }

    private void loadAllUsers(){
        new Thread(() -> {
            DataUtil.fetchAllUsers(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<User> users = DataUtil.parseUsers(dataSnapshot);
                    userList.clear();
                    originalUserList.clear();
                    userList.addAll(users);
                    originalUserList.addAll(users);
                    filterList(filterText);
                    new Handler(Looper.getMainLooper()).post(() -> userAdapter.notifyDataSetChanged());
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        int selectedTabPosition = tabs.getSelectedTabPosition();
        if (selectedTabPosition == 0) {
            setUpUserRecyclerView();
        } else if (selectedTabPosition == 1) {
            setUpRecipeRecyclerView();
            Toast.makeText(getApplicationContext(), "Recipes", Toast.LENGTH_SHORT).show();

        } else if (selectedTabPosition == 2) {
            Toast.makeText(getApplicationContext(), "API", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onUserFetched(User user) {
        if (user != null) {
            this.user = user;
            for (String follower : user.getFollowers()) {
                Log.d("User", follower);
            }
        }

    }

    @Override
    public void onError(DatabaseError databaseError) {

        Log.d("User", "User not found");

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.w("NAV ITEM SELECTED", ""+ item.getItemId() + " "+R.id.home_icon + " "+ R.id.profile_icon);
        int itemId = item.getItemId();
        if(itemId == R.id.home_icon) {
            Intent goHome = new Intent(SearchActivity.this, HomePage.class);
            goHome.putExtra("username", username);
            startActivity(goHome);
            return true;
        } else if(itemId == R.id.profile_icon) {
            Intent goProfile = new Intent(SearchActivity.this, ProfileActivity.class);
            goProfile.putExtra("currentUsername", username);
            goProfile.putExtra("profileUsername", username);
            goProfile.putExtra("isCurrentUser", true);
            startActivity(goProfile);
            return true;
        }
        else if(itemId == R.id.message_icon) {
            Toast.makeText(this, "MESSAGES", Toast.LENGTH_LONG).show();
//            Intent goMessages = new Intent(HomePage.this, MessageActivity.class);
//            goMessages.putExtra("username", user.getUsername());
//            startActivity(goMessages);
            return true;
        }
        return false;
    }

    public void setUpDiscoverRecipeRecyclerView() {
        discoverRecipeRecyclerView = findViewById(R.id.userRecyclerID);
        discoverRecipeRecyclerView.setHasFixedSize(true);
        discoverRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        discoverRecipeList = new ArrayList<>();
        discoverRecipeAdapter = new DiscoverRecipeViewAdapter(discoverRecipeList, this);
        discoverRecipeRecyclerView.setAdapter(discoverRecipeAdapter);
    }

    public void openSearchInput(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View inputView = inflater.inflate(R.layout.discover_search_input, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this).setTitle("Search Recipes");
        alertDialogBuilder.setView(inputView);

        peanutAllergy = inputView.findViewById(R.id.peanutBoxId);
        glutenFree = inputView.findViewById(R.id.glutenFreeBoxId);
        vegetarian = inputView.findViewById(R.id.vegetarianBoxId);
        vegan = inputView.findViewById(R.id.veganBoxId);
        cuisineInput = inputView.findViewById(R.id.cuisineSpinnerId);
        dishInput = inputView.findViewById(R.id.dishInputId);
        calorieRange = inputView.findViewById(R.id.rangeSlider);

        alertDialogBuilder.setPositiveButton("Search", (dialog, which) -> {
            if (discoverRecipeList != null && discoverRecipeList.size() > 0) {
                while (discoverRecipeList.size() > 0) {
                    discoverRecipeList.remove(0);
                    recipeAdapter.notifyItemRemoved(0);
                }
            }
            handleSearch();

        });

        alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        alertDialogBuilder.show();
    }

    public void handleSearch(){
        new Thread(() -> {
            try {
                String url = getUrlRequest();
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    String jsonResponse = response.toString();
                    handleAPIResponse(jsonResponse);

                } else {
                    Log.e("API Request", "Error response code: " + responseCode);
                }
                connection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleAPIResponse(String response){
        try {
            JSONObject json = new JSONObject(response);
            JSONArray results = json.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject recipeResponseItem = results.getJSONObject(i);

                String title = recipeResponseItem.getString("title");
                String imageUrl = recipeResponseItem.getString("image");
                String recipeUrl = recipeResponseItem.getString("sourceUrl");
                String description = recipeResponseItem.getString("summary");
                if (description.length() ==0){
                    description = "No description.";
                }
                int calories = (int) Math.round(recipeResponseItem.getJSONObject("nutrition")
                        .getJSONArray("nutrients")
                        .getJSONObject(0)
                        .getDouble("amount"));

                String finalDescription = description.replaceAll("<.*?>", "");;

                SearchActivity.this.apiHandler.post(() -> {
                    discoverRecipeList.add(new DiscoverRecipeItem(title, imageUrl, calories, recipeUrl, finalDescription));
                    discoverRecipeAdapter.notifyItemInserted(discoverRecipeList.size() -1);
                });
            }
            if (results.length() == 0) {
               // toast no results found
            }
        } catch (JSONException e) {
        }
    }


    private String getUrlRequest() {
        String url = "https://api.spoonacular.com/recipes/complexSearch?";
        String key = "&apiKey=50f8f70732c647c585df4f225cf14e8c";
        String info = "&addRecipeInformation=true";
        String diet = "&intolerances=";
        boolean dietChecked = false;
        if (!dishInput.getText().toString().equals("")){
            url += ("&query=" + dishInput.getText().toString());
        }
        if (peanutAllergy.isChecked()){
            url += "&excludeIngredients=peanuts";
        }
        if (vegan.isChecked()){
            diet += "vegan,";
            dietChecked = true;
        }
        if (vegetarian.isChecked()){
            diet += "vegetarian,";
            dietChecked = true;
        }
        if (glutenFree.isChecked()){
            diet += "gluten%20free,";
            dietChecked = true;
        }
        if (dietChecked){
            diet = diet.substring(0, diet.length() - 1);
            url+= diet;
        }
        url += "&cuisine="+ cuisineInput.getSelectedItem().toString().toLowerCase();
        minCalories = Math.round(calorieRange.getValues().get(0));
        maxCalories = Math.round(calorieRange.getValues().get(1));
        url += ("&minCalories="+minCalories + "&maxCalories="+maxCalories)+ "&number=30" + info + key;
        return url;
    }
}