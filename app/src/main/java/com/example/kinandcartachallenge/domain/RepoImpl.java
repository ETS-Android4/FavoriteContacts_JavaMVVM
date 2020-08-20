package com.example.kinandcartachallenge.domain;

import androidx.lifecycle.LiveData;

import com.example.kinandcartachallenge.data.model.Contact;
import com.example.kinandcartachallenge.data.remote.DataSource;
import com.example.kinandcartachallenge.vo.StateData;

import java.util.List;

public class RepoImpl implements Repo{
    private DataSource dataSource;

    public RepoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public LiveData<StateData<List<Contact>>> fechContacts() {
        return dataSource.fechContacts();
    }
}
