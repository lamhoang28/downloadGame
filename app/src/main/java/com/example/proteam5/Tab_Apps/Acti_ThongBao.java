package com.example.proteam5.Tab_Apps;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.proteam5.DAO.ThongBaoDao;
import com.example.proteam5.Model.ThongBao;
import com.example.proteam5.Model_Adapter.ThongBao_Adapter;
import com.example.proteam5.R;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Acti_ThongBao extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<ThongBao> list;
    ThongBao_Adapter adapter;
    ThongBaoDao thongBaoDao;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acti_thong_bao);
        AnhXa();
        READ();
    }
    public void AnhXa(){
        recyclerView = findViewById(R.id.RecyclerView_thongbao);
        thongBaoDao = new ThongBaoDao(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ThongBao_Adapter(list, this);
        recyclerView.setAdapter(adapter);
    }
    private void READ() {
        firebaseFirestore.collection("ThongBao").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                thongBaoDao.ReadFireBase_ThongBao(list,adapter);
            }
        });
    }
}