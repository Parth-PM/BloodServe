package com.pm.bloodserve.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pm.bloodserve.R;
import com.pm.bloodserve.adapter.DonorSearchingAdapter;
import com.pm.bloodserve.models.DonorModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class DonorFragment extends Fragment {

    private View myview;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, userRef;

    Spinner bloodGroup, division;
    Button Search;
    ProgressDialog progressDialog;
    List<DonorModel> donorList;
    private RecyclerView recyclerView;

    private DonorSearchingAdapter donorSearchingAdapter;

    public DonorFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myview = inflater.inflate(R.layout.donor_search_fragment, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);




        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("donors");

        bloodGroup = myview.findViewById(R.id.btngetBloodGroup);
        division = myview.findViewById(R.id.btngetDivison);
        Search = myview.findViewById(R.id.btnSearch);

        getActivity().setTitle("Search Donors");

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                donorList = new ArrayList<>();
                donorList.clear();
                donorSearchingAdapter = new DonorSearchingAdapter(donorList);
                recyclerView = (RecyclerView) myview.findViewById(R.id.showDonorList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                RecyclerView.LayoutManager donorSearch = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(donorSearch);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(donorSearchingAdapter);
                Query qpath  = databaseReference.child(division.getSelectedItem().toString())
                        .child(bloodGroup.getSelectedItem().toString());
                qpath.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       if(dataSnapshot.exists())
                       {
                           for(DataSnapshot singleitem : dataSnapshot.getChildren())
                           {
                               DonorModel donorData = singleitem.getValue(DonorModel.class);
                               donorList.add(donorData);
                               donorSearchingAdapter.notifyDataSetChanged();
                           }
                       }
                       else
                       {

                           Toast.makeText(getActivity(), "Database is empty!",
                                   Toast.LENGTH_LONG).show();

                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {
                       Log.d("User", databaseError.getMessage());

                   }
               });
               progressDialog.dismiss();
            }
        });
        return myview;
    }

}
