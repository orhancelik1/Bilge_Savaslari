package com.example.bilgisavaslari;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InformationApi {

    @GET("questions")
    Call<List<Post>> getPost();
}
