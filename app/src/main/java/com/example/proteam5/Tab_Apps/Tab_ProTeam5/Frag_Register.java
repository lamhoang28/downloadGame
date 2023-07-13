package com.example.proteam5.Tab_Apps.Tab_ProTeam5;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proteam5.DAO.User_DAO;
import com.example.proteam5.Model.User;
import com.example.proteam5.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;


public class Frag_Register extends Fragment {
    User_DAO user_dao;
    TextInputLayout textInput_Age, textInput_mail, textInput_pass, textInput_passRe, textInput_name;
    String _age, _mail, _pass, _passRe, _name;
    TextView error;
    CheckBox checkAge;
    Button createMember;
    User user;
    ProgressDialog progressDialog;

    public Frag_Register() {
    }

    public static Frag_Register newInstance(String param1, String param2) {
        Frag_Register fragment = new Frag_Register();
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
        View view = inflater.inflate(R.layout.fragment_frag_register, container, false);
        user_dao = new User_DAO(getContext());
        progressDialog = new ProgressDialog(getContext());
        textInput_Age = view.findViewById(R.id.til_create_age);
        textInput_mail = view.findViewById(R.id.til_create_Gmail);
        textInput_pass = view.findViewById(R.id.til_create_pass);
        textInput_passRe = view.findViewById(R.id.til_create_pass2);
        textInput_name = view.findViewById(R.id.til_create_name);
        error = view.findViewById(R.id.tv_error);
        checkAge = view.findViewById(R.id.chk_yes);
        createMember = view.findViewById(R.id.btn_createMember);

        createMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = 0;
                _mail = textInput_mail.getEditText().getText().toString();
                _name = textInput_name.getEditText().getText().toString();
                _pass = textInput_pass.getEditText().getText().toString();
                _passRe = textInput_passRe.getEditText().getText().toString();
                _age = textInput_Age.getEditText().getText().toString();

                //Check mail
                if (_mail.trim().isEmpty()) {
                    textInput_mail.setError("Mời bạn nhập email");
                    textInput_mail.setErrorEnabled(true);
                    temp++;
                } else if (!_mail.endsWith("@fpt.edu.vn") && !_mail.endsWith("@gmail.com") && !_mail.endsWith("@gmail.com.vn")) {
                    textInput_mail.setError("Định dạng email không đúng!");
                    textInput_mail.setErrorEnabled(true);
                    temp++;
                } else {
                    textInput_mail.setErrorEnabled(false);
                }

                //Check name
                if (_name.trim().isEmpty()) {
                    textInput_name.setError("Mời bạn nhập họ tên đầy đủ");
                    textInput_name.setErrorEnabled(true);
                    temp++;
                } else if (_name.length() < 5) {
                    textInput_name.setError("Độ dài họ tên quá ngắn");
                    textInput_name.setErrorEnabled(true);
                    temp++;
                } else {
                    textInput_name.setErrorEnabled(false);
                }

                //Check tuổi
                if (_age.trim().isEmpty()) {
                    textInput_Age.setError("Mời bạn nhập dữ liệu");
                    textInput_Age.setErrorEnabled(true);
                    temp++;
                } else if (Integer.parseInt(_age.trim()) < 12) {
                    textInput_Age.setError("Bạn phải từ 12 tuổi trở lên để đăng ký tài khoản");
                    textInput_Age.setErrorEnabled(true);
                    temp++;
                } else {
                    textInput_Age.setErrorEnabled(false);
                }

                //Check pass
                if (_pass.trim().isEmpty()) {
                    textInput_pass.setError("Mời bạn nhập mật khẩu");
                    textInput_pass.setErrorEnabled(true);
                    temp++;
                } else if (_pass.trim().length() < 6) {
                    textInput_pass.setError("Mật khẩu tối thiểu 6 ký tự");
                    textInput_pass.setErrorEnabled(true);
                    temp++;
                } else {
                    textInput_pass.setErrorEnabled(false);
                }

                //Check passRe
                if (_passRe.trim().isEmpty()) {
                    textInput_passRe.setError("Mời bạn nhập mật khẩu xác nhận");
                    textInput_passRe.setErrorEnabled(true);
                    temp++;
                } else if (!_passRe.equals(_pass)) {
                    textInput_passRe.setError("Mật khẩu xác nhận không khớp");
                    textInput_passRe.setErrorEnabled(true);
                    temp++;
                } else {
                    textInput_passRe.setErrorEnabled(false);
                }

                //Check điều khoản
                if (!checkAge.isChecked()) {
                    error.setText("Bạn cần đồng ý với điều khoản sử dụng để có thể đăng ký");
                    temp++;
                } else {
                    error.setText("");
                }
                if (temp == 0) {
                    progressDialog.show();
                    user = new User("NONAME", _name.trim(), _mail.trim(), Integer.parseInt(_age.trim()), 0);
                    user_dao.CreateUser(user, _pass.trim(), progressDialog, error);
                }
            }
        });
        return view;
    }

}