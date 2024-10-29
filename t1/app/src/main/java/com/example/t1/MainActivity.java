package com.example.t1;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SongAdapter adapter;
    private List<Song> songList;
    private EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        editTextSearch = findViewById(R.id.editTextSearch);

        songList = new ArrayList<>();
        adapter = new SongAdapter(songList);
        recyclerView.setAdapter(adapter);

        // Initially fetch all songs
        fetchSongs("");

        // Add text change listener to EditText for search functionality
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fetchSongs(s.toString()); // Fetch songs based on search query
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });
    }

    // Fetch song data from API, optionally with search query
    private void fetchSongs(String query) {
        new Thread(() -> {
            try {
                // Build the API URL with search query
                String apiUrl = "https://api.jamendo.com/v3.0/tracks/?client_id=900dafad&limit=20";
                if (!query.isEmpty()) {
                    apiUrl += "&search=" + query; // Add search parameter to the URL
                }

                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                parseJson(result.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Parse JSON response and update song list
    private void parseJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray("results");

            songList.clear();  // Clear list trước khi thêm kết quả mới

            for (int i = 0; i < results.length(); i++) {
                JSONObject track = results.getJSONObject(i);
                String id = track.getString("id");
                String name = track.getString("name");
                String artistName = track.getString("artist_name");
                String audioUrl = track.getString("audio");
                String imageUrl = track.getString("image");  // Lấy URL của ảnh bài hát

                songList.add(new Song(id, name, artistName, audioUrl, imageUrl));
            }

            runOnUiThread(() -> adapter.notifyDataSetChanged());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
