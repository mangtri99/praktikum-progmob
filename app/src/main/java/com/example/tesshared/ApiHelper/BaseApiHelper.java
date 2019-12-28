package com.example.tesshared.ApiHelper;

import com.example.tesshared.Produk.GetProduk;
import com.example.tesshared.ui.notifications.ValueUser;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BaseApiHelper {
    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> loginRequest(@Field("email") String email,
                                    @Field("password") String password);

    // Fungsi ini untuk memanggil API http://10.0.2.2/mahasiswa/register.php
    @FormUrlEncoded
    @POST("register")
    Call<ResponseBody> registerRequest(@Field("name") String nama,
                                       @Field("email") String email,
                                       @Field("password") String password,
                                       @Field("c_password") String c_password);

    @FormUrlEncoded
    @POST("pemesanan")
    Call<ResponseBody> addProduk(@Field("user_id") int user_id,
                                 @Field("produk_id") int produk_id,
                                 @Field("nama_pengirim") String nama_pengirim,
                                 @Field("nama_penerima") String nama_penerima,
                                 @Field("alamat") String alamat,
                                 @Field("ucapan") String ucapan,
                                 @Field("catatan") String catatan,
                                 @Field("total_harga") int total_harga,
                                 @Field("status") String status);

    @GET("produk")
    Call<GetProduk> getProduk();

    @FormUrlEncoded
    @PUT("updatePasswor/")
    Call<ResponseBody> updatePassword(@Field("id") int id, @Field("pass_lama") String pass_lama, @Field("pass_baru") String pass_baru);


    @GET("produk/{id}")
    Call<GetProduk> getProdukDetail(@Path("id") int id);

    @GET("show/{id}")
    Call<ValueUser> viewUser(@Path("id") int id);

    @FormUrlEncoded
    @PUT("updateProfile/")
    Call<ResponseBody> updateProfile(@Field("id") int id, @Field("email") String email, @Field("name") String name);
}
