package com.okro;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class CategoriesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Context context;
    ImageView ivBack;
    Spinner spinnerCategory;
    RecyclerView rvList;
    ArrayList<String> categories;
    CategoryListAdapter categoryListAdapter;
    LinearLayoutManager llmCategory;
    ArrayList<CategoryData> categoryDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        context         = this;
        ivBack          = findViewById(R.id.ivBack);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        rvList          = findViewById(R.id.rvList);
        categories      = new ArrayList<>();
        categoryDataList= new ArrayList<>();


        spinnerCategory.setOnItemSelectedListener(this);
        ivBack.setOnClickListener(this);

        categories.add("Category 1");
        categories.add("Category 2");
        categories.add("Category 3");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, categories){

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                // TODO Auto-generated method stub

                View view = super.getView(position, convertView, parent);

                TextView text = view.findViewById(android.R.id.text1);
                text.setTextColor(getResources().getColor(R.color.hex));
                text.setTypeface(FontManager.getNexaLight(context));

                return view;

            }

            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                // TODO Auto-generated method stub

                View view = super.getView(position, convertView, parent);

                TextView text = view.findViewById(android.R.id.text1);
                text.setTextColor(getResources().getColor(R.color.white));
                text.setTypeface(FontManager.getNexaLight(context));

                return view;

            }
        };
        spinnerCategory.setAdapter(spinnerAdapter);


        llmCategory = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        rvList.setLayoutManager(llmCategory);

       loadCategoryData();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        loadCategoryData();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void loadCategoryData(){
        categoryDataList.clear();
        CategoryData categoryData = new CategoryData();
        categoryData.setCatName("Water");
        categoryData.setImage(R.drawable.images);
        categoryData.setPrice("$2");
        categoryData.setQuantity("1 lt");
        categoryDataList.add(categoryData);

        CategoryData categoryData2 = new CategoryData();
        categoryData2.setCatName("Earth");
        categoryData2.setImage(R.drawable.earth_icon_vector);
        categoryData2.setPrice("$120");
        categoryData2.setQuantity("1 pc");
        categoryDataList.add(categoryData2);

        CategoryData categoryData3 = new CategoryData();
        categoryData3.setCatName("Fire");
        categoryData3.setImage(R.drawable.creativboard_converted);
        categoryData3.setPrice("Rs20");
        categoryData3.setQuantity("1 bl");
        categoryDataList.add(categoryData3);

        categoryListAdapter = new CategoryListAdapter(context,categoryDataList);
        rvList.setAdapter(categoryListAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
               onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(CategoriesActivity.this,LoginActivity.class));
        finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
