package com.professional.mikoapp.viewModel;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.professional.mikoapp.model.DataModel;
import com.professional.mikoapp.network.APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends ViewModel {

    private static final String TAG = MainActivityViewModel.class.getSimpleName();

    public MutableLiveData<List<DataModel.Record>> recordMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> recordMutableLiveDataFailureResponse = new MutableLiveData<>();

    public void loadData() {
        try {
            Call<DataModel> call = APIClient.getClient().fetchData();
            call.enqueue(new Callback<DataModel>() {
                @Override
                public void onResponse(Call<DataModel> call, Response<DataModel> response) {
                    if (response != null && response.isSuccessful()) {
                        Log.i("Response", "Success" + response.body());
                        DataModel dataModel = response.body();
                        DataModel.Data data = dataModel.data;

                        recordMutableLiveData.setValue(data.records);

                    }
                }

                @Override
                public void onFailure(Call<DataModel> call, Throwable t) {
                    Log.i("Response", "Failure response" + t.getMessage());
                    recordMutableLiveDataFailureResponse.setValue(t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }

}


