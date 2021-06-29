package com.professional.mikoapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.professional.mikoapp.R;
import com.professional.mikoapp.repository.Miko;
import com.professional.mikoapp.utilities.Utilities;
import com.professional.mikoapp.viewModel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv_recordList;

    private TextView tv_emptyMessage;

    private CustomRecyclerViewAdapter customRecyclerViewAdapter;

    private List<Miko> dataModelArrayList;

    private ProgressBar progress_circular;

    private static final String TAG = MainActivity.class.getSimpleName();

    MainActivityViewModel mainActivityViewModel;

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

        /*Instantiate ViwModel*/

        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        try {

            progress_circular.setVisibility(View.VISIBLE);

            if (Utilities.isNetworkAvailable(this)) {

                if (mainActivityViewModel != null) {
                    mainActivityViewModel.loadData();
                }

            } else {
                Toast.makeText(this, "Network Unavailable", Toast.LENGTH_SHORT).show();
            }

            displayData();


        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

    private void displayData() {

        mainActivityViewModel.getAllRecords().observe(this, new Observer<List<Miko>>() {
            @Override
            public void onChanged(List<Miko> recordList) {
                dataModelArrayList = new ArrayList<>();

                if (recordList != null && recordList.size() > 0) {
                    dataModelArrayList = recordList;
                    customRecyclerViewAdapter = new CustomRecyclerViewAdapter(MainActivity.this, dataModelArrayList);
                    rv_recordList.setAdapter(customRecyclerViewAdapter);
                    tv_emptyMessage.setVisibility(View.GONE);
                } else {
                    tv_emptyMessage.setVisibility(View.VISIBLE);
                }

                progress_circular.setVisibility(View.GONE);
            }
        });


        /*try {
            mainActivityViewModel.recordMutableLiveData.observe(this, recordList -> {
                dataModelArrayList = new ArrayList<>();

                if (recordList != null && recordList.size() > 0) {
                    dataModelArrayList = recordList;
                    customRecyclerViewAdapter = new CustomRecyclerViewAdapter(MainActivity.this, dataModelArrayList);
                    rv_recordList.setAdapter(customRecyclerViewAdapter);
                    tv_emptyMessage.setVisibility(View.GONE);
                } else {
                    tv_emptyMessage.setVisibility(View.VISIBLE);
                }

                progress_circular.setVisibility(View.GONE);
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }*/
    }

    private void failureResponse() {
        try {

            mainActivityViewModel.recordMutableLiveDataFailureResponse.observe(this, error -> {
                progress_circular.setVisibility(View.GONE);
            });

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

}