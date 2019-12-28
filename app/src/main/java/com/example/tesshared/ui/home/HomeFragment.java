package com.example.tesshared.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.tesshared.ApiHelper.BaseApiHelper;
import com.example.tesshared.ApiHelper.UtilsApi;
import com.example.tesshared.Login;
import com.example.tesshared.Produk.GetProduk;
import com.example.tesshared.Produk.ProdukRecyclerUserAdapter;
import com.example.tesshared.Produk.ProdukRecyclerViewAdapter;
import com.example.tesshared.Produk.ResultProduk;
import com.example.tesshared.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    Button btnKampus;
    public static final String URL = "http://10.0.2.2:8000/api/";
    private List<ResultProduk> results = new ArrayList<>();
    private ProdukRecyclerViewAdapter viewAdapter;
    BaseApiHelper mApiService;
    private static String EXTRA = "extra";
    String nama_univ = "";
    RecyclerView recyclerView;
    ProgressBar progressBar;
    FloatingActionButton gaskan;
    NestedScrollView test;
    TextView result_name;
    String resultName;

    Button btn_logout;

    SharedPreferences sharedPreferences;
    boolean session = false;
    String token;
    final String SHARED_PREFERENCES_NAME = "shared_preferences";
    final String SESSION_STATUS = "session";
    public final static String TAG_TOKEN = "token";
    public final static Integer TAG_ID = 0;
    public final static Integer TAG_ADMIN = 0;


    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        session = sharedPreferences.getBoolean(SESSION_STATUS, false);
        token = sharedPreferences.getString(TAG_TOKEN, null);
        // untuk mendapatkan data dari activity sebelumnya, yaitu activity login.

        /* Men-set Status dan User yang sedang login menjadi default atau kosong di
         * Data Preferences. Kemudian menuju ke LoginActivity*/

        btn_logout = (Button) root.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Menghapus Status login dan kembali ke Login Activity
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(SESSION_STATUS);
                editor.remove(TAG_TOKEN);
                editor.remove(String.valueOf(TAG_ID));
                editor.remove(String.valueOf(TAG_ADMIN));
                editor.apply();
                startActivity(new Intent(getActivity(), Login.class));
                getActivity().finish();
            }
        });
        recyclerView = (RecyclerView) root.findViewById(R.id.rv_home);
        mApiService = UtilsApi.getAPIService();
        viewAdapter = new ProdukRecyclerViewAdapter (getActivity(), results);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(viewAdapter);
        Log.e("m", "mantap");
        loadDataKategori();
        return root;
    }

    private void loadDataKategori() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BaseApiHelper api = retrofit.create(BaseApiHelper.class);
        Call<GetProduk> call = api.getProduk();
        Log.e("PROGRESSSS", "SUDAH SAMPAI SINI");
        call.enqueue(new Callback<GetProduk>() {
            @Override
            public void onResponse(Call<GetProduk> call, Response<GetProduk> response) {
                Log.e("PROGRESSSS", "SUDAH SAMPAI SINI2");
//                progressBar.setVisibility(View.GONE);
                String value = response.body().getStatus();
                results = response.body().getResult();
                Log.e("ERROR", "asa" + results.size());
                viewAdapter = new ProdukRecyclerViewAdapter(getActivity(), results);
                recyclerView.setAdapter(viewAdapter);
            }

            @Override
            public void onFailure(Call<GetProduk> call, Throwable t) {

            }
        });
    }

}