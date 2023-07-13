package com.example.proteam5.DAO;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.proteam5.Model.HoaDonNapCoin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class HoaDonNapDao {
    FirebaseFirestore db;
    CollectionReference hoadonnapDao;
    Context context;

    public HoaDonNapDao(Context context) {
        db = FirebaseFirestore.getInstance();
        hoadonnapDao = db.collection("HoaDonNapCoin");
        this.context = context;
    }

    public void addHoaDonNap(HoaDonNapCoin a){
        a.setId(hoadonnapDao.document().getId());
        hoadonnapDao.add(a).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("Thông báo", "Đã tạo hóa đơn" + documentReference.getId());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Thông báo", "Không tạo được hóa đơn", e);
                    }
                });
    }
}
