package com.datcute.chatapplication.dao;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MessageDao {

    public static void createMessage(String senDerID , String receiverId ,String content){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("senderID", senDerID);
        messageData.put("receiverID", receiverId);
        messageData.put("content", content);

// Lấy tham chiếu đến collection cụ thể (ví dụ: "users")
        Task<Void> usersRef = db.collection("Messgaes").document(senDerID+receiverId)
                .set(messageData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}
