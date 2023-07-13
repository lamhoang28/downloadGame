package com.example.proteam5.ViewPage_Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.proteam5.Tab_Apps.Tab_Game.Frag_GameOffline;
import com.example.proteam5.Tab_Apps.Tab_Game.Frag_GameOnline;

public class Game_ViewPage_Adapter extends FragmentStatePagerAdapter {
    public Game_ViewPage_Adapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new Frag_GameOnline();

            case 1:return new Frag_GameOffline();

            default: return new Frag_GameOnline();
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
            case 0: title ="Game Online";
                break;
            case 1: title ="Game Onfline";
                break;
        }
        return title;
    }
}
