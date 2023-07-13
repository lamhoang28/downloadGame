package com.example.proteam5.Tab_Apps;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proteam5.DAO.Apps_DAO;
import com.example.proteam5.DAO.Game_DAO;
import com.example.proteam5.DAO.TinTuc_DAO;
import com.example.proteam5.Model.Apps;
import com.example.proteam5.Model.Game;
import com.example.proteam5.Model.TinTuc;
import com.example.proteam5.Model_Adapter.Apps_Adapter;
import com.example.proteam5.Model_Adapter.Game_Adapter;
import com.example.proteam5.Model_Adapter.TinTuc_Adapter;
import com.example.proteam5.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class Frag_Home extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swipeRefreshLayout;

    private FirebaseFirestore firebaseFirestore;

    LinearLayoutManager managerTinTuc, managerGame, managerApps;

    RecyclerView recyclerViewTinTuc, recyclerViewGame, recyclerViewApps;

    TextView tv_tintuc, tv_game, tv_app;

    TinTuc_Adapter adapterTinTuc;
    Game_Adapter adapterGame;
    Apps_Adapter adapterApps;

    List<TinTuc> listTinTuc;
    List<Game> listGame;
    List<Apps> listApps;
    Game_DAO game_dao;
    TinTuc_DAO tinTuc_dao;
    Apps_DAO apps_dao;

    public Frag_Home() {
    }

    public static Frag_Home newInstance(String param1, String param2) {
        Frag_Home fragment = new Frag_Home();
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
        View view = inflater.inflate(R.layout.fragment_frag_home, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();

        swipeRefreshLayout = view.findViewById(R.id.SwipeRefreshLayout_home);
        tv_app = view.findViewById(R.id.tv_ungdung);
        tv_game = view.findViewById(R.id.tv_game);
        tv_tintuc = view.findViewById(R.id.tv_tintuc);


        //Tin Tuc
        recyclerViewTinTuc = view.findViewById(R.id.recyclerView_Home_TinTuc);
        managerTinTuc = new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false);
        recyclerViewTinTuc.setLayoutManager(managerTinTuc);
        listTinTuc = new ArrayList<>();
        adapterTinTuc = new TinTuc_Adapter(listTinTuc, view.getContext());
        recyclerViewTinTuc.setAdapter(adapterTinTuc);
        tinTuc_dao = new TinTuc_DAO();


        //Game
        recyclerViewGame = view.findViewById(R.id.recyclerView_Home_Game);
        managerGame = new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false);
        recyclerViewGame.setLayoutManager(managerGame);
        listGame = new ArrayList<>();
        adapterGame = new Game_Adapter(listGame, view.getContext());
        recyclerViewGame.setAdapter(adapterGame);
        game_dao = new Game_DAO(getContext());

        //Apps

        recyclerViewApps = view.findViewById(R.id.recyclerView_Home_Apps);
        managerApps = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        recyclerViewApps.setLayoutManager(managerApps);
        listApps = new ArrayList<>();
        adapterApps = new Apps_Adapter(listApps, view.getContext());
        recyclerViewApps.setAdapter(adapterApps);
        apps_dao = new Apps_DAO();
        READ();
        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);
        return view;
    }

    private void READ() {
        firebaseFirestore.collection("TinTuc").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                tinTuc_dao.ReadFireBase_TinTuc(listTinTuc, adapterTinTuc);
            }
        });
        firebaseFirestore.collection("Game").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                game_dao.ReadFireBase_Game(listGame, adapterGame);
            }
        });
        firebaseFirestore.collection("Apps").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                apps_dao.ReadFireBase_Apps(listApps, adapterApps);
            }
        });

    }


    @Override
    public void onRefresh() {
        READ();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    swipeRefreshLayout.setRefreshing(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}