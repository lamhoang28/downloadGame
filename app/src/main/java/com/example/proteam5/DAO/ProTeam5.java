package com.example.proteam5.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProTeam5 {

    public void Delete_image(String urlImage, Context context){

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference gsReference=firebaseStorage.getReferenceFromUrl(urlImage);

        String url = gsReference.getPath();

        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference desertRef = storageRef.child(url);
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Xoa anh goc thanh cong", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "Xoa anh goc thanh cong: " );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("TAG", "Xoa anh goc khong thanh cong: " );
            }
        });
    }
}
