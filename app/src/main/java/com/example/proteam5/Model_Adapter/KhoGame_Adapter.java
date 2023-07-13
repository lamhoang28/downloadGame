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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.proteam5.DAO.HoaDonDao;
import com.example.proteam5.Model.HoaDon;
import com.example.proteam5.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class KhoGame_Adapter extends RecyclerView.Adapter<KhoGame_Adapter.ViewHoder> implements Filterable {

    List<HoaDon> list;
    List<HoaDon> listOld;
    Context context;
    HoaDon hoaDon;
    HoaDonDao hoaDonDao;

    public KhoGame_Adapter(List<HoaDon> list, Context context) {
        this.list = list;
        this.listOld = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_khogame, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, @SuppressLint("RecyclerView") int position) {
        hoaDon = list.get(position);
        if (holder == null) {
            return;
        }
        hoaDonDao = new HoaDonDao(context);
        Picasso.get().load(hoaDon.getImgGame()).resize(350, 200).centerCrop().into(holder.imgKhogame);
        holder.titlekhogame.setText(hoaDon.getNameGame());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        holder.ngaymuagame.setText("Ngày mua: " + sdf.format(hoaDon.getNgaymua()));
        hoaDonDao.loadngchoicung(holder.nguoichoi, hoaDon.getID_game());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hoaDon = list.get(position);
                Dialog dialog = new Dialog(v.getContext());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_thongbao_download);
                ImageView imageView = dialog.findViewById(R.id.img_hinhAnhDownLoad);
                Button dowloadApk = dialog.findViewById(R.id.btn_DownLoadApk);
                TextView moichoi = dialog.findViewById(R.id.tv_thua);


                List<HoaDon> a = new ArrayList<>();
                RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView_dialog);
                if(holder.nguoichoi.getText().toString().equals("Bạn là người duy nhất mua game này")){
                    moichoi.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext(),RecyclerView.VERTICAL,false);
                Dialog_Adapter adapter = new Dialog_Adapter(a, v.getContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
                hoaDonDao.Read(a, adapter, hoaDon.getID_game());
                Picasso.get().load(hoaDon.getImgGame()).resize(390, 200).centerCrop().into(imageView);
                dowloadApk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hoaDonDao.startDownloadFile(hoaDon.getLinkdownload());
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
                    list = listOld;
                } else {
                    List<HoaDon> _list = new ArrayList<>();
                    for (HoaDon hoaDon : listOld) {
                        if (hoaDon.getNameGame().toUpperCase().contains(name.toUpperCase())) {
                            _list.add(hoaDon);
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
                list = (List<HoaDon>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHoder extends RecyclerView.ViewHolder {
        ImageView imgKhogame;
        TextView titlekhogame, ngaymuagame, nguoichoi;
        LinearLayout linearLayout;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            imgKhogame = itemView.findViewById(R.id.img_item_imgKhoGame);
            titlekhogame = itemView.findViewById(R.id.tv_item_titlekhogame);
            ngaymuagame = itemView.findViewById(R.id.tv_item_ngaymua);
            nguoichoi = itemView.findViewById(R.id.tv_item_nguoichoi);
            linearLayout = itemView.findViewById(R.id.layout_itemKhoGame);
        }
    }

}
