package com.example.tesshared.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tesshared.ApiHelper.BaseApiHelper;
import com.example.tesshared.ApiHelper.UtilsApi;
import com.example.tesshared.MainActivity;
import com.example.tesshared.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditPassword extends AppCompatActivity {

    Context mContext;
    BaseApiHelper mApiService;
    SharedPreferences sharedPreferences;
    boolean session = false;
    String token;

    public static final String URL = "http://10.0.2.2:8000/api/";
    final String SHARED_PREFERENCES_NAME = "shared_preferences";
    final String SESSION_STATUS = "session";
    public final static String TAG_TOKEN = "token";
    public final static int TAG_ID = 0;
    String pass_lama;
    String pass_baru;
    int id_user;
    EditText etPassLama, etPassBaru;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);


        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        session = sharedPreferences.getBoolean(SESSION_STATUS, false);
        token = sharedPreferences.getString(TAG_TOKEN, null);
        id_user = sharedPreferences.getInt(String.valueOf(TAG_ID), 0);
        etPassLama = findViewById(R.id.etPasswordLama);
        etPassBaru = findViewById(R.id.etPasswordBaru);

        Button btnSave = findViewById(R.id.btn_simpan);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass_lama = etPassLama.getText().toString();
                pass_baru = etPassBaru.getText().toString();

                Log.e("DATA", id_user+pass_baru+pass_lama);
                mApiService = UtilsApi.getAPIService();

                mApiService.updatePassword(id_user, pass_lama, pass_baru).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.e("TEST", "onResponse: berhasil" );
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("TEST", "onResponse: gagal" );
                    }
                });


            }


        });

    }
}