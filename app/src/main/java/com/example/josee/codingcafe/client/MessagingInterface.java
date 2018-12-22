package com.example.josee.codingcafe.client;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MessagingInterface {
    @POST("/sendMessage")
    Call<ResponseBody> sendMessage(@Body RequestBody requestBody);
}