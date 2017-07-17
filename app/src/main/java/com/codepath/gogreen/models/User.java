package com.codepath.gogreen.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by anyazhang on 7/14/17.
 */
@ParseClassName("User")
public class User extends ParseObject {
    long uid;
    String username;
    String email;
    String password;
    ArrayList<Long> friends;
}
