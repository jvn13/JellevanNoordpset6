package com.example.jelle.jellevannoord_pset6;

public class User implements Comparable<User> {

    Long karma;
    String username;

    public User() {
    }

    public User(Long karma, String username) {
        this.karma = karma;
        this.username = username;
    }

    @Override
    public int compareTo(User other){
        // compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than
        // other and 0 if they are supposed to be equal
        return other.karma.compareTo(this.karma);
    }
}
