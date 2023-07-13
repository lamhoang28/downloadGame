package com.example.proteam5.Tab_Apps;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.proteam5.DAO.Game_DAO;
import com.example.proteam5.R;
import com.example.proteam5.Tab_Apps.Tab_Game.Frag_GameOnline;
import com.example.proteam5.ViewPage_Adapter.Game_ViewPage_Adapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


public class Frag_Game extends Fragment {

    ViewPager viewPager;
    TabLayout tabLayout;
    BottomNavigationView bottomNavigationView;


    public Frag_Game() {
    }

    public static Frag_Game newInstance(String param1, String param2) {
        Frag_Game fragment = new Frag_Game();
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
        View view=inflater.inflate(R.layout.fragment_frag_game, container, false);

        tabLayout = view.findViewById(R.id.tabLayout_game);
        viewPager = view.findViewById(R.id.viewPager_Frag_Game);
        Game_ViewPage_Adapter viewPagerAdapter= new Game_ViewPage_Adapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}