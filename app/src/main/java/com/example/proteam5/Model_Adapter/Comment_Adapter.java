package com.example.proteam5.Model_Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.proteam5.DAO.Comment_DAO;
import com.example.proteam5.DAO.User_DAO;
import com.example.proteam5.Model.Comment;
import com.example.proteam5.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Comment_Adapter extends RecyclerView.Adapter<Comment_Adapter.ViewHoder> {
    List<Comment> list;
    Context context;
    Comment comment;
    Comment_DAO comment_dao;

    public Comment_Adapter(List<Comment> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, @SuppressLint("RecyclerView") int position) {
        comment = list.get(position);
        if (holder == null) {
            return;
        }
        User_DAO user_dao = new User_DAO(context);
        FirebaseAuth auth= FirebaseAuth.getInstance();
        FirebaseUser USER = auth.getCurrentUser();
        if (!comment.getGmail().equals(USER.getEmail())){
            if (user_dao.check_Admin() == -1) {
                holder.imgDel.setVisibility(View.GONE);
            }
        }

        comment_dao = new Comment_DAO();
        holder.noiDung.setText(comment.getTitle());
        holder.userName.setText(comment.getUserName().toUpperCase());
        Date ngaycmt = comment.getNgaycmt();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        holder.ngaycmt.setText(sdf.format(ngaycmt));
        Picasso.get().load(comment.getAvatar()).resize(350, 200).centerCrop().into(holder.hinhAnh);
        holder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = list.get(position);
                String index = comment.getID();
                comment_dao.CommentDelete(index, context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView noiDung, userName, ngaycmt;
        ImageView hinhAnh, imgDel;


        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layout_itemComment);
            noiDung = itemView.findViewById(R.id.tv_item_titleMember);
            userName = itemView.findViewById(R.id.tv_item_nameMember);
            hinhAnh = itemView.findViewById(R.id.img_item_imgMember);
            imgDel = itemView.findViewById(R.id.img_delete_Comment);
            ngaycmt = itemView.findViewById(R.id.tv_item_ngaycmt);
        }
    }
}
