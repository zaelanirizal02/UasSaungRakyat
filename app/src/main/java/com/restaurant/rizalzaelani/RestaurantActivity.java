package com.restaurant.rizalzaelani;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.restaurant.rizalzaelani.data.Constans;
import com.restaurant.rizalzaelani.data.Session;
import com.restaurant.rizalzaelani.model.RestaurantAction;
import com.restaurant.rizalzaelani.utils.DialogUtils;

public class RestaurantActivity extends AppCompatActivity {

    Session session;
    EditText resto_name, resto_category, resto_imgurl, resto_address;
    Button create_restaurant;
    ProgressDialog progressDialog;
    String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);
        session = new Session(this);
        progressDialog = new ProgressDialog(this);
        userId = getIntent().getStringExtra("userId");
        initBinding();
        initClick();
    }

    private void initBinding() {
        resto_name = findViewById(R.id.resto_name);
        resto_category = findViewById(R.id.resto_category);
        resto_imgurl = findViewById(R.id.resto_imgurl);
        resto_address = findViewById(R.id.resto_address);
        create_restaurant = findViewById(R.id.btn_create_restaurant);
    }

    private void initClick() {
        create_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resto_name.getText().toString().isEmpty()){
                    Toast.makeText(RestaurantActivity.this, "Nama harus diisi", Toast.LENGTH_SHORT).show();
                }else if(resto_category.getText().toString().isEmpty()){
                    Toast.makeText(RestaurantActivity.this, "Kategori harus diisi", Toast.LENGTH_SHORT).show();
                }else if(resto_imgurl.getText().toString().isEmpty()){
                    Toast.makeText(RestaurantActivity.this, "Url Gambar harus diisi", Toast.LENGTH_SHORT).show();
                }else if(resto_address.getText().toString().isEmpty()){
                    Toast.makeText(RestaurantActivity.this, "Alamat harus diisi", Toast.LENGTH_SHORT).show();
                } else {
                    createRestaurant();
                }
            }
        });
    }
    public void createRestaurant() {
        DialogUtils.openDialog(this);
        AndroidNetworking.post(Constans.CREATE_RESTAURANT)
                .addBodyParameter("userid", userId)
                .addBodyParameter("resto_name", resto_name.getText().toString())
                .addBodyParameter("resto_category", resto_category.getText().toString())
                .addBodyParameter("resto_imgurl", resto_imgurl.getText().toString())
                .addBodyParameter("resto_address", resto_address.getText().toString())
                .build()
                .getAsObject(RestaurantAction.class, new ParsedRequestListener() {
                    @Override
                    public void onResponse(Object response) {
                        if (response instanceof RestaurantAction) {
                            RestaurantAction res = (RestaurantAction) response;
                            if (res.getStatus().equals("success")) {
                                Toast.makeText(RestaurantActivity.this,"Berhasil menambah restauran", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(RestaurantActivity.this,"Gagal menambah restauran", Toast.LENGTH_SHORT).show();
                            }
                        }
                        DialogUtils.closeDialog();
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(RestaurantActivity.this, "Terjadi kesalahan API", Toast.LENGTH_SHORT).show();
                        Toast.makeText(RestaurantActivity.this, "Terjadi kesalahan API : "+anError.getCause().toString(), Toast.LENGTH_SHORT).show();
                        DialogUtils.closeDialog();
                    }
                });
    }

}
