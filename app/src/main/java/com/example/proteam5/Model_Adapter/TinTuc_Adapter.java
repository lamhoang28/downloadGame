package com.example.proteam5.Model_Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proteam5.DAO.ProTeam5;
import com.example.proteam5.DAO.TinTuc_DAO;
import com.example.proteam5.DAO.User_DAO;
import com.example.proteam5.Model.Game;
import com.example.proteam5.Model.TinTuc;
import com.example.proteam5.R;
import com.example.proteam5.Tab_Apps.View.Acti_ViewTinTuc;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TinTuc_Adapter extends RecyclerView.Adapter<TinTuc_Adapter.ViewHoder> implements Filterable {


    List<TinTuc> list;
    List<TinTuc> listOld;
    Context context;
    TinTuc tinTuc;
    FirebaseStorage storage;
    TinTuc_DAO tinTuc_dao;
    User_DAO user_dao;


    int ADMIN;

    public TinTuc_Adapter(List<TinTuc> list, Context context) {
        this.list = list;
        this.listOld = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tintuc,parent,false);
        return new ViewHoder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, @SuppressLint("RecyclerView") int position) {
        tinTuc = list.get(position);
        if (holder == null) {
            return;
        }
        tinTuc_dao = new TinTuc_DAO();
        holder.noiDung.setText(tinTuc.getTieuDe().toUpperCase() + ": \n" + tinTuc.getNoiDung());
        SimpleDateFormat sdf = new SimpleDateFormat("  HH:mm dd-MM-yyyy   ");
        holder.ngaydang.setText(sdf.format(tinTuc.getNgaydang()));
        Picasso.get().load(tinTuc.getImgShow()).resize(350, 200).centerCrop().into(holder.hinhAnh);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        user_dao = new User_DAO(context);
        ADMIN = user_dao.check_Admin();
        if (ADMIN != 1) {
            holder.imgDel.setVisibility(View.GONE);
        }

        //truyen du lieu
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tinTuc = list.get(position);
                onClickViewTinTuc(tinTuc);
            }
        });
        //delete
        holder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tinTuc = list.get(position);
                String index = tinTuc.getMaBaiViet();
                String ur = tinTuc.getImgShow();
                String ur1 = tinTuc.getImgReView();
                String ur2 = tinTuc.getImgReView1();


                storage = FirebaseStorage.getInstance();
                StorageReference gsReference, gsReference1, gsReference2;
                gsReference = storage.getReferenceFromUrl(ur);
                gsReference1 = storage.getReferenceFromUrl(ur1);
                gsReference2 = storage.getReferenceFromUrl(ur2);

                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("Xóa tin tức");
                dialog.setMessage("Bạn có chắc chắn muốn xóa?");
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("TAG", "Faided: ");
                        dialog.dismiss();
                    }
                });
                dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long kq = tinTuc_dao.TinTucDelete(index, context);
                        if (kq < 0) {
                            Log.e("TAG", "khong thanh cong: ");
                            return;
                        }
                        Delete_image(gsReference.getPath());
                        Delete_image(gsReference1.getPath());
                        Delete_image(gsReference2.getPath());
                        Log.e("TAG", "thanh cong: ");
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    public void Delete_image(String url) {
        StorageReference storageRef = storage.getReference();
        StorageReference desertRef = storageRef.child(url);
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Delete img Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }

    public void onClickViewTinTuc(TinTuc tinTuc) {
        Intent intent = new Intent(context, Acti_ViewTinTuc.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("TinTuc", tinTuc);
        intent.putExtras(bundle);
        context.startActivity(intent);
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
                    List<TinTuc> _list = new ArrayList<>();
                    for (TinTuc tinTuc : listOld) {
                        if (tinTuc.getTieuDe().toUpperCase().contains(name.toUpperCase())) {
                            _list.add(tinTuc);
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
                list = (List<TinTuc>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView noiDung, ngaydang;
        ImageView hinhAnh, imgDel;


        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layout_itemtintuc);
            noiDung = itemView.findViewById(R.id.tv_tieudetintuc);
            hinhAnh = itemView.findViewById(R.id.img_hinhAnhTinTuc);
            imgDel = itemView.findViewById(R.id.img_delTinTuc);
            ngaydang = itemView.findViewById(R.id.itemtintuc_ngaydang);
        }
    }
}
