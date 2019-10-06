package com.jctubino.itunessearch;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

//This class will add a progressbar to the class that uses this
public abstract class BaseActivity extends AppCompatActivity {

    public ProgressBar mProgressBar;

    @Override
    public void setContentView(int layoutResID) {

        //Parent of this activity's layout file
        ConstraintLayout constraintLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        //Inside of ConstraintLayout
        FrameLayout frameLayout = constraintLayout.findViewById(R.id.activity_content);

        mProgressBar = constraintLayout.findViewById(R.id.progress_bar);

        //Associate frameLayout with the Base Activity
        //frameLayout serves as the container for all the activities that extend this class
        getLayoutInflater().inflate(layoutResID, frameLayout, true);
        super.setContentView(constraintLayout);
    }

    public void showProgressBar(boolean visibility){
        mProgressBar.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }
}
