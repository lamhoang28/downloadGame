package com.example.proteam5.Model_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proteam5.Model.HoaDon;
import com.example.proteam5.Model.HoaDonNapCoin;
import com.example.proteam5.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class HoaDonNap_Adapter extends RecyclerView.Adapter<HoaDonNap_Adapter.ViewHoder> {
    List<HoaDonNapCoin> list;
    Context context;
    HoaDonNapCoin hoaDonNap;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public HoaDonNap_Adapter(List<HoaDonNapCoin> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hoadonnap, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        hoaDonNap = list.get(position);
        if (hoaDonNap == null) {
            return;
        }
        if (hoaDonNap.getUsername().isEmpty()) {
            holder.nameUser.setText(hoaDonNap.getUsermail().toUpperCase());
        } else {
            holder.nameUser.setText(hoaDonNap.getUsername().toUpperCase());
        }
        holder.coinNap.setText("Số tiền nạp:  " + hoaDonNap.getTiennap());
        holder.adminname.setText("Người nạp: " + hoaDonNap.getAdminNap());
        holder.ngaynap.setText("Ngày nạp: " + sdf.format(hoaDonNap.getNgaynap()));
        holder.noidung.setText("Nội dung:\n" + hoaDonNap.getNoidung());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHoder extends RecyclerView.ViewHolder {

        TextView nameUser, coinNap, adminname, ngaynap, noidung;
        LinearLayout linearLayout;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            nameUser = itemView.findViewById(R.id.tv_item_nameMemberNap);
            coinNap = itemView.findViewById(R.id.tv_item_coinNap);
            adminname = itemView.findViewById(R.id.tv_item_AdminThanhToan);
            linearLayout = itemView.findViewById(R.id.layout_itemHoaDon);
            ngaynap = itemView.findViewById(R.id.tv_item_ngaynap);
            noidung = itemView.findViewById(R.id.tv_item_noidungnap);
        }
    }
}
