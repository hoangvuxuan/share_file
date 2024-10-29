package com.example.t1;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class PlaySongActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private String audioUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        // Lấy dữ liệu truyền từ Intent
        Intent intent = getIntent();
        String songTitle = intent.getStringExtra("songTitle");
        String imageUrl = intent.getStringExtra("imageUrl");
        audioUrl = intent.getStringExtra("audioUrl");

        // Gán dữ liệu vào các View
        ImageView songImage = findViewById(R.id.song_image);
        TextView songTitleView = findViewById(R.id.song_title);
        Button playButton = findViewById(R.id.play_button);
        Button pauseButton = findViewById(R.id.pause_button);
        Button stopButton = findViewById(R.id.stop_button);

        songTitleView.setText(songTitle);

        // Sử dụng Glide để tải và hiển thị ảnh bài hát
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(songImage);

        // Khởi tạo MediaPlayer với URL của audio
        mediaPlayer = new MediaPlayer();

        playButton.setOnClickListener(v -> playAudio());
        pauseButton.setOnClickListener(v -> pauseAudio());
        stopButton.setOnClickListener(v -> stopAudio());
    }

    private void playAudio() {
        try {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.setDataSource(audioUrl);
                mediaPlayer.prepare(); // Chuẩn bị phát nhạc
                mediaPlayer.start();   // Bắt đầu phát nhạc
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pauseAudio() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause(); // Tạm dừng phát nhạc
        }
    }

    private void stopAudio() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop(); // Dừng phát nhạc
            mediaPlayer.reset(); // Reset lại MediaPlayer
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Giải phóng tài nguyên MediaPlayer
            mediaPlayer = null;
        }
    }
}
