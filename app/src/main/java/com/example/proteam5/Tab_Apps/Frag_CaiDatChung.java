package com.example.proteam5.Tab_Apps;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proteam5.DAO.User_DAO;
import com.example.proteam5.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class Frag_CaiDatChung extends Fragment {

    TextView pass, delete, inputMail, logout, support, ic_suport;
    Button yes;
    Dialog dialog;
    User_DAO user_dao;


    public Frag_CaiDatChung() {
    }

    public static Frag_CaiDatChung newInstance(String param1, String param2) {
        Frag_CaiDatChung fragment = new Frag_CaiDatChung();
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
        View view = inflater.inflate(R.layout.fragment_frag_cai_dat_chung, container, false);
        user_dao = new User_DAO(getContext());

        pass = view.findViewById(R.id.tv_passWord);
        delete = view.findViewById(R.id.tv_deleteMember);
        logout = view.findViewById(R.id.tv_logout);
        support = view.findViewById(R.id.tv_hotro);
        ic_suport = view.findViewById(R.id.ic_sp);

        if (user_dao.check_Admin() == 1) {
            support.setVisibility(View.GONE);
            ic_suport.setVisibility(View.GONE);
        }

        // đổi mật khẩu
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.dialog_fogot_password);
                inputMail = dialog.findViewById(R.id.edt_dia_input);
                yes = dialog.findViewById(R.id.btn_dia_send);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Gmail = inputMail.getText().toString().trim();
                        if (Gmail.isEmpty()) {
                            Toast.makeText(getContext(), "Bạn chưa nhập dữ liệu!!!", Toast.LENGTH_SHORT).show();
                        } else if (!Gmail.endsWith("@fpt.edu.vn") && !Gmail.endsWith("@gmail.com") && !Gmail.endsWith("@gmail.com.vn")) {
                            Toast.makeText(getContext(), "Bạn nhập sai định dang!!!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (Gmail.equals(user_dao.USER.getEmail())) {
                                user_dao.UpdatePassToEmail(Gmail);
                                startActivity(new Intent(getContext(), Acti_ProTeam5.class));
                                dialog.dismiss();
                            } else {
                                Toast.makeText(getContext(), "Bạn nhập sai email. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    }
                });
                dialog.show();
            }
        });

        //vô hiệu hóa tài khoản
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("Xóa Tài Khoản");
                dialog.setMessage("Bạn có chắc chắn muốn xóa tài khoản?");
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Dialog Dialog_del = new Dialog(v.getContext());
                        Dialog_del.setContentView(R.layout.dialog_fogot_password);
                        inputMail = Dialog_del.findViewById(R.id.edt_dia_input);
                        yes = Dialog_del.findViewById(R.id.btn_dia_send);
                        yes.setText("Xác Nhận Xóa\nTài Khoản");
                        yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String Gmail = inputMail.getText().toString().trim();
                                if (Gmail.isEmpty()) {
                                    Toast.makeText(getContext(), "Bạn chưa nhập dữ liệu!!!", Toast.LENGTH_SHORT).show();
                                } else if (!Gmail.endsWith("@fpt.edu.vn") && !Gmail.endsWith("@gmail.com") && !Gmail.endsWith("@gmail.com.vn")) {
                                    Toast.makeText(getContext(), "Bạn nhập sai định dang!!!", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (Gmail.equals(user_dao.USER.getEmail())) {
                                        user_dao.VoHieuHoa();
                                        FirebaseAuth.getInstance().signOut();
                                        startActivity(new Intent(getContext(), Acti_ProTeam5.class));
                                        Dialog_del.dismiss();
                                    } else {
                                        Toast.makeText(getContext(), "Bạn nhập sai email. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                                        Dialog_del.dismiss();
                                    }
                                }
                            }
                        });
                        Dialog_del.show();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        //hỗ trợ
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Acti_Hotro.class));
            }
        });
        //đăng xuất
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), Acti_ProTeam5.class));
                getActivity().finish();
            }
        });
        return view;
    }

}