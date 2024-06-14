package com.wits.grofast_vendor.Details;

import static com.wits.grofast_vendor.CommonUtilities.handleApiError;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import android.view.WindowManager;

import android.widget.ImageView;
import android.widget.RadioButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;

import com.wits.grofast_vendor.Adapter.CategorySpinnerAdapter;
import com.wits.grofast_vendor.Adapter.TaxesSpinnerAdapter;
import com.wits.grofast_vendor.Adapter.UnitSpinnerAdapter;
import com.wits.grofast_vendor.Api.Interface.CategoriesInterface;
import com.wits.grofast_vendor.Api.Interface.TaxInterface;
import com.wits.grofast_vendor.Api.Interface.UnitInterface;
import com.wits.grofast_vendor.Api.Model.CategoryModel;
import com.wits.grofast_vendor.Api.Model.TaxModel;
import com.wits.grofast_vendor.Api.Model.UnitModel;
import com.wits.grofast_vendor.Api.Response.CategoryResponse;
import com.wits.grofast_vendor.Api.Response.TaxReponse;
import com.wits.grofast_vendor.Api.Response.UnitResponse;
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
    private List<TaxModel> taxModelList = new ArrayList<>();
    private List<UnitModel> unitModelList = new ArrayList<>();
    int categoryId, taxid, unitId;

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
        fetchTaxes();
        fetchUnit();
        populateCategorySpinner(categoryList);
        populateTaxesSpinner(taxModelList);
        populateUnitSpinner(unitModelList);
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

    private void fetchTaxes() {
        Log.e(TAG, "onResponse: token " + session.getToken());
        Call<TaxReponse> call = Retrofirinstance.getClient(session.getToken()).create(TaxInterface.class).fetchtaxes();
        call.enqueue(new Callback<TaxReponse>() {
            @Override
            public void onResponse(Call<TaxReponse> call, Response<TaxReponse> response) {
                if (response.isSuccessful()) {
                    TaxReponse taxReponse = response.body();
                    taxModelList = taxReponse.gettax();
                    if (taxModelList != null && !taxModelList.isEmpty()) {
                        populateTaxesSpinner(taxModelList);
                    }
                    Log.e(TAG, "onResponse: fragment Show all taxes");
                    Log.e(TAG, "onResponse: message " + taxReponse.getMessage());
                } else {
                    handleApiError(TAG, response, getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<TaxReponse> call, Throwable t) {

            }
        });
    }

    private void populateTaxesSpinner(List<TaxModel> taxModelList) {
        taxModelList.add(new TaxModel("friuts", 1));
        taxModelList.add(new TaxModel("veg", 2));
        taxModelList.add(new TaxModel("dairy", 3));
        taxModelList.add(new TaxModel("baby products", 4));
        taxModelList.add(new TaxModel("chaval", 5));

//        ArrayAdapter<CategoryModel> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryList);
        TaxesSpinnerAdapter adapter = new TaxesSpinnerAdapter(getApplicationContext(), taxModelList);
//        adapter.set(android.R.layout.simple_spinner_dropdown_item);
        tax.setAdapter(adapter);

        for (TaxModel tax : taxModelList) {
            taxid = tax.getId();
            Log.e(TAG, "onResponse: categories name : " + tax.getName());
        }
    }

    private void fetchUnit() {
        Log.e(TAG, "onResponse: token " + session.getToken());
        Call<UnitResponse> call = Retrofirinstance.getClient(session.getToken()).create(UnitInterface.class).fetchproductunit();
        call.enqueue(new Callback<UnitResponse>() {
            @Override
            public void onResponse(Call<UnitResponse> call, Response<UnitResponse> response) {
                if (response.isSuccessful()) {
                    UnitResponse unitResponse = response.body();
                    unitModelList = unitResponse.getUnitModel();
                    if (unitModelList != null && !unitModelList.isEmpty()) {
                        populateUnitSpinner(unitModelList);
                    }
                    Log.e(TAG, "onResponse: fragment Show all Unit");
                    Log.e(TAG, "onResponse: message " + unitResponse.getMessage());
                } else {
                    handleApiError(TAG, response, getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<UnitResponse> call, Throwable t) {

            }
        });
    }

    private void populateUnitSpinner(List<UnitModel> unitModelList) {
        unitModelList.add(new UnitModel("friuts", 1));
        unitModelList.add(new UnitModel("veg", 2));
        unitModelList.add(new UnitModel("dairy", 3));
        unitModelList.add(new UnitModel("baby products", 4));
        unitModelList.add(new UnitModel("chaval", 5));

//        ArrayAdapter<CategoryModel> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryList);
        UnitSpinnerAdapter adapter = new UnitSpinnerAdapter(getApplicationContext(), unitModelList);
//        adapter.set(android.R.layout.simple_spinner_dropdown_item);
        unit.setAdapter(adapter);

        for (UnitModel unit : unitModelList) {
            unitId = unit.getId();
            Log.e(TAG, "onResponse: Unit name : " + unit.getUnit_name());
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