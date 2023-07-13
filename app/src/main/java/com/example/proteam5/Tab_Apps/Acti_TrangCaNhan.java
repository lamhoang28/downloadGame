package com.example.proteam5.Tab_Apps;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proteam5.DAO.User_DAO;
import com.example.proteam5.MainActivity;
import com.example.proteam5.R;
import com.example.proteam5.Tab_Apps.MiniGame.Acti_MiniGame;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class Acti_TrangCaNhan extends AppCompatActivity {
    User_DAO user_dao;
    TextView tv_mail, tv_name, tv_age, tv_coin, img_addCoin;
    Button btn_update;
    CircleImageView circleImageView;
    LinearLayout btn_quanly;
    Uri ImageData;
    private static final int Img_User = 1;
    private static final int RESULT_OK = -1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Img_User && resultCode == RESULT_OK) {
            ImageData = data.getData();
            try {
                InputStream inputStream = this.getContentResolver().openInputStream(ImageData);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                circleImageView.setImageBitmap(bitmap);
                user_dao.Update_Avatar(ImageData, tv_name);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acti_trang_ca_nhan);
        user_dao = new User_DAO(this);
        img_addCoin = findViewById(R.id.btn_addCoin);
        tv_name = findViewById(R.id.tv_userName);
        tv_mail = findViewById(R.id.tv_userGmail);
        tv_age = findViewById(R.id.tv_userAge);
        tv_coin = findViewById(R.id.tv_userCoin);
        circleImageView = findViewById(R.id.CircleImageView_Profile);
        btn_update = findViewById(R.id.btn_update);
        btn_quanly = findViewById(R.id.btn_quanly);

        //Mini game lấy coin
        img_addCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Acti_TrangCaNhan.this, Acti_MiniGame.class));
            }
        });

        //load profile khi cập nhật
        Read();

        Picasso.get().load(user_dao.USER.getPhotoUrl()).resize(350, 200).centerCrop().into(circleImageView);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, Img_User);
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.dialog_edit_user);
                EditText _userName = dialog.findViewById(R.id.edt_dia_passWordOld);
                EditText _age = dialog.findViewById(R.id.edt_dia_passWordNew);
                Button send = dialog.findViewById(R.id.btn_dia_send);
                _userName.setText(user_dao.USER.getDisplayName());
                _age.setText(tv_age.getText().toString().trim());

                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (_userName.getText().toString().trim().isEmpty() || _age.getText().toString().trim().isEmpty()) {
                            Toast.makeText(Acti_TrangCaNhan.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (Integer.parseInt(_age.getText().toString().trim()) < 12) {
                            Toast.makeText(Acti_TrangCaNhan.this, "Tuổi phải lớn hơn 12", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        user_dao.Update_Profile(_userName.getText().toString().trim(), Integer.parseInt(_age.getText().toString().trim()), Integer.parseInt(tv_coin.getText().toString().trim()));
                        user_dao.Update_Profile_Auth(_userName.getText().toString().trim());
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        if (user_dao.check_Admin() < 0) {
            btn_quanly.setVisibility(View.GONE);
        }

        btn_quanly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Acti_TrangCaNhan.this, Acti_QuanLy.class));
            }
        });
    }

    public void Read() {
        user_dao.userDao.document(user_dao.USER.getEmail()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                user_dao.loadProfile(tv_name, tv_mail, tv_age, tv_coin);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}