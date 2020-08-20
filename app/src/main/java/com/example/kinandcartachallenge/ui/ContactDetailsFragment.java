package com.example.kinandcartachallenge.ui;

/**
 * Created by Lorenzo on 20,August,2020
 */

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.transition.Slide;

import com.bumptech.glide.Glide;
import com.example.kinandcartachallenge.R;
import com.example.kinandcartachallenge.data.model.Address;
import com.example.kinandcartachallenge.data.model.Contact;
import com.example.kinandcartachallenge.data.model.Phone;
import com.example.kinandcartachallenge.data.remote.DataSource;
import com.example.kinandcartachallenge.databinding.FragmentContactDetailsBinding;
import com.example.kinandcartachallenge.domain.RepoImpl;
import com.example.kinandcartachallenge.presentation.MainViewModel;
import com.example.kinandcartachallenge.presentation.ViewModelFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ContactDetailsFragment extends Fragment {
    private MainViewModel viewModel;
    private FragmentContactDetailsBinding fragmentContactDetailsBinding;
    private Contact mContact;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        this.setEnterTransition(new Slide(Gravity.END));
        this.setExitTransition(new Slide(Gravity.START));

        fragmentContactDetailsBinding = FragmentContactDetailsBinding.inflate(inflater, container, false);

        if (Objects.nonNull(getArguments())) {
            mContact = ContactDetailsFragmentArgs.fromBundle(requireArguments()).getSelectedContact();

            if (Objects.nonNull(mContact)) {
                //IMAGE
                setImageContact(mContact.getLargeImageURL());
                //NAME
                setNameInformation(mContact.getName());
                //COMPANY
                setCompanyInformation(mContact.getCompanyName());
                //PHONE
                setPhoneInformation(mContact.getPhone());
                //ADDRESS
                setAddressInformation(mContact.getAddress());
                //BIRTHDATE
                setBirthdateInformation(mContact.getBirthdate());
                //EMAIL
                setEmailAddressInformation(mContact.getEmailAddress());
                //FAVORITE STAR
                setFavoriteState(mContact.getFavorite());
            }
        }

        fragmentContactDetailsBinding.toolbarContactDetails.isFavorite.setOnClickListener(v -> {
            mContact.setFavorite(!mContact.getFavorite());
            viewModel.updateFavoriteContact(mContact);
            setFavoriteState(mContact.getFavorite());
        });

        fragmentContactDetailsBinding.toolbarContactDetails.containerBackToContacts.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_nav_contact_details_to_nav_contacts);
        });

        return fragmentContactDetailsBinding.getRoot();
    }

    private void setFavoriteState(Boolean favorite) {
        if (Objects.nonNull(favorite))
            fragmentContactDetailsBinding.toolbarContactDetails
                    .isFavorite
                    .setImageResource(favorite ? R.drawable.favorite_selected : R.drawable.favorite_unselected);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(new RepoImpl(new DataSource()))).get(MainViewModel.class);
    }

    private void setImageContact(String largeImageURL) {
        Glide.with(requireContext())
                .load(largeImageURL)
                .placeholder(R.drawable.l_user_icon)
                .into(fragmentContactDetailsBinding.contactLargeImage);
    }

    private void setCompanyInformation(String companyName) {
        fragmentContactDetailsBinding.contactCompany.setText(companyName);
    }

    private void setNameInformation(String name) {
        fragmentContactDetailsBinding.contactName.setText(name);
    }

    private void setEmailAddressInformation(@NonNull String emailAddress) {
        if (Objects.nonNull(emailAddress) && !emailAddress.isEmpty()) {
            fragmentContactDetailsBinding.emailAddress.contactDetailTitle.setText(getString(R.string.title_email));
            fragmentContactDetailsBinding.emailAddress.contactDetailData.setText(emailAddress);
            fragmentContactDetailsBinding.emailAddress.getRoot().setVisibility(View.VISIBLE);
        }
    }

    private void setBirthdateInformation(@NonNull String birthdate) {
        if (Objects.nonNull(birthdate) && !birthdate.isEmpty()) {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date;
            try {
                date = (Date) formatter.parse(birthdate);
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }

            if (date != null) {
                SimpleDateFormat newFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                fragmentContactDetailsBinding.birthdate.contactDetailTitle.setText(getString(R.string.title_birthdate));
                fragmentContactDetailsBinding.birthdate.contactDetailData.setText(newFormat.format(date));
                fragmentContactDetailsBinding.birthdate.getRoot().setVisibility(View.VISIBLE);
            }
        }
    }

    private void setAddressInformation(@NonNull Address address) {
        StringBuilder stringBuilderAddress = new StringBuilder();

        if (Objects.nonNull(address)) {
            stringBuilderAddress.append(Objects.nonNull(address.getStreet()) ? address.getStreet() + "\n" : "");
            stringBuilderAddress.append(Objects.nonNull(address.getCity()) ? address.getCity() + ", " : "");
            stringBuilderAddress.append(Objects.nonNull(address.getState()) ? address.getState() + ", " : "");
            stringBuilderAddress.append(Objects.nonNull(address.getZipCode()) ? address.getZipCode() + ", " : "");
            stringBuilderAddress.append(Objects.nonNull(address.getZipCode()) ? address.getCountry() : "");
        }

        if (stringBuilderAddress.length() > 0) {
            fragmentContactDetailsBinding.address.contactDetailTitle.setText(getString(R.string.title_address));
            fragmentContactDetailsBinding.address.contactDetailData.setText(stringBuilderAddress);
            fragmentContactDetailsBinding.address.getRoot().setVisibility(View.VISIBLE);
        }
    }

    private void setPhoneInformation(@NonNull Phone phone) {
        if (Objects.nonNull(phone)) {
            //Home
            if (Objects.nonNull(phone.getHome()) && !phone.getHome().isEmpty()) {
                fragmentContactDetailsBinding.phoneHome.getRoot().setVisibility(View.VISIBLE);
                fragmentContactDetailsBinding.phoneHome.contactDetailTitle.setText(getString(R.string.title_phone));
                fragmentContactDetailsBinding.phoneHome.contactDetailType.setText(getString(R.string.home_phone_type));
                fragmentContactDetailsBinding.phoneHome.contactDetailData.setText(phone.getHome());
            }
            //Mobile
            if (Objects.nonNull(phone.getMobile()) && !phone.getMobile().isEmpty()) {
                fragmentContactDetailsBinding.phoneMobile.getRoot().setVisibility(View.VISIBLE);
                fragmentContactDetailsBinding.phoneMobile.contactDetailTitle.setText(getString(R.string.title_phone));
                fragmentContactDetailsBinding.phoneMobile.contactDetailType.setText(getString(R.string.mobile_phone_type));
                fragmentContactDetailsBinding.phoneMobile.contactDetailData.setText(phone.getMobile());
            }
            //Work
            if (Objects.nonNull(phone.getWork()) && !phone.getWork().isEmpty()) {
                fragmentContactDetailsBinding.phoneWork.getRoot().setVisibility(View.VISIBLE);
                fragmentContactDetailsBinding.phoneWork.contactDetailTitle.setText(getString(R.string.title_phone));
                fragmentContactDetailsBinding.phoneWork.contactDetailType.setText(getString(R.string.work_phone_type));
                fragmentContactDetailsBinding.phoneWork.contactDetailData.setText(phone.getWork());
            }
        }
    }

}
