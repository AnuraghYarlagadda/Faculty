package com.example.facultyactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FacultyActivity extends AppCompatActivity {
    int userexists=0;
    private TextView mTextMessage;
    private FacultyCourseAdapter mCourseAdapter;
    private FirebaseAuth mFirebaseAuth;
    private ListView mCourseListView;
    private ChildEventListener mChildEventListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mCoursesDatabaseReference,mRollDatabaseReference,mUsersDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mCoursesDatabaseReference = mFirebaseDatabase.getReference().child("Courses");
        mRollDatabaseReference=mFirebaseDatabase.getReference().child("Roll");
        mUsersDatabaseReference=mFirebaseDatabase.getReference().child("Users");
        final List<Courses> courses = new ArrayList<>();
        mCourseListView = (ListView) findViewById(R.id.list);
        mCourseAdapter = new FacultyCourseAdapter(this, R.layout.facultylist_item, courses);
        mCourseListView.setAdapter(mCourseAdapter);
        final String ccurrentuser=mFirebaseAuth.getCurrentUser().getEmail();
        mChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Courses course = dataSnapshot.getValue(Courses.class);
                Log.d("chek", course.getCourseName());
                ArrayList<String> emails=course.getEmails();
                if(emails.contains(ccurrentuser)) {
                    mCourseAdapter.add(course);
                    //courses.add(course);
                    Log.d("cheki", course.getCourseName());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Courses course = dataSnapshot.getValue(Courses.class);
                mCourseAdapter.remove(course);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mCoursesDatabaseReference.child("All").addChildEventListener(mChildEventListener);

    }
    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }
    public void showAtt(final Courses thisCourse){
        Query query= FirebaseDatabase.getInstance().getReference().child("Attendance").child(thisCourse.year).child(thisCourse.courseName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Intent intent=new Intent(FacultyActivity.this,ShowAttActivity.class);
                    intent.putExtra("CourseName",thisCourse.courseName);
                    intent.putExtra("Year",thisCourse.year);
                    FacultyActivity.this.startActivity(intent);
                } else {
                    Log.e("hhh", "N");
                    Toast.makeText(FacultyActivity.this, "Attendance Not Added", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public void permitaddatt(final Courses thisCourse){
        final String courseName=thisCourse.courseName;
        Query query= FirebaseDatabase.getInstance().getReference().child("Roll").child(thisCourse.year).child(courseName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Intent intent = new Intent(FacultyActivity.this, AddAttActivity.class);
                    intent.putExtra("CourseName", courseName);
                    intent.putExtra("Year",thisCourse.year);
                    FacultyActivity.this.startActivity(intent);
                    Log.e("hhhh", "Y" + "");
                } else {
                    Log.e("hhh", "N");
                    Toast.makeText(FacultyActivity.this, "Please Add Excel", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent x=new Intent(FacultyActivity.this,MainActivity.class);
        startActivity(x);
    }
}
