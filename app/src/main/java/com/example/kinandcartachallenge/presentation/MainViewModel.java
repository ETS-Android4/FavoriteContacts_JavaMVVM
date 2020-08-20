package com.example.kinandcartachallenge.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kinandcartachallenge.data.model.Contact;
import com.example.kinandcartachallenge.domain.Repo;
import com.example.kinandcartachallenge.vo.StateData;

import java.util.List;

public class MainViewModel extends ViewModel {
    private Repo repo;
    private MutableLiveData<List<Contact>> liveContacts = new MutableLiveData<>();

    public MainViewModel(Repo repo) {
        this.repo = repo;
    }

    public LiveData<StateData<List<Contact>>> getListContacts() {
        return repo.fechContacts();
    }

    public MutableLiveData<List<Contact>> getLiveContacts() {
        if (liveContacts == null) {
            liveContacts = new MutableLiveData<>();
        }
        return liveContacts;
    }

    public void setLiveContacts(List<Contact> liveContacts) {
        this.liveContacts.setValue(liveContacts);
    }

    public void updateFavoriteContact(Contact contact) {
        if (liveContacts.getValue() != null)
            if (!liveContacts.getValue().isEmpty())
                liveContacts.getValue().set(liveContacts.getValue().indexOf(contact), contact);
    }
}
