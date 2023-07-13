package com.example.proteam5.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.proteam5.Model.Comment;
import com.example.proteam5.Model.HoaDon;
import com.example.proteam5.Model_Adapter.Comment_Adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Comment_DAO {

    public  void CreateNewComment(Comment comment){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("Comment").document();

        Map<String,Object> item = new HashMap<>();
        comment.setID(documentReference.getId());
        item.put("ID",documentReference.getId());
        item.put("Avatar",comment.getAvatar());
        item.put("UserName",comment.getUserName());
        item.put("IdBaiViet",comment.getIdBaiViet());
        item.put("Title",comment.getTitle());
        item.put("ngaycmt", comment.getNgaycmt());

        documentReference.set(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.e("TAG", "successfully: " );
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
    public void ReadFireBase_Comment(List<Comment> commnetList, Comment_Adapter adapter,String idGame){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Comment")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()) {
                        if (commnetList!=null){
                            commnetList.clear();
                        }
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Comment thongBao = document.toObject(Comment.class);
                            commnetList.add(thongBao);
                            for (int i = 0; i < commnetList.size(); i++) {
                                if (!idGame.equals(commnetList.get(i).getIdBaiViet())){
                                    commnetList.remove(i);
                                }
                            }
                        }
                        Collections.sort(commnetList, new Comparator<Comment>() {
                            @Override
                            public int compare(Comment o1, Comment o2) {
                                if (o1.getNgaycmt().compareTo(o2.getNgaycmt()) <= 0) {
                                    return 1;
                                }
                                return -1;
                            }
                        });
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w("TAG", "Error getting documents.", task.getException());
                    }
                }
            });
    }
    public long CommentDelete(String index, Context context){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Comment").document(index).
                delete().
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.e("TAG", "onSuccess: ");
                        Toast.makeText(context, "onSuccess", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "Faided: ");
                        Toast.makeText(context, "Faided", Toast.LENGTH_SHORT).show();
                    }
                });
        return 1;
    }
}
