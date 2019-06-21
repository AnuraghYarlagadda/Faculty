package com.example.facultyactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowAttActivity extends AppCompatActivity {
    TextView text;
    private static List<RollNumbers> totalRoll =new ArrayList<>();
    private final List<RollNumbers> checkedRoll =new ArrayList<>();
    private FirebaseDatabase mFirebaseDatabase;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mAttendanceDatabaseReference,mRollDatabaseReference,mCoursesDatabaseReference,mTimeDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_att);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mCoursesDatabaseReference = mFirebaseDatabase.getReference().child("Courses");
        // mRollDatabaseReference=mFirebaseDatabase.getReference().child("Roll");
        mAttendanceDatabaseReference=mFirebaseDatabase.getReference().child("Attendance");
        mTimeDatabaseReference=mFirebaseDatabase.getReference().child("Time");
        final LinearLayout ll1 = (LinearLayout)findViewById(R.id.my_layout1);
        text=(TextView)findViewById(R.id.status);
        Intent intent = getIntent();
        final String value = intent.getStringExtra("CourseName");
        final String yearval=intent.getStringExtra("Year");
        Toast.makeText(this,"Value:"+value,Toast.LENGTH_LONG);
        Query query1=mTimeDatabaseReference.child(yearval).child(value);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String time= (String) dataSnapshot.getValue();
                text.setText("Last Updated On: "+time);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        text.setText(value);
        totalRoll.clear();
        Query query=mAttendanceDatabaseReference.child(yearval).child(value);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<RollNumbers>> t = new GenericTypeIndicator<List<RollNumbers>>() {};
                totalRoll = dataSnapshot.getValue(t);
                Log.e("hh",dataSnapshot.getValue()+"");
                getData(ll1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getData(LinearLayout ll){
        for(int i = 0; i < totalRoll.size(); i++) {
            Log.d("TAG", "onCreate: "+totalRoll.get(i).getRollnum());
            TextView cb = new TextView(this);
            cb.setText(totalRoll.get(i).getRollnum());
            cb.setId(i);
            ll.addView(cb);

        }
    }
}
