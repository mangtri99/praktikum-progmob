package com.example.tesshared.Produk;

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
import android.widget.Toast;

import com.example.tesshared.ApiHelper.BaseApiHelper;
import com.example.tesshared.ApiHelper.UtilsApi;
import com.example.tesshared.Login;
import com.example.tesshared.MainActivity;
import com.example.tesshared.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Pemesanan extends AppCompatActivity {
    EditText mNamaPengirim, mNamaPenerima, mAlamat, mUcapan, mCatatan;
    Button mCheck;
    BaseApiHelper mApiService;
    SharedPreferences sharedPreferences;
    boolean session = false;
    int userId;
    String token;
    final String SHARED_PREFERENCES_NAME = "shared_preferences";
    final String SESSION_STATUS = "session";
    public final static String TAG_TOKEN = "token";
    public final static Integer TAG_ID = 0;
    public final static Integer TAG_ADMIN = 0;
    int idProduk;
    int hargaProduk;
    String namaProduk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);
        sharedPreferences = this.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        session = sharedPreferences.getBoolean(SESSION_STATUS, false);
        token = sharedPreferences.getString(TAG_TOKEN, null);
        userId = sharedPreferences.getInt(String.valueOf(TAG_ID), 0);
        mNamaPenerima = findViewById(R.id.etNamaPenerima);
        mNamaPengirim = findViewById(R.id.etNamaPengirim);
        mAlamat = findViewById(R.id.etAlamat);
        mUcapan = findViewById(R.id.etUcapan);
        mCatatan = findViewById(R.id.etCatatan);
        mCheck = findViewById(R.id.btn_checkout);
        idProduk = getIntent().getIntExtra("idProduk",0);
        hargaProduk = getIntent().getIntExtra("hargaProduk", 0);
        namaProduk = getIntent().getStringExtra("namaProduk");
        mApiService = UtilsApi.getAPIService();
        mCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertPemesanan();
            }
        });
        Log.e("USERID", namaProduk+"");
    }


    private void insertPemesanan() {
        Log.e("LOLOK", "insertPemesanan: ");
        mApiService.addProduk(
                userId,
                idProduk,
                mNamaPengirim.getText().toString(),
                mNamaPenerima.getText().toString(),
                mAlamat.getText().toString(),
                mUcapan.getText().toString(),
                mCatatan.getText().toString(),
                hargaProduk + 50000,
                "Menunggu Pembayaran"
        )
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Log.e("debug", "onResponse: BERHASIL");
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Log.e("debug", "onResponse: GA BERHASIL");

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());

                    }
                });
    }
}
