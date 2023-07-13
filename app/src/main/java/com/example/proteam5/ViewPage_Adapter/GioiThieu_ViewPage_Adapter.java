package com.example.proteam5.ViewPage_Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.proteam5.Tab_Apps.Tab_Game.Frag_GameOffline;
import com.example.proteam5.Tab_Apps.Tab_Game.Frag_GameOnline;
import com.example.proteam5.Tab_Apps.Tab_GioiThieu.Frag_slide_1;
import com.example.proteam5.Tab_Apps.Tab_GioiThieu.Frag_slide_2;
import com.example.proteam5.Tab_Apps.Tab_GioiThieu.Frag_slide_3;

public class GioiThieu_ViewPage_Adapter extends FragmentStatePagerAdapter {

    public GioiThieu_ViewPage_Adapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new Frag_slide_1();

            case 1:return new Frag_slide_2();

            case 2:return new Frag_slide_3();

            default: return new Frag_slide_1();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }
}
