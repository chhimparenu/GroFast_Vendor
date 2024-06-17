package com.wits.grofast_vendor.Details;

import static com.wits.grofast_vendor.CommonUtilities.handleApiError;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import android.view.View;
import android.view.WindowManager;

import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;

import com.wits.grofast_vendor.Adapter.CategorySpinnerAdapter;
import com.wits.grofast_vendor.Adapter.TaxesSpinnerAdapter;
import com.wits.grofast_vendor.Adapter.UnitSpinnerAdapter;
import com.wits.grofast_vendor.Api.Interface.CategoriesInterface;
import com.wits.grofast_vendor.Api.Interface.ProductInterface;
import com.wits.grofast_vendor.Api.Interface.TaxInterface;
import com.wits.grofast_vendor.Api.Interface.UnitInterface;
import com.wits.grofast_vendor.Api.Model.CategoryModel;
import com.wits.grofast_vendor.Api.Model.ProductModel;
import com.wits.grofast_vendor.Api.Model.TaxModel;
import com.wits.grofast_vendor.Api.Model.UnitModel;
import com.wits.grofast_vendor.Api.Response.ProductResponse;
import com.wits.grofast_vendor.Api.Response.CategoryResponse;
import com.wits.grofast_vendor.Api.Response.TaxReponse;
import com.wits.grofast_vendor.Api.Response.UnitResponse;
import com.wits.grofast_vendor.Api.Retrofirinstance;
import com.wits.grofast_vendor.R;
import com.wits.grofast_vendor.session.SupplierActivitySession;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProduct extends AppCompatActivity {
    AppCompatSpinner categories, unit, tax;
    RadioButton return_true, return_false, stock_true, stock_false;
    AppCompatEditText name, quantity, price, detail, descount, per;
    ImageView proimage;
    SupplierActivitySession session;
    private MultipartBody.Part image;
    private File imageFile;
    private final String TAG = "ShowAllCategories";
    private List<CategoryModel> categoryList = new ArrayList<>();
    private List<TaxModel> taxModelList = new ArrayList<>();
    private List<UnitModel> unitModelList = new ArrayList<>();
    int categoryId, taxid, unitId;
    AppCompatButton addproduct;

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
        stock_true = findViewById(R.id.stock_true);
        stock_false = findViewById(R.id.stock_false);

        //Edittext
        name = findViewById(R.id.add_product_name);
        quantity = findViewById(R.id.add_product_quantity);
        price = findViewById(R.id.add_product_price);
        detail = findViewById(R.id.add_product_detail);
        descount = findViewById(R.id.add_product_discount);
        per = findViewById(R.id.add_product_per);

        //Button
        addproduct = findViewById(R.id.product_add);

        //image
        proimage = findViewById(R.id.add_product_image);


        fetchCategories();
        fetchTaxes();
        fetchUnit();

        populateCategorySpinner(categoryList);
        populateTaxesSpinner(taxModelList);
        populateUnitSpinner(unitModelList);

        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedreturnpolicy = null;
                if (return_true.isChecked()) {
                    selectedreturnpolicy = return_true.getText().toString();
                } else if (return_false.isChecked()) {
                    selectedreturnpolicy = return_false.getText().toString();
                }

                int selectedReturnPolicy = return_true.isChecked() ? 1 : 0;
                int selectedStock = stock_true.isChecked() ? 1 : 0;

                String selectedstock = null;
                if (stock_true.isChecked()) {
                    selectedstock = stock_true.getText().toString();
                } else if (stock_false.isChecked()) {
                    selectedstock = stock_false.getText().toString();
                }

                String uname = name.getText().toString().trim();
                CategoryModel ucategories = (CategoryModel) categories.getSelectedItem();
                TaxModel utax = (TaxModel) tax.getSelectedItem();
                UnitModel uunit = (UnitModel) unit.getSelectedItem();
                String uper = per.getText().toString().trim();
                String uprice = price.getText().toString().trim();
                String uquantity = quantity.getText().toString().trim();
                String udiscount = descount.getText().toString().trim();
                String udetails = detail.getText().toString().trim();

                if (uname.isEmpty()) {
                    showToastAndFocus(getString(R.string.toast_message_enter_name), name);
                    return;
                }

                if (ucategories == null || ucategories.equals(getString(R.string.select_category))) {
                    showToastAndFocus(getString(R.string.toast_message_select_categories), categories);
                    return;
                }

                if (utax == null || tax.equals(getString(R.string.select_tax))) {
                    showToastAndFocus(getString(R.string.toast_message_select_tax), tax);
                    return;
                }

                if (uper.isEmpty()) {
                    showToastAndFocus(getString(R.string.toast_message_enter_per), per);
                    return;
                }

                if (uunit == null || unit.equals(getString(R.string.select_unit))) {
                    showToastAndFocus(getString(R.string.toast_message_select_unit), unit);
                    return;
                }

                if (uprice.isEmpty()) {
                    showToastAndFocus(getString(R.string.toast_message_enter_price), price);
                    return;
                }

                if (uquantity.isEmpty()) {
                    showToastAndFocus(getString(R.string.toast_message_enter_quantity), quantity);
                    return;
                }
                if (udiscount.isEmpty()) {
                    showToastAndFocus(getString(R.string.toast_message_enter_discount), descount);
                    return;
                }
                if (udetails.isEmpty()) {
                    showToastAndFocus(getString(R.string.toast_message_enter_detail), detail);
                    return;
                }

                if (selectedstock == null) {
                    showToastAndFocus(getString(R.string.toast_message_select_stock), return_true);
                    return;
                }

                if (selectedreturnpolicy == null) {
                    showToastAndFocus(getString(R.string.toast_message_select_return_policy), return_true);
                    return;
                }

                RequestBody adname = RequestBody.create(MediaType.parse("text/plain"), name.getText().toString());
                RequestBody adcategories = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(ucategories.getId()));
                RequestBody adunit = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(uunit.getId()));
                RequestBody adtax = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(utax.getId()));
                RequestBody adquantity = RequestBody.create(MediaType.parse("text/plain"), quantity.getText().toString());
                RequestBody adprice = RequestBody.create(MediaType.parse("text/plain"), price.getText().toString());
                RequestBody addiscount = RequestBody.create(MediaType.parse("text/plain"), descount.getText().toString());
                RequestBody adproductdetail = RequestBody.create(MediaType.parse("text/plain"), detail.getText().toString());
                RequestBody adper = RequestBody.create(MediaType.parse("text/plain"), per.getText().toString());

                if (selectedreturnpolicy != null && selectedstock != null) {
                    RequestBody policy = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(selectedReturnPolicy));
                    RequestBody stock = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(selectedReturnPolicy));
                    Call<ProductResponse> addProductResponseCall = Retrofirinstance.getClient(session.getToken()).create(ProductInterface.class).addProduct(adname, adcategories, adunit, adtax, adquantity, adprice, addiscount, policy, adproductdetail, stock, adper, image);
                    addProductResponseCall.enqueue(new Callback<ProductResponse>() {
                        @Override
                        public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                            if (response.isSuccessful()) {
                                ProductResponse productResponse = response.body();
                                Log.d(TAG, "Message " + productResponse.getMessage());
                                Log.d(TAG, "Status " + productResponse.getStatus());
                                Log.d(TAG, "Product " + productResponse.getProduct());
                                Toast.makeText(AddProduct.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    String errorBody = response.errorBody().string();
                                    Log.d(TAG, "onResponse -> status: " + response.code());
                                    Log.d(TAG, "onResponse -> errorMessage: " + errorBody);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                handleApiError(TAG, response, getApplicationContext());
                            }
                        }

                        @Override
                        public void onFailure(Call<ProductResponse> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    private void showToastAndFocus(String message, View view) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        view.requestFocus();
        showKeyboard(view);
    }

    private void showKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
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

            }
        });
    }

    private void populateCategorySpinner(List<CategoryModel> categoryList) {
        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(getApplicationContext(), categoryList);
        categories.setAdapter(adapter);

        for (CategoryModel category : categoryList) {
            categoryId = category.getId();
            Log.e(TAG, "onResponse: categories name : " + category.getCategory_name());
        }
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

            }
        });
    }

    private void populateTaxesSpinner(List<TaxModel> taxModelList) {
        TaxesSpinnerAdapter adapter = new TaxesSpinnerAdapter(getApplicationContext(), taxModelList);
        tax.setAdapter(adapter);

        for (TaxModel tax : taxModelList) {
            taxid = tax.getId();
            Log.e(TAG, "onResponse: categories name : " + tax.getName());
        }
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

            }
        });
    }

    private void populateUnitSpinner(List<UnitModel> unitModelList) {
        UnitSpinnerAdapter adapter = new UnitSpinnerAdapter(getApplicationContext(), unitModelList);
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