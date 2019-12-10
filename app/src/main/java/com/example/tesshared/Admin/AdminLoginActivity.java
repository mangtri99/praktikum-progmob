package com.example.tesshared.Admin;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tesshared.ApiHelper.BaseApiHelper;
import com.example.tesshared.ApiHelper.UtilsApi;
import com.example.tesshared.Login;
import com.example.tesshared.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AdminLoginActivity extends AppCompatActivity {

    TextView user;
    EditText etEmail;
    EditText etPassword;
    Button btnLogin;
    ProgressDialog loading;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences2;
    Context mContext;
    BaseApiHelper mApiService;
    boolean session = false;

    String token;
    Integer id_user;
    Integer admin;
    final String SHARED_PREFERENCES_NAME = "shared_preferences";
    final String SHARED_PREFERENCES_test = "shared_preferences";
    final String SESSION_STATUS = "session";
    public final static String TAG_TOKEN = "token";
    public final static Integer TAG_ID = 0;
    public final static Integer TAG_ADMIN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        mContext = this;
        mApiService = UtilsApi.getAPIService(); // meng-init yang ada di package apihelper
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        sharedPreferences2 = getSharedPreferences(SHARED_PREFERENCES_test, Context.MODE_PRIVATE);
        session = sharedPreferences.getBoolean(SESSION_STATUS, false);
        token = sharedPreferences.getString(TAG_TOKEN, null);
        id_user = sharedPreferences.getInt(String.valueOf(TAG_ID), 0);
        admin = sharedPreferences.getInt(String.valueOf(TAG_ADMIN), 0);
        Log.d("asasa", "onCreate: admin  " + admin);
        if (admin == 1) {
            Intent intent = new Intent(AdminLoginActivity.this, AdminActivity.class);
            intent.putExtra(TAG_TOKEN, token);
            intent.putExtra(String.valueOf(TAG_ID), id_user);
            intent.putExtra(String.valueOf(TAG_ADMIN), admin);
            finish();
            startActivity(intent);
        }
        initComponents();
    }

    private void initComponents() {
        etEmail = (EditText) findViewById(R.id.email_admin);
        etPassword = (EditText) findViewById(R.id.password_admin);
        btnLogin = (Button) findViewById(R.id.btn_login_admin);
        user = (TextView) findViewById(R.id.text_user);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                Login();
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, Login.class));
            }
        });
    }

    private void Login() {
        mApiService.loginRequest(etEmail.getText().toString(), etPassword.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("success")) {
                                    // Jika login berhasil maka data nama yang ada di response API
                                    // akan diparsing ke activity selanjutnya.

                                    String sukses = jsonRESULTS.getJSONObject("data").getString("token");
                                    Log.d("wanjay", "onResponse: " + sukses);
                                    String name = jsonRESULTS.getJSONObject("user").getString("name");
                                    Integer id = jsonRESULTS.getJSONObject("user").getInt("id");
                                    Log.d("id_user", "onResponse: " + id);
                                    Integer is_admin = jsonRESULTS.getJSONObject("user").getInt("is_admin");
                                    Log.d("admin", "onResponse: " + is_admin);
                                    token = sukses;
                                    if (is_admin == 1) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean(SESSION_STATUS, true);
                                        editor.putString(TAG_TOKEN, token);
                                        editor.putInt(String.valueOf(TAG_ID), jsonRESULTS.getJSONObject("user").getInt("id"));
                                        editor.putInt(String.valueOf(TAG_ADMIN), is_admin);
                                        Log.d("login admin", "dalam login admin :" + is_admin);
                                        editor.apply();
                                        Toast.makeText(mContext, "Login Sebagai Admin " + id, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(mContext, AdminActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(mContext, "Anda bukan admin goblok", Toast.LENGTH_LONG).show();

                                    }
                                } else {
                                    // Jika login gagal
                                    Toast.makeText(mContext, "EEROOR", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        loading.dismiss();
                    }

                });
    }
}
