package com.pm.bloodserve.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pm.bloodserve.R;
import com.pm.bloodserve.Dashboard;
import com.pm.bloodserve.models.DonorModel;
import com.pm.bloodserve.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.TimeZone;



public class AchievementFragment extends Fragment {

    private int currentDay, currentMonth, currentYear, day, month, year, totalday;
    private Calendar mycalendar;
    private ProgressDialog progressDialog;
    DatabaseReference databaseref, userref;
    FirebaseAuth firebaseAuth;

    private TextView allDonation, lastDonation, nextDonation, infoDonation;

    private String[] bloodGroup, division;
    private String lastDate;

    private View myview;
    private Button yesButton;
    private LinearLayout linearLayout;

    public AchievementFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.achievement_fragment, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        bloodGroup = getResources().getStringArray(R.array.Blood_Group);
        division = getResources().getStringArray(R.array.division_list);
        lastDonation = myview.findViewById(R.id.setLastDonate);
        allDonation = myview.findViewById(R.id.settotalDonate);
        infoDonation = myview.findViewById(R.id.donateInfo);

        getActivity().setTitle("Achievements");
        firebaseAuth  = FirebaseAuth.getInstance();
        lastDate = "";


        databaseref = FirebaseDatabase.getInstance().getReference("donors");
        userref = FirebaseDatabase.getInstance().getReference("users");

        Query userQ = userref.child(firebaseAuth.getCurrentUser().getUid());

        try {
            progressDialog.show();
            userQ.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists())
                    {
                        final UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        final int getdivision = userModel.getDivision();
                        final int getbloodgroup = userModel.getBloodGroup();
                        final Query donorQ = databaseref.child(division[getdivision])
                                .child(bloodGroup[getbloodgroup])
                                .child(firebaseAuth.getCurrentUser().getUid());

                        donorQ.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists())
                                {
                                    final DonorModel donorModel = dataSnapshot.getValue(DonorModel.class);
                                    allDonation.setText(donorModel.getTotalDonate()+" times");
                                    if(donorModel.getTotalDonate() == 0) {
                                        lastDate = "01/01/2001";
                                        lastDonation.setText("No donation yet!");
                                    }
                                    else {
                                        lastDate = donorModel.getLastDonate();
                                        lastDonation.setText(donorModel.getLastDonate());
                                    }

                                    totalday = 0;
                                    nextDonation = myview.findViewById(R.id.nextDonate);
                                    linearLayout = myview.findViewById(R.id.yesnolayout);
                                    if(lastDate.length() != 0) {

                                        int cnt = 0;
                                        int tot = 0;
                                        Log.e("parth", ""+lastDate);
                                        for (int i = 0; i < lastDate.length(); i++) {
                                            if (cnt == 0 && lastDate.charAt(i) == '/') {
                                                day = tot;
                                                tot=0;
                                                cnt+=1;

                                            } else if (cnt == 1 && lastDate.charAt(i) == '/') {
                                                cnt+=1;
                                                month = tot;
                                                tot=0;

                                            } else {
                                                tot = tot * 10 + (lastDate.charAt(i) - '0');
                                                Log.e("parth total", ""+tot);

                                            }
                                        }
                                        year = tot;
                                        mycalendar = Calendar.getInstance(TimeZone.getDefault());
                                        currentDay = mycalendar.get(Calendar.DAY_OF_MONTH);
                                        currentMonth = mycalendar.get(Calendar.MONTH)+1;
                                        currentYear = mycalendar.get(Calendar.YEAR);

                                        if(day>currentDay) {
                                            currentDay += 30;
                                            currentMonth -= 1;
                                        }
                                        totalday += (currentDay - day);

                                        if(month>currentMonth)
                                        {
                                            currentMonth+=12;
                                            currentYear -=1;
                                        }
                                        totalday += ((currentMonth - month)*30);

                                        totalday += ((currentYear - year)*365);

                                        try
                                        {
                                            if(totalday>120)
                                            {
                                                infoDonation.setText("Did you donate today?");
                                                nextDonation.setVisibility(View.GONE);
                                                linearLayout.setVisibility(View.VISIBLE);

                                                yesButton = myview.findViewById(R.id.btnYes);
                                               currentDay = mycalendar.get(Calendar.DAY_OF_MONTH);
                                               currentMonth = mycalendar.get(Calendar.MONTH)+1;
                                               currentYear = mycalendar.get(Calendar.YEAR);

                                                yesButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        databaseref.child(division[getdivision])
                                                                .child(bloodGroup[getbloodgroup])
                                                                .child(firebaseAuth.getCurrentUser().getUid())
                                                                .child("LastDonate").setValue(currentDay+"/"+currentMonth+"/"+currentYear);
                                                        databaseref.child(division[getdivision])
                                                                .child(bloodGroup[getbloodgroup])
                                                                .child(firebaseAuth.getCurrentUser().getUid())
                                                                .child("TotalDonate").setValue(donorModel.getTotalDonate()+1);
                                                        startActivity(new Intent(getActivity(), Dashboard.class));
                                                    }
                                                });
                                            }
                                            else
                                            {
                                                infoDonation.setText("Next donation will be in:");
                                                linearLayout.setVisibility(View.GONE);
                                                nextDonation.setVisibility(View.VISIBLE);
                                                nextDonation.setText((120-totalday)+" days");
                                            }
                                        } catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }

                                    }



                                }
                                else
                                {
                                    LinearLayout linearLayout = myview.findViewById(R.id.donorAchiev);
                                    linearLayout.setVisibility(View.GONE);
                                    TextView tv  = myview.findViewById(R.id.ShowInof);
                                    tv.setVisibility(View.VISIBLE);
                                    Toast.makeText(getActivity(), "Update the profile to be a donor first.", Toast.LENGTH_LONG)
                                            .show();
                                }
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                    else
                    {
                        Toast.makeText(getActivity(), "You are not a user."+division[0]+" "+bloodGroup[0], Toast.LENGTH_LONG)
                                .show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Log.d("User", databaseError.getMessage());
                }

            });


        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return myview;
    }

}
