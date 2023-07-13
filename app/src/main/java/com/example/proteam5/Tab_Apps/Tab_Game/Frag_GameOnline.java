package com.example.proteam5.Tab_Apps.Tab_Game;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proteam5.DAO.Game_DAO;
import com.example.proteam5.DAO.ProTeam5;
import com.example.proteam5.DAO.User_DAO;
import com.example.proteam5.Model.Game;
import com.example.proteam5.Model_Adapter.Game_Adapter;
import com.example.proteam5.R;
import com.example.proteam5.Tab_Apps.View.Acti_ViewGame;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class Frag_GameOnline extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout swipeRefreshLayout;

    Game_DAO game_dao;
    User_DAO user_dao;
    ProTeam5 chucNang;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage storage;

    StorageReference Folder;
    List<Game> list_GameOnline;
    Game_Adapter adapter;

    RecyclerView recyclerView;
    SearchView searchView;

    EditText tenGameOrMod,gioiThieu,giaTien,FAQ, urlVideo,link;
    RadioButton hanhDong,chienThuat,kinhDi,ON,OFF;
    int checkonline=0,checkTheLoai=0;
    String _tengameMod,_gioiThieu,_imgGame,_review,_review1,_review2,_urlVideo,_dieuKhoan,_link,_coin,folderImage;
    TextView error,tuyChon;
    Button them,huy;
    FloatingActionButton floatingActionButton;
    ImageView img,img1,img2,img3;

    private static final int ImgBack = 1;
    private static final int ImgBack1 = 2;
    private static final int ImgBack2 = 3;
    private static final int ImgBack3 = 4;
    public static final int RESULT_OK = -1;

    public Frag_GameOnline() {
    }

    public static Frag_GameOnline newInstance(String param1, String param2) {
        Frag_GameOnline fragment = new Frag_GameOnline();
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
        View view =inflater.inflate(R.layout.fragment_frag_game_online, container, false);
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
        AnhXa(view);
        READ();

        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);


        tuyChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPer();
                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.dialog_game);
                tenGameOrMod = dialog.findViewById(R.id.edt_add_nameGame);
                gioiThieu = dialog.findViewById(R.id.edt_add_GioiThieuGame);
                giaTien = dialog.findViewById(R.id.edt_add_CoinGame);
                //radio
                hanhDong = dialog.findViewById(R.id.radio_hd);
                chienThuat = dialog.findViewById(R.id.radio_ct);
                kinhDi= dialog.findViewById(R.id.radio_kd);
                ON= dialog.findViewById(R.id.radio_online);
                OFF= dialog.findViewById(R.id.radio_offline);

                img= dialog.findViewById(R.id.btn_add_imgGame);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent,ImgBack);
                    }
                });
                img1= dialog.findViewById(R.id.btn_add_imgGame1);
                img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent,ImgBack1);
                    }
                });
                img2= dialog.findViewById(R.id.btn_add_imgGame2);
                img2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent,ImgBack2);
                    }
                });
                img3= dialog.findViewById(R.id.btn_add_imgGame3);
                img3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent,ImgBack3);
                    }
                });
                //img
                FAQ = dialog.findViewById(R.id.edt_add_dieuKhoanGame);
                urlVideo = dialog.findViewById(R.id.edt_add_videoGame);
                link = dialog.findViewById(R.id.edt_add_linkApk);
                error = dialog.findViewById(R.id.tv_error_createGameorMod);

                them = dialog.findViewById(R.id.btn_addGame);
                them.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _tengameMod =tenGameOrMod.getText().toString().trim();
                        _gioiThieu =gioiThieu.getText().toString().trim();
                        _dieuKhoan =FAQ.getText().toString().trim();
                        _link =link.getText().toString().trim();
                        _urlVideo = urlVideo.getText().toString();
                        _coin = giaTien.getText().toString();
                        if (!ON.isChecked()&&!OFF.isChecked()){
                            error.setText("Thiếu thông tin Game Online hay Offline");
                            return;
                        }
                        if (!hanhDong.isChecked()&&!chienThuat.isChecked()&&!kinhDi.isChecked()){
                            error.setText("Thiếu Thể Loại Game");
                            return;
                        }

                        if (ON.isChecked()){
                            checkonline = 0;
                        }
                        if (OFF.isChecked()){
                            checkonline = 1;
                        }

                        if (hanhDong.isChecked()){
                            checkTheLoai =0;
                        }
                        if (chienThuat.isChecked()){
                            checkTheLoai = 1;
                        }
                        if (kinhDi.isChecked()){
                            checkTheLoai = 2;
                        }

                        if (_tengameMod.isEmpty()||
                                _gioiThieu.isEmpty()||
                                _dieuKhoan.isEmpty()||
                                _link.isEmpty()||
                                _urlVideo.isEmpty()||
                                _coin.isEmpty())
                        {
                            error.setText("Thiếu thông tin!");
                            return;
                        }

                        int Giagame = Integer.parseInt(_coin);
                        game_dao = new Game_DAO(getContext());
                        Game game = new Game("idtutangcuafirestore",_tengameMod,_gioiThieu,_imgGame,_review,_review1,_review2,_urlVideo,_dieuKhoan,_link,checkonline,checkTheLoai,Giagame,0);
                        game_dao.CreateNewGame_Online(game);
                        READ();
                        dialog.dismiss();
                    }
                });
                huy = dialog.findViewById(R.id.btn_CannerGame);
                huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (_imgGame!=null){
                            chucNang.Delete_image(_imgGame,getContext());
                        }
                        if (_review!=null){
                            chucNang.Delete_image(_review,getContext());
                        }
                        if (_review1!=null){
                            chucNang.Delete_image(_review1,getContext());
                        }
                        if (_review2!=null){
                            chucNang.Delete_image(_review2,getContext());
                        }
                        dialog.dismiss();
                    }
                });
                dialog.setCancelable(true);
                dialog.show();
            }
        });

        return view;
    }

    public void AnhXa(View view){
        firebaseFirestore = FirebaseFirestore.getInstance();
        Folder = FirebaseStorage.getInstance().getReference().child("ImageGame");
        recyclerView = view.findViewById(R.id.recyclerView_GameOnline);
        floatingActionButton = view.findViewById(R.id.floating_GameOnline);
        swipeRefreshLayout = view.findViewById(R.id.SwipeRefreshLayout_game_Onl);

        LinearLayoutManager manager = new LinearLayoutManager(view.getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        list_GameOnline = new ArrayList<>();
        adapter = new Game_Adapter(list_GameOnline,view.getContext());
        recyclerView.setAdapter(adapter);
        tuyChon = view.findViewById(R.id.tv_tuyChon_on);
        game_dao = new Game_DAO(getContext());
        user_dao= new User_DAO(getContext());
        chucNang = new ProTeam5();
        int ADMIN =user_dao.check_Admin();
        if (ADMIN!=1){
            floatingActionButton.setVisibility(View.GONE);
        }
    }
    private void READ(){
        firebaseFirestore.collection("Game").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                game_dao.ReadFireBase(list_GameOnline,adapter,1);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        folderImage = tenGameOrMod.getText().toString().trim();
        if (requestCode==ImgBack&&resultCode==RESULT_OK){
            if (_imgGame!=null){
                chucNang.Delete_image(_imgGame,getContext());
            }
            Uri ImageData = data.getData();
            Picasso.get().load(ImageData).resize(350,200).centerCrop().into(img);
            StorageReference ImageName = Folder.child(folderImage+"1"+ImageData.getLastPathSegment());
            ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Tải lên ảnh bìa thành công.", Toast.LENGTH_SHORT).show();
                    ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            _imgGame = uri.toString();
                            Log.e("TAG", "onSuccess _imgGame: "+_imgGame );
                        }
                    });
                }
            });
        }
        if (requestCode==ImgBack1&&resultCode==RESULT_OK){
            if (_review!=null){
                chucNang.Delete_image(_review,getContext());
            }
            Uri ImageData = data.getData();
            Picasso.get().load(ImageData).resize(350,200).centerCrop().into(img1);
            StorageReference ImageName = Folder.child(folderImage+"2"+ImageData.getLastPathSegment());
            ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Tải lên ảnh review 1 thành công.", Toast.LENGTH_SHORT).show();
                    ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            _review = uri.toString();
                            Log.e("TAG", "onSuccess _review: "+_review );
                        }
                    });
                }
            });
        }
        if (requestCode==ImgBack2&&resultCode==RESULT_OK){
            if (_review1!=null){
                chucNang.Delete_image(_review1,getContext());
            }
            Uri ImageData = data.getData();
            Picasso.get().load(ImageData).resize(350,200).centerCrop().into(img2);
            StorageReference ImageName = Folder.child(folderImage+"3"+ImageData.getLastPathSegment());
            ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Tải lên ảnh review 2 thành công.", Toast.LENGTH_SHORT).show();
                    ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            _review1 = uri.toString();
                            Log.e("TAG", "onSuccess _review1: "+_review1 );
                        }
                    });
                }
            });
        }
        if (requestCode==ImgBack3&&resultCode==RESULT_OK){
            if (_review2!=null){
                chucNang.Delete_image(_review2,getContext());
            }
            Uri ImageData = data.getData();
            Picasso.get().load(ImageData).resize(350,200).centerCrop().into(img3);
            StorageReference ImageName = Folder.child(folderImage+"4"+ImageData.getLastPathSegment());
            ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Tải lên ảnh review 3 thành công.", Toast.LENGTH_SHORT).show();
                    ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            _review2 = uri.toString();
                            Log.e("TAG", "onSuccess _review2: "+_review2 );
                        }
                    });
                }
            });
        }
    }

    public void showMenu(){
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
                        game_dao.ReadFireBase_GametuyChon(list_GameOnline,adapter,1,0);
                        break;
                    case R.id.mn_chienThuat: tuyChon.setText("Chiến Thuật");
                        game_dao.ReadFireBase_GametuyChon(list_GameOnline,adapter,1,1);
                        break;
                    case R.id.mn_dinhDi: tuyChon.setText("Kinh Dị");
                        game_dao.ReadFireBase_GametuyChon(list_GameOnline,adapter,1,2);
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
                    Thread.sleep(1000);
                    swipeRefreshLayout.setRefreshing(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void checkPer(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                return;
            }
        }
    }
}