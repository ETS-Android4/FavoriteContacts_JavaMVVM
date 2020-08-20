package com.example.kinandcartachallenge.ui;

/**
 * Created by Lorenzo on 20,August,2020
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kinandcartachallenge.R;
import com.example.kinandcartachallenge.data.model.Contact;
import com.example.kinandcartachallenge.data.model.Header;
import com.example.kinandcartachallenge.databinding.ItemContactBinding;
import com.example.kinandcartachallenge.databinding.ItemSectionBinding;
import com.example.kinandcartachallenge.utils.ClickRecyclerListener;

import java.util.List;

public class AdapterContacts extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<Contact> contactArrayList;
    private ClickRecyclerListener clickRecyclerListener;
    private Context context;

    public AdapterContacts(List<Contact> contactArrayList) {
        this.contactArrayList = contactArrayList;
    }

    public void setOnClickListener(ClickRecyclerListener clickListener) {
        this.clickRecyclerListener = clickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER)
            return new ViewHolderSection(ItemSectionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        else {
            ViewHolderContacts view = new ViewHolderContacts(ItemContactBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            ;
            return view;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_HEADER:
                Header currentItem = (Header) contactArrayList.get(position);
                ((ViewHolderSection) holder).itemSectionBinding.section.setText(currentItem.getHeader());
                break;
            case TYPE_ITEM:
                Contact contact = (Contact) contactArrayList.get(position);
                ((ViewHolderContacts) holder).itemContactBinding.name.setText(contact.getName());
                ((ViewHolderContacts) holder).itemContactBinding.companyName.setText(contact.getCompanyName());
                ((ViewHolderContacts) holder).itemContactBinding.favoriteStar.setVisibility(contact.getFavorite() ? View.VISIBLE : View.INVISIBLE);
                ((ViewHolderContacts) holder).itemContactBinding.cardViewContact.setOnClickListener(v -> clickRecyclerListener.onItemClick(position, v));

                Glide.with(context)
                        .load(contact.getSmallImageURL())
                        .placeholder(R.drawable.s_user_icon)
                        .into(((ViewHolderContacts) holder).itemContactBinding.smallUserIcon);
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return contactArrayList.get(position) instanceof Header;
    }

    @Override
    public int getItemCount() {
        if (contactArrayList != null) return contactArrayList.size();
        else return 0;
    }

    public Contact getItem(int position) {
        return contactArrayList.get(position);
    }

    static class ViewHolderContacts extends RecyclerView.ViewHolder {

        private ItemContactBinding itemContactBinding;

        ViewHolderContacts(ItemContactBinding itemContactBinding) {
            super(itemContactBinding.getRoot());
            this.itemContactBinding = itemContactBinding;
        }
    }

    static class ViewHolderSection extends RecyclerView.ViewHolder {
        private ItemSectionBinding itemSectionBinding;

        ViewHolderSection(ItemSectionBinding itemSectionBinding) {
            super(itemSectionBinding.getRoot());
            this.itemSectionBinding = itemSectionBinding;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }
}
