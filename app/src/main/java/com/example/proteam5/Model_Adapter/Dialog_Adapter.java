package com.example.proteam5.Model_Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proteam5.DAO.HoaDonDao;
import com.example.proteam5.DAO.ThongBaoDao;
import com.example.proteam5.Model.HoaDon;
import com.example.proteam5.Model.ThongBao;
import com.example.proteam5.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Dialog_Adapter extends RecyclerView.Adapter<Dialog_Adapter.ViewHoder> implements Filterable {

    List<HoaDon> list;
    List<HoaDon> listOld;
    Context context;
    HoaDon hoaDon;

    public Dialog_Adapter(List<HoaDon> list, Context context) {
        this.list = list;
        this.listOld = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lv_dialog, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, @SuppressLint("RecyclerView") int position) {
        hoaDon = list.get(position);
        if (holder == null) {
            return;
        }
        holder.tv_ten.setText(hoaDon.getNameUser());
        holder.tv_moichoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hoaDon = list.get(position);
                ThongBaoDao thongBaoDao = new ThongBaoDao(context);
                ThongBao a = new ThongBao();
                a.setKieuThongBao(3);
                a.setUserGui(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                a.setImgGame(hoaDon.getImgGame());
                a.setNgaygui(Calendar.getInstance().getTime());
                a.setUserNhan(hoaDon.getUsermail());
                a.setTenGame(hoaDon.getNameGame());
                a.setLinkGame(hoaDon.getLinkdownload());
                thongBaoDao.addThongBao(a);
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
        TextView tv_ten,tv_moichoi;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            tv_ten = itemView.findViewById(R.id.tv_itemlv_dialog);
            tv_moichoi = itemView.findViewById(R.id.tv_itemlv_moichoi);
        }
    }
}
