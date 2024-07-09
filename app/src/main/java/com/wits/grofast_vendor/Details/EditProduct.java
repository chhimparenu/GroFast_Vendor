package com.wits.grofast_vendor.Details;

import static com.wits.grofast_vendor.Api.Retrofirinstance.domain;
import static com.wits.grofast_vendor.CommonUtilities.getPathFromUri;
import static com.wits.grofast_vendor.CommonUtilities.getSelectedSpinnerItemPosition;
import static com.wits.grofast_vendor.CommonUtilities.handleApiError;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;

import com.bumptech.glide.Glide;
import com.wits.grofast_vendor.Adapter.CustomSpinnerAdapter;
import com.wits.grofast_vendor.Api.Interface.CategoriesInterface;
import com.wits.grofast_vendor.Api.Interface.ProductInterface;
import com.wits.grofast_vendor.Api.Interface.TaxInterface;
import com.wits.grofast_vendor.Api.Interface.UnitInterface;
import com.wits.grofast_vendor.Api.Model.CategoryModel;
import com.wits.grofast_vendor.Api.Model.ProductModel;
import com.wits.grofast_vendor.Api.Model.SpinnerModel;
import com.wits.grofast_vendor.Api.Model.TaxModel;
import com.wits.grofast_vendor.Api.Model.UnitModel;
import com.wits.grofast_vendor.Api.Response.CategoryResponse;
import com.wits.grofast_vendor.Api.Response.ProductResponse;
import com.wits.grofast_vendor.Api.Response.TaxReponse;
import com.wits.grofast_vendor.Api.Response.UnitResponse;
import com.wits.grofast_vendor.Api.Retrofirinstance;
import com.wits.grofast_vendor.R;
import com.wits.grofast_vendor.session.SupplierActivitySession;

