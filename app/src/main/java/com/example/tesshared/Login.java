package com.example.tesshared;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
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

import com.example.tesshared.Admin.AdminActivity;
import com.example.tesshared.Admin.AdminLoginActivity;
import com.example.tesshared.ApiHelper.BaseApiHelper;
import com.example.tesshared.ApiHelper.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Login extends AppCompatActivity {
    EditText email;
    EditText password;
    Button btn_login;
    TextView text_register, text_admin;
    ProgressDialog loading;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences2;

    Integer id_user;
    Integer admin;

    boolean session = false;
    Context mContext;
    BaseApiHelper mApiService;

    String token;
    final String SHARED_PREFERENCES_NAME = "shared_preferences";

    final String SESSION_STATUS = "session";
    public final static String TAG_TOKEN = "token";
    public final static Integer TAG_ID = 0;
    public final static Integer TAG_ADMIN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        mApiService = UtilsApi.getAPIService();
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        session = sharedPreferences.getBoolean(SESSION_STATUS, false);
        token = sharedPreferences.getString(TAG_TOKEN, null);
        id_user = sharedPreferences.getInt(String.valueOf(TAG_ID),0);
        admin = sharedPreferences.getInt(String.valueOf(TAG_ADMIN),0);
        if (admin==2){
            Intent intent = new Intent(Login.this, MainActivity.class);
            intent.putExtra(TAG_TOKEN, token);
            intent.putExtra(String.valueOf(TAG_ADMIN),admin);
            intent.putExtra(String.valueOf(TAG_ID),id_user);
            finish();
            startActivity(intent);
        }else if(admin==1){
            Intent intent = new Intent(Login.this, AdminActivity.class);
            intent.putExtra(TAG_TOKEN, token);
            intent.putExtra(String.valueOf(TAG_ID),id_user);
            intent.putExtra(String.valueOf(TAG_ADMIN),admin);
            finish();
            startActivity(intent);
        }

        initComponents();
    }

    private void initComponents() {
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        btn_login = (Button) findViewById(R.id.btn_login);
        text_register = (TextView) findViewById(R.id.text_register);
        text_admin = (TextView) findViewById(R.id.text_admin);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                Login();
            }
        });

        text_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, Register.class));
            }
        });

        text_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AdminLoginActivity.class));
            }
        });
    }


    private void requestLogin() {
        mApiService.loginRequest(email.getText().toString(), password.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String test = response.body().string();
                                Log.d("wanjya", "onResponse: "+test);
                                token = test;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(mContext, "Berhasil Login", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(SESSION_STATUS, true);
                            editor.putInt(String.valueOf(TAG_ADMIN),admin);
                            editor.putString(TAG_TOKEN,token );
                            editor.apply();
                            Intent intent = new Intent(mContext, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // Jika login gagal
                            Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
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

    private void Login() {
            mApiService.loginRequest(email.getText().toString(), password.getText().toString())
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
                                        Log.d("wanjay", "onResponse: "+sukses);
                                        String name = jsonRESULTS.getJSONObject("user").getString("name");
                                        Integer id = jsonRESULTS.getJSONObject("user").getInt("id");
                                        Log.d("id_user", "onResponse: "+id);
                                        Integer is_admin = jsonRESULTS.getJSONObject("user").getInt("is_admin");
                                        Log.d("admin", "onResponse: "+is_admin);
                                        token = name;

                                        if (is_admin==2){
                                            admin = is_admin;
                                            Log.d("admin", "onanjay "+admin);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putBoolean(SESSION_STATUS, true);
                                            editor.putString(TAG_TOKEN, token);
                                            editor.putInt(String.valueOf(TAG_ADMIN),admin);
                                            editor.putInt(String.valueOf(TAG_ID), jsonRESULTS.getJSONObject("user").getInt("id"));
                                            editor.apply();
                                            Toast.makeText(mContext, "ID ANDA " + id, Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(mContext, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            Toast.makeText(mContext, "Mungkin anda admin" , Toast.LENGTH_SHORT).show();
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
