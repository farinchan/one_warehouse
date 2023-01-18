package com.gariskode.onewarehouse.retrofit;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.gariskode.onewarehouse.models.FinancesModels;
import com.gariskode.onewarehouse.models.GetCategory;
import com.gariskode.onewarehouse.models.GetFinancesModels;
import com.gariskode.onewarehouse.models.GetProductsModels;
import com.gariskode.onewarehouse.models.LoginModels;
import com.gariskode.onewarehouse.models.ProductModels;
import com.gariskode.onewarehouse.models.ProfileModels;
import com.gariskode.onewarehouse.models.RegisterModels;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiEndpoint {

    //Login
    @FormUrlEncoded
    @POST("login")
    Call<LoginModels> loginApi(@Field("email") String email,
                              @Field("password") String password);

    //Register
    @FormUrlEncoded
    @POST("register")
    Call<RegisterModels> registerApi(@Field("email") String email,
                                     @Field("password") String password,
                                     @Field("nameShop") String nameShop,
                                     @Field("address") String Address);

    @GET("profile")
    Call<ProfileModels> profileApi(@Header("auth-token") String token);

    //CATEGORY
    //Get Categories
    @GET("categories ")
    Call<GetCategory> getCategoryApi(@Header("auth-token") String token);

    //Add Category
    @FormUrlEncoded
    @POST("categories")
    Call<RegisterModels> addCategory(@Header("auth-token") String token,
                                    @Field("name") String name,
                                    @Field("slug") String slug);


    //FINANCE
    //Add Finances
    @FormUrlEncoded
    @POST("finances")
    Call<RegisterModels> addFinanse(@Header("auth-token") String token,
                                    @Field("name") String name,
                                    @Field("description") String description,
                                    @Field("date") String date,
                                    @Field("amount") Integer amount,
                                    @Field("note") String note);

    //get Finances
    @GET("finances")
    Call<GetFinancesModels> getFinance(@Header("auth-token") String token);

    //get Finances
    @GET("finances/search")
    Call<GetFinancesModels> getFinanceSearch(@Header("auth-token") String token,
                                             @Query("date") String date);
    //delete Products
    @DELETE("finances/delete")
    Call<FinancesModels> deleteFinance(@Header("auth-token") String token,
                                       @Query("financeId") int financeId);


    //PRODUCTS
    //get Products
    @GET("products")
    Call<GetProductsModels> getProducts(@Header("auth-token") String token,
                                        @Query("categoryId") int categoryId);

    //get search Products
    @GET("products/search")
    Call<GetProductsModels> getSearchProducts(@Header("auth-token") String token,
                                              @Query("search") String search);

    //Add Product
    @FormUrlEncoded
    @POST("products")
    Call<ProductModels> addProduct(@Header("auth-token") String token,
                                   @Field("name") String name,
                                   @Field("description") String description,
                                   @Field("barcode") String barcode,
                                   @Field("stock") Integer stock,
                                   @Field("sellingPrice") Integer sellingPrice,
                                   @Field("capitalPrice") Integer capitalPrice,
                                   @Field("category_id") Integer category_id);

    //update Product
    @FormUrlEncoded
    @PUT("products/update")
    Call<ProductModels> editProduct(@Header("auth-token") String token,
                                    @Query("productId") int productId,
                                   @Field("name") String name,
                                   @Field("description") String description,
                                   @Field("barcode") String barcode,
                                   @Field("stock") Integer stock,
                                   @Field("sellingPrice") Integer sellingPrice,
                                   @Field("capitalPrice") Integer capitalPrice,
                                   @Field("category_id") Integer category_id);

    //delete Products
    @DELETE("products/delete")
    Call<ProductModels> deleteProduct(@Header("auth-token") String token,
                                      @Query("productId") int productId);

}
