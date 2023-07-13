package com.example.proteam5.Tab_Apps.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proteam5.DAO.ProTeam5;
import com.example.proteam5.DAO.TinTuc_DAO;
import com.example.proteam5.DAO.User_DAO;
import com.example.proteam5.Model.TinTuc;
import com.example.proteam5.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class Acti_ViewTinTuc extends AppCompatActivity {
    public static final int RESULT_OK = -1;
    private static final int ImgBack = 1;
    private static final int ImgBack1 = 2;
    private static final int ImgBack2 = 3;
    FirebaseStorage storage;
    StorageReference Folder;
    TinTuc tinTuc;
    Dialog dialog;
    TinTuc_DAO tinTuc_dao;
    ProTeam5 chucNang;
    User_DAO user_dao;
    int ADMIN;

    EditText edt_tieuDe, edt_noiDung, edt_urlVideo;

    TextView tv_tieuDe, tv_noiDung, tv_error;
    ImageView Anh, Anh1, Anh2, addimg, addimg1, addimg2;
    String _urlVideo, _index, _link, _link1, _link2, _tieuDe, _noiDung;
    Button update, editTinTuc;
    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acti_tin_tuc);

        AnhXa();

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        tinTuc = (TinTuc) bundle.get("TinTuc");

        _index = tinTuc.getMaBaiViet();


        tv_tieuDe.setText(tinTuc.getTieuDe());
        tv_noiDung.setText(tinTuc.getNoiDung());
        _link = tinTuc.getImgShow();
        _link1 = tinTuc.getImgReView();
        _link2 = tinTuc.getImgReView1();

        Picasso.get().load(_link).resize(350, 200).centerCrop().into(Anh);
        Picasso.get().load(_link1).resize(350, 200).centerCrop().into(Anh1);
        Picasso.get().load(_link2).resize(350, 200).centerCrop().into(Anh2);

        _urlVideo = tinTuc.getUrlVideo();
        youTubePlayerView = findViewById(R.id.YouTubePlayerView_viewTinTuc);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                youTubePlayer.loadVideo(_urlVideo, 0);
            }
        });


        if (user_dao.check_Admin() != 1) {
            update.setVisibility(View.GONE);
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.dialog_tintuc);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                edt_tieuDe = dialog.findViewById(R.id.edt_add_tieuDe_tinTuc);
                edt_tieuDe.setText(tinTuc.getTieuDe());
                edt_noiDung = dialog.findViewById(R.id.edt_add_noiDung_tinTuc);
                edt_noiDung.setText(tinTuc.getNoiDung());
                addimg = dialog.findViewById(R.id.btn_add_img);
                addimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chucNang.Delete_image(_link, Acti_ViewTinTuc.this);
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, ImgBack);
                    }
                });
                addimg1 = dialog.findViewById(R.id.btn_add_img1);
                addimg1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chucNang.Delete_image(_link1, Acti_ViewTinTuc.this);
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, ImgBack1);
                    }
                });
                addimg2 = dialog.findViewById(R.id.btn_add_img2);
                addimg2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chucNang.Delete_image(_link2, Acti_ViewTinTuc.this);
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, ImgBack2);
                    }
                });

                Picasso.get().load(_link).resize(350, 200).centerCrop().into(addimg);
                Picasso.get().load(_link1).resize(350, 200).centerCrop().into(addimg1);
                Picasso.get().load(_link2).resize(350, 200).centerCrop().into(addimg2);

                edt_urlVideo = dialog.findViewById(R.id.edt_add_videoTinTuc);
                edt_urlVideo.setText(tinTuc.getUrlVideo());
                tv_error = dialog.findViewById(R.id.tv_error_tinTuc);
                editTinTuc = dialog.findViewById(R.id.btn_add_TinTuc);
                editTinTuc.setText("Sửa bài viết");
                editTinTuc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _tieuDe = edt_tieuDe.getText().toString().trim();
                        _noiDung = edt_noiDung.getText().toString().trim();
                        _urlVideo = edt_urlVideo.getText().toString().trim();

                        if (_tieuDe.isEmpty() ||
                                _noiDung.isEmpty() ||
                                _urlVideo.isEmpty()) {
                            tv_error.setText("Thiếu thông Tin");
                            return;
                        }

                        tinTuc_dao = new TinTuc_DAO();
                        tinTuc = new TinTuc(tinTuc.getMaBaiViet(), _tieuDe, _link, _link1, _link2, _urlVideo, _noiDung, tinTuc.getNgaydang());
                        tinTuc_dao.EditTinTuc(tinTuc, tinTuc.getMaBaiViet(), Acti_ViewTinTuc.this);
                        dialog.dismiss();
                    }
                });
                Button huy = dialog.findViewById(R.id.btn_huy_TinTuc);
                huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    public void AnhXa() {
        storage = FirebaseStorage.getInstance();
        Folder = FirebaseStorage.getInstance().getReference().child("ImageTinTuc");
        tv_tieuDe = findViewById(R.id.tv_viewTinTuc_title);
        tv_noiDung = findViewById(R.id.tv_viewTinTuc_noiDung);
        Anh = findViewById(R.id.img_viewTinTuc_imgShow);
        Anh1 = findViewById(R.id.img_viewTinTuc_anhReview1);
        Anh2 = findViewById(R.id.img_viewTinTuc_anhReview2);
        tinTuc_dao = new TinTuc_DAO();
        update = findViewById(R.id.btn_viewTinTuc_Update);
        user_dao = new User_DAO(this);
        chucNang = new ProTeam5();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImgBack && resultCode == RESULT_OK) {
            Uri ImageData = data.getData();
            Picasso.get().load(ImageData).resize(350, 200).centerCrop().into(addimg);
            StorageReference ImageName = Folder.child(tinTuc.getTieuDe() + ImageData.getLastPathSegment());
            ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Acti_ViewTinTuc.this, "Upload Success", Toast.LENGTH_SHORT).show();
                    ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            _link = uri.toString();
                            Log.e("TAG", "sua anh goc thu 1 thanh cong: " + _link);
                        }
                    });
                }
            });

        }
        if (requestCode == ImgBack1 && resultCode == RESULT_OK) {
            Uri ImageData = data.getData();
            Picasso.get().load(ImageData).resize(350, 200).centerCrop().into(addimg1);
            StorageReference ImageName = Folder.child(tinTuc.getTieuDe() + ImageData.getLastPathSegment());
            ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Acti_ViewTinTuc.this, "Upload Success", Toast.LENGTH_SHORT).show();
                    ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            _link1 = uri.toString();
                            Log.e("TAG", "sua anh goc thu 2 thanh cong: " + _link1);
                        }
                    });
                }
            });
        }
        if (requestCode == ImgBack2 && resultCode == RESULT_OK) {
            Uri ImageData = data.getData();
            Picasso.get().load(ImageData).resize(350, 200).centerCrop().into(addimg2);
            StorageReference ImageName = Folder.child(tinTuc.getTieuDe() + ImageData.getLastPathSegment());
            ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Acti_ViewTinTuc.this, "Upload Success", Toast.LENGTH_SHORT).show();
                    ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            _link2 = uri.toString();
                            Log.e("TAG", "sua anh goc thu 3 thanh cong: " + _link2);
                        }
                    });
                }
            });
        }
    }
}