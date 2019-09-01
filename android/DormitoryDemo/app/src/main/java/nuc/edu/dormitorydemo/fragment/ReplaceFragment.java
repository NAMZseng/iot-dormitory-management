package nuc.edu.dormitorydemo.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nuc.edu.dormitorydemo.base.BasePager;


@SuppressLint("ValidFragment")
public  class ReplaceFragment extends Fragment {

    private BasePager currPager;

    public ReplaceFragment(BasePager basePager){
        this.currPager = basePager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return currPager.rootView;
    }

}