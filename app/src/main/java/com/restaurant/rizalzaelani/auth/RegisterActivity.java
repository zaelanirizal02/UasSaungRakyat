package com.restaurant.rizalzaelani.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.restaurant.rizalzaelani.R;
import com.restaurant.rizalzaelani.model.RegisterAction;
import com.restaurant.rizalzaelani.utils.DialogUtils;

import static com.restaurant.rizalzaelani.data.Constans.REGISTER;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegister;
    TextView txtLogin;
    EditText username;
    EditText name;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initBinding();
        initButton();
    }

    private void initBinding(){
        btnRegister = findViewById(R.id.btn_reg_sign_up);
        txtLogin = findViewById(R.id.txt_reg_link_login);
        username = findViewById(R.id.et_reg_input_email);
        name = findViewById(R.id.et_reg_input_name);
        password = findViewById(R.id.et_reg_input_password);
    }
    private void initButton(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this, "Nama harus diisi", Toast.LENGTH_SHORT).show();
                }else if(username.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this, "Username Tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else if(password.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this, "Password  Tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else {
                    register();
                }
            }
        });
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
    public void register() {
        DialogUtils.openDialog(this);
        AndroidNetworking.post(REGISTER)
                .addBodyParameter("userid", username.getText().toString())
                .addBodyParameter("password", password.getText().toString())
                .addBodyParameter("nama", name.getText().toString())
                .build()
                .getAsObject(RegisterAction.class, new ParsedRequestListener() {
                    @Override
                    public void onResponse(Object response) {
                        if (response instanceof RegisterAction) {
                            RegisterAction res = (RegisterAction)
                                    response;
                            if (res.getStatus().equals("success")) {
                                Toast.makeText(RegisterActivity.this,
                                        "Register Berhasil, silakan login kembali",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this,
                                        "Username sudah digunakan", Toast.LENGTH_SHORT).show();
                            }
                            DialogUtils.closeDialog();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(RegisterActivity.this,
                                "Terjadi kesalahan API", Toast.LENGTH_SHORT).show();
                        DialogUtils.closeDialog();
                    }
                });
    }


}
