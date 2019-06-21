package com.example.facultyactivity;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FacultyCourseAdapter extends ArrayAdapter<Courses> {
    private Context mContext;
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
        Button addatt=(Button)convertView.findViewById(R.id.add);
        Button showatt=(Button)convertView.findViewById(R.id.show);
        addatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContext instanceof MainActivity){
                    Courses thisCourse=getItem((position));
                    ((FacultyActivity)mContext).permitaddatt(thisCourse);
                }
            }
        });
        showatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContext instanceof MainActivity){
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
