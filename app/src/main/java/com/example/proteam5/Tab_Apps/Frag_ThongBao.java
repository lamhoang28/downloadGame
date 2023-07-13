package com.example.proteam5.Tab_Apps;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proteam5.DAO.HoaDonDao;
import com.example.proteam5.DAO.ThongBaoDao;
import com.example.proteam5.Model.ThongBao;
import com.example.proteam5.Model_Adapter.KhoGame_Adapter;
import com.example.proteam5.Model_Adapter.ThongBao_Adapter;
import com.example.proteam5.R;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Frag_ThongBao extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<ThongBao> list;
    ThongBao_Adapter adapter;
    ThongBaoDao thongBaoDao;
    FirebaseFirestore firebaseFirestore;

    public Frag_ThongBao() {
    }

    public static Frag_ThongBao newInstance(String param1, String param2) {
        Frag_ThongBao fragment = new Frag_ThongBao();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag__thong_bao, container, false);
        AnhXa(view);
        READ();
        return view;
    }
    public void AnhXa(View view){
        recyclerView = view.findViewById(R.id.RecyclerView_thongbao);
        thongBaoDao = new ThongBaoDao(view.getContext());
        firebaseFirestore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(view.getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ThongBao_Adapter(list, view.getContext());
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