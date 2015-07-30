package com.raduvarga.railscommitviewer.controller;

import android.util.Log;

import com.raduvarga.railscommitviewer.model.Commit;
import com.raduvarga.railscommitviewer.view.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

/**
 * Created by radu on 30.07.2015.
 */
public class CommitGrabber {

    private static CommitGrabber instance;
    private MainActivity activity;

    private CommitGrabber() {
        grabCommits("");
    }

    public static CommitGrabber getInstance() {
        if (instance == null) {
            instance = new CommitGrabber();
        }
        return instance;
    }

    public void setActivity(MainActivity activity){
        this.activity = activity;
    }

    public void grabCommits(String repository) {
        Log.i("CommitGrabber", "grabCommits");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String result = "Did not work.";
                try {
                    URL url = new URL("https://api.github.com/repos/rails/rails/commits");
                    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                    SSLContext sc;
                    sc = SSLContext.getInstance("TLS");
                    sc.init(null, null, new java.security.SecureRandom());
                    urlConnection.setSSLSocketFactory(sc.getSocketFactory());

                    try {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        result = readStream(in);
                    } finally {
                        urlConnection.disconnect();
                    }

                    ArrayList<Commit> commits = new ArrayList<Commit>();
                    JSONArray array = new JSONArray(result);
                    Log.i("CommitGrabber", "length = " + array.length());
                    for(int i=0; i<array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        JSONObject commit = object.getJSONObject("commit");
                        JSONObject author = commit.getJSONObject("author");
                        commits.add(new Commit(author.getString("name"), "Commit: " + object.getString("sha"),
                                "Message: " +commit.getString("message")));
                    }

                    Collections.sort(commits, new CommitComparator());
                    activity.populateListView(commits);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.i("CommitGrabber", "result = " + result);
            }
        });

        thread.start();
    }

    // convert inputstream to String
    private static String readStream(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
        return result;
    }

    public class CommitComparator implements Comparator<Commit> {
        @Override
        public int compare(Commit o1, Commit o2) {
            return o1.author.compareTo(o2.author);
        }
    }

}
