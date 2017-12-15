package com.example.jelle.jellevannoord_pset6;

/*
User class which saves a user from the database.
It is possible to get the karma and username from the user and compare the karma of users.
 */

public class User implements Comparable<User> {

    // Variables
    public Long karma;
    public String username;

    public User() {
    }

    public User(Long mKarma, String username) {
        this.karma = mKarma;
        this.username = username;
    }

    @Override
    public int compareTo(User other){
        // compare karma
        return other.karma.compareTo(this.karma);
    }
}
