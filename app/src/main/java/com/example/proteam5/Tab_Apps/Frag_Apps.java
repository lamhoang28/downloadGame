package com.example.proteam5.Tab_Apps;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proteam5.DAO.Apps_DAO;
import com.example.proteam5.DAO.ProTeam5;
import com.example.proteam5.DAO.User_DAO;
import com.example.proteam5.Model.Apps;
import com.example.proteam5.Model.Game;
import com.example.proteam5.Model_Adapter.Apps_Adapter;
import com.example.proteam5.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
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

public class Frag_Apps extends Fragment {

    private static final int ImgBack1 = 1;
    private static final int ImgBack2 = 2;
    private static final int ImgBack3 = 3;
    private static final int ImgBack4 = 4;
    public static final int RESULT_OK = -1;

    Apps apps;
    Apps_DAO apps_dao;
    ProTeam5 chucNang;
    FirebaseFirestore firebaseFirestore;
    StorageReference Folder;

    List<Apps> list;
    Apps_Adapter adapter;
    RecyclerView recyclerView;

    EditText tenApps,gioiThieu,FAQ,link;
    ImageView imgApps,review,review1,review2;
    String _tengApps,_gioiThieu, _imgApps,_review,_review1,_review2,_dieuKhoan,_link;
    TextView error;
    Button them;


    public Frag_Apps() {
    }

    public static Frag_Apps newInstance(String param1, String param2) {
        Frag_Apps fragment = new Frag_Apps();
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
        View view= inflater.inflate(R.layout.fragment_frag_apps, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView_Apps);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        list = new ArrayList<>();
        adapter = new Apps_Adapter(list,view.getContext());
        recyclerView.setAdapter(adapter);
        READ();
        apps_dao = new Apps_DAO();
        chucNang = new ProTeam5();
        Folder = FirebaseStorage.getInstance().getReference().child("ImageApps");

        User_DAO user_dao = new User_DAO(getContext());
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floating_Apps);
        int ADMIN =user_dao.check_Admin();
        if (ADMIN!=1){
            floatingActionButton.setVisibility(View.GONE);
        }


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.dialog_apps);
                tenApps = dialog.findViewById(R.id.edt_add_nameApps);
                gioiThieu = dialog.findViewById(R.id.edt_add_GioiThieuApps);
                imgApps = dialog.findViewById(R.id.edt_add_imgAppsShow);
                imgApps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, ImgBack1);
                    }
                });
                review = dialog.findViewById(R.id.edt_add_AppsReview);
                review.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, ImgBack2);
                    }
                });
                review1 = dialog.findViewById(R.id.edt_add_AppsReview1);
                review1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, ImgBack3);
                    }
                });
                review2 = dialog.findViewById(R.id.edt_add_AppsReview2);
                review2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, ImgBack4);
                    }
                });

                FAQ = dialog.findViewById(R.id.edt_add_dieuKhoanApps);
                link = dialog.findViewById(R.id.edt_add_linkApps);
                error = dialog.findViewById(R.id.tv_error_createApps);
                them = dialog.findViewById(R.id.btn_addApps);
                them.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _tengApps = tenApps.getText().toString().trim();
                        _gioiThieu =gioiThieu.getText().toString().trim();
                        _dieuKhoan =FAQ.getText().toString().trim();
                        _link =link.getText().toString().trim();

                        if (_tengApps.isEmpty()||
                                _gioiThieu.isEmpty()||
                                _dieuKhoan.isEmpty()||
                                _link.isEmpty())
                        {
                            error.setText("opps !!! Thiếu thông tin");
                            return;
                        }
                        if (
                        _imgApps==null||
                        _review==null||
                        _review1==null||
                        _review2==null)
                        {
                            error.setText("opps !!! Thiếu thông tin");
                            return;
                        }

                        apps = new Apps("tengameModgioiThieuimgGame", _tengApps,_gioiThieu, _imgApps,_review,_review1,_review2,_dieuKhoan,_link);
                        apps_dao.CreateNew(apps);
                        apps_dao.ReadFireBase_Apps(list,adapter);
                        _imgApps= null;
                        _review= null;
                        _review1= null;
                        _review2= null;
                        dialog.dismiss();
                    }
                });
                Button cance = dialog.findViewById(R.id.btn_huyAddApps);
                cance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (_imgApps != null) {
                            chucNang.Delete_image(_imgApps, getContext());
                        }
                        if (_review != null) {
                            chucNang.Delete_image(_review, getContext());
                        }
                        if (_review1 != null) {
                            chucNang.Delete_image(_review1, getContext());
                        }
                        if (_review2 != null) {
                            chucNang.Delete_image(_review2, getContext());
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
        firebaseFirestore.collection("Apps").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                apps_dao.ReadFireBase_Apps(list,adapter);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImgBack1 && resultCode == RESULT_OK) {
            if (_imgApps != null) {
                chucNang.Delete_image(_imgApps, getContext());
            }
            Uri ImageData = data.getData();
            Picasso.get().load(ImageData).resize(350, 200).centerCrop().into(imgApps);
            StorageReference ImageName = Folder.child(tenApps.getText() + ImageData.getLastPathSegment());
            ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Upload Success", Toast.LENGTH_SHORT).show();
                    ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            _imgApps = uri.toString();
                            Log.e("TAG", "onSuccess: " + _imgApps);
                        }
                    });
                }
            });
        }
        if (requestCode == ImgBack2 && resultCode == RESULT_OK) {
            if (_review != null) {
                chucNang.Delete_image(_review, getContext());
            }
            Uri ImageData = data.getData();
            Picasso.get().load(ImageData).resize(350, 200).centerCrop().into(review);
            StorageReference ImageName = Folder.child(tenApps.getText() + ImageData.getLastPathSegment());
            ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Upload Success", Toast.LENGTH_SHORT).show();
                    ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            _review = uri.toString();
                            Log.e("TAG", "onSuccess: " + _review);
                        }
                    });
                }
            });
        }
        if (requestCode == ImgBack3 && resultCode == RESULT_OK) {
            if (_review1 != null) {
                chucNang.Delete_image(_review1, getContext());
            }
            Uri ImageData = data.getData();
            Picasso.get().load(ImageData).resize(350, 200).centerCrop().into(review1);
            StorageReference ImageName = Folder.child(tenApps.getText() + ImageData.getLastPathSegment());
            ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Upload Success", Toast.LENGTH_SHORT).show();
                    ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            _review1 = uri.toString();
                            Log.e("TAG", "onSuccess: " + _review1);
                        }
                    });
                }
            });
        }
        if (requestCode == ImgBack4 && resultCode == RESULT_OK) {
            if (_review2!= null) {
                chucNang.Delete_image(_review2 ,getContext());
            }
            Uri ImageData = data.getData();
            Picasso.get().load(ImageData).resize(350, 200).centerCrop().into(review2);
            StorageReference ImageName = Folder.child(tenApps.getText() + ImageData.getLastPathSegment());
            ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Upload Success", Toast.LENGTH_SHORT).show();
                    ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            _review2 = uri.toString();
                            Log.e("TAG", "onSuccess: " + _review2);
                        }
                    });
                }
            });
        }
    }
}