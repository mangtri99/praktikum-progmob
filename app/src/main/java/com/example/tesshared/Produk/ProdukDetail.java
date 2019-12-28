package com.example.tesshared.Produk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tesshared.ApiHelper.BaseApiHelper;
import com.example.tesshared.ApiHelper.UtilsApi;
import com.example.tesshared.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProdukDetail extends AppCompatActivity {
    public static final String URL = "http://10.0.2.2:8000/api/";
    private List<ResultProduk> results = new ArrayList<>();
    private ProdukRecyclerViewAdapter viewAdapter;
    BaseApiHelper mApiService;
    int idProduk;
    int mIntentHarga;
    String mIntentNama;
    TextView mKode, mHarga, mKategori, mUkuran;
    Button mPemesanan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk_detail);
        mKode = findViewById(R.id.kode_produk);
        mHarga = findViewById(R.id.harga_produk);
        mUkuran = findViewById(R.id.ukuran_produk);
        mKategori = findViewById(R.id.kategori_produk);
        idProduk = getIntent().getIntExtra("id",0);
        mPemesanan = findViewById(R.id.btn_pesan);
        mApiService = UtilsApi.getAPIService();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BaseApiHelper api = retrofit.create(BaseApiHelper.class);
        Call<GetProduk> call = api.getProdukDetail(idProduk);
        Log.e("PROGRESSSS", "SUDAH SAMPAI SINI");
        call.enqueue(new Callback<GetProduk>() {
            @Override
            public void onResponse(Call<GetProduk> call, Response<GetProduk> response) {
                Log.e("PROGRESSSS", "SUDAH SAMPAI SINI2");
//                progressBar.setVisibility(View.GONE);
                String value = response.body().getStatus();
                results = response.body().getResult();
                mKode.setText(results.get(0).getKode());
                mIntentHarga =  results.get(0).getHarga();
                mIntentNama = results.get(0).getKode();
                idProduk = results.get(0).getId();
                mHarga.setText(String.valueOf(results.get(0).getHarga()));
                mUkuran.setText(results.get(0).getUkuran());
                mKategori.setText(results.get(0).getKategori());

            }

            @Override
            public void onFailure(Call<GetProduk> call, Throwable t) {

            }
        });

        mPemesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProdukDetail.this, Pemesanan.class);
                Log.e("harga", "onClick: " + mIntentHarga );
                i.putExtra("idProduk",idProduk);
                i.putExtra("namaProduk",mIntentNama);
                i.putExtra("hargaProduk",mIntentHarga);
                startActivity(i);

            }
        });


    }

}
