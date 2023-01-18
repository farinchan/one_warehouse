package com.gariskode.onewarehouse.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

private static String baseUrl = "http://vps1.fajri-dev.com:3000/api/toko/";
//private static String baseUrl = "http://10.0.2.2:3000/api/toko/";
//private static String baseUrl = "http://192.168.100.123:3000/api/toko/";

private static Retrofit retrofit;

public static ApiEndpoint apiEndpoint(){
    retrofit = new Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    return retrofit.create(ApiEndpoint.class);
}

}
