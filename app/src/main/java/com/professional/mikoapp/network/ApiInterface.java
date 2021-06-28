package com.professional.mikoapp.network;

import com.professional.mikoapp.model.DataModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("/testdata.json")
//    @GET("/api/unknown")
    Call<DataModel> fetchData();
}
