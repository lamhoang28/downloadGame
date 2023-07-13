package com.example.proteam5.Tab_Apps.QuanLy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proteam5.DAO.ThongKeDao;
import com.example.proteam5.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Acti_ThongKe extends AppCompatActivity {

    TextView tv_doanhthu, tv_ngaybdau, tv_ngaykthuc;
    ImageView img_ngaybdau, img_ngaykthuc;
    Button btn_thongke;
    RecyclerView re_Thongke;
    ThongKeDao thongKeDao;
    int mYear, mMonth, mDay;
    Date ngaybdau, ngaykthuc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acti_thong_ke);

        tv_doanhthu = findViewById(R.id.tv_doanhthu);
        tv_ngaybdau = findViewById(R.id.tv_thongkeDay1);
        tv_ngaykthuc = findViewById(R.id.tv_thongkeDay2);
        img_ngaybdau = findViewById(R.id.img_ngaybdau);
        img_ngaykthuc = findViewById(R.id.img_ngaykthuc);
        btn_thongke = findViewById(R.id.btn_thongKe);
        re_Thongke = findViewById(R.id.RecyclerView_thongKe);
        LinearLayoutManager manager = new LinearLayoutManager(Acti_ThongKe.this, RecyclerView.VERTICAL, false);
        re_Thongke.setLayoutManager(manager);

        thongKeDao = new ThongKeDao(Acti_ThongKe.this);


        img_ngaybdau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog d = new DatePickerDialog(Acti_ThongKe.this, 0, setngaybdau, mYear, mMonth, mDay);
                d.show();
            }
        });
        tv_ngaybdau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog d = new DatePickerDialog(Acti_ThongKe.this, 0, setngaybdau, mYear, mMonth, mDay);
                d.show();
            }
        });
        img_ngaykthuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog d = new DatePickerDialog(Acti_ThongKe.this, 0, setngaykthuc, mYear, mMonth, mDay);
                d.show();
            }
        });
        tv_ngaykthuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog d = new DatePickerDialog(Acti_ThongKe.this, 0, setngaykthuc, mYear, mMonth, mDay);
                d.show();
            }
        });
        btn_thongke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ngaybdau == null || ngaykthuc == null) {
                    Toast.makeText(Acti_ThongKe.this, "Bạn cần chọn ngày để thống kê doanh thu", Toast.LENGTH_SHORT).show();
                } else if (ngaybdau.compareTo(ngaykthuc) > 0) {
                    Toast.makeText(Acti_ThongKe.this, "Ngày bắt đầu phải là ngày trước ngày kết thúc", Toast.LENGTH_SHORT).show();
                } else {
                    thongKeDao.Thongke(ngaybdau, ngaykthuc, tv_doanhthu, re_Thongke);
                }
            }
        });
    }

    DatePickerDialog.OnDateSetListener setngaybdau = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfYear) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfYear;
            GregorianCalendar c = new GregorianCalendar(mYear, mMonth, mDay);
            tv_ngaybdau.setText("Ngày " + mDay + "   Tháng " + (mMonth + 1) + "   Năm " + mYear);
            ngaybdau = c.getTime();
        }
    };
    DatePickerDialog.OnDateSetListener setngaykthuc = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfYear) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfYear;
            GregorianCalendar c = new GregorianCalendar(mYear, mMonth, mDay);
            tv_ngaykthuc.setText("Ngày " + mDay + "   Tháng " + (mMonth + 1) + "   Năm " + mYear);
            ngaykthuc = c.getTime();
        }
    };
}