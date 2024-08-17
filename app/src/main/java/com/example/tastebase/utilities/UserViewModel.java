package com.example.tastebase.utilities;

import androidx.lifecycle.ViewModel;
import com.example.tastebase.models.User;

public class UserViewModel extends ViewModel {
    private final User user = new User();

    public User getUser() {
        return user;
    }
}