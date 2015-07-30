package com.raduvarga.railscommitviewer.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.raduvarga.railscommitviewer.R;
import com.raduvarga.railscommitviewer.controller.CommitGrabber;
import com.raduvarga.railscommitviewer.model.Commit;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CommitGrabber.getInstance().setActivity(this);
    }

    public void populateListView(final ArrayList<Commit> commits){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListView listView = (ListView) findViewById(R.id.lv_commits);
                CommitsListAdapter adapter = new CommitsListAdapter(MainActivity.this, R.layout.row_commit, commits);
                listView.setAdapter(adapter);
            }
        });
    }

    public class CommitsListAdapter extends ArrayAdapter<Commit> {

        public CommitsListAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public CommitsListAdapter(Context context, int resource, List<Commit> items) {
            super(context, resource, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.row_commit, null);
            }

            Commit commit = getItem(position);

            if (commit != null) {
                TextView tvAuthor = (TextView) v.findViewById(R.id.tv_author);
                TextView tvSha = (TextView) v.findViewById(R.id.tv_sha);
                TextView tvMessage = (TextView) v.findViewById(R.id.tv_message);

                if (tvAuthor != null) {
                    tvAuthor.setText(commit.author);
                }

                if (tvSha != null) {
                    tvSha.setText(commit.sha);
                }

                if (tvMessage != null) {
                    tvMessage.setText(commit.message);
                }
            }

            return v;
        }

    }
}
