package com.google;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class PlaylistLibrary {
    private final HashMap<String, VideoPlaylist> videoPlayLists;

    public PlaylistLibrary() {
        this.videoPlayLists = new HashMap<>();
    }

    /**
     * Get all videoPlayLists. Returns null if there is any.
     */
    List<VideoPlaylist> getVideoPlayLists() {
        return new ArrayList<>(this.videoPlayLists.values());
    }

    /**
     * Get a getVideoPlayList by playlist title. Returns null if the videoPlayList is not found.
     */
    VideoPlaylist getVideoPlayList(String title) {
//        return this.videoPlayLists.get(title);
        VideoPlaylist videoPlaylist = this.videoPlayLists.values()
                .stream()
                .filter(o -> o.getTitle().toLowerCase().equals(title.toLowerCase())).findAny().orElse(null);
        return videoPlaylist;
    }


    /**
     * Get a getvideoPlayList by id. Returns null if the videoPlayList is not found.
     */
    boolean createPlayList(String title) {
        boolean listContainsNewPlayList = this.videoPlayLists.values().stream().anyMatch(o -> o.getTitle().toLowerCase().equals(title.toLowerCase()));

        if (listContainsNewPlayList) {
            System.out.println("Cannot create playlist: A playlist with the same name already exists");
            return false;
        } else {
            VideoPlaylist newVideoPlayList = new VideoPlaylist(title, null);
            this.videoPlayLists.put(title, newVideoPlayList);
            System.out.println(String.format("Successfully created new playlist: %s", newVideoPlayList.title));
            return true;
        }
    }

    /**
     * Add Video to VideoPlayList. returns false if does not exists;
     */
    boolean addVideoToPlayList(String title, Video video) {
        return addOrRemovedVideoToPlaylist(title, video, true);
    }

    /**
     * Remove Video from VideoPlayList. returns false if does not exists;
     */
    boolean removeVideoFromPlayList(String title, Video video) {
        return addOrRemovedVideoToPlaylist(title, video, false);
    }

    private boolean addOrRemovedVideoToPlaylist(String title, Video video, boolean addOrRemove) {
        try {
            //TODO: change videoList to a List<String> and add videoId
            // get playlist by title
            VideoPlaylist videoPlaylist = this.getVideoPlayList(title);
            List<String> videosList = videoPlaylist.getVideosList();
            // initiate videoList if it is null
            if (videosList == null)
                videosList = new ArrayList<String>();

            try {
                // check if video is flagged
                if(video.getFlagged())
                {
                    String reason = video.getFlaggedReason() != null ? video.getFlaggedReason() : "Not supplied";
                    System.out.println(String.format("Cannot add video to %s: Video is currently flagged (reason:" +
                            " %s)", title, reason));
                    return false;
                }

                // check if video exists in the playlist
                var videoId = video.getVideoId();
                var videoListContainsNewVideo =
                        videosList.stream().anyMatch(o -> o.equals(videoId));
                if (videoListContainsNewVideo && addOrRemove) {
                    System.out.println(String.format("Cannot add video to %s: Video already added", title));
                    return false;
                }

                if(!videoListContainsNewVideo && !addOrRemove && video != null) {
                    // video is not in the playlist
                    System.out.println(String.format("Cannot remove video from %s: Video is not in playlist", title));
                    return false;
                }

                if (addOrRemove) {
                    // add video to playlist videos
                    videosList.add(video.getVideoId());
                }
                else
                    // remove video to playlist videos
                    videosList.remove(video.getVideoId());

                // update playlist
                videoPlaylist.setVideosList(videosList);
                this.videoPlayLists.put(videoPlaylist.title, videoPlaylist);
                System.out.println(addOrRemove ? String.format("Added video " +
                        "to %s: %s", title, video.getTitle())
                        : String.format("Removed video from %s: %s", title, video.getTitle()));
            } catch (Exception e) {
                System.out.println(addOrRemove ?
                        String.format("Cannot add video to %s: Video does not exist", title)
                        : String.format("Cannot remove video from %s: Video does not exist", title));
                return false;
            }
        } catch (Exception e) {
            System.out.println(addOrRemove ? String.format("Cannot add video to %s: Playlist does not exist", title)
                    : String.format("Cannot remove video from %s: Playlist does not exist", title));
            return false;
        }

        return true;
    }

    /**
     * Remove All Videos from VideoPlayList. returns false if does not exists;
     */
    boolean removeAllVideosFromPlayList(String title) {
        try {
            // get playlist by title
            VideoPlaylist videoPlaylist = this.getVideoPlayList(title);
            List<String> videosList = videoPlaylist.getVideosList();

            for (String videoId: new ArrayList<String>(videosList)) {
                // remove video to playlist videos
                videosList.remove(videoId);
            }

            // update playlist
            videoPlaylist.setVideosList(videosList);

            System.out.println(String.format("Successfully removed all videos from %s", title));

        } catch (Exception e) {
            System.out.println(String.format("Cannot clear playlist %s: Playlist does not exist", title));
            return false;
        }
        return true;
    }

    /**
     * Delete VideoPlayList. returns false if does not exists;
     */
    boolean deletePlayList(String title) {
        try {
            if(this.videoPlayLists.values().stream().anyMatch(x -> x.getTitle().equalsIgnoreCase(title))) {
                this.videoPlayLists.remove(title);
                System.out.println(String.format("Deleted playlist: %s", title));
            } else {
                System.out.println(String.format("Cannot delete playlist %s: Playlist does not exist", title));
            }
        } catch (Exception e) {
            System.out.println(String.format("Cannot delete playlist %s: Playlist does not exist", title));
            return false;
        }
        return true;
    }
}