import org.jetbrains.annotations.Nullable;

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
    private String taxName, unitName;
    AppCompatButton editproduct, addimage, editimage;
    ProgressBar progressBar;
    List<SpinnerModel> categorySpinnerList = new ArrayList<>();
    List<SpinnerModel> taxSpinnerList = new ArrayList<>();
    List<SpinnerModel> unitSpinnerList = new ArrayList<>();
    CustomSpinnerAdapter categoryAdapter, taxAdapter, unitAdapter;
    ProductModel product;
    int id;

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
        editproduct = findViewById(R.id.product_edit);
        addimage = findViewById(R.id.add_edit_product_image);
        editimage = findViewById(R.id.edit_edit_product_image);

        //image
        showimage = findViewById(R.id.show_edit_product_image);

        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        editimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        if (getIntent() != null) {
            product = getIntent().getParcelableExtra("Product_Item");
            // Set the product details to the views
            if (product != null) {
                id = product.getId();
                name.setText(product.getName());
                per.setText(product.getPer());
                price.setText(product.getPrice());
                Log.e(TAG, "onCreate: product descount " + product.getDiscount());
                descount.setText(product.getDiscount());
                detail.setText(product.getProduct_detail());

                // Load image
                String imageUrl = product.getImage();
                if (imageUrl != null && !imageUrl.equals(domain + "assets/images/no_image.jpg")) {
                    Glide.with(this).load(imageUrl).placeholder(R.drawable.add_product).into(showimage);
                    addimage.setVisibility(View.GONE);
                    editimage.setVisibility(View.VISIBLE);
                } else {
                    Glide.with(this).load(R.drawable.add_product).into(showimage);
                    addimage.setVisibility(View.VISIBLE);
                    editimage.setVisibility(View.GONE);
                }

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

        editproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edituserproduct();
            }
        });
    }

    private void edituserproduct() {
        String selectedreturnpolicy = null;
        if (return_true.isChecked()) {
            selectedreturnpolicy = return_true.getText().toString();
        } else if (return_false.isChecked()) {
            selectedreturnpolicy = return_false.getText().toString();
        }

        int selectedReturnPolicy = return_true.isChecked() ? 1 : 0;
        int selectedStockPolicy = stock_true.isChecked() ? 1 : 0;

        String selectedstock = null;
        if (stock_true.isChecked()) {
            selectedstock = stock_true.getText().toString();
        } else if (stock_false.isChecked()) {
            selectedstock = stock_false.getText().toString();
        }

        String uname = name.getText().toString().trim();
        SpinnerModel ucategories = (SpinnerModel) categorySpinner.getSelectedItem();
        SpinnerModel utax = (SpinnerModel) taxSpinner.getSelectedItem();
        SpinnerModel uunit = (SpinnerModel) unitSpinner.getSelectedItem();
        String uper = per.getText().toString().trim();
        String uprice = price.getText().toString().trim();
        String udiscount = descount.getText().toString().trim();
        String udetails = detail.getText().toString().trim();

        if (uname.isEmpty()) {
            showToastAndFocus(getString(R.string.toast_message_enter_name), name);
            return;
        }

        if (ucategories == null || ucategories.equals(getString(R.string.select_category))) {
            showToastAndFocus(getString(R.string.toast_message_select_categories), categorySpinner);
            return;
        }

        if (utax == null || utax.equals(getString(R.string.select_tax))) {
            showToastAndFocus(getString(R.string.toast_message_select_tax), taxSpinner);
            return;
        }

        if (uper.isEmpty()) {
            showToastAndFocus(getString(R.string.toast_message_enter_per), per);
            return;
        }

        if (uunit == null || uunit.equals(getString(R.string.select_unit))) {
            showToastAndFocus(getString(R.string.toast_message_select_unit), unitSpinner);
            return;
        }

        if (uprice.isEmpty()) {
            showToastAndFocus(getString(R.string.toast_message_enter_price), price);
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
        RequestBody adprice = RequestBody.create(MediaType.parse("text/plain"), price.getText().toString());
        RequestBody addiscount = RequestBody.create(MediaType.parse("text/plain"), descount.getText().toString());
        RequestBody adproductdetail = RequestBody.create(MediaType.parse("text/plain"), detail.getText().toString());
        RequestBody adper = RequestBody.create(MediaType.parse("text/plain"), per.getText().toString());

        if (selectedreturnpolicy != null && selectedstock != null) {
            RequestBody policy = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(selectedReturnPolicy));
            RequestBody stock = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(selectedStockPolicy));
            progressBar.setVisibility(View.VISIBLE);
            editproduct.setVisibility(View.GONE);
            Call<ProductResponse> editProductResponseCall = Retrofirinstance.getClient(session.getToken()).create(ProductInterface.class).updateproduct(id, adname, adcategories, adunit, adtax, adprice, addiscount, policy, adproductdetail, stock, adper, image);
            editProductResponseCall.enqueue(new Callback<ProductResponse>() {
                @Override
                public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    editimage.setVisibility(View.VISIBLE);
                    if (response.isSuccessful()) {
                        ProductResponse productResponse = response.body();
                        Log.e(TAG, "Message " + productResponse.getMessage());
                        Log.e(TAG, "Status " + productResponse.getStatus());
                        Log.e(TAG, "Product " + productResponse.getProduct());
                        Log.e(TAG, "onResponse: discount" + productResponse.getProduct().getDiscount());
                        showSuccessDialog(productResponse.getMessage());
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
                    t.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    editproduct.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.edit_profile_image_heading).setItems(new String[]{getString(R.string.change_image), getString(R.string.remove_image)}, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        openGallery();
                        break;
                    case 1:
                        showimage.setImageResource(R.drawable.add_product);
                        showaddimage();
                        image = null;
                        isRemoveProfile = true;
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }


    private void showToastAndFocus(String message, View view) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        view.requestFocus();
        showKeyboard(view);
    }

    private void showSuccessDialog(String message) {
        if (isFinishing() || isDestroyed()) {
            return;
        }
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_success, null);

        TextView successMessage = dialogView.findViewById(R.id.success_message);
        successMessage.setText(message);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView).setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (alert.isShowing()) {
                    alert.dismiss();
                    reloadPage();
                }
            }
        }, 2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                showimage.setImageBitmap(bitmap);
                showeditImage();
                imageFile = new File(getPathFromUri(getApplicationContext(), data.getData()));
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
                image = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showaddimage() {
        addimage.setVisibility(View.VISIBLE);
        editimage.setVisibility(View.GONE);
    }

    private void showeditImage() {
        addimage.setVisibility(View.GONE);
        editimage.setVisibility(View.VISIBLE);
    }

    private void reloadPage() {
        finish();
//        startActivity(getIntent());
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

    @Override
    protected void onStart() {
        super.onStart();
    }
}