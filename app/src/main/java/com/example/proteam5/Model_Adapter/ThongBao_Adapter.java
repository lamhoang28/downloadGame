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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proteam5.DAO.HoaDonDao;
import com.example.proteam5.DAO.ThongBaoDao;
import com.example.proteam5.Model.HoaDon;
import com.example.proteam5.Model.ThongBao;
import com.example.proteam5.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ThongBao_Adapter extends RecyclerView.Adapter<ThongBao_Adapter.ViewHoder> implements Filterable {

    List<ThongBao> list;
    List<ThongBao> listOld;
    Context context;
    ThongBao thongBao;
    ThongBaoDao thongBaoDao;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public ThongBao_Adapter(List<ThongBao> list, Context context) {
        this.list = list;
        this.listOld = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thongbao, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, @SuppressLint("RecyclerView") int position) {
        thongBao = list.get(position);
        if (holder == null) {
            return;
        }
        thongBaoDao = new ThongBaoDao(context);
        holder.tv_nguoigui.setText("Người gửi:  " + thongBao.getUserGui());
        holder.tv_ngaygui.setText("Ngày gửi:  " + sdf.format(thongBao.getNgaygui()));
        if (thongBao.getKieuThongBao() == 1) {
            holder.tv_noidung.setText("Người dùng " + thongBao.getUserGui() + " có câu hỏi:\n" + thongBao.getNoiDung());
        } else if (thongBao.getKieuThongBao() == 2) {
            Picasso.get().load(R.drawable.img_cautraloi).resize(100, 100).centerCrop().into(holder.img_thongbao);
            holder.tv_noidung.setText("Quản trị viên " + thongBao.getUserGui() + " đã trả lời bạn:\n" + thongBao.getNoiDung());
        } else if (thongBao.getKieuThongBao() == 3) {
            Picasso.get().load(thongBao.getImgGame()).resize(100, 100).centerCrop().into(holder.img_thongbao);
            holder.tv_noidung.setText("Người chơi " + thongBao.getUserGui() + " muốn cùng bạn chơi " + thongBao.getTenGame() + "\nTải game và cùng chơi với " + thongBao.getUserGui() + "?");
        } else if (thongBao.getKieuThongBao() == 4) {
            Picasso.get().load(R.drawable.img_tien).resize(100, 100).centerCrop().into(holder.img_thongbao);
            holder.tv_noidung.setText("Quản trị viên " + thongBao.getUserGui() + " đã nạp " + thongBao.getTenGame() + " coin cho bạn với lời nhắn:\n" + thongBao.getNoiDung());
        }else if (thongBao.getKieuThongBao()==5){
            Picasso.get().load(R.drawable.ic_gameoffline).resize(100, 100).centerCrop().into(holder.img_thongbao);
        }   holder.tv_noidung.setText(thongBao.getNoiDung());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thongBao = list.get(position);
                Dialog dialog = new Dialog(v.getContext());
                if (thongBao.getKieuThongBao() == 1) {
                    dialog.setContentView(R.layout.dialog_fogot_password);
                    EditText edt = dialog.findViewById(R.id.edt_dia_input);
                    TextView tv_1 = dialog.findViewById(R.id.tv_title_dialog);
                    TextView tv_2 = dialog.findViewById(R.id.tv_dialog_noiDung);
                    Button btn = dialog.findViewById(R.id.btn_dia_send);
                    tv_1.setText("Nhập phản hồi của bạn");
                    tv_2.setText("Nội dung: ");
                    btn.setText("Gửi phản hồi");
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ThongBao a = new ThongBao();
                            a.setUserGui(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                            a.setKieuThongBao(2);
                            a.setUserNhan(thongBao.getUserGui());
                            a.setNoiDung(edt.getText().toString());
                            a.setNgaygui(Calendar.getInstance().getTime());
                            thongBaoDao.addThongBao(a);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else if (thongBao.getKieuThongBao() == 3) {
                    dialog.setContentView(R.layout.dialog_thongbao_download);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    ImageView imageView = dialog.findViewById(R.id.img_hinhAnhDownLoad);
                    Button dowloadApk = dialog.findViewById(R.id.btn_DownLoadApk);
                    TextView tv = dialog.findViewById(R.id.tv_thua);
                    tv.setVisibility(View.GONE);
                    Picasso.get().load(thongBao.getImgGame()).resize(390, 200).centerCrop().into(imageView);
                    dowloadApk.setText("Tải xuống và chơi ngay cùng " + thongBao.getUserGui());
                    dowloadApk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HoaDonDao hoaDonDao = new HoaDonDao(context);
                            hoaDonDao.startDownloadFile(thongBao.getLinkGame());
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }


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
                    List<ThongBao> _list = new ArrayList<>();
                    for (ThongBao thongBao : listOld) {
                        if (thongBao.getNoiDung().toUpperCase().contains(name.toUpperCase())) {
                            _list.add(thongBao);
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
                list = (List<ThongBao>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHoder extends RecyclerView.ViewHolder {
        ImageView img_thongbao;
        TextView tv_nguoigui, tv_noidung, tv_ngaygui;
        LinearLayout linearLayout;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            img_thongbao = itemView.findViewById(R.id.img_item_thongbao);
            tv_ngaygui = itemView.findViewById(R.id.tv_item_ngayguiTB);
            tv_nguoigui = itemView.findViewById(R.id.tv_item_thongbaousergui);
            tv_noidung = itemView.findViewById(R.id.tv_item_noidungthongbao);
            linearLayout = itemView.findViewById(R.id.layout_itemthongbao);
        }
    }
}
