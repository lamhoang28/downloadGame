package com.example.proteam5.Tab_Apps.Tab_GioiThieu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proteam5.R;


public class Frag_slide_1 extends Fragment {

    public Frag_slide_1() {
    }

    public static Frag_slide_1 newInstance(String param1, String param2) {
        Frag_slide_1 fragment = new Frag_slide_1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_frag_slide_1, container, false);
    }
}