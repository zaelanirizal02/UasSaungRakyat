package com.restaurant.rizalzaelani.auth;

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
import com.restaurant.rizalzaelani.R;
import com.restaurant.rizalzaelani.data.Constans;
import com.restaurant.rizalzaelani.data.Session;
import com.restaurant.rizalzaelani.model.RegisterAction;
import com.restaurant.rizalzaelani.utils.DialogUtils;

public class ChangePasswordActivity extends AppCompatActivity {
    Session session;
    EditText name,oldPass,newPass;
    Button changeProfil;
    ProgressDialog progressDialog;
    String nama="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        session = new Session(this);
        progressDialog = new ProgressDialog(this);
        nama = getIntent().getStringExtra("nama");
        initBinding();
        initClick();
    }

    private void initBinding() {
        name = findViewById(R.id.et_name);
        oldPass = findViewById(R.id.et_old_password);
        newPass = findViewById(R.id.et_new_password);
        changeProfil = findViewById(R.id.btn_change_profil);
        name.setText(nama);
    }

    private void initClick() {
        changeProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty()){
                    Toast.makeText(ChangePasswordActivity.this, "Nama harus diisi", Toast.LENGTH_SHORT).show();
                }else if(oldPass.getText().toString().isEmpty()){
                    Toast.makeText(ChangePasswordActivity.this, "Password lama tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else if(newPass.getText().toString().isEmpty()){
                    Toast.makeText(ChangePasswordActivity.this, "Password baru tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    updateUser();
                }
            }
        });
    }
    public void updateUser() {
        DialogUtils.openDialog(this);
        AndroidNetworking.post(Constans.UPDATE_PROFILE_USER+"/"+session.getUserId())
                .addBodyParameter("nama", name.getText().toString())
                .addBodyParameter("old_password", oldPass.getText().toString())
                .addBodyParameter("new_password", newPass.getText().toString())
                .build()
                .getAsObject(RegisterAction.class, new ParsedRequestListener() {
                    @Override
                    public void onResponse(Object response) {
                        if (response instanceof RegisterAction) {
                            RegisterAction res = (RegisterAction) response;
                            if (res.getStatus().equals("success")) {
                                Toast.makeText(ChangePasswordActivity.this,"Berhasil Edit Profil", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ChangePasswordActivity.this,"Gagal Edit Profil", Toast.LENGTH_SHORT).show();
                            }
                        }
                        DialogUtils.closeDialog();
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ChangePasswordActivity.this, "Terjadi kesalahan API", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ChangePasswordActivity.this, "Terjadi kesalahan API : "+anError.getCause().toString(), Toast.LENGTH_SHORT).show();
                        DialogUtils.closeDialog();
                    }
                });
    }


}
