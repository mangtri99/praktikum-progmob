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
import android.widget.TextView;

import com.example.tesshared.ApiHelper.BaseApiHelper;
import com.example.tesshared.ApiHelper.UtilsApi;
import com.example.tesshared.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileDetail extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    int id_user;
    final String SHARED_PREFERENCES_NAME = "shared_preferences";
    final String SESSION_STATUS = "session";
    public final static String TAG_TOKEN = "token";
    public final static int TAG_ID = 0;
    BaseApiHelper mApiService;
    public static final String URL = "http://10.0.2.2:8000/api/";
    private NotificationsViewModel notificationsViewModel;
    EditText mNama,mEmail;
    Button mEdit,mSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        id_user = sharedPreferences.getInt(String.valueOf(TAG_ID),0);
        Log.e("ID_USER", "onCreateView: "+id_user);
        mApiService = UtilsApi.getAPIService();
        mNama = findViewById(R.id.etNamaUser);
        mEmail = findViewById(R.id.etEmailUser);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BaseApiHelper api = retrofit.create(BaseApiHelper.class);
        Call<ValueUser> call = api.viewUser(id_user);
        call.enqueue(new Callback<ValueUser>() {
            @Override
            public void onResponse(Call<ValueUser> call, Response<ValueUser> response) {
                Log.e("AS", "onResponse: "+response.body().getEmail());
                mEmail.setText(response.body().getEmail());
                mNama.setText(response.body().getName());
            }

            @Override
            public void onFailure(Call<ValueUser> call, Throwable t) {

            }
        });

        mSave=findViewById(R.id.btn_simpan);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newEmail = mEmail.getText().toString();
                String newName = mNama.getText().toString();
                Log.e("email", newEmail);
                mApiService.updateProfile(id_user, newEmail, newName).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.e("BERHASIL", "DATA BERHASIL DIUPDATE");
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });

        mEdit = findViewById(R.id.btn_editPassword);
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditPassword.class));
            }
        });


    }
}
