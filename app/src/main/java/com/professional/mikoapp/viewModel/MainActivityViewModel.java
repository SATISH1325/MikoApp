package com.professional.mikoapp.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.professional.mikoapp.model.DataModel;
import com.professional.mikoapp.network.APIClient;
import com.professional.mikoapp.repository.Miko;
import com.professional.mikoapp.repository.MikoRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends AndroidViewModel {

    private static final String TAG = MainActivityViewModel.class.getSimpleName();

    public MutableLiveData<List<Miko>> recordMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> recordMutableLiveDataFailureResponse = new MutableLiveData<>();

    private LiveData<List<Miko>> allRecords;

    private MikoRepository mikoRepository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mikoRepository = new MikoRepository(application);
        allRecords = mikoRepository.getAllRecords();

    }

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

                        List<DataModel.Record> recordList = data.records;

                        mikoRepository.isRowIsExist(recordList);

                        /*if (recordList != null && recordList.size() > 0) {
                            for (int i = 0; i < recordList.size(); i++) {

                                Miko miko = new Miko();
                                miko.setTitle(recordList.get(i).title);
                                miko.setShortDescription(recordList.get(i).shortDescription);
                                miko.setCollectedValue(recordList.get(i).collectedValue);
                                miko.setTotalValue(recordList.get(i).totalValue);
                                miko.setStartDate(recordList.get(i).startDate);
                                miko.setEndDate(recordList.get(i).endDate);
                                miko.setMainImageURL(recordList.get(i).mainImageURL);

                                mikoRepository.insert(miko);

                            }
                        }*/

                        /*recordMutableLiveData.setValue(mikoDao.getRecords());*/

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


    public LiveData<List<Miko>> getAllRecords() {
        return allRecords;
    }

}


