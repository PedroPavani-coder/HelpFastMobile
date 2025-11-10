package com.example.helpfastmobile;

import com.google.gson.annotations.SerializedName;

// Esta classe representa a estrutura do JSON de erro retornado pela API.
public class ApiErrorResponse {

    @SerializedName("error")
    private String message;

    public String getMessage() {
        return message;
    }
}
