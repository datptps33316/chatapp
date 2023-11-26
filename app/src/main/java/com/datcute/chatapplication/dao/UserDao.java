package com.datcute.chatapplication.dao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.datcute.chatapplication.MainActivity;
import com.datcute.chatapplication.fragment.FragmentFriendRq;
import com.datcute.chatapplication.interfacce.FCMService;
import com.datcute.chatapplication.interfacce.GetInformationCallback;
import com.datcute.chatapplication.interfacce.OnLoginResultListener;
import com.datcute.chatapplication.interfacce.SenderIdCallback;
import com.datcute.chatapplication.model.FCMNotification;
import com.datcute.chatapplication.model.FCMPayload;
import com.datcute.chatapplication.model.FCMResponse;
import com.datcute.chatapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserDao {

    static final String COLLECTION_FRIENDS = "Friends";
    static ArrayList<User> listUser = new ArrayList<>();
    static String token;

    public static String getReceiverId() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null ? mAuth.getCurrentUser().getUid() : null;
    }

    public static void getInformationUser(String uid, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(uid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.e("hhhhh", "kdfdkfkdfd");
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name = document.getString("name");
                                String img = document.getString("image");
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.putExtra("name", name);
                                intent.putExtra("image", img);
                                context.startActivity(intent);

                            } else {
                                // Tài liệu không tồn tại
                            }
                        } else {
                            // Xử lý khi không thể lấy được tài liệu
                        }
                    }
                });
    }

    static long diffInDays;
    public static void getListSenderIdFromFirebase(String receiverId, String value, SenderIdCallback callback) {
        Log.e("đfd", "llllllll");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.e("id", receiverId.toString());
        ArrayList<UserSender> listSenderId = new ArrayList<>();
        db.collection(COLLECTION_FRIENDS)
                .whereEqualTo("receiverId", receiverId)
                .whereEqualTo("status", value)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e(getClass().getName(), error.toString());
                        }
                        if (value != null) {

                            for (DocumentSnapshot document : value) {
                                Timestamp  time = document.getTimestamp("timestamp");
                                Date currentTime = Calendar.getInstance().getTime();
                                // Tính khoảng thời gian
                                long diffInMillis = currentTime.getTime() - time.toDate().getTime();
                                // Chuyển đổi khoảng thời gian thành số ngày
                                diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);
                                String senderIdUser = document.getString("senderId");
                                Log.e("senderid", senderIdUser);
                                listSenderId.add(new UserSender(senderIdUser,diffInDays));
                            }
                            callback.onSenderListReady(listSenderId);
                        }
                    }
                });
    }


    public static void getFriendsRealtime(String receiverId, String value, GetInformationCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        getListSenderIdFromFirebase(receiverId, value, new SenderIdCallback() {
            @Override
            public void onSenderListReady(ArrayList<UserSender> list) {
                for (UserSender us : list) {
                    String senderID = us.getSenderID();
                    long time = us.getTime();
                    db.collection("Users")
                            .whereEqualTo("uid", senderID)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                                    if (e != null) {
                                        // Xử lý lỗi nếu có
                                        return;
                                    }
                                    listUser.clear();
                                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                        String name = document.getString("name");
                                        String image = document.getString("image");
                                        String email = document.getString("email");
                                        String uid = document.getString("uid");

                                        if (image == null) {
                                            image = "gs://chat-app-de681.appspot.com/images1700071640945/profile.jpg";
                                        }
                                        listUser.add(new User(name,email, uid, Uri.parse(image),time));
                                    }
                                    callback.onGetListFriendsRequest(listUser);
                                }
                            });
                }
            }

        });
    }

    public static void accept(String senderId, FragmentFriendRq.UpdateStatusFriends updateStatusFriends) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String document = senderId + getReceiverId();
        DocumentReference docRef = db.collection(COLLECTION_FRIENDS).document(document);
        docRef.update("status", "1")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            updateStatusFriends.onSuccess(true);
                        } else {
                            updateStatusFriends.onSuccess(false);
                            // Xử lý khi có lỗi xảy ra
                        }
                    }
                });
    }


    public static void getFcmToken(TokenCallback callback) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.e(getClass().getName(), "Không lấy được token");
                    return;
                }
                String token = task.getResult();
                Log.e("Token", token);
                if (callback != null) {
                    callback.onTokenReceived(token);
                }
            }
        });
    }


    public static void saveTokenFromFireStore(String tokens, String uid) {
        Map<String, Object> token = new HashMap<>();
        token.put("token", tokens);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(uid).update(token);

    }

    public static void CheckLogin(Context context, String email, String password, OnLoginResultListener onLoginResultListener) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = getUidUser();
                            getFcmToken(new TokenCallback() {
                                @Override
                                public void onTokenReceived(String token) {
                                    saveTokenFromFireStore(token, uid);
                                    saveSharedPreferences(context, uid, email, password);
                                }
                            });

                            onLoginResultListener.onLoginSuccess(uid);
                        } else {
                            onLoginResultListener.onLoginFailure(task.getException().getMessage());
                        }
                    }
                });


    }

    public static String getUidUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            // Trả về UID của người dùng hiện tại
            return uid;
        } else {
            // Người dùng chưa đăng nhập
            return null;
        }
    }

    public interface TokenCallback {
        void onTokenReceived(String token);
    }


    public static void saveSharedPreferences(Context context, String uid, String email, String passWord) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", uid); // userEmail là thông tin email của người dùng
        editor.putString("email", email);
        editor.putString("password", passWord);// userToken là token hoặc thông tin đăng nhập khác
        editor.apply();
    }

    static Boolean check = false;

    public static void sendFriendInvitations(User user, Context context ) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser usersender = mAuth.getCurrentUser();
        String senderId = usersender.getUid();
        String receiverId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> friendRequestData = new HashMap<>();
        friendRequestData.put("senderId", senderId);
        friendRequestData.put("receiverId", receiverId);
        friendRequestData.put("status", "0");// 0 đã giử lời mời // 1 đã chấp nhận
        friendRequestData.put("timestamp", FieldValue.serverTimestamp());
        DocumentReference docRef = db.collection("Friends").document(senderId + receiverId);

        if (check == false) {

            db.collection("Friends").document(senderId + receiverId)
                    .set(friendRequestData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "đã gửi lời mời kết bạn", Toast.LENGTH_LONG).show();
                            sendAddFriendRequest(user.getTokenFcm(), usersender.getDisplayName());
                            check = true;
                        }
                    });
        } else {

            docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(context, "Đã xóa lời mời kết bạn", Toast.LENGTH_LONG).show();
                    check = false;
                    // Thực hiện các hành động khác sau khi xóa thành công
                }

            });
        }
    }

    public static void sendAddFriendRequest(String friendToken, String sender) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FCMService service = retrofit.create(FCMService.class);
        FCMNotification notification = new FCMNotification("Chat App", "Bạn có lời mời kết bạn từ" + sender);
        FCMPayload payload = new FCMPayload(friendToken, notification);
        Call<FCMResponse> call = service.sendMessage(payload);
        call.enqueue(new Callback<FCMResponse>() {
            @Override
            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                if (response.isSuccessful()) {
                    FCMResponse fcmResponse = response.body();
                    Log.e("thadfdfads", "thành công rồi");
                } else {
                    Log.e("thadfdfads", "no  rồi");
                }
            }

            @Override
            public void onFailure(Call<FCMResponse> call, Throwable t) {
                Log.e("thadfdfads", t.toString());

            }
        });
    }
    public static void upDateName( String name ,Context context){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Users").document(userId);
        userRef.update("name", name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Cập nhật tên thành công
                        Toast.makeText(context, "Tên đã được cập nhật!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý khi cập nhật tên thất bại
                        Toast.makeText(context, "Cập nhật tên thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public static class UserSender{
       private String SenderID ;
       private long time;

        public UserSender(String senderID, long time) {
            SenderID = senderID;
            this.time = time;
        }

        public UserSender() {
        }

        public String getSenderID() {
            return SenderID;
        }

        public void setSenderID(String senderID) {
            SenderID = senderID;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }

}






