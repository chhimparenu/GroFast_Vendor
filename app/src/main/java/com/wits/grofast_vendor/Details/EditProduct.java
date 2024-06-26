package com.wits.grofast_vendor.Details;

import static com.wits.grofast_vendor.CommonUtilities.getSelectedSpinnerItemPosition;
import static com.wits.grofast_vendor.CommonUtilities.handleApiError;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;

import com.bumptech.glide.Glide;
import com.wits.grofast_vendor.Adapter.CustomSpinnerAdapter;
import com.wits.grofast_vendor.Api.Interface.CategoriesInterface;
import com.wits.grofast_vendor.Api.Interface.TaxInterface;
import com.wits.grofast_vendor.Api.Interface.UnitInterface;
import com.wits.grofast_vendor.Api.Model.CategoryModel;
import com.wits.grofast_vendor.Api.Model.ProductModel;
import com.wits.grofast_vendor.Api.Model.SpinnerModel;
import com.wits.grofast_vendor.Api.Model.TaxModel;
import com.wits.grofast_vendor.Api.Model.UnitModel;
import com.wits.grofast_vendor.Api.Response.CategoryResponse;
import com.wits.grofast_vendor.Api.Response.TaxReponse;
import com.wits.grofast_vendor.Api.Response.UnitResponse;
import com.wits.grofast_vendor.Api.Retrofirinstance;
import com.wits.grofast_vendor.R;
import com.wits.grofast_vendor.session.SupplierActivitySession;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProduct extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private boolean isRemoveProfile = false;
    AppCompatSpinner categorySpinner, unitSpinner, taxSpinner;
    RadioButton return_true, return_false, stock_true, stock_false;
    AppCompatEditText name, price, detail, descount, per;
    ImageView showimage;
    SupplierActivitySession session;
    private MultipartBody.Part image;
    private File imageFile;
    private final int defaultImage = R.drawable.add_product;
    private final String TAG = "EditProduct";
    private List<CategoryModel> categoryList = new ArrayList<>();
    private List<TaxModel> taxModelList = new ArrayList<>();
    private List<UnitModel> unitModelList = new ArrayList<>();
    int categoryId, taxid, unitId;
    private String taxName, unitName;
    AppCompatButton addproduct, addimage, editimage;
    ProgressBar progressBar;
    List<SpinnerModel> categorySpinnerList = new ArrayList<>();
    List<SpinnerModel> taxSpinnerList = new ArrayList<>();
    List<SpinnerModel> unitSpinnerList = new ArrayList<>();
    CustomSpinnerAdapter categoryAdapter, taxAdapter, unitAdapter;
    ProductModel product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.edit_product_page_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        setContentView(R.layout.activity_edit_product);


        session = new SupplierActivitySession(getApplicationContext());

        //Spinner
        categorySpinner = findViewById(R.id.edit_Categories_spinner);
        unitSpinner = findViewById(R.id.edit_unit_spinner);
        taxSpinner = findViewById(R.id.edit_tax_spinner);

        progressBar = findViewById(R.id.loader_edit_product);

        //Radio button
        return_true = findViewById(R.id.edit_retun_true);
        return_false = findViewById(R.id.edit_retun_false);
        stock_true = findViewById(R.id.edit_stock_true);
        stock_false = findViewById(R.id.edit_stock_false);

        //Edittext
        name = findViewById(R.id.edit_product_name);
        price = findViewById(R.id.edit_product_price);
        detail = findViewById(R.id.edit_product_detail);
        descount = findViewById(R.id.edit_product_discount);
        per = findViewById(R.id.edit_product_per);

        //Button
        addproduct = findViewById(R.id.product_edit);
        addimage = findViewById(R.id.add_edit_product_image);
        editimage = findViewById(R.id.edit_edit_product_image);

        //image
        showimage = findViewById(R.id.show_edit_product_image);

        if (getIntent() != null) {
            product = getIntent().getParcelableExtra("Product_Item");
            // Set the product details to the views
            if (product != null) {
                name.setText(product.getName());
                per.setText(product.getPer());
                price.setText(product.getPrice());
                descount.setText(product.getDiscount());
                detail.setText(product.getProduct_detail());
                Glide.with(this).load(product.getImage()).placeholder(R.drawable.add_product).into(showimage);

                taxName = product.getTax_id();
                unitName = product.getUnit_id();
                Log.e(TAG, "onCreate:  taxName " + taxName);
                Log.e(TAG, "onCreate: unitName " + unitName);
                if (product.getReturn_policy() == 1) {
                    return_true.setChecked(true);
                } else {
                    return_false.setChecked(true);
                }
            }


            if (product.getStock_status() == 1) {
                stock_true.setChecked(true);
            } else {
                stock_false.setChecked(true);
            }
            fetchCategories();
            fetchTaxes();
            fetchUnit();

            populateCategorySpinner(categoryList);
            populateTaxesSpinner(taxModelList);
            populateUnitSpinner(unitModelList);
        }
    }

    private void fetchCategories() {
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
                t.printStackTrace();
            }
        });
    }

    private void populateCategorySpinner(List<CategoryModel> categoryList) {
        categorySpinnerList.clear();
        for (CategoryModel categorymodel : categoryList) {
            categorySpinnerList.add(new SpinnerModel(categorymodel.getCategory_name(), categorymodel.getId()));
        }
        int selectedCategoryPosition = getSelectedSpinnerItemPosition(categorySpinnerList, product.getCategory_id() + "");
        Log.e(TAG, "populateCategorySpinner: selected category position " + selectedCategoryPosition);

        categoryAdapter = new CustomSpinnerAdapter(getApplicationContext(), categorySpinnerList, getString(R.string.select_category));
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setSelection(selectedCategoryPosition);
    }

    private void fetchTaxes() {
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
                t.printStackTrace();
            }
        });
    }

    private void populateTaxesSpinner(List<TaxModel> taxModelList) {
        taxSpinnerList.clear();
        for (TaxModel model : taxModelList) {
            taxSpinnerList.add(new SpinnerModel(model.getName(), model.getId()));
        }
        taxAdapter = new CustomSpinnerAdapter(getApplicationContext(), taxSpinnerList, getString(R.string.select_tax));
        taxSpinner.setAdapter(taxAdapter);

        int selectedPosition = getSelectedSpinnerItemPosition(taxSpinnerList, taxName);
        taxSpinner.setSelection(selectedPosition);
    }

    private void fetchUnit() {
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
                t.printStackTrace();
            }
        });
    }

    private void populateUnitSpinner(List<UnitModel> unitModelList) {
        unitSpinnerList.clear();

        for (UnitModel model : unitModelList) {
            unitSpinnerList.add(new SpinnerModel(model.getUnit_name(), model.getId()));
        }
        unitAdapter = new CustomSpinnerAdapter(getApplicationContext(), unitSpinnerList, getString(R.string.select_unit));
        unitSpinner.setAdapter(unitAdapter);

        int selectedPosition = getSelectedSpinnerItemPosition(unitSpinnerList, unitName);
        unitSpinner.setSelection(selectedPosition);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    private int getSelectedSpinnerItemPosition(List<SpinnerModel> spinnerModelList, int categoryid) {
//        int position = 1;
//        for (SpinnerModel model : spinnerModelList) {
//            if (model.getId() == categoryid) {
//                break;
//            }
//            position++;
//        }
//        return position;
//    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}