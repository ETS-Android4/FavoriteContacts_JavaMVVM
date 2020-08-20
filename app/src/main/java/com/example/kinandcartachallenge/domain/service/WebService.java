package com.example.kinandcartachallenge.domain.service;

import com.example.kinandcartachallenge.data.model.Contact;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WebService {
    @GET("contacts.json")
    Call<List<Contact>> getContacts();
}
