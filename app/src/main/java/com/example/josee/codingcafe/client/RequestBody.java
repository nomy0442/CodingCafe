package com.example.josee.codingcafe.client;

import com.google.gson.annotations.SerializedName;

public class RequestBody {

    @SerializedName("request")
    private String requestString;

    public RequestBody()
    {
        this.requestString = "";

    }

    public RequestBody(String message)
    {
        this.requestString = message;

    }
}
