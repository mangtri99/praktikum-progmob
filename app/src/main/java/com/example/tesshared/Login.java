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

import com.example.tesshared.ApiHelper.BaseApiHelper;
import com.example.tesshared.ApiHelper.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Login extends AppCompatActivity {
    private EditText mViewUser, mViewPassword;
    EditText email;
    EditText password;
    Button btn_login;
    TextView text_register;
    ProgressDialog loading;
    SharedPreferences sharedPreferences;

    boolean session = false;
    Context mContext;
    BaseApiHelper mApiService;

    String token;
    final String SHARED_PREFERENCES_NAME = "shared_preferences";
    final String SESSION_STATUS = "session";
    public final static String TAG_TOKEN = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        mApiService = UtilsApi.getAPIService();
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        session = sharedPreferences.getBoolean(SESSION_STATUS, false);
        token = sharedPreferences.getString(TAG_TOKEN, null);
        if (session){
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.putExtra(TAG_TOKEN, token);
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

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                requestLogin();
            }
        });

        text_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, Register.class));
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
                                Log.d("anjya", "onResponse: "+test);
                                token = test;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(mContext, "Berhasil Login", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(SESSION_STATUS, true);
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

                                        String name = jsonRESULTS.getJSONObject("data").getString("token");
                                        Log.d("wanjay", "onResponse: "+name);
                                        token = name;
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean(SESSION_STATUS, true);
                                        editor.putString(TAG_TOKEN,token );
                                        editor.apply();
                                        Toast.makeText(mContext, "token "+name, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(mContext, MainActivity.class);
                                        startActivity(intent);
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
