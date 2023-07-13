package com.example.proteam5.ViewPage_Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.proteam5.Tab_Apps.Tab_ProTeam5.Frag_Login;
import com.example.proteam5.Tab_Apps.Tab_ProTeam5.Frag_Register;

public class ProTeam_Viewpage_Adapter extends FragmentStatePagerAdapter {

    public ProTeam_Viewpage_Adapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new Frag_Login();

            case 1: return new Frag_Register();

            default: return new Frag_Login();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title ="";
        switch (position){
            case 0: title="Đăng nhập";
                break;

            case 1: title="Đăng ký mới";
                break;
        }

        return title;
    }
}
