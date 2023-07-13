package com.example.proteam5.Tab_Apps;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.proteam5.R;
import com.example.proteam5.Tab_Apps.QuanLy.Acti_MemberManager;
import com.example.proteam5.Tab_Apps.QuanLy.Acti_ThongKe;
import com.example.proteam5.Tab_Apps.QuanLy.Acti_ThongKeNap;

public class Frag_QuanLy extends Fragment {


    public Frag_QuanLy() {
    }

    public static Frag_QuanLy newInstance(String param1, String param2) {
        Frag_QuanLy fragment = new Frag_QuanLy();
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
        View view=inflater.inflate(R.layout.fragment_frag__quan_ly, container, false);
        ImageView member = view.findViewById(R.id.img_acti_member);
        member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Acti_MemberManager.class));
            }
        });
        ImageView thongKe = view.findViewById(R.id.img_acti_thongKe);
        thongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Acti_ThongKe.class));
            }
        });
        ImageView thongKeNap = view.findViewById(R.id.img_acti_thongKeNap);
        thongKeNap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Acti_ThongKeNap.class));
            }
        });
        return view;
    }
}