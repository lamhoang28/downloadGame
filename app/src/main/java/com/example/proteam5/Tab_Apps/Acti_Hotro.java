package com.example.proteam5.Tab_Apps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proteam5.DAO.ThongBaoDao;
import com.example.proteam5.Model.ThongBao;
import com.example.proteam5.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class Acti_Hotro extends AppCompatActivity {

    Button btn_hotro;
    EditText edt_hotro;
    ThongBaoDao thongBaoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acti_hotro);
        btn_hotro = findViewById(R.id.btn_hotro);
        edt_hotro = findViewById(R.id.edt_hotro);
        thongBaoDao = new ThongBaoDao(this);
        btn_hotro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_hotro.getText().toString().isEmpty()) {
                    Toast.makeText(Acti_Hotro.this, "Bạn cần nhập câu hỏi!", Toast.LENGTH_SHORT).show();
                } else {
                    ThongBao a = new ThongBao();
                    a.setKieuThongBao(1);
                    a.setUserGui(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    a.setNoiDung(edt_hotro.getText().toString());
                    a.setNgaygui(Calendar.getInstance().getTime());
                    a.setUserNhan("dungnmph18838@fpt.edu.vn");
                    thongBaoDao.addThongBao(a);
                    a.setUserNhan("lamnhph18826@fpt.edu.vn");
                    thongBaoDao.addThongBao(a);
                    edt_hotro.setText("");
                }

            }
        });
    }
}