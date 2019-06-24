package com.example.facultyactivity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FacultyCourseAdapter extends ArrayAdapter<Courses> {
    private Context mContext;
    FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mStatusDatabaseReference;
    public FacultyCourseAdapter(Context context, int resource, List<Courses> objects) {
        super(context, resource,objects);
        this.mContext=context;
    }
    public View getView(final int position, View convertView, ViewGroup parent){


        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.facultylist_item, parent, false);
        }
        TextView courseTextView = (TextView) convertView.findViewById(R.id.course);
        TextView facultyTextView = (TextView) convertView.findViewById(R.id.faculty);
        TextView emailsTextView = (TextView) convertView.findViewById(R.id.emails);
        emailsTextView.setSelected(true);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mStatusDatabaseReference=mFirebaseDatabase.getReference().child("Status");
        Button addatt=(Button)convertView.findViewById(R.id.add);
        Button showatt=(Button)convertView.findViewById(R.id.show);
        final Courses thiscourse=getItem(position);
        addatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query= mStatusDatabaseReference.child(thiscourse.year).child(thiscourse.courseName);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String status=(String)dataSnapshot.getValue();
                        if(mContext instanceof FacultyActivity){
                            Courses thisCourse=getItem((position));
                            ((FacultyActivity)mContext).permitaddatt(thisCourse,status);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        showatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContext instanceof FacultyActivity){
                    Courses thisCourse=getItem((position));
                    ((FacultyActivity)mContext).showAtt(thisCourse);
                }
            }
        });
        Courses course=getItem(position);
        courseTextView.setText(course.getCourseName());
        facultyTextView.setText(course.getCourseFaculty());
        ArrayList<String> emails=course.getEmails();
        String allemails="";
        if(!emails.isEmpty()) {
            for (int i = 0; i < emails.size(); i++) {
                String now = emails.get(i);
                allemails = allemails + now + "\n";
            }
        }
        emailsTextView.setText(allemails);


        return convertView;
    }
}