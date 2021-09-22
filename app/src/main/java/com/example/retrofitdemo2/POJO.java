package com.example.retrofitdemo2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class POJO {
    @SerializedName("success")
    @Expose
    int success;

    @SerializedName("message")
    @Expose
    String message;

    public int getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
