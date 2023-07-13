package com.example.proteam5.Tab_Apps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.proteam5.R;
import com.example.proteam5.Tab_Apps.QuanLy.Acti_MemberManager;
import com.example.proteam5.Tab_Apps.QuanLy.Acti_ThongKe;
import com.example.proteam5.Tab_Apps.QuanLy.Acti_ThongKeNap;

public class Acti_QuanLy extends AppCompatActivity {
    ImageView member, thongKe, thongKeNap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acti_quan_ly);

        member = findViewById(R.id.img_acti_member);
        thongKe = findViewById(R.id.img_acti_thongKe);
        thongKeNap = findViewById(R.id.img_acti_thongKeNap);

        member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Acti_QuanLy.this, Acti_MemberManager.class));
            }
        });

        thongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Acti_QuanLy.this, Acti_ThongKe.class));
            }
        });

        thongKeNap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Acti_QuanLy.this, Acti_ThongKeNap.class));
            }
        });
    }
}