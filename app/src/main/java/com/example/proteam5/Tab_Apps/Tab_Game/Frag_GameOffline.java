package com.example.proteam5.Tab_Apps.Tab_Game;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.proteam5.DAO.Game_DAO;
import com.example.proteam5.Model.Game;
import com.example.proteam5.Model_Adapter.Game_Adapter;
import com.example.proteam5.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class Frag_GameOffline extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout swipeRefreshLayout;
    Game_DAO game_dao;

    FirebaseFirestore firebaseFirestore;
    List<Game> list_GameOffline;
    Game_Adapter adapter;

    RecyclerView recyclerView;
    TextView tuyChon;
    SearchView searchView;

    public Frag_GameOffline() {
    }

    public static Frag_GameOffline newInstance(String param1, String param2) {
        Frag_GameOffline fragment = new Frag_GameOffline();
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
        View view= inflater.inflate(R.layout.fragment_frag_game_offline, container, false);
        searchView = view.findViewById(R.id.SearchView_Game);
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
        swipeRefreshLayout = view.findViewById(R.id.SwipeRefreshLayout_gameOff);

        AnhXA(view);
        READ();

        tuyChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);
        setHasOptionsMenu(true);
        return view;
    }

    public void AnhXA(View view){
        game_dao = new Game_DAO(getContext());
        tuyChon = view.findViewById(R.id.tv_tuyChon_of);
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView_GameOffline);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        list_GameOffline = new ArrayList<>();
        adapter = new Game_Adapter(list_GameOffline,view.getContext());
        recyclerView.setAdapter(adapter);
    }
    private void READ(){
        firebaseFirestore.collection("Game").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                game_dao.ReadFireBase(list_GameOffline,adapter,0);
            }
        });
    }

    private void showMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(),tuyChon);
        popupMenu.getMenuInflater().inflate(R.menu.menu_game_theloai,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.mn_all: tuyChon.setText("Tất Cả");
                        READ();
                        break;
                    case R.id.mn_hanhDong: tuyChon.setText("Hành Động");
                        game_dao.ReadFireBase_GametuyChon(list_GameOffline,adapter,0,0);
                        break;
                    case R.id.mn_chienThuat: tuyChon.setText("Chiến Thuật");
                        game_dao.ReadFireBase_GametuyChon(list_GameOffline,adapter,0,1);
                        break;
                    case R.id.mn_dinhDi: tuyChon.setText("Kinh Dị");
                        game_dao.ReadFireBase_GametuyChon(list_GameOffline,adapter,0,2);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }


    @Override
    public void onRefresh() {
        READ();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2222);
                    swipeRefreshLayout.setRefreshing(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}