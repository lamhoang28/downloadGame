package com.example.proteam5.DAO;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.proteam5.MainActivity;
import com.example.proteam5.Model.User;
import com.example.proteam5.Model_Adapter.User_Adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class User_DAO {
    Context context;
    FirebaseFirestore db;
    public FirebaseAuth auth;
    public CollectionReference userDao;
    public FirebaseUser USER;

    public User_DAO(Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
        userDao = db.collection("User");
        auth = FirebaseAuth.getInstance();
        USER = auth.getCurrentUser();
    }

    public void CreateUser(User user, String passWord, ProgressDialog progressDialog, TextView error) {
        userDao.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int temp = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getId().equalsIgnoreCase(user.getMail())) {
                            temp++;
                        }
                        Log.i("Thong bao", document.getId());
                    }
                    if (temp == 0) {
                        userDao.document(user.getMail()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                auth.createUserWithEmailAndPassword(user.getMail(), passWord)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    FirebaseUser Fuser = auth.getCurrentUser();
                                                    Fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                                    .setDisplayName(user.getUserName())
                                                                    .build();
                                                            Fuser.updateProfile(profileUpdates)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                Log.e("TAG", "User profile FirebaseAuth updated.");
                                                                            }
                                                                        }
                                                                    });
                                                            error.setText("Tạo tài khoản thành công. Vui lòng kiểm tra email");
                                                            error.setTextColor(Color.BLUE);
                                                            progressDialog.dismiss();
                                                        }
                                                    });
                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    Log.e("TAG", "createUserWithEmail:failure", task.getException());
                                                    Toast.makeText(context, "Tạo tài khoản thất bại", Toast.LENGTH_SHORT).show();
                                                    error.setText("Tạo tài khoản thất bại. Vui lòng thử lại");
                                                    progressDialog.dismiss();
                                                }
                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                error.setText("Tạo tài khoản thất bại. Vui lòng thử lại");
                                progressDialog.dismiss();
                                Log.e("TAG", "onFailure: " + e);
                            }
                        });
                    } else {
                        error.setText("Email này đã được đăng ký tài khoản. Vui lòng sử dụng email khác để đăng ký" + user.getMail());
                        progressDialog.dismiss();
                        return;
                    }
                } else {
                }
            }
        });

    }

    public void SendPassReset(String mail) {
        auth.sendPasswordResetEmail(mail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Mời bạn kiểm tra mail để lấy lại mật khẩu", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void Update_User(User user) {
        userDao.document(user.getMail())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Cập nhật thông tin thành công!!!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Cập nhật thông tin thất bại!!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void ReadFireBase_User(List<User> list, User_Adapter adapter) {
        userDao.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (list != null) {
                                list.clear();
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);
                                if (!user.getMail().equals("lamnhph18826@fpt.edu.vn") && !user.getMail().equals("dungnmph18838@fpt.edu.vn")) {
                                    list.add(user);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    public int check_Admin() {
        if (USER == null) {
            return 0;
        }
        if (USER.getEmail().equals("lamnhph18826@fpt.edu.vn") || USER.getEmail().equals("dungnmph18838@fpt.edu.vn")) {
            return 1;
        }
        return -1;
    }

    public void UpdatePassToEmail(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Đã gửi email đổi mật khẩu. Vui lòng kiểm tra!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    public void VoHieuHoa() {
        String mail = USER.getEmail();
        USER.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            userDao.document(USER.getEmail()).
                                    delete().
                                    addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.e("TAG", "Delete_User onSuccess: ");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("TAG", "Faided: ");
                                        }
                                    });
                            Toast.makeText(context, "Đã xóa tài khoản!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void loadProfile(TextView name, TextView mail, TextView age, TextView coin) {
        userDao.document(USER.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User us = task.getResult().toObject(User.class);
                name.setText(USER.getDisplayName());
                mail.setText("   " + us.getMail());
                age.setText("" + us.getAge());
                coin.setText("" + us.getCoin());
            }
        });
    }

    public void loadProfileHome(TextView name, TextView coin, ImageView img) {
        if (USER == null) {
            return;
        }
        userDao.document(USER.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User us = task.getResult().toObject(User.class);
                if (check_Admin() == 1) {
                    name.setText(us.getUserName() + " - ADMIN");
                    coin.setText("" + us.getCoin() + " $");
                    Picasso.get().load(USER.getPhotoUrl()).resize(350, 200).centerCrop().into(img);
                } else {
                    name.setText(us.getUserName() + " - Member");
                    coin.setText("" + us.getCoin() + " $");
                    Picasso.get().load(USER.getPhotoUrl()).resize(350, 200).centerCrop().into(img);
                }
            }
        });
    }

    public void Update_Avatar(Uri ImageData, TextView tv_name) {
        StorageReference Folder = FirebaseStorage.getInstance().getReference().child("Image_User");
        StorageReference ImageName = Folder.child(tv_name.getText().toString() + ImageData.getLastPathSegment());
        ImageName.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(uri)
                                .build();
                        USER.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(context, "Cập nhật ảnh đại diện thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    public void Update_Profile(String _user, int age, int coin) {
        String Mail = USER.getEmail();
        String avtt = "";
        if (USER.getPhotoUrl() != null) {
            avtt = USER.getPhotoUrl().toString();
        }
        User UserUpdate = new User(avtt, _user, Mail, age, coin);
        userDao.document(Mail)
                .set(UserUpdate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void Napcoin(User _user) {
        userDao.document(_user.getMail())
                .set(_user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Nạp coin thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Nạp coin thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void Update_Profile_Auth(String user) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(user)
                .build();
        USER.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        }
                    }
                });
    }


}
