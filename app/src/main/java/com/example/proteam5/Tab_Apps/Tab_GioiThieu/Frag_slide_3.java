package com.example.proteam5.Tab_Apps.Tab_GioiThieu;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.proteam5.R;
import com.example.proteam5.Tab_Apps.Acti_ProTeam5;

public class Frag_slide_3 extends Fragment {
    CheckBox checkBox;

    public Frag_slide_3() {
    }

    public static Frag_slide_3 newInstance(String param1, String param2) {
        Frag_slide_3 fragment = new Frag_slide_3();
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
        View view = inflater.inflate(R.layout.fragment_frag_slide_3, container, false);
        Button button = view.findViewById(R.id.button);
        checkBox = view.findViewById(R.id.checkbox_dongyDieuKhoan);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    startActivity(new Intent(getActivity(), Acti_ProTeam5.class));
                    getActivity().finish();
                }else{
                    Toast.makeText(getContext(), "Bạn cần đồng ý với điều khoản", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }
}