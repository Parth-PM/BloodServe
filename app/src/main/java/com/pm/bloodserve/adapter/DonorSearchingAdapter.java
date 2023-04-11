package com.pm.bloodserve.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pm.bloodserve.R;
import com.pm.bloodserve.models.DonorModel;

import java.util.List;



public class DonorSearchingAdapter extends RecyclerView.Adapter<DonorSearchingAdapter.PostHolder> {


    private List<DonorModel> DonorModelList;

    public class PostHolder extends RecyclerView.ViewHolder
    {
        TextView name, address, contactnumber, lastposted, totaldonation;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.donorName);
            contactnumber = itemView.findViewById(R.id.donorContact);
            totaldonation = itemView.findViewById(R.id.totaldonate);
            address = itemView.findViewById(R.id.donorAddress);
            lastposted = itemView.findViewById(R.id.lastdonate);

        }
    }

    public DonorSearchingAdapter(List<DonorModel> DonorLists)
    {
        this.DonorModelList = DonorLists;
    }

    @Override
    public PostHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View items = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.donorsearch_list, viewGroup, false);

        return new PostHolder(items);
    }

    @Override
    public void onBindViewHolder(PostHolder postHolder, int i) {

        if(i%2==0)
        {
            postHolder.itemView.setBackgroundColor(Color.parseColor("#C13F31"));
        }
        else
        {
            postHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        DonorModel donorDataModel = DonorModelList.get(i);
        postHolder.name.setText("Name: "+donorDataModel.getName());
        postHolder.contactnumber.setText(donorDataModel.getContact());
        postHolder.address.setText("Address: "+donorDataModel.getAddress());
        postHolder.totaldonation.setText("Total Donation: "+donorDataModel.getTotalDonate()+" times");
        postHolder.lastposted.setText("Last Donation: "+donorDataModel.getLastDonate());


    }

    @Override
    public int getItemCount() {
        return DonorModelList.size();
    }
}
