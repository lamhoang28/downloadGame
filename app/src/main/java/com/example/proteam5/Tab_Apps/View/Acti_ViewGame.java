package com.example.proteam5.Tab_Apps.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proteam5.DAO.Comment_DAO;
import com.example.proteam5.DAO.Game_DAO;
import com.example.proteam5.DAO.HoaDonDao;
import com.example.proteam5.DAO.User_DAO;
import com.example.proteam5.Model.Comment;
import com.example.proteam5.Model.Game;
import com.example.proteam5.DAO.ProTeam5;
import com.example.proteam5.Model.HoaDon;
import com.example.proteam5.Model.User;
import com.example.proteam5.Model_Adapter.Comment_Adapter;
import com.example.proteam5.R;
import com.example.proteam5.Tab_Apps.HuongDan.Acti_HuongDan;
import com.example.proteam5.Tab_Apps.HuongDan.Acti_HuongDanObb;
import com.example.proteam5.app;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Acti_ViewGame extends AppCompatActivity {

    private static final int REQUSET_PERMISTION_CODE = 10;
    NotificationManagerCompat notificationManagerCompat;
    StorageReference Folder;
    YouTubePlayerView youTubePlayerView;
    LinearLayoutManager linearLayoutManager;
    Comment_Adapter adapter;

    ImageView imgShow, imgReview1, imgReview2, imgReview3;
    TextView tv_TenGame, tv_GioiThieu, tv_FAQ, tv_TheLoai, tv_Download;
    EditText edt_tenGameOrMod, edt_gioiThieu, edt_giaTien, edt_FAQ, edt_urlVideo, edt_linkGame, edt_titleComment;
    RadioButton hanhDong, chienThuat, kinhDi, ON, OFF;

    int checkonline = 0, checkTheLoai = 0;
    String INDEX, _tengameMod, _gioiThieu, _imgShow, _review, _review1, _review2, _urlVideo, _dieuKhoan, _linkDownload, _coin;
    TextView error;
    Game_DAO game_dao;
    Comment_DAO comment_dao;
    ProTeam5 chucNang;
    HoaDonDao hoaDonDao;




    Button update, edit, ViewApk, ViewObb, comment;
    ImageView btn_img, btn_img1, btn_img2, btn_img3;
    String id, urlVideo, theloaiGame, onOff, nameGame, linkDownload, folderImage;



    private static final int ImgShow = 1;
    private static final int ImgReview = 2;
    private static final int ImgReview1 = 3;
    private static final int ImgReview2 = 4;
    public static final int RESULT_OK = -1;

    private FirebaseFirestore firebaseFirestore;
    private List<User> userList;
    private List<Comment> listComment;
    RecyclerView recyclerView;
    User_DAO user_dao;
    private User user;
    String mail, age, userCoin, avt, userName;
    int giaGame, ADMIN;
    FirebaseUser USER;
    Game gameMod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acti_view_game);
        AnhXa();
        ReadFireBase_User();
        READ();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        game_dao = new Game_DAO(this);
        gameMod = (Game) bundle.get("GameMod");
        id = gameMod.getMaBaiViet();
        linkDownload = gameMod.getLinkDownload();
        nameGame = gameMod.getTieuDe();
        tv_TenGame.setText(nameGame);
        tv_GioiThieu.setText(gameMod.getGioiThieu());
        giaGame = gameMod.getGiaTien();
        if (gameMod.getOnlineorOffline() == 0) {
            onOff = "Game Online";
        } else {
            onOff = "Game Offline";
        }

        if (gameMod.getTheLoai() == 0) {
            theloaiGame = "Hành Động";
        } else if (gameMod.getTheLoai() == 1) {
            theloaiGame = "Chiến Thuật";
        } else {
            theloaiGame = "kinh Dị";
        }
        tv_TheLoai.setText(" - " + onOff + " * " + "Thể loại " + theloaiGame);
        tv_FAQ.setText(gameMod.getDieuKhoan());
        urlVideo = gameMod.getUrlVideo();
        INDEX = gameMod.getMaBaiViet();
        _imgShow = gameMod.getImgShow();
        _review = gameMod.getImgReview();
        _review1 = gameMod.getImgReview1();
        _review2 = gameMod.getImgReview2();

        Picasso.get().load(_imgShow).resize(350, 200).centerCrop().into(imgShow);
        Picasso.get().load(_review).resize(350, 200).centerCrop().into(imgReview1);
        Picasso.get().load(_review1).resize(350, 200).centerCrop().into(imgReview2);
        Picasso.get().load(_review2).resize(350, 200).centerCrop().into(imgReview3);

        notificationManagerCompat = NotificationManagerCompat.from(this);

        String titleAll = "Mua " + gameMod.getTieuDe() + " với giá " + gameMod.getGiaTien() + "$";
        game_dao.checkActiMuaGame(tv_Download, gameMod.getMaBaiViet(), titleAll, this);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                youTubePlayer.loadVideo(gameMod.getUrlVideo(), 0);
            }
        });


        tv_Download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_Download.getText().toString().equals("  Đã mua")) {
                    Dialog dialog = new Dialog(Acti_ViewGame.this);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.dialog_thongbao_download);
                    ImageView imageView = dialog.findViewById(R.id.img_hinhAnhDownLoad);
                    Button dowloadApk = dialog.findViewById(R.id.btn_DownLoadApk);
                    Picasso.get().load(gameMod.getImgShow()).resize(390, 200).centerCrop().into(imageView);
                    TextView tv = dialog.findViewById(R.id.tv_thua);
                    tv.setVisibility(View.GONE);
                    dowloadApk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkPerMistion();
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    int CoinUser = Integer.parseInt(userCoin);
                    if (CoinUser < gameMod.getGiaTien()) {
                        Toast.makeText(Acti_ViewGame.this, "Bạn không đủ Coin.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Dialog dialog = new Dialog(v.getContext());
                    dialog.setContentView(R.layout.dialog_thanhtoan);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView content = dialog.findViewById(R.id.tv_dialog_content_thanhToan);
                    content.setText("Mua " + gameMod.getTieuDe() + " phí thanh toán " + gameMod.getGiaTien() + " $");
                    TextView yes = dialog.findViewById(R.id.btn_dialog_yes_thanhToan);
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String title1 = "ProTeam5";
                            String message = "Đã mua " + gameMod.getTieuDe();
                            Notification notification = new NotificationCompat.Builder(Acti_ViewGame.this, app.CHANNEL_1_ID)
                                    .setSmallIcon(R.drawable.ic_caidat)
                                    .setContentTitle(title1)
                                    .setContentText(message)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                    .build();
                            int notificationId = 1;
                            notificationManagerCompat.notify(notificationId, notification);
                            themHoaDon();
                            dialog.dismiss();
                            game_dao.checkActiMuaGame(tv_Download, gameMod.getMaBaiViet(), titleAll, Acti_ViewGame.this);
                        }
                    });
                    TextView no = dialog.findViewById(R.id.btn_dialog_no_thanhToan);
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }

            }
        });
        update = findViewById(R.id.btn_viewGame_Update);
        ADMIN = user_dao.check_Admin();
        if (ADMIN != 1) {
            update.setVisibility(View.GONE);
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.dialog_game);

                edt_tenGameOrMod = dialog.findViewById(R.id.edt_add_nameGame);
                edt_gioiThieu = dialog.findViewById(R.id.edt_add_GioiThieuGame);
                edt_giaTien = dialog.findViewById(R.id.edt_add_CoinGame);
                edt_FAQ = dialog.findViewById(R.id.edt_add_dieuKhoanGame);
                edt_urlVideo = dialog.findViewById(R.id.edt_add_videoGame);
                edt_linkGame = dialog.findViewById(R.id.edt_add_linkApk);
                error = dialog.findViewById(R.id.tv_error_createGameorMod);
                TextView tiltle = dialog.findViewById(R.id.tv_add_title);

                //radio
                hanhDong = dialog.findViewById(R.id.radio_hd);
                chienThuat = dialog.findViewById(R.id.radio_ct);
                kinhDi = dialog.findViewById(R.id.radio_kd);
                ON = dialog.findViewById(R.id.radio_online);
                OFF = dialog.findViewById(R.id.radio_offline);

                if (gameMod.getOnlineorOffline() == 0) {
                    ON.setChecked(true);
                } else {
                    OFF.setChecked(true);
                }

                if (gameMod.getTheLoai() == 0) {
                    hanhDong.setChecked(true);
                } else if (gameMod.getTheLoai() == 1) {
                    chienThuat.setChecked(true);
                } else {
                    kinhDi.setChecked(true);
                }

                //settextt
                tiltle.setText("Sửa Thông Tin Game");
                edt_tenGameOrMod.setText(gameMod.getTieuDe());
                edt_gioiThieu.setText(gameMod.getGioiThieu());
                edt_giaTien.setText(String.valueOf(gameMod.getGiaTien()));
                edt_FAQ.setText(gameMod.getDieuKhoan());
                edt_urlVideo.setText(gameMod.getUrlVideo());
                edt_linkGame.setText(gameMod.getLinkDownload());
                //setImage


                btn_img = dialog.findViewById(R.id.btn_add_imgGame);
                btn_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chucNang.Delete_image(gameMod.getImgShow(), Acti_ViewGame.this);
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, ImgShow);
                    }
                });
                btn_img1 = dialog.findViewById(R.id.btn_add_imgGame1);
                btn_img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chucNang.Delete_image(gameMod.getImgReview(), Acti_ViewGame.this);
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, ImgReview);
                    }
                });
                btn_img2 = dialog.findViewById(R.id.btn_add_imgGame2);
                btn_img2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chucNang.Delete_image(gameMod.getImgReview1(), Acti_ViewGame.this);
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, ImgReview1);
                    }
                });
                btn_img3 = dialog.findViewById(R.id.btn_add_imgGame3);
                btn_img3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chucNang.Delete_image(gameMod.getImgReview2(), Acti_ViewGame.this);
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, ImgReview2);
                    }
                });
                Picasso.get().load(_imgShow).resize(350, 200).centerCrop().into(btn_img);
                Picasso.get().load(_review).resize(350, 200).centerCrop().into(btn_img1);
                Picasso.get().load(_review1).resize(350, 200).centerCrop().into(btn_img2);
                Picasso.get().load(_review2).resize(350, 200).centerCrop().into(btn_img3);
                edit = dialog.findViewById(R.id.btn_addGame);
                Button huy = dialog.findViewById(R.id.btn_CannerGame);
                huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                edit.setText("Sửa Thông Tin Game");
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _tengameMod = edt_tenGameOrMod.getText().toString().trim();
                        _gioiThieu = edt_gioiThieu.getText().toString().trim();
                        _dieuKhoan = edt_FAQ.getText().toString().trim();
                        _linkDownload = edt_linkGame.getText().toString().trim();
                        _urlVideo = edt_urlVideo.getText().toString();
                        _coin = edt_giaTien.getText().toString();
                        if (!ON.isChecked() && !OFF.isChecked()) {
                            error.setText("Thiếu thông tin Game Online hay Offline");
                            return;
                        }
                        if (!hanhDong.isChecked() && !chienThuat.isChecked() && !kinhDi.isChecked()) {
                            error.setText("Thiếu Thể Loại Game");
                            return;
                        }

                        if (ON.isChecked()) {
                            checkonline = 0;
                        }
                        if (OFF.isChecked()) {
                            checkonline = 1;
                        }

                        if (hanhDong.isChecked()) {
                            checkTheLoai = 0;
                        }
                        if (chienThuat.isChecked()) {
                            checkTheLoai = 1;
                        }
                        if (kinhDi.isChecked()) {
                            checkTheLoai = 2;
                        }

                        if (_tengameMod.isEmpty() ||
                                _gioiThieu.isEmpty() ||
                                _dieuKhoan.isEmpty() ||
                                _linkDownload.isEmpty() ||
                                _urlVideo.isEmpty() ||
                                _coin.isEmpty()) {
                            error.setText("Thiếu thông tin");
                            return;
                        }

                        int Giagame = Integer.parseInt(_coin);

                        Game game = new Game(gameMod.getMaBaiViet(), _tengameMod, _gioiThieu, _imgShow, _review, _review1, _review2, _urlVideo, _dieuKhoan, _linkDownload, checkonline, checkTheLoai, Giagame,0);
                        game_dao.Edit_Game(game, gameMod.getMaBaiViet(), Acti_ViewGame.this);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String avt = USER.getPhotoUrl().toString();
                String titlee = edt_titleComment.getText().toString();
                if (titlee.isEmpty()) {
                    Toast.makeText(Acti_ViewGame.this, "Chưa nhập nội dung comment", Toast.LENGTH_SHORT).show();
                    return;
                }
                Comment comment = new Comment("idtutangtufirebse", avt, USER.getDisplayName(),USER.getEmail(), gameMod.getMaBaiViet(), titlee, Calendar.getInstance().getTime());
                comment_dao.CreateNewComment(comment);
                edt_titleComment.setText("");
                READ();
            }
        });
    }

    public void AnhXa() {
        user_dao = new User_DAO(this);
        chucNang = new ProTeam5();
        hoaDonDao = new HoaDonDao(this);
        user_dao = new User_DAO(this);
        comment_dao = new Comment_DAO();
        user = new User();

        userList = new ArrayList<>();

        USER = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        listComment = new ArrayList<>();
        recyclerView = findViewById(R.id.RecyclerView_CommentGame);
        linearLayoutManager = new LinearLayoutManager(Acti_ViewGame.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new Comment_Adapter(listComment, Acti_ViewGame.this);
        recyclerView.setAdapter(adapter);

        Folder = FirebaseStorage.getInstance().getReference().child("ImageGame");
        imgShow = findViewById(R.id.img_add_anhGame);
        imgReview1 = findViewById(R.id.img_add_anhReview);
        imgReview2 = findViewById(R.id.img_add_anhReview1);
        imgReview3 = findViewById(R.id.img_add_anhReview2);

        tv_TenGame = findViewById(R.id.tv_add_tenGame);
        tv_TheLoai = findViewById(R.id.tv_add_theLoai);
        tv_GioiThieu = findViewById(R.id.tv_add_gioiThieuGame);
        tv_FAQ = findViewById(R.id.tv_add_DieuKhoan);
        tv_Download = findViewById(R.id.btn_add_downloadGame);

        ViewApk = findViewById(R.id.btn_ViewGame_huongDanApk);
        ViewObb = findViewById(R.id.btn_ViewGame_huongDanObb);
        //
        youTubePlayerView = findViewById(R.id.YouTubePlayerView_viewGame);

        ViewApk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Acti_ViewGame.this, Acti_HuongDan.class));
            }
        });
        ViewObb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Acti_ViewGame.this, Acti_HuongDanObb.class));
            }
        });
        comment = findViewById(R.id.btn_addComment);
        edt_titleComment = findViewById(R.id.edt_commnent_Game);
    }

    private void READ() {
        firebaseFirestore.collection("Comment").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                comment_dao.ReadFireBase_Comment(listComment, adapter, id);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        folderImage = edt_tenGameOrMod.getText().toString().trim();
        if (requestCode == ImgShow && resultCode == RESULT_OK) {
            Uri ImageData = data.getData();
            Picasso.get().load(ImageData).resize(350, 200).centerCrop().into(btn_img);
            StorageReference ImageName = Folder.child(folderImage + "1" + ImageData.getLastPathSegment());
            ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Acti_ViewGame.this, "Upload Success", Toast.LENGTH_SHORT).show();
                    ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            _imgShow = uri.toString();
                            Log.e("TAG", "sua anh _imgShow thanh cong: " + _imgShow);
                        }
                    });
                }
            });
        }
        if (requestCode == ImgReview && resultCode == RESULT_OK) {
            Uri ImageData = data.getData();
            Picasso.get().load(ImageData).resize(350, 200).centerCrop().into(btn_img1);
            StorageReference ImageName = Folder.child(folderImage + "2" + ImageData.getLastPathSegment());
            ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Acti_ViewGame.this, "Upload Success", Toast.LENGTH_SHORT).show();
                    ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            _review = uri.toString();
                            Log.e("TAG", "sua anh _review thanh cong: " + _review);
                        }
                    });
                }
            });
        }
        if (requestCode == ImgReview1 && resultCode == RESULT_OK) {
            Uri ImageData = data.getData();
            Picasso.get().load(ImageData).resize(350, 200).centerCrop().into(btn_img2);
            StorageReference ImageName = Folder.child(folderImage + "3" + ImageData.getLastPathSegment());
            ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Acti_ViewGame.this, "Upload Success", Toast.LENGTH_SHORT).show();
                    ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            _review1 = uri.toString();
                            Log.e("TAG", "sua anh _review1 thanh cong " + _review1);
                        }
                    });
                }
            });
        }
        if (requestCode == ImgReview2 && resultCode == RESULT_OK) {
            Uri ImageData = data.getData();
            Picasso.get().load(ImageData).resize(350, 200).centerCrop().into(btn_img3);
            StorageReference ImageName = Folder.child(folderImage + "4" + ImageData.getLastPathSegment());
            ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Acti_ViewGame.this, "Upload Success", Toast.LENGTH_SHORT).show();
                    ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            _review2 = uri.toString();
                            Log.e("TAG", "sua anh _review2 thanh cong: " + _review2);
                        }
                    });
                }
            });
        }
    }

    private void checkPerMistion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] perMis = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(perMis, REQUSET_PERMISTION_CODE);
            } else {
                hoaDonDao.startDownloadFile(gameMod.getLinkDownload());
            }
        } else {
            startDownloadFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUSET_PERMISTION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDownloadFile();
            } else {
                Toast.makeText(this, "Opps!!! :(((", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void startDownloadFile() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(linkDownload));
        Log.e("TAG", "startDownloadFile: " + linkDownload);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("Download");
        request.setDescription("DownloadFile ... ");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, String.valueOf(System.currentTimeMillis()));
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }
    }

    public void themHoaDon(){
        int CoinUser = Integer.parseInt(userCoin);
        int giaconlai = CoinUser - giaGame;
        int _age = Integer.parseInt(age);
        user = new User(avt, userName, USER.getEmail(), _age, giaconlai);
        user_dao.Update_User(user);
        HoaDon a = new HoaDon("tu sinh",gameMod.getMaBaiViet(), mail, gameMod.getImgShow(), gameMod.getTieuDe(), USER.getDisplayName(), gameMod.getLinkDownload(), gameMod.getGiaTien(), Calendar.getInstance().getTime());
        hoaDonDao.addHoaDon(a);
        gameMod.setLuotTai(gameMod.getLuotTai()+1);
        game_dao.Edit_Game(gameMod, gameMod.getMaBaiViet(), this);
    }
    public void ReadFireBase_User() {
        firebaseFirestore.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User us = document.toObject(User.class);
                                userList.add(us);
                            }
                            for (int i = 0; i < userList.size(); i++) {
                                if (userList.get(i).getMail().equals(USER.getEmail())) {
                                    mail = userList.get(i).getMail();
                                    age = String.valueOf(userList.get(i).getAge());
                                    userCoin = String.valueOf(userList.get(i).getCoin());
                                    avt = userList.get(i).getAvatar();
                                    userName = userList.get(i).getUserName();
                                }
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}