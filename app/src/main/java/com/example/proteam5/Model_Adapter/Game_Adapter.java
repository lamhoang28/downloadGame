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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proteam5.DAO.Game_DAO;
import com.example.proteam5.DAO.ProTeam5;
import com.example.proteam5.DAO.User_DAO;
import com.example.proteam5.Model.Game;
import com.example.proteam5.R;
import com.example.proteam5.Tab_Apps.View.Acti_ViewGame;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Game_Adapter extends RecyclerView.Adapter<Game_Adapter.ViewHoder> implements Filterable {

    List<Game> list;
    List<Game> listOld;
    Game_DAO game_dao;
    User_DAO user_dao;
    ProTeam5 deltete_Img;
    Context context;
    FirebaseStorage storage;

    Game game;


    int ADMIN;


    public Game_Adapter(List<Game> list, Context context) {
        this.list = list;
        this.listOld = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, @SuppressLint("RecyclerView") int position) {
        game = list.get(position);
        if (game == null) {
            return;
        }
        game_dao = new Game_DAO(context);
        holder.titleGame.setText(game.getTieuDe().toUpperCase());
        game_dao.checkMuaGame(holder.coinGame, game.getMaBaiViet(), game.getGiaTien());
        Picasso.get().load(game.getImgShow()).resize(350, 200).centerCrop().into(holder.inGame);

        user_dao = new User_DAO(context);
        deltete_Img = new ProTeam5();

        ADMIN = user_dao.check_Admin();
        if (ADMIN != 1) {
            holder.delGame.setVisibility(View.GONE);
        }

        //delete
        holder.delGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game = list.get(position);
                String index = game.getMaBaiViet();

                String ur = game.getImgShow();
                String ur1 = game.getImgReview();
                String ur2 = game.getImgReview1();
                String ur3 = game.getImgReview2();
                storage = FirebaseStorage.getInstance();
                StorageReference gsReference, gsReference1, gsReference2, gsReference3;
                gsReference = storage.getReferenceFromUrl(ur);
                gsReference1 = storage.getReferenceFromUrl(ur1);
                gsReference2 = storage.getReferenceFromUrl(ur2);
                gsReference3 = storage.getReferenceFromUrl(ur3);


                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("Xóa Game");
                dialog.setMessage("Bạn có chắc chắn muốn xóa " + game.getTieuDe());
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long kq = game_dao.Delete_Game(game, context);
                        if (kq != 1) {
                            return;
                        }
                        Delete_image(gsReference.getPath());
                        Delete_image(gsReference1.getPath());
                        Delete_image(gsReference2.getPath());
                        Delete_image(gsReference3.getPath());
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
                game = list.get(position);
                OnClick(game);
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

    public void OnClick(Game game) {
        Intent intent = new Intent(context, Acti_ViewGame.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("GameMod", game);
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String name = constraint.toString();
                if (name.isEmpty()) {
                    list = listOld;
                } else {
                    List<Game> _list = new ArrayList<>();
                    for (Game game : listOld) {
                        if (game.getTieuDe().toUpperCase().contains(name.toUpperCase())) {
                            _list.add(game);
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
                list = (List<Game>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        ImageView inGame, delGame;
        TextView titleGame, coinGame;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layout_itemGame);
            inGame = itemView.findViewById(R.id.img_game);
            delGame = itemView.findViewById(R.id.img_delGame);
            titleGame = itemView.findViewById(R.id.tv_item_titleGame);
            coinGame = itemView.findViewById(R.id.tv_item_coinGame);
        }
    }
}
