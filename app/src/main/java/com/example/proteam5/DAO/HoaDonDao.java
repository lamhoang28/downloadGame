package com.example.proteam5.DAO;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.proteam5.Model.HoaDon;
import com.example.proteam5.Model_Adapter.Dialog_Adapter;
import com.example.proteam5.Model_Adapter.KhoGame_Adapter;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HoaDonDao {
    FirebaseFirestore db;
    CollectionReference hoaDonDao;
    Context context;

    public HoaDonDao(Context context) {
        db = FirebaseFirestore.getInstance();
        hoaDonDao = db.collection("HoaDon");
        this.context = context;
    }

    public void addHoaDon(HoaDon a) {
        a.setID(hoaDonDao.document().getId());
        hoaDonDao.document(a.getID()).set(a).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }
        });
    }

    public void startDownloadFile(String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        Log.e("TAG", "startDownloadFile: " + url);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("Download");
        request.setDescription("DownloadFile ... ");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, String.valueOf(System.currentTimeMillis()));
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }
    }

    public void ReadFireBase_KhoGame(List<HoaDon> list, KhoGame_Adapter adapter) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            return;
        }
        hoaDonDao.whereEqualTo("usermail", user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    list.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        HoaDon hoaDon = document.toObject(HoaDon.class);
                        list.add(hoaDon);
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
                    adapter.notifyDataSetChanged();
                } else {
                    Log.w("TAG", "Error getting documents.", task.getException());
                }
            }
        });
    }

    public void loadngchoicung(TextView textView, String idgame) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        List<HoaDon> list = new ArrayList<>();

        hoaDonDao.whereEqualTo("id_game", idgame).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String a = "Người cùng mua: ";
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        HoaDon hoaDon = document.toObject(HoaDon.class);
                        if (!user.getEmail().equals(hoaDon.getUsermail())) {
                            list.add(hoaDon);
                        }
                    }
                    for (int i = 0; i < list.size(); i++) {
                        if (i == 3){
                            a = a + "....";
                            break;
                        }
                        if (i == list.size() - 1) {
                            a = a + list.get(i).getNameUser();
                        } else {
                            a = a + list.get(i).getNameUser() + ", ";
                        }

                    }
                    if (list.size() == 0) {
                        textView.setText("Bạn là người duy nhất mua game này");
                    } else {
                        textView.setText(a);
                    }


                } else {

                }
            }
        });
    }

    public void Read(List<HoaDon> list, Dialog_Adapter adapter, String idgame) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        list.clear();
        hoaDonDao.whereEqualTo("id_game", idgame).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        HoaDon hoaDon = document.toObject(HoaDon.class);
                        if (!user.getEmail().equals(hoaDon.getUsermail())) {
                            list.add(hoaDon);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {

                }
            }
        });
    }
}
