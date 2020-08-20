package com.example.kinandcartachallenge.data.remote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.kinandcartachallenge.data.model.Contact;
import com.example.kinandcartachallenge.domain.service.WebService;
import com.example.kinandcartachallenge.vo.RetrofitService;
import com.example.kinandcartachallenge.vo.StateData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataSource {
    private WebService webService;

    public DataSource() {
        webService = RetrofitService.getService();
    }

    public LiveData<StateData<List<Contact>>> fechContacts() {
        MutableLiveData<StateData<List<Contact>>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(new StateData<List<Contact>>().loading());

        webService.getContacts().enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(new StateData<List<Contact>>().success(response.body()));
                }else{
                    mutableLiveData.setValue(new StateData<List<Contact>>().error(new Throwable(response.errorBody().toString())));
                }
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                mutableLiveData.setValue(new StateData<List<Contact>>().error(t));
            }
        });
        return mutableLiveData;
    }

}
