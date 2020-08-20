package com.example.kinandcartachallenge.ui;

/**
 * Created by Lorenzo on 20,August,2020
 */

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.kinandcartachallenge.R;
import com.example.kinandcartachallenge.data.remote.DataSource;
import com.example.kinandcartachallenge.domain.RepoImpl;
import com.example.kinandcartachallenge.presentation.MainViewModel;
import com.example.kinandcartachallenge.presentation.ViewModelFactory;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;
    private static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this, new ViewModelFactory(new RepoImpl(new DataSource()))).get(MainViewModel.class);
        viewModel.getListContacts().observe(this, listStateData -> {
            switch (listStateData.getStatus()) {
                case LOADING:
                    Log.d(TAG, "Loading");
                    break;
                case SUCCESS:
                    Log.d(TAG, "Success");
                    if (listStateData.getData() != null) {
                        viewModel.setLiveContacts(listStateData.getData());
                    }
                    break;
                case ERROR:
                    if (listStateData.getError() != null) {
                        Log.e(TAG, "Error:" + listStateData.getError().getMessage());
                        Toast.makeText(this, listStateData.getError().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        });
    }
}