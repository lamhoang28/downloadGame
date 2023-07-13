package com.example.proteam5.Tab_Apps.QuanLy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.example.proteam5.DAO.User_DAO;
import com.example.proteam5.Model.User;
import com.example.proteam5.Model_Adapter.User_Adapter;
import com.example.proteam5.R;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Acti_MemberManager extends AppCompatActivity {
    RecyclerView recyclerView;
    User_Adapter user_adapter;
    List<User> list;
    User_DAO user_dao;
    LinearLayoutManager linearLayoutManager;
    FirebaseFirestore firebaseFirestore;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acti_member_manager);
        AnhXa();
        READ();
    }


    private void AnhXa() {
        recyclerView = findViewById(R.id.RecyclerView_Member);
        list = new ArrayList<>();
        user_dao = new User_DAO(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        linearLayoutManager = new LinearLayoutManager(Acti_MemberManager.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        user_adapter = new User_Adapter(list, Acti_MemberManager.this);
        recyclerView.setAdapter(user_adapter);
    }

    private void READ() {
        firebaseFirestore.collection("User").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                user_dao.ReadFireBase_User(list, user_adapter);
            }
        });
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_game_find, menu);
        searchView = (SearchView) menu.findItem(R.id.mn_find).getActionView();
        searchView.setQueryHint("Nhập tên người dùng hoặc email cần tìm");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                user_adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                user_adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}