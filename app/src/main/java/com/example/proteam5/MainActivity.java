package com.example.proteam5;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.proteam5.DAO.User_DAO;
import com.example.proteam5.Tab_Apps.Acti_ThongBao;
import com.example.proteam5.Tab_Apps.Acti_TrangCaNhan;
import com.example.proteam5.Tab_Apps.Frag_Apps;
import com.example.proteam5.Tab_Apps.Frag_CaiDatChung;
import com.example.proteam5.Tab_Apps.Frag_Game;
import com.example.proteam5.Tab_Apps.Frag_Home;
import com.example.proteam5.Tab_Apps.Frag_KhoGame;
import com.example.proteam5.Tab_Apps.Frag_TinTuc;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private static final int REQUSET_PERMISTION_CODE = -1;
    ChipNavigationBar chipNavigationBar;
    User_DAO user_dao;
    TextView username, coin;
    CircleImageView img;
    LinearLayout profile;
    ImageView img_Thongbao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        chipNavigationBar = findViewById(R.id.ChipNavigationBar_Home);
        chipNavigationBar.setItemSelected(R.id.menu_Home, true);
        username = findViewById(R.id.tv_username);
        coin = findViewById(R.id.tv_userCoin);

        img = findViewById(R.id.img_user);
        profile = findViewById(R.id.linear_profile);
        img_Thongbao = findViewById(R.id.img_thongBao);
        getSupportFragmentManager().beginTransaction().replace(R.id.framlayout, new Frag_Home()).commit();
        user_dao = new User_DAO(this);
        Bottom_menu();
        checkUser();

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Acti_TrangCaNhan.class));
            }
        });

        img_Thongbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Acti_ThongBao.class));
            }
        });

    }

    public void checkUser() {
        READ();
//        checkPerMistion();
    }

    private void READ() {
        user_dao.userDao.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                user_dao.loadProfileHome(username, coin, img);
            }
        });
    }

//    public int checkPerMistion() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
//                String[] perMis = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                requestPermissions(perMis, REQUSET_PERMISTION_CODE);
//            } else {
//                return 1;
//            }
//        } else {
//            return 1;
//        }
//        return -1;
//    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user_dao.USER.getPhotoUrl() == null) {
            return;
        }
        Picasso.get().load(user_dao.USER.getPhotoUrl()).resize(350, 200).centerCrop().into(img);
    }

    private void Bottom_menu() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.menu_Home:
                        fragment = new Frag_Home();
                        break;
                    case R.id.menu_Game:
                        fragment = new Frag_Game();
                        break;
                    case R.id.menu_tinTuc:
                        fragment = new Frag_TinTuc();
                        break;
                    case R.id.mn_Apps:
                        fragment = new Frag_Apps();
                        break;
                    case R.id.menu_myGame:
                        fragment = new Frag_KhoGame();
                        break;
                    case R.id.menu_setting:
                        fragment = new Frag_CaiDatChung();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.framlayout, fragment).commit();
            }
        });
    }
}