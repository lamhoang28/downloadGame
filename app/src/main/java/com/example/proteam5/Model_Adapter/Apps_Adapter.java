package com.example.proteam5.Model_Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proteam5.DAO.Apps_DAO;
import com.example.proteam5.DAO.ProTeam5;
import com.example.proteam5.DAO.User_DAO;
import com.example.proteam5.Model.Apps;
import com.example.proteam5.R;
import com.example.proteam5.Tab_Apps.View.Acti_ViewApps;
import com.example.proteam5.Tab_Apps.View.Acti_ViewGame;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Apps_Adapter extends RecyclerView.Adapter<Apps_Adapter.ViewHoder> {

    List<Apps> list;
    Context context;
    Apps_DAO apps_dao;
    int ADMIN;
    User_DAO user_dao;
    FirebaseStorage storage;
    ProTeam5 chucNang;

    Apps apps;


    public Apps_Adapter(List<Apps> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_apps, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, @SuppressLint("RecyclerView") int position) {
        apps = list.get(position);
        if (apps == null) {
            return;
        }
        apps_dao = new Apps_DAO();
        user_dao = new User_DAO(context);
        ADMIN = user_dao.check_Admin();
        if (ADMIN != 1) {
            holder.delApps.setVisibility(View.GONE);
        }


        holder.titleApps.setText(String.valueOf(apps.getTieuDe().toUpperCase()));
        Picasso.get().load(apps.getImgShow()).resize(350, 200).centerCrop().into(holder.inApps);
        //delete
        holder.delApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chucNang = new ProTeam5();
                apps = list.get(position);
                String index = apps.getID();
                String imgShow = apps.getImgShow();
                String imgreview1 = apps.getImgReview();
                String imgreview2 = apps.getImgReview1();
                String imgreview3 = apps.getImgReview2();

                storage = FirebaseStorage.getInstance();
                StorageReference gsReference, gsReference1, gsReference2, gsReference3;
                gsReference = storage.getReferenceFromUrl(imgShow);
                gsReference1 = storage.getReferenceFromUrl(imgreview1);
                gsReference2 = storage.getReferenceFromUrl(imgreview2);
                gsReference3 = storage.getReferenceFromUrl(imgreview3);

                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("Xóa ứng dụng");
                dialog.setMessage("Bạn có chắc chắn muốn xóa " + apps.getTieuDe());
                dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        apps_dao.Delete_Apps(index, context);
                        Delete_image(gsReference.getPath());
                        Delete_image(gsReference1.getPath());
                        Delete_image(gsReference2.getPath());
                        Delete_image(gsReference3.getPath());

//                        chucNang.Delete_image(gsReference.getPath(),context);
//                        chucNang.Delete_image(gsReference1.getPath(),context);
//                        chucNang.Delete_image(gsReference2.getPath(),context);
//                        chucNang.Delete_image(gsReference3.getPath(),context);
                        dialog.dismiss();
                    }
                });
                dialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apps = list.get(position);
                OnClick(apps);
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

    public void OnClick(Apps apps) {
        Intent intent = new Intent(context, Acti_ViewApps.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Apps", apps);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        ImageView inApps, delApps;
        TextView titleApps;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layout_itemApps);
            inApps = itemView.findViewById(R.id.img_item_Apps);
            delApps = itemView.findViewById(R.id.img_delApps);
            titleApps = itemView.findViewById(R.id.tv_item_title);
        }
    }
}
