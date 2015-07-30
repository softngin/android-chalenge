package com.raduvarga.railscommitviewer.model;

/**
 * Created by radu on 30.07.2015.
 */
public class Commit {
    public String author;
    public String message;
    public String sha;


    public Commit( String author, String sha, String message) {
        this.message = message;
        this.author = author;
        this.sha = sha;
    }
}
