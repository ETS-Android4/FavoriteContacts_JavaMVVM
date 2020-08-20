package com.example.kinandcartachallenge.ui;

/**
 * Created by Lorenzo on 20,August,2020
 */

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.emoji.bundled.BundledEmojiCompatConfig;
import androidx.emoji.text.EmojiCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.transition.Slide;

import com.example.kinandcartachallenge.R;
import com.example.kinandcartachallenge.data.model.Contact;
import com.example.kinandcartachallenge.data.model.Header;
import com.example.kinandcartachallenge.data.remote.DataSource;
import com.example.kinandcartachallenge.databinding.FragmentContactsBinding;
import com.example.kinandcartachallenge.domain.RepoImpl;
import com.example.kinandcartachallenge.presentation.MainViewModel;
import com.example.kinandcartachallenge.presentation.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


public class ContactsFragment extends Fragment {
    private static String TAG = ContactsFragment.class.getSimpleName();

    private AdapterContacts contactsAdapter;
    private ContactsFragmentDirections.ActionContactsToDetails actionContactsToDetails;
    private FragmentContactsBinding fragmentContactsBinding;
    private ArrayList<String> sections = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        this.setEnterTransition(new Slide(Gravity.END));
        this.setExitTransition(new Slide(Gravity.START));

        EmojiCompat.Config config = new BundledEmojiCompatConfig(requireContext());
        EmojiCompat.init(config);

        sections.add(getString(R.string.section_favorite_contacts));
        sections.add(getString(R.string.section_other_contacts));

        fragmentContactsBinding = FragmentContactsBinding.inflate(inflater, container, false);

        return fragmentContactsBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainViewModel viewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(new RepoImpl(new DataSource()))).get(MainViewModel.class);
        viewModel.getLiveContacts().observe(getViewLifecycleOwner(), getLiveContactsObserver);
    }

    private void groupAndSortContacts(List<Contact> contactList) {
        Log.d(TAG, "groupedContacts");
        Map<Boolean, List<Contact>> contactsGrouped = contactList.stream().collect(Collectors.groupingBy(Contact::getFavorite));

        List<Contact> groupedContacts = new ArrayList<>();

        Header header = new Header();
        header.setHeader(sections.get(0));
        groupedContacts.add(header);

        if (contactsGrouped.get(true) != null)
            groupedContacts.addAll(contactsGrouped.get(true)
                    .stream()
                    .sorted((object1, object2) -> object1.getName().compareTo(object2.getName())).collect(Collectors.toList()));

        header = new Header();
        header.setHeader(sections.get(1));
        groupedContacts.add(header);

        if (contactsGrouped.get(false) != null)
            groupedContacts.addAll(contactsGrouped.get(false)
                    .stream()
                    .sorted((object1, object2) -> object1.getName().compareTo(object2.getName())).collect(Collectors.toList()));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        fragmentContactsBinding.recyclerview.setLayoutManager(linearLayoutManager);
        contactsAdapter = new AdapterContacts(groupedContacts);
        fragmentContactsBinding.recyclerview.setAdapter(contactsAdapter);
        contactsAdapter.notifyDataSetChanged();

        contactsAdapter.setOnClickListener((position, v) -> {
            actionContactsToDetails = ContactsFragmentDirections.actionContactsToDetails(contactsAdapter.getItem(position));
            if (Objects.requireNonNull(Navigation.findNavController(v).getCurrentDestination()).getId() == R.id.nav_contacts) {
                Navigation.findNavController(v).navigate(actionContactsToDetails);
            }
        });
    }

    private Observer<? super List<Contact>> getLiveContactsObserver = (Observer<List<Contact>>) this::groupAndSortContacts;

}
