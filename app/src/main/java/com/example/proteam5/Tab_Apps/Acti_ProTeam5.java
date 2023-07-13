package com.example.proteam5.Tab_Apps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import com.example.proteam5.R;
import com.example.proteam5.ViewPage_Adapter.ProTeam_Viewpage_Adapter;
import com.google.android.material.tabs.TabLayout;

public class Acti_ProTeam5 extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acti_pro_team5);
        tabLayout = findViewById(R.id.tabLayout_Acti_ProTeam5);
        viewPager = findViewById(R.id.viewPager_Acti_ProTeam5);

        ProTeam_Viewpage_Adapter viewPagerAdapter = new ProTeam_Viewpage_Adapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}