package com.restaurant.rizalzaelani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.restaurant.rizalzaelani.auth.ChangePasswordActivity;
import com.restaurant.rizalzaelani.data.Constans;
import com.restaurant.rizalzaelani.data.Session;
import com.restaurant.rizalzaelani.model.LoginAction;
import com.restaurant.rizalzaelani.model.RestaurantAction;
import com.restaurant.rizalzaelani.utils.DialogUtils;

public class ProfileActivity extends AppCompatActivity {

    Session session;
    TextView nama, userId;
    Button btnChange;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        session = new Session(this);
        progressDialog = new ProgressDialog(this);
        initBinding();
        loadItem();
        initClick();
    }
    //Method untuk load data dari api
    public void loadItem() {
        //show progress dialog
        Toast.makeText(this, "id "+ session.getUserId(), Toast.LENGTH_SHORT).show();
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();
        AndroidNetworking.get(Constans.GET_PROFIL_USER + "/" + session.getUserId())
                .build()
                .getAsObject(LoginAction.class, new ParsedRequestListener() {
                    @Override
                    public void onResponse(Object response) {
                        progressDialog.dismiss();
                        if (((LoginAction) response).getLogin() != null)
                        {
                            loadDataRes(((LoginAction) response));
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void loadDataRes(LoginAction response) {
        nama.setText(response.getLogin().getNama());
        userId.setText(response.getLogin().getUserid());
    }
    private void initBinding() {
        nama = findViewById(R.id.tv_name);
        userId = findViewById(R.id.tv_user_id);
        btnChange = findViewById(R.id.btn_change);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItem();
    }

    private void initClick() {
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
                i.putExtra("nama", nama.getText().toString());
                startActivity(i);
            }
        });
    }

    //Method ini digunakan untuk menampilkan menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }
    //Menthod ini digunakan untuk menangani kejadian saat OptionMenu diklik
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_resto_create:
                Intent i = new Intent(ProfileActivity.this, RestaurantActivity.class);
                i.putExtra("userId", userId.getText().toString());
                startActivity(i);
                break;
            case R.id.menu_resto_delete:
                if (session.getUserId().isEmpty()){
                    Toast.makeText(ProfileActivity.this,"User ID tidak tersedia", Toast.LENGTH_SHORT).show();
                }else{
                    deleteRestaurant(userId.getText().toString());
                }
                break;
            case R.id.menu_logout:
                session.logoutUser();
                break;
        }
        return true;
    }

    public void deleteRestaurant(String userId) {
        DialogUtils.openDialog(this);
        AndroidNetworking.post(Constans.DELETE_RESTAURANT+"/"+userId)
                .addBodyParameter("userid", userId)
                .build()
                .getAsObject(RestaurantAction.class, new ParsedRequestListener() {
                    @Override
                    public void onResponse(Object response) {
                        if (response instanceof RestaurantAction) {
                            RestaurantAction res = (RestaurantAction) response;
                            if (res.getStatus().equals("success")) {
                                Toast.makeText(ProfileActivity.this,"Berhasil menghapus restauran", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ProfileActivity.this,"Gagal gagal menghapus restauran", Toast.LENGTH_SHORT).show();
                            }
                        }
                        DialogUtils.closeDialog();
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ProfileActivity.this, "Terjadi kesalahan API : "+anError.getCause().toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(ProfileActivity.this, "Terjadi kesalahan API", Toast.LENGTH_SHORT).show();
                        DialogUtils.closeDialog();
                    }
                });
    }

}
