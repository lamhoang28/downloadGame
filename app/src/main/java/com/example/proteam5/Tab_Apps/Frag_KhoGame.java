package com.example.proteam5.Tab_Apps;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proteam5.DAO.HoaDonDao;
import com.example.proteam5.Model.HoaDon;
import com.example.proteam5.Model_Adapter.KhoGame_Adapter;
import com.example.proteam5.R;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Frag_KhoGame extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    HoaDonDao hoaDonDao;
    FirebaseFirestore firebaseFirestore;
    List<HoaDon> list;
    KhoGame_Adapter adapter;
    SearchView searchView;

    public Frag_KhoGame() {
    }

    public static Frag_KhoGame newInstance(String param1, String param2) {
        Frag_KhoGame fragment = new Frag_KhoGame();
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
        View view=inflater.inflate(R.layout.fragment_frag_khogame, container, false);
        searchView = view.findViewById(R.id.SearchView_khoGame);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        AnhXa(view);
        READ();
        setHasOptionsMenu(true);
        return view;
    }

    public void AnhXa(View view){
        recyclerView = view.findViewById(R.id.recyclerView_khogame);
        hoaDonDao = new HoaDonDao(getContext());
        firebaseFirestore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(view.getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new KhoGame_Adapter(list,view.getContext());
        recyclerView.setAdapter(adapter);
    }
    private void READ() {
        firebaseFirestore.collection("HoaDon").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                hoaDonDao.ReadFireBase_KhoGame(list,adapter);
            }
        });
    }

}