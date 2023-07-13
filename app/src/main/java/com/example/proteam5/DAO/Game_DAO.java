package com.example.proteam5.DAO;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.proteam5.Model.Game;
import com.example.proteam5.Model.HoaDon;
import com.example.proteam5.Model.HoaDonNapCoin;
import com.example.proteam5.Model.ThongBao;
import com.example.proteam5.Model.User;
import com.example.proteam5.Model_Adapter.Game_Adapter;
import com.example.proteam5.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game_DAO {
    ThongBaoDao thongBaoDao;
    HoaDonDao hoaDonDao;
    User_DAO user_dao;
    Context context;
    CollectionReference game_dao;

    public Game_DAO(Context context) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        this.context = context;
        thongBaoDao = new ThongBaoDao(context);
        game_dao = firebaseFirestore.collection("Game");
        user_dao = new User_DAO(context);
        hoaDonDao = new HoaDonDao(context);
    }

    public void CreateNewGame_Online(Game game) {
        DocumentReference documentReference = game_dao.document();
        game.setMaBaiViet(documentReference.getId());
        Map<String, Object> newGame = new HashMap<>();
        newGame.put("maBaiViet", documentReference.getId());
        newGame.put("TieuDe", game.getTieuDe());
        newGame.put("gioiThieu", game.getGioiThieu());
        newGame.put("imgShow", game.getImgShow());
        newGame.put("imgReview", game.getImgReview());
        newGame.put("imgReview1", game.getImgReview1());
        newGame.put("imgReview2", game.getImgReview2());
        newGame.put("dieuKhoan", game.getDieuKhoan());
        newGame.put("urlVideo", game.getUrlVideo());
        newGame.put("OnlineorOffline", game.getOnlineorOffline());
        newGame.put("theLoai", game.getTheLoai());
        newGame.put("linkDownload", game.getLinkDownload());
        // Add a new document with a generated ID
        documentReference.set(game)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.e("TAG", "successfully: ");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "Error adding document", e);
                    }
                });

    }

    public void ReadFireBase(List<Game> list_GameOnline, Game_Adapter adapter, int key) {
        game_dao.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (list_GameOnline != null) {
                    list_GameOnline.clear();
                }
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> _list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : _list) {
                        Game game = d.toObject(Game.class);
                        list_GameOnline.add(game);
                        for (int i = 0; i < list_GameOnline.size(); i++) {
                            if (list_GameOnline.get(i).getOnlineorOffline() == key) {
                                list_GameOnline.remove(i);
                            }
                        }
                        Collections.sort(list_GameOnline, Comparator.comparing(Game::getLuotTai));
                        Collections.reverse(list_GameOnline);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void ReadFireBase_GametuyChon(List<Game> list_GameOffline, Game_Adapter adapter, int keyOn, int theLoai) {
        game_dao.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (list_GameOffline != null) {
                    list_GameOffline.clear();
                }
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> _list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : _list) {
                        Game game = d.toObject(Game.class);
                        list_GameOffline.add(game);
                        for (int i = 0; i < list_GameOffline.size(); i++) {
                            if (list_GameOffline.get(i).getOnlineorOffline() == keyOn || list_GameOffline.get(i).getTheLoai() != theLoai) {
                                list_GameOffline.remove(i);
                            }
                        }
                    }
                    Collections.sort(list_GameOffline, Comparator.comparing(Game::getLuotTai));
                    Collections.reverse(list_GameOffline);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public long Delete_Game(Game game, Context context) {
        game_dao.document(game.getMaBaiViet()).
                delete().
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Đã xóa game", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "Faided: ");
                        Toast.makeText(context, "Xóa Thất Bại", Toast.LENGTH_SHORT).show();
                    }
                });
        //Thông báo xóa game khỏi hệ thống
        hoaDonDao.hoaDonDao.whereEqualTo("id_game", game.getMaBaiViet()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : task.getResult()) {
                    HoaDon hoaDon = document.toObject(HoaDon.class);
                    ThongBao thongBao = new ThongBao();
                    thongBao.setKieuThongBao(5);
                    thongBao.setNgaygui(Calendar.getInstance().getTime());
                    thongBao.setNoiDung("Game " + hoaDon.getNameGame() + " đã bị xóa khỏi hệ thống");
                    thongBao.setUserGui("Hệ Thống");
                    thongBao.setUserNhan(hoaDon.getUsermail());
                    thongBaoDao.addThongBao(thongBao);
                }
            }
        });

        //Xóa hóa đơn của game
        hoaDonDao.hoaDonDao.whereEqualTo("id_game", game.getMaBaiViet()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot x : task.getResult()) {
                    hoaDonDao.hoaDonDao.document(x.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(context, "Đã xóa hóa đơn", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
        return 1;
    }

    public void Edit_Game(Game game, String index, Context context) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Game").document(index)
                .set(game)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Update Faild !!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void ReadFireBase_Game(List<Game> listGame, Game_Adapter adapter) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Game").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (listGame != null) {
                    listGame.clear();
                }
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> _list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : _list) {
                        Game game = d.toObject(Game.class);
                        listGame.add(game);
                    }
                    Collections.sort(listGame, Comparator.comparing(Game::getLuotTai));
                    Collections.reverse(listGame);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void checkMuaGame(TextView tv, String idgame, int giatien) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.getInstance().collection("HoaDon").whereEqualTo("id_game", idgame).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getData().get("usermail").toString().equals(user.getEmail())) {
                            tv.setText("Đã mua");
                            return;
                        }
                    }
                    tv.setText(giatien + " $");
                    tv.setTextColor(Color.parseColor("#FFF7B3"));
                } else {
                }
            }
        });
    }


    public void checkActiMuaGame(TextView tv, String idgame, String tieude, Activity activity) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.getInstance().collection("HoaDon").whereEqualTo("id_game", idgame).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getData().get("usermail").toString().equals(user.getEmail())) {
                            tv.setText("  Đã mua");
                            return;
                        }
                    }
                    tv.setText("  " + tieude);
                    tv.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                }
            }
        });
    }
}
