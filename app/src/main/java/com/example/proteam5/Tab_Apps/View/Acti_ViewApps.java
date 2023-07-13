package com.example.proteam5.Tab_Apps.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.proteam5.DAO.Apps_DAO;
import com.example.proteam5.DAO.ProTeam5;
import com.example.proteam5.DAO.User_DAO;
import com.example.proteam5.Model.Apps;
import com.example.proteam5.Model.TinTuc;
import com.example.proteam5.Model.User;
import com.example.proteam5.R;
import com.example.proteam5.app;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Acti_ViewApps extends AppCompatActivity {

    private static final int REQUSET_PERMISTION_CODE = 10;
    private static final int ImgBack1 = 1;
    private static final int ImgBack2 = 2;
    private static final int ImgBack3 = 3;
    private static final int ImgBack4 = 4;
    public static final int RESULT_OK = -1;
    NotificationManagerCompat notificationManagerCompat;
    StorageReference Folder;

    Apps_DAO apps_dao;
    User_DAO user_dao;
    ProTeam5 chucNang;

    TextView tieuDe,noiDung,Download,error;
    ImageView imgApps,review,review1,review2;
    EditText tenApps,gioiThieu,FAQ,linkDownload;
    String index,link;
    String _tieuDe,_noiDung,_imgApps,_review,_review1,_review2,_dieuKhoan,_link;
    Button update;
    Apps apps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acti_view_apps);

        apps_dao = new Apps_DAO();
        chucNang = new ProTeam5();
        user_dao = new User_DAO(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle==null){
            return;
        }
        apps = (Apps) bundle.get("Apps");
        Folder = FirebaseStorage.getInstance().getReference().child("ImageApps");
        index= apps.getID();
        link = apps.getLink();

        tieuDe = findViewById(R.id.tv_view_tenApps);
        noiDung = findViewById(R.id.tv_view_noiDungApps);
        imgApps = findViewById(R.id.img_view_anhReviewApps);
        review = findViewById(R.id.img_view_anhReview1Apps);
        review1 = findViewById(R.id.img_view_anhReview2Apps);
        review2 = findViewById(R.id.img_view_anhReview3Apps);

        tieuDe.setText(apps.getTieuDe());
        noiDung.setText(apps.getNoiDung());
        String link = apps.getImgShow();
        String link1 = apps.getImgReview();
        String link2 = apps.getImgReview1();
        String link3 = apps.getImgReview2();

        _imgApps = apps.getImgShow();
        _review = apps.getImgReview();
        _review1 = apps.getImgReview1();
        _review2 = apps.getImgReview2();

        Picasso.get().load(link).resize(350,200).centerCrop().into(imgApps);
        Picasso.get().load(link1).resize(350,200).centerCrop().into(review);
        Picasso.get().load(link2).resize(350,200).centerCrop().into(review1);
        Picasso.get().load(link3).resize(350,200).centerCrop().into(review2);
        Download = findViewById(R.id.btn_add_download_Apps);
        notificationManagerCompat = NotificationManagerCompat.from(this);

        String titleAll = "Mua "+apps.getTieuDe();

        Download.setText(titleAll);
        Download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.dialog_thanhtoan);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView content = dialog.findViewById(R.id.tv_dialog_content_thanhToan);
                content.setText("Mua "+apps.getTieuDe()+" miễn phí?");
                TextView yes = dialog.findViewById(R.id.btn_dialog_yes_thanhToan);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title1 = "ProTeam5";
                        String message = "Download "+apps.getTieuDe();
                        Notification notification = new NotificationCompat.Builder(Acti_ViewApps.this, app.CHANNEL_1_ID)
                                .setSmallIcon(R.drawable.ic_caidat)
                                .setContentTitle(title1)
                                .setContentText(message)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                .build();
                        int notificationId = 1;
                        notificationManagerCompat.notify(notificationId, notification);
                        checkPerMistion();
                        dialog.dismiss();
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
        });
        update =findViewById(R.id.btn_apps_update);
        if (user_dao.check_Admin()!=1){
            update.setVisibility(View.GONE);
        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String index = apps.getID();

                Button sua;
                Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.dialog_apps);

                String link = apps.getImgShow();
                String link1 = apps.getImgReview();
                String link2 = apps.getImgReview1();
                String link3 = apps.getImgReview2();

                ImageView appShow = dialog.findViewById(R.id.edt_add_imgAppsShow);
                appShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                ImageView appShow1 = dialog.findViewById(R.id.edt_add_AppsReview);
                appShow1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                ImageView appShow2 = dialog.findViewById(R.id.edt_add_AppsReview1);
                appShow2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                ImageView appShow3 = dialog.findViewById(R.id.edt_add_AppsReview2);
                appShow3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                Picasso.get().load(link).resize(350,200).centerCrop().into(appShow);
                Picasso.get().load(link1).resize(350,200).centerCrop().into(appShow1);
                Picasso.get().load(link2).resize(350,200).centerCrop().into(appShow2);
                Picasso.get().load(link3).resize(350,200).centerCrop().into(appShow3);

                tenApps = dialog.findViewById(R.id.edt_add_nameApps);
                tenApps.setText(apps.getTieuDe());
                gioiThieu = dialog.findViewById(R.id.edt_add_GioiThieuApps);
                gioiThieu.setText(apps.getNoiDung());
                imgApps = dialog.findViewById(R.id.edt_add_imgAppsShow);
                imgApps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (_imgApps != null) {
                            chucNang.Delete_image(_imgApps, Acti_ViewApps.this);
                        }
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, ImgBack1);
                    }
                });
                review = dialog.findViewById(R.id.edt_add_AppsReview);
                review.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (_review != null) {
                            chucNang.Delete_image(_review, Acti_ViewApps.this);
                        }
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, ImgBack2);
                    }
                });
                review1 = dialog.findViewById(R.id.edt_add_AppsReview1);
                review1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (_review1 != null) {
                            chucNang.Delete_image(_review1, Acti_ViewApps.this);
                        }
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, ImgBack3);
                    }
                });
                review2 = dialog.findViewById(R.id.edt_add_AppsReview2);
                review2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (_review2!= null) {
                            chucNang.Delete_image(_review2 ,Acti_ViewApps.this);
                        }
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, ImgBack4);
                    }
                });
                FAQ = dialog.findViewById(R.id.edt_add_dieuKhoanApps);
                FAQ.setText(apps.getFAQ());
                linkDownload = dialog.findViewById(R.id.edt_add_linkApps);
                linkDownload.setText(apps.getLink());
                error = dialog.findViewById(R.id.tv_error_createApps);
                sua = dialog.findViewById(R.id.btn_addApps);



                sua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _tieuDe = tenApps.getText().toString().trim();
                        _noiDung = gioiThieu.getText().toString().trim();
                        _dieuKhoan = FAQ.getText().toString().trim();
                        _link = linkDownload.getText().toString().trim();

                        apps = new Apps(index,_tieuDe,_noiDung,_imgApps,_review,_review1,_review2,_dieuKhoan,_link);

                        apps_dao.edit_Apps(apps,index,Acti_ViewApps.this);
                        dialog.dismiss();
                    }
                });
                dialog.show();
                Button huy = dialog.findViewById(R.id.btn_huyAddApps);
                huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImgBack1 && resultCode == RESULT_OK) {

            Uri ImageData = data.getData();
            Picasso.get().load(ImageData).resize(350, 200).centerCrop().into(imgApps);
            StorageReference ImageName = Folder.child(tenApps.getText() + ImageData.getLastPathSegment());
            ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Acti_ViewApps.this, "Upload Success", Toast.LENGTH_SHORT).show();
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

            Uri ImageData = data.getData();
            Picasso.get().load(ImageData).resize(350, 200).centerCrop().into(review);
            StorageReference ImageName = Folder.child(tenApps.getText() + ImageData.getLastPathSegment());
            ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Acti_ViewApps.this, "Upload Success", Toast.LENGTH_SHORT).show();
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

            Uri ImageData = data.getData();
            Picasso.get().load(ImageData).resize(350, 200).centerCrop().into(review1);
            StorageReference ImageName = Folder.child(tenApps.getText() + ImageData.getLastPathSegment());
            ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Acti_ViewApps.this, "Upload Success", Toast.LENGTH_SHORT).show();
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

            Uri ImageData = data.getData();
            Picasso.get().load(ImageData).resize(350, 200).centerCrop().into(review2);
            StorageReference ImageName = Folder.child(tenApps.getText() + ImageData.getLastPathSegment());
            ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Acti_ViewApps.this, "Upload Success", Toast.LENGTH_SHORT).show();
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

    private void checkPerMistion() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                String[] perMis ={Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(perMis,REQUSET_PERMISTION_CODE);
            }else{
                startDownloadFile();
                Toast.makeText(this, "Downloading", Toast.LENGTH_SHORT).show();
            }
        }else{
            startDownloadFile();
            Toast.makeText(this, "Downloading", Toast.LENGTH_SHORT).show();
        }
    }

    private void startDownloadFile() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(link));

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("Download");
        request.setDescription("DownloadFile ... ");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,String.valueOf(System.currentTimeMillis()));
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager!=null){
            downloadManager.enqueue(request);
        }
    }

}