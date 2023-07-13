package com.example.proteam5.Model_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.proteam5.Model.HoaDon;
import com.example.proteam5.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class HoaDon_Adapter extends RecyclerView.Adapter<HoaDon_Adapter.ViewHoder> {
    List<HoaDon> list;
    Context context;
    HoaDon hoaDon;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public HoaDon_Adapter(List<HoaDon> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hoadon, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        hoaDon = list.get(position);
        if (hoaDon == null) {
            return;
        }
        holder.nameUser.setText(hoaDon.getNameUser().toUpperCase());
        holder.coinGame.setText("Giá tiền:  " + hoaDon.getCoin());
        holder.nameGame.setText(hoaDon.getNameGame().toUpperCase());
        holder.ngaymua.setText("Ngày mua: " + sdf.format(hoaDon.getNgaymua()));
        Picasso.get().load(hoaDon.getImgGame()).resize(350, 200).centerCrop().into(holder.avtGame);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHoder extends RecyclerView.ViewHolder {

        ImageView avtGame;
        TextView nameUser, nameGame, coinGame, ngaymua;
        LinearLayout linearLayout;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            avtGame = itemView.findViewById(R.id.img_item_imgGame);
            nameUser = itemView.findViewById(R.id.tv_item_nameMember);
            nameGame = itemView.findViewById(R.id.tv_item_nameGame);
            coinGame = itemView.findViewById(R.id.tv_item_coinGame);
            linearLayout = itemView.findViewById(R.id.layout_itemHoaDon);
            ngaymua = itemView.findViewById(R.id.tv_item_ngaymua);
        }
    }
}
