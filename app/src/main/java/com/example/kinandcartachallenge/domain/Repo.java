package com.example.kinandcartachallenge.domain;

import androidx.lifecycle.LiveData;

import com.example.kinandcartachallenge.data.model.Contact;
import com.example.kinandcartachallenge.vo.StateData;

import java.util.List;

public interface Repo {
    LiveData<StateData<List<Contact>>> fechContacts();
}
