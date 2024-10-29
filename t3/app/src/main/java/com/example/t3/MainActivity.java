package com.example.t3;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText startDateInput;
    private EditText endDateInput;
    private TextView allTracksInfoTextView;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startDateInput = findViewById(R.id.start_date_input);
        endDateInput = findViewById(R.id.end_date_input);
        allTracksInfoTextView = findViewById(R.id.all_tracks_info);
        searchButton = findViewById(R.id.search_button);

        // Gán sự kiện khi người dùng nhấn nút tìm kiếm
        searchButton.setOnClickListener(v -> {
            String startDate = startDateInput.getText().toString().trim();
            String endDate = endDateInput.getText().toString().trim();

            // Kiểm tra xem người dùng đã nhập ngày hay chưa
            if (startDate.isEmpty() || endDate.isEmpty()) {
                allTracksInfoTextView.setText("Please enter both start and end dates.");
            } else {
                fetchTopTracks(startDate, endDate);
            }
        });
    }

    // Hàm lấy thông tin top bài hát với khoảng thời gian tùy chỉnh
    private void fetchTopTracks(String startDate, String endDate) {
        String clientId = "900dafad";  // Client ID của bạn

        // Xây dựng URL với ngày bắt đầu và kết thúc
        String url = "https://api.jamendo.com/v3.0/tracks/?client_id=" + clientId + "&format=json&order=popularity_total&limit=10";
        url += "&datebetween=" + startDate + "_" + endDate;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> allTracksInfoTextView.setText("Failed to fetch top tracks info"));
                Log.e("API_ERROR", "Failed to fetch top tracks: ", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    runOnUiThread(() -> {
                        try {
                            displayAllTracks(jsonResponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSON_ERROR", "Error parsing JSON: ", e);
                        }
                    });
                } else {
                    runOnUiThread(() -> allTracksInfoTextView.setText("No top tracks found"));
                    Log.e("API_ERROR", "API response unsuccessful: " + response.code());
                }
            }
        });
    }

    // Hàm hiển thị danh sách top bài hát
    private void displayAllTracks(String jsonResponse) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray tracks = jsonObject.getJSONArray("results");

        StringBuilder allTracksInfo = new StringBuilder();

        for (int i = 0; i < tracks.length(); i++) {
            JSONObject track = tracks.getJSONObject(i);
            String songName = track.getString("name");
            String artistName = track.getString("artist_name");
            boolean canDownload = track.getBoolean("audiodownload_allowed");  // Kiểm tra trường audiodownload_allowed

            // Chỉ hiển thị bài hát cho phép tải về
            if (canDownload) {
                allTracksInfo.append(i + 1).append(". ")
                        .append("Song: ").append(songName)
                        .append("\nArtist: ").append(artistName)
                        .append("\nDownload Allowed: ").append(canDownload)
                        .append("\n\n");
            }
        }

        // Hiển thị toàn bộ danh sách bài hát trong TextView
        if (allTracksInfo.length() > 0) {
            allTracksInfoTextView.setText(allTracksInfo.toString());
        } else {
            allTracksInfoTextView.setText("No tracks available for download for the selected period.");
        }
    }
}
