package com.example.tastebase.utilities.interfaces;

import com.example.tastebase.models.User;

import java.util.ArrayList;

public interface GetUsersFromDB {

    void onSuccess(ArrayList<User> users);

    void onFailure(String errorMessage);
}
