package com.google;

import java.util.List;

/** A class used to represent a Playlist */
class VideoPlaylist {

    public String title;
    public List<String> videosList;

    VideoPlaylist(String title, List<String> videosList) {
        this.title = title;
        this.videosList = videosList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = this.replaceWhitespace(title);
    }

    public List<String> getVideosList() {
        return videosList;
    }

    public void setVideosList(List<String> videosList) {
        this.videosList = videosList;
    }

    public String replaceWhitespace(String playListTitle){
        playListTitle = playListTitle.replaceAll(" ", "_");
        return playListTitle;
    }
}
