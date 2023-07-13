package com.example.proteam5.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.proteam5.Model.HoaDon;
import com.example.proteam5.Model.HoaDonNapCoin;
import com.example.proteam5.Model.ThongBao;
import com.example.proteam5.Model_Adapter.KhoGame_Adapter;
import com.example.proteam5.Model_Adapter.ThongBao_Adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ThongBaoDao {
    CollectionReference thongbaodao;
    Context context;

    public ThongBaoDao(Context context) {
        this.context = context;
        thongbaodao = FirebaseFirestore.getInstance().collection("ThongBao");
    }

    public void addThongBao(ThongBao a) {
        a.setID(thongbaodao.document().getId());
        thongbaodao.add(a).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        if (a.getKieuThongBao() == 1) {
                            Toast.makeText(context, "Đã gửi câu hỏi cho Admin", Toast.LENGTH_SHORT).show();
                        } else if (a.getKieuThongBao() == 2) {
                            Toast.makeText(context, "Đã phản hồi người dùng", Toast.LENGTH_SHORT).show();
                        } else if (a.getKieuThongBao() == 3) {
                            Toast.makeText(context, "Đã gửi lời mời chơi game", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Thông báo", "Error adding document", e);
                    }
                });
    }

    public void ReadFireBase_ThongBao(List<ThongBao> list, ThongBao_Adapter adapter) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        thongbaodao.whereEqualTo("userNhan", user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    list.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ThongBao a = document.toObject(ThongBao.class);
                        list.add(a);
                    }
                    Collections.sort(list, new Comparator<ThongBao>() {
                        @Override
                        public int compare(ThongBao o1, ThongBao o2) {
                            if (o1.getNgaygui().compareTo(o2.getNgaygui()) <= 0) {
                                return 1;
                            }
                            return -1;
                        }
                    });
                    adapter.notifyDataSetChanged();
                } else {
                    Log.w("TAG", "Error getting documents.", task.getException());
                }
            }
        });
    }
}
