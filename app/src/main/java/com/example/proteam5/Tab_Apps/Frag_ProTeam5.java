package com.example.proteam5.Tab_Apps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.proteam5.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;


public class Frag_ProTeam5 extends Fragment {

    ChipNavigationBar chipNavigationBar;

    public Frag_ProTeam5() {
    }


    public static Frag_ProTeam5 newInstance(String param1, String param2) {
        Frag_ProTeam5 fragment = new Frag_ProTeam5();
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
        View view =inflater.inflate(R.layout.fragment_frag_pro_team5, container, false);
        chipNavigationBar =view.findViewById(R.id.ChipNavigationBar_Home);
        chipNavigationBar.setItemSelected(R.id.menu_Home,true);
        getFragmentManager().beginTransaction().replace(R.id.abcxyz,new Frag_Home()).commit();
        Bottom_menu();
        return view;
    }
    private void Bottom_menu() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment= null;
                switch (i){
                    case R.id.menu_Home:fragment = new Frag_Home();
                        break;
                    case R.id.menu_Game:fragment = new Frag_Game();
                        break;
                    case R.id.menu_tinTuc:fragment = new Frag_TinTuc();
                        break;
                    case R.id.mn_Apps:fragment = new Frag_Apps();
                        break;
                    case R.id.menu_myGame:fragment = new Frag_KhoGame();
                        break;
                }
                getFragmentManager().beginTransaction().replace(R.id.abcxyz,fragment).commit();
            }
        });
    }

}