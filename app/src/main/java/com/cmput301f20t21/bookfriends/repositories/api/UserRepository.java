package com.cmput301f20t21.bookfriends.repositories.api;

import com.cmput301f20t21.bookfriends.entities.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public interface UserRepository {
    Task<Void> add(String uid, String username, String email);
    Task<User> getByUsername(String username);
    Task<User> getByUid(String uid);
    Task<List<User>> getByUsernameStartWith(String username);
    Task<Void> updateUserEmail(User user, String email);
}
