package com.pm.bloodserve.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pm.bloodserve.R;
import com.pm.bloodserve.adapter.RequestBloodAdapter;
import com.pm.bloodserve.models.UserCustomDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;




public class HomeFragment extends Fragment {

    private View myview;
    private RecyclerView recyclerView;

    private DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    private RequestBloodAdapter requestBloodAdapter;
    private List<UserCustomDataModel> dataModelList;
    private ProgressDialog progressDialog;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myview = inflater.inflate(R.layout.home_fragment, container, false);
        recyclerView = myview.findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseReference = FirebaseDatabase.getInstance().getReference();
        dataModelList = new ArrayList<>();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();
        getActivity().setTitle("Blood Serve");

        requestBloodAdapter = new RequestBloodAdapter(dataModelList);
        RecyclerView.LayoutManager pmLayout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(pmLayout);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(requestBloodAdapter);

        AddPosts();
        return myview;

    }
    private void AddPosts()
    {
        Query posts = databaseReference.child("posts");
        progressDialog.show();
        posts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {

                    for (DataSnapshot singlepost : dataSnapshot.getChildren()) {
                        UserCustomDataModel customUserData = singlepost.getValue(UserCustomDataModel.class);
                        dataModelList.add(customUserData);
                        requestBloodAdapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "DB is empty!",
                            Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d("User", databaseError.getMessage());

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
