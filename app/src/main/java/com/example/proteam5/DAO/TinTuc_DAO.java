package com.example.proteam5.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.proteam5.Model.HoaDon;
import com.example.proteam5.Model.TinTuc;
import com.example.proteam5.Model_Adapter.TinTuc_Adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TinTuc_DAO {
    public  void CreateNewTinTuc(TinTuc tinTuc){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("TinTuc").document();

        Map<String,Object> item = new HashMap<>();
        tinTuc.setMaBaiViet(documentReference.getId());
        item.put("maTieuDe",documentReference.getId());
        item.put("TieuDe",tinTuc.getTieuDe());
        item.put("Anh",tinTuc.getImgShow());
        item.put("Anh1",tinTuc.getImgReView());
        item.put("Anh2",tinTuc.getImgReView1());
        item.put("urlVideo",tinTuc.getUrlVideo());
        item.put("NoiDung",tinTuc.getNoiDung());
        item.put("ngaydang",tinTuc.getNgaydang());

        documentReference.set(tinTuc).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Log.e("TAG", "successfully: " );
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("TAG", "Error adding document", e);
                }
            });
    }
    public void ReadFireBase_TinTuc(List<TinTuc> listTinTuc, TinTuc_Adapter adapter){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TinTuc")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()) {
                        if (listTinTuc!=null){
                            listTinTuc.clear();
                        }
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            TinTuc tinTuc = document.toObject(TinTuc.class);
                            listTinTuc.add(tinTuc);
                        }
                        Collections.sort(listTinTuc, new Comparator<TinTuc>() {
                            @Override
                            public int compare(TinTuc o1, TinTuc o2) {
                                if (o1.getNgaydang().compareTo(o2.getNgaydang()) <= 0) {
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

    public void EditTinTuc(TinTuc tinTuc, String index, Context context){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TinTuc").document(index)
            .set(tinTuc)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(context, "Update Success !!!", Toast.LENGTH_SHORT).show();
                }

            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Update Faild !!!", Toast.LENGTH_SHORT).show();
                }
            });
    }
    public long TinTucDelete(String index,Context context){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TinTuc").document(index).
                delete().
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.e("TAG", "onSuccess: ");
                        Toast.makeText(context, "onSuccess", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "Faided: ");
                        Toast.makeText(context, "Faided", Toast.LENGTH_SHORT).show();
                    }
                });
        return 1;
    }

}
