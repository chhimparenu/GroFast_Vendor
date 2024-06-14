package com.wits.grofast_vendor.Details;

import static com.wits.grofast_vendor.CommonUtilities.handleApiError;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.wits.grofast_vendor.Adapter.CategorySpinnerAdapter;
import com.wits.grofast_vendor.Api.Interface.CategoriesInterface;
import com.wits.grofast_vendor.Api.Model.CategoryModel;
import com.wits.grofast_vendor.Api.Response.CategoryResponse;
import com.wits.grofast_vendor.Api.Retrofirinstance;
import com.wits.grofast_vendor.R;
import com.wits.grofast_vendor.session.SupplierActivitySession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProduct extends AppCompatActivity {
    AppCompatSpinner categories, unit, tax;
    RadioButton return_true, return_false;
    AppCompatEditText name, quantity, price, detail, descount;
    ImageView image;
    SupplierActivitySession session;
    private final String TAG = "ShowAllCategories";
    private List<CategoryModel> categoryList = new ArrayList<>();
    int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.add_product_page_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        setContentView(R.layout.activity_add_product);

        session = new SupplierActivitySession(getApplicationContext());

        //Spinner
        categories = findViewById(R.id.Categories_spinner);
        unit = findViewById(R.id.unit_spinner);
        tax = findViewById(R.id.tax_spinner);

        //Radio button
        return_true = findViewById(R.id.retun_true);
        return_false = findViewById(R.id.retun_false);

        //Edittext
        name = findViewById(R.id.add_product_name);
        quantity = findViewById(R.id.add_product_quantity);
        price = findViewById(R.id.add_product_price);
        detail = findViewById(R.id.add_product_detail);
        descount = findViewById(R.id.add_product_discount);

        //image
        image = findViewById(R.id.add_product_image);
        fetchCategories();
        populateCategorySpinner(categoryList);
//        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                CategoryModel selectedCategory = (CategoryModel) parent.getItemAtPosition(position);
////                categoryId = selectedCategory.getId();
////                String categoryName = selectedCategory.getCategory_name();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
    }

    private void fetchCategories() {
        Log.e(TAG, "onResponse: token " + session.getToken());
        Call<CategoryResponse> call = Retrofirinstance.getClient(session.getToken()).create(CategoriesInterface.class).fetchCategories();
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful()) {
                    CategoryResponse categoryResponse = response.body();
                    categoryList = categoryResponse.getCategories();
                    if (categoryList != null && !categoryList.isEmpty()) {
                        populateCategorySpinner(categoryList);
                    }
                    Log.e(TAG, "onResponse: fragment Show all categories");
                    Log.e(TAG, "onResponse: message " + categoryResponse.getMessage());
                } else {
                    handleApiError(TAG, response, getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {

            }
        });
    }

    private void populateCategorySpinner(List<CategoryModel> categoryList) {

        categoryList.add(new CategoryModel("friuts", 1));
        categoryList.add(new CategoryModel("veg", 2));
        categoryList.add(new CategoryModel("dairy", 3));
        categoryList.add(new CategoryModel("baby products", 4));
        categoryList.add(new CategoryModel("chaval", 5));

//        ArrayAdapter<CategoryModel> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryList);
        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(getApplicationContext(), categoryList);
//        adapter.set(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);

        for (CategoryModel category : categoryList) {
            categoryId = category.getId();
            Log.e(TAG, "onResponse: categories name : " + category.getCategory_name());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}