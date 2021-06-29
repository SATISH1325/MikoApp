package com.professional.mikoapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.professional.mikoapp.R;
import com.professional.mikoapp.model.DataModel;
import com.professional.mikoapp.network.APIClient;
import com.professional.mikoapp.network.ApiInterface;
import com.professional.mikoapp.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv_recordList;

    private TextView tv_emptyMessage;

    private CustomRecyclerViewAdapter customRecyclerViewAdapter;

    private ArrayList<DataModel.Record> dataModelArrayList;

    private ProgressBar progress_circular;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {

        rv_recordList = findViewById(R.id.rv_recordList);

        tv_emptyMessage = findViewById(R.id.tv_emptyMessage);

        dataModelArrayList = new ArrayList<>();

        progress_circular = findViewById(R.id.progress_circular);
        progress_circular.setVisibility(View.VISIBLE);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        rv_recordList.setLayoutManager(layoutManager);

        try {

            if (Utilities.isNetworkAvailable(this)) {
                loadData();
            } else {
                Toast.makeText(this, "Network Unavailable", Toast.LENGTH_SHORT).show();
                progress_circular.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

    private void loadData() {
        try {
            Call<DataModel> call = APIClient.getClient().fetchData();
            call.enqueue(new Callback<DataModel>() {
                @Override
                public void onResponse(Call<DataModel> call, Response<DataModel> response) {
                    if (response != null && response.isSuccessful()) {
                        Log.i("Response", "Success" + response.body());
                        DataModel dataModel = response.body();
                        DataModel.Data data = dataModel.data;

                        List<DataModel.Record> recordsList = data.records;
                        dataModelArrayList = new ArrayList<>();

                        if (recordsList != null && recordsList.size() > 0) {
                            dataModelArrayList.addAll(recordsList);
                            customRecyclerViewAdapter = new CustomRecyclerViewAdapter(MainActivity.this, dataModelArrayList);
                            rv_recordList.setAdapter(customRecyclerViewAdapter);
                            tv_emptyMessage.setVisibility(View.GONE);
                        } else {
                            tv_emptyMessage.setVisibility(View.VISIBLE);
                        }

                    }

                    progress_circular.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<DataModel> call, Throwable t) {
                    Log.i("Response", "Failure response" + t.getMessage());
                    progress_circular.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }
}