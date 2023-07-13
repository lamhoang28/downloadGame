package com.example.proteam5.Tab_Apps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.proteam5.R;
import com.example.proteam5.ViewPage_Adapter.GioiThieu_ViewPage_Adapter;

import me.relex.circleindicator.CircleIndicator;

public class Acti_GioiThieu extends AppCompatActivity {
    RelativeLayout  relativeLayout_Botton;
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    TextView next,skip;
    GioiThieu_ViewPage_Adapter gioiThieu_adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acti_gioi_thieu);

        relativeLayout_Botton = findViewById(R.id.layout_next);
        viewPager = findViewById(R.id.viewPager_gioiThieu);
        circleIndicator = findViewById(R.id.circleIndicator_gioiThieu);
        next = findViewById(R.id.tv_next);
        skip = findViewById(R.id.tv_skip);
        gioiThieu_adapter = new GioiThieu_ViewPage_Adapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(gioiThieu_adapter);
        circleIndicator.setViewPager(viewPager);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem()<2){
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                }
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==2){
                    skip.setVisibility(View.GONE);
                    relativeLayout_Botton.setVisibility(View.GONE);
                }else{
                    skip.setVisibility(View.VISIBLE);
                    relativeLayout_Botton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}