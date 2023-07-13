package com.example.proteam5.Tab_Apps.Tab_ProTeam5;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proteam5.DAO.User_DAO;
import com.example.proteam5.MainActivity;
import com.example.proteam5.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;


public class Frag_Login extends Fragment {

    TextInputLayout textInputLayout_gmail, textInputLayout_pass;
    String userN, passW;
    TextView fogotPassWord, login;
    CheckBox reMemberMe;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    User_DAO user_dao;

    public Frag_Login() {
    }

    public static Frag_Login newInstance(String param1, String param2) {
        Frag_Login fragment = new Frag_Login();
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
        View view = inflater.inflate(R.layout.fragment_frag_login, container, false);
        textInputLayout_gmail = view.findViewById(R.id.til_login_gmail);
        textInputLayout_pass = view.findViewById(R.id.til_login_pass);
        login = view.findViewById(R.id.tv_login);
        fogotPassWord = view.findViewById(R.id.tv_FogotPass);
        user_dao = new User_DAO(getContext());

        sharedPreferences = getActivity().getSharedPreferences("Member", Context.MODE_PRIVATE);
        textInputLayout_gmail.getEditText().setText(sharedPreferences.getString("UserN", ""));
        textInputLayout_pass.getEditText().setText(sharedPreferences.getString("PassW", ""));
        reMemberMe = view.findViewById(R.id.chk_checkB);
        reMemberMe.setChecked(sharedPreferences.getBoolean("Re", false));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = 0;
                userN = textInputLayout_gmail.getEditText().getText().toString();
                passW = textInputLayout_pass.getEditText().getText().toString();

                //check mail
                if (userN.isEmpty()) {
                    textInputLayout_gmail.setError("Mời bạn nhập email");
                    textInputLayout_gmail.setErrorEnabled(true);
                    temp++;
                } else if (!userN.toLowerCase().contains("@gmail.com") && !userN.toLowerCase().contains("@gmail.com.vn") && !userN.toLowerCase().contains("@fpt.edu.vn")){
                    textInputLayout_gmail.setError("Định dạng email không đúng");
                    textInputLayout_gmail.setErrorEnabled(true);
                    temp++;
                }
                else{
                    textInputLayout_gmail.setErrorEnabled(false);
                }

                //check pass
                if (passW.isEmpty()) {
                    textInputLayout_pass.setError("Mời bạn nhập mật khẩu");
                    textInputLayout_pass.setErrorEnabled(true);
                    temp++;
                } else if (passW.length() < 5) {
                    textInputLayout_pass.setError("Mật khẩu tối thiểu 5 ký tự.");
                    textInputLayout_pass.setErrorEnabled(true);
                    temp++;
                } else {
                    textInputLayout_pass.setErrorEnabled(false);

                }

                if (temp == 0) {
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("Đang Đăng Nhập");
                    progressDialog.show();
                    CheckLoginMember();
                } else {
                    return;
                }
            }
        });


        fogotPassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.dialog_fogot_password);
                EditText gmail = dialog.findViewById(R.id.edt_dia_input);
                Button send = dialog.findViewById(R.id.btn_dia_send);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mail = gmail.getText().toString();
                        user_dao.SendPassReset(mail);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        return view;
    }

    public void CheckLoginMember() {

        user_dao.auth.signInWithEmailAndPassword(userN, passW)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = user_dao.auth.getCurrentUser();
                            boolean emailVerified = firebaseUser.isEmailVerified();
                            if (emailVerified != true) {
                                Toast.makeText(getContext(), "Bạn cần xác nhận email trước khi đăng nhập", Toast.LENGTH_SHORT).show();
                            } else {
                                RememberMe(userN, passW, reMemberMe.isChecked());
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                startActivity(intent);
                            }
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            Log.e("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Đăng nhập thất bại!!!\nVui lòng kiểm tra lại thông tin đăng nhập",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void RememberMe(String userName, String passWord, boolean check) {
        sharedPreferences = getActivity().getSharedPreferences("Member", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if (!check) {
            edit.clear();
        } else {
            edit.putString("UserN", userName);
            edit.putString("PassW", passWord);
            edit.putBoolean("Re", check);
        }
        edit.commit();
    }


}