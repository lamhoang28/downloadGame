package com.example.proteam5.Tab_Apps;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proteam5.DAO.ProTeam5;
import com.example.proteam5.DAO.TinTuc_DAO;
import com.example.proteam5.DAO.User_DAO;
import com.example.proteam5.Model.TinTuc;
import com.example.proteam5.Model_Adapter.TinTuc_Adapter;
import com.example.proteam5.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Frag_TinTuc extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final int ImgBack = 1;
    private static final int ImgBack1 = 2;
    private static final int ImgBack2 = 3;
    public static final int RESULT_OK = -1;

    SwipeRefreshLayout swipeRefreshLayout;
    FirebaseStorage storage;
    StorageReference Folder;

    private FirebaseFirestore firebaseFirestore;

    LinearLayoutManager managerTinTuc;

    RecyclerView recyclerViewTinTuc;

    TinTuc_Adapter adapterTinTuc;
    SearchView SearchView;

    TinTuc tinTuc;
    User_DAO user_dao;
    ProTeam5 chucNang;

    List<TinTuc> listTinTuc;
    TinTuc_DAO tinTuc_dao;

    EditText tieuDe, noiDung, urlVideo;
    String _tieuDe, _noiDung, _urlVideo;
    Button themTinTuc, huyThem;
    FloatingActionButton floatingActionButton;
    ImageView addimg, addimg1, addimg2;
    TextView error;
    Dialog dialog;
    String link, link1, link2;



    public Frag_TinTuc() {
    }

    public static Frag_TinTuc newInstance(String param1, String param2) {
        Frag_TinTuc fragment = new Frag_TinTuc();
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
        View view = inflater.inflate(R.layout.fragment_frag_tin_tuc, container, false);
        SearchView = view.findViewById(R.id.SearchView_tinTuc);
        SearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("TAG", "query: "+query);
                adapterTinTuc.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("TAG", "onQueryTextChange: "+newText);
                adapterTinTuc.getFilter().filter(newText);
                return false;
            }
        });
        AnhXa(view);

        READ();
        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPer();
                link = null;
                link1 = null;
                link2 = null;
                dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.dialog_tintuc);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                tieuDe = dialog.findViewById(R.id.edt_add_tieuDe_tinTuc);
                noiDung = dialog.findViewById(R.id.edt_add_noiDung_tinTuc);
                addimg = dialog.findViewById(R.id.btn_add_img);
                addimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, ImgBack);
                    }
                });
                addimg1 = dialog.findViewById(R.id.btn_add_img1);
                addimg1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, ImgBack1);
                    }
                });
                addimg2 = dialog.findViewById(R.id.btn_add_img2);
                addimg2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, ImgBack2);
                    }
                });
                urlVideo = dialog.findViewById(R.id.edt_add_videoTinTuc);
                error = dialog.findViewById(R.id.tv_error_tinTuc);
                themTinTuc = dialog.findViewById(R.id.btn_add_TinTuc);

                themTinTuc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _tieuDe = tieuDe.getText().toString().trim();
                        _noiDung = noiDung.getText().toString().trim();
                        _urlVideo = urlVideo.getText().toString().trim();

                        if (_tieuDe.isEmpty() ||
                                _noiDung.isEmpty() ||
                                _urlVideo.isEmpty() || link == null || link1 == null || link2 == null) {
                            error.setText("Thiếu thông Tin");
                            return;
                        }
                        tinTuc_dao = new TinTuc_DAO();

                        tinTuc = new TinTuc("ID_REFERENCES", _tieuDe, link, link1, link2, _urlVideo, _noiDung, Calendar.getInstance().getTime());
                        tinTuc_dao.CreateNewTinTuc(tinTuc);
                        tinTuc_dao.ReadFireBase_TinTuc(listTinTuc, adapterTinTuc);
                        dialog.dismiss();
                    }
                });
                huyThem = dialog.findViewById(R.id.btn_huy_TinTuc);
                huyThem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (link != null) {
                            chucNang.Delete_image(link, getContext());
                        }
                        if (link1 != null) {
                            chucNang.Delete_image(link1, getContext());
                        }
                        if (link2 != null) {
                            chucNang.Delete_image(link2, getContext());
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

    private void READ() {
        firebaseFirestore.collection("TinTuc").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                tinTuc_dao.ReadFireBase_TinTuc(listTinTuc, adapterTinTuc);
            }
        });
    }


    public void AnhXa(View view) {
        swipeRefreshLayout = view.findViewById(R.id.SwipeRefreshLayout_tinTuc);
        storage = FirebaseStorage.getInstance();
        Folder = FirebaseStorage.getInstance().getReference().child("ImageTinTuc");
        tinTuc_dao = new TinTuc_DAO();
        chucNang = new ProTeam5();
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerViewTinTuc = view.findViewById(R.id.recyclerview_tinTuc);
        managerTinTuc = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        recyclerViewTinTuc.setLayoutManager(managerTinTuc);
        listTinTuc = new ArrayList<>();
        adapterTinTuc = new TinTuc_Adapter(listTinTuc, view.getContext());
        recyclerViewTinTuc.setAdapter(adapterTinTuc);
        floatingActionButton = view.findViewById(R.id.floating_tintuc);
        user_dao = new User_DAO(getContext());
        int ADMIN = user_dao.check_Admin();
        if (ADMIN != 1) {
            floatingActionButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImgBack && resultCode == RESULT_OK) {
            if (link != null) {
                chucNang.Delete_image(link, getContext());
            }
            Uri ImageData = data.getData();
            Picasso.get().load(ImageData).resize(350, 200).centerCrop().into(addimg);
            StorageReference ImageName = Folder.child(tieuDe.getText() + ImageData.getLastPathSegment());
            ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Upload Success", Toast.LENGTH_SHORT).show();
                    ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            link = uri.toString();
                            Log.e("TAG", "onSuccess: " + link);
                        }
                    });
                }
            });
        }
        if (requestCode == ImgBack1 && resultCode == RESULT_OK) {
            if (link1 != null) {
                chucNang.Delete_image(link1, getContext());
            }
            Uri ImageData = data.getData();
            Picasso.get().load(ImageData).resize(350, 200).centerCrop().into(addimg1);
            StorageReference ImageName = Folder.child(tieuDe.getText() + ImageData.getLastPathSegment());
            ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Upload Success", Toast.LENGTH_SHORT).show();
                    ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            link1 = uri.toString();
                            Log.e("TAG", "onSuccess: " + link1);
                        }
                    });
                }
            });
        }
        if (requestCode == ImgBack2 && resultCode == RESULT_OK) {
            if (link2 != null) {
                chucNang.Delete_image(link2, getContext());
            }
            Uri ImageData = data.getData();
            Picasso.get().load(ImageData).resize(350, 200).centerCrop().into(addimg2);
            StorageReference ImageName = Folder.child(tieuDe.getText() + ImageData.getLastPathSegment());
            ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Upload Success", Toast.LENGTH_SHORT).show();
                    ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            link2 = uri.toString();
                            Log.e("TAG", "onSuccess: " + link2);
                        }
                    });
                }
            });
        }
    }


    @Override
    public void onRefresh() {
        tinTuc_dao.ReadFireBase_TinTuc(listTinTuc, adapterTinTuc);
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

    public void checkPer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                return;
            }
        }
    }
}