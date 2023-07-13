package com.example.proteam5.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proteam5.Model.HoaDon;
import com.example.proteam5.Model.HoaDonNapCoin;
import com.example.proteam5.Model_Adapter.HoaDonNap_Adapter;
import com.example.proteam5.Model_Adapter.HoaDon_Adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ThongKeDao {
    FirebaseFirestore db;
    CollectionReference hoaDonDao;
    CollectionReference hoaDonNapDao;
    Context context;

    public ThongKeDao(Context context) {
        db = FirebaseFirestore.getInstance();
        hoaDonDao = db.collection("HoaDon");
        hoaDonNapDao = db.collection("HoaDonNapCoin");
        this.context = context;
    }

    public void Thongke(Date ngaybdau, Date ngaykthuc, TextView tv_doanhthu, RecyclerView re_thongke) {
        hoaDonDao.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<HoaDon> list = new ArrayList<>();
                            int doanhthu = 0;
                            ngaykthuc.setDate(ngaykthuc.getDate() + 1);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HoaDon a =document.toObject(HoaDon.class);
                                if (ngaybdau.compareTo(a.getNgaymua()) <= 0 && a.getNgaymua().compareTo(ngaykthuc) <= 0) {
                                    doanhthu = doanhthu + a.getCoin();
                                    list.add(a);
                                }
                            }
                            Collections.sort(list, new Comparator<HoaDon>() {
                                @Override
                                public int compare(HoaDon o1, HoaDon o2) {
                                    if (o1.getNgaymua().compareTo(o2.getNgaymua()) <= 0) {
                                        return 1;
                                    }
                                    return -1;
                                }
                            });
                            tv_doanhthu.setText("Doanh Thu:  " + doanhthu + " VNĐ");
                            HoaDon_Adapter adapter = new HoaDon_Adapter(list, context);
                            re_thongke.setAdapter(adapter);
                        } else {
                            Log.w("Thông báo", "Không load được hóa đơn mua", task.getException());
                        }
                    }
                });
    }

    public void ThongkeNap(Date ngaybdau, Date ngaykthuc, TextView tv_doanhthu, RecyclerView re_thongke) {
        hoaDonNapDao.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<HoaDonNapCoin> list = new ArrayList<>();
                            int tiennap = 0;
                            ngaykthuc.setDate(ngaykthuc.getDate() + 1);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HoaDonNapCoin a = document.toObject(HoaDonNapCoin.class);
                                if (ngaybdau.compareTo(a.getNgaynap()) <= 0 && a.getNgaynap().compareTo(ngaykthuc) <= 0) {
                                    tiennap = tiennap + a.getTiennap();
                                    list.add(a);
                                }
                            }
                            Collections.sort(list, new Comparator<HoaDonNapCoin>() {
                                @Override
                                public int compare(HoaDonNapCoin o1, HoaDonNapCoin o2) {
                                    if (o1.getNgaynap().compareTo(o2.getNgaynap()) <= 0) {
                                        return 1;
                                    }
                                    return -1;
                                }
                            });
                            tv_doanhthu.setText("Tổng tiền Nạp:  " + tiennap + " VNĐ");
                            HoaDonNap_Adapter adapter = new HoaDonNap_Adapter(list, context);
                            re_thongke.setAdapter(adapter);
                        } else {
                            Log.w("Thông báo", "Không load được hóa đơn nạp", task.getException());
                        }
                    }
                });
    }
}
