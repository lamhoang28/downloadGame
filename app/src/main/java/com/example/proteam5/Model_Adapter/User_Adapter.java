package com.example.proteam5.Model_Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proteam5.DAO.HoaDonNapDao;
import com.example.proteam5.DAO.ThongBaoDao;
import com.example.proteam5.DAO.User_DAO;
import com.example.proteam5.Model.HoaDon;
import com.example.proteam5.Model.HoaDonNapCoin;
import com.example.proteam5.Model.ThongBao;
import com.example.proteam5.Model.User;
import com.example.proteam5.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class User_Adapter extends RecyclerView.Adapter<User_Adapter.ViewHoder> implements Filterable {
    List<User> list;
    List<User> listold;
    Context context;
    User user;
    User_DAO user_dao;
    HoaDonNapDao hoaDonNapDao;
    ThongBaoDao thongBaoDao;

    public User_Adapter(List<User> list, Context context) {
        this.list = list;
        this.listold = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, @SuppressLint("RecyclerView") int position) {
        user = list.get(position);
        if (holder == null) {
            return;
        }
        user_dao = new User_DAO(context);
        hoaDonNapDao = new HoaDonNapDao(context);
        thongBaoDao = new ThongBaoDao(context);
        Picasso.get().load(user.getAvatar()).resize(350, 200).centerCrop().into(holder.imgMember);
        holder.nameMember.setText(user.getUserName());
        holder.gmailMember.setText(user.getMail());
        holder.coinMember.setText("Số coin:  " + user.getCoin() + " $");
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = list.get(position);
                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.dialog_member);
                TextView tieude = dialog.findViewById(R.id.tv_tiltlenap);
                EditText sotien = dialog.findViewById(R.id.edt_sotiennap);
                EditText noidung = dialog.findViewById(R.id.edt_noidungnap);
                Button add = dialog.findViewById(R.id.btn_naptien);
                if (user.getUserName().isEmpty() || user.getUserName() == null) {
                    tieude.setText("Nạp tiền cho " + user.getMail());
                } else {
                    tieude.setText("Nạp tiền cho " + user.getUserName());
                }
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.setCoin(user.getCoin() + Integer.parseInt(sotien.getText().toString()));
                        user_dao.Napcoin(user);
                        HoaDonNapCoin hoaDonNapCoin = new HoaDonNapCoin("", user.getUserName(), user.getMail(), user_dao.USER.getEmail(), noidung.getText().toString().trim(), Calendar.getInstance().getTime(), Integer.parseInt(sotien.getText().toString()));
                        hoaDonNapDao.addHoaDonNap(hoaDonNapCoin);
                        ThongBao thongBao = new ThongBao();
                        thongBao.setKieuThongBao(4);
                        thongBao.setNgaygui(Calendar.getInstance().getTime());
                        thongBao.setNoiDung(hoaDonNapCoin.getNoidung());
                        thongBao.setUserGui(hoaDonNapCoin.getAdminNap());
                        thongBao.setUserNhan(user.getMail());
                        thongBao.setTenGame("" + hoaDonNapCoin.getTiennap());
                        thongBaoDao.addThongBao(thongBao);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String name = constraint.toString();
                if (name.isEmpty()) {
                    list = listold;
                } else {
                    List<User> _list = new ArrayList<>();
                    for (User user : listold) {
                        if (user.getUserName().isEmpty() || user.getUserName() == null) {
                            if (user.getMail().toUpperCase().contains(name.toUpperCase())) {
                                _list.add(user);
                            }
                        } else {
                            if (user.getUserName().toUpperCase().contains(name.toUpperCase()) || user.getMail().toUpperCase().contains(name.toUpperCase())) {
                                _list.add(user);
                            }
                        }

                    }
                    list = _list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<User>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHoder extends RecyclerView.ViewHolder {
        ImageView imgMember;
        TextView nameMember, gmailMember, coinMember;
        LinearLayout linearLayout;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            imgMember = itemView.findViewById(R.id.img_item_imgMember);
            nameMember = itemView.findViewById(R.id.tv_item_nameMember);
            gmailMember = itemView.findViewById(R.id.tv_item_gmailMember);
            coinMember = itemView.findViewById(R.id.tv_item_coinmember);
            linearLayout = itemView.findViewById(R.id.layout_itemMember);
        }
    }
}
