package com.example.t1;

public class Song {
    private String id;
    private String name;
    private String artistName;
    private String audioUrl;
    private String imageUrl;  // Thêm thuộc tính imageUrl

    public Song(String id, String name, String artistName, String audioUrl, String imageUrl) {
        this.id = id;
        this.name = name;
        this.artistName = artistName;
        this.audioUrl = audioUrl;
        this.imageUrl = imageUrl;  // Khởi tạo thuộc tính imageUrl
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getArtistName() { return artistName; }
    public String getAudioUrl() { return audioUrl; }
    public String getImageUrl() { return imageUrl; }  // Getter cho imageUrl
}
