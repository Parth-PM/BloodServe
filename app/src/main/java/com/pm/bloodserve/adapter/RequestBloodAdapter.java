package com.pm.bloodserve.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pm.bloodserve.R;
import com.pm.bloodserve.models.UserCustomDataModel;

import java.util.List;


public class RequestBloodAdapter extends RecyclerView.Adapter<RequestBloodAdapter.PostHolder> {


    private List<UserCustomDataModel> postLists;

    public class PostHolder extends RecyclerView.ViewHolder
    {
        TextView name, bloodgroup, address, contactnumber, posted;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.reqstUser);
            contactnumber = itemView.findViewById(R.id.targetCN);
            bloodgroup = itemView.findViewById(R.id.targetBG);
            address = itemView.findViewById(R.id.reqstLocation);
            posted = itemView.findViewById(R.id.posted);

        }
    }

    public RequestBloodAdapter(List<UserCustomDataModel> postLists)
    {
        this.postLists = postLists;
    }

    @Override
    public PostHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View listitem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.bloodrequest_item, viewGroup, false);

        return new PostHolder(listitem);
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

        UserCustomDataModel customUserData = postLists.get(i);
        postHolder.contactnumber.setText(customUserData.getContact());
        postHolder.bloodgroup.setText("Needs "+customUserData.getBloodGroup());
        postHolder.name.setText("Posted by: "+customUserData.getName());
        postHolder.address.setText("From: "+customUserData.getAddress()+", "+customUserData.getDivision());
        postHolder.posted.setText("Posted on:"+customUserData.getTime()+", "+customUserData.getDate());


    }

    @Override
    public int getItemCount() {
        return postLists.size();
    }
}
