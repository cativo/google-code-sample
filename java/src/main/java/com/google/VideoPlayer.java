package com.google;

import java.lang.module.FindException;
import java.util.*;
import java.util.stream.Collectors;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private final PlaylistLibrary playlistLibrary;
  private String currentPlayingVideoId;
  private boolean currentVideoPaused;
//  private List<VideoPlaylist> videoPlaylists;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
    this.playlistLibrary = new PlaylistLibrary();
//    this.videoPlaylists = new ArrayList<VideoPlaylist>();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  /*
    list videos and sort them lexicographical order by title
  */
  public void showAllVideos() {
    System.out.println("Here's a list of all available videos:");
    List<Video> videoList = videoLibrary.getVideos();
    sortAndListVideos(videoList, true);
  }

  private void sortAndListVideos(List<Video> videoList, boolean sort) {
    videoList = sort ? videoList.stream().sorted(Comparator.comparing(Video::getTitle)).collect(Collectors.toList()) : videoList;
    for (Video video : videoList) {
      // get flagged reason, if exist
      String flagged = "";
      if (video.getFlagged())
        flagged = video.getFlaggedReason() != null ? String.format("- FLAGGED (reason: %s)",
                video.getFlaggedReason()) : "- " +
                "FLAGGED (reason: Not supplied)";
      // printout videos details
      System.out.println(String.format("%s (%s) %s %s",
              video.getTitle(),
              video.getVideoId(),
              video.getTags().toString().replaceAll(",", ""),
              flagged));
    }
  }

  public void playVideo(String videoId) {
    Video video = this.videoLibrary.getVideo(videoId);

    // check if video is flagged
    if(video != null && video.getFlagged())
    {
      String reason = video.getFlaggedReason() != null ? video.getFlaggedReason() : "Not supplied";
      System.out.println(String.format("Cannot add video to %s: Video is currently flagged (reason:" +
              " %s)", video.getTitle(), reason));
      return;
    }

    try{
      if (this.currentPlayingVideoId != null)
        this.stopVideo();

      this.currentPlayingVideoId = videoId;
      currentVideoPaused = false;
      System.out.println(String.format("Playing video: %s", video.getTitle()));
    } catch (Exception e) {
      System.out.println("Cannot play video: Video does not exist");
    }
  }

  public void stopVideo() {
    try {
      if (this.currentPlayingVideoId != null && !this.currentPlayingVideoId.equals(" ")) {
        System.out.println(String.format("Stopping video: %s", this.videoLibrary.getVideo(this.currentPlayingVideoId).getTitle()));
        this.currentPlayingVideoId = null;
        currentVideoPaused = false;
      } else {
        System.out.println("Cannot stop video: No video is currently playing");
      }
    } catch (Exception e) {
      System.out.println("Cannot stop video: Video does not exist");
    }
  }

  public void playRandomVideo() {
    try {
      if (this.currentPlayingVideoId != null)
        this.stopVideo();

      var videoList = this.videoLibrary.getVideos().stream().filter(x -> !x.getFlagged()).collect(Collectors.toList());
      Random rand = new Random();
      Video randomVideo = videoList.get(rand.nextInt(videoList.size()));

      this.currentPlayingVideoId = randomVideo.getVideoId();
      currentVideoPaused = false;

      System.out.println(String.format("Playing video: %s", this.videoLibrary.getVideo(randomVideo.getVideoId()).getTitle()));
    } catch (Exception e) {
      System.out.println("No videos available");
    }
  }

  public void pauseVideo() {
      if (this.currentPlayingVideoId != null && !this.currentPlayingVideoId.equals(" ")) {
        if (!currentVideoPaused) {
          System.out.println(String.format("Pausing video: %s", this.videoLibrary.getVideo(this.currentPlayingVideoId).getTitle()));
          currentVideoPaused = true;
        }
        else
          System.out.println(String.format("Video already paused: %s", this.videoLibrary.getVideo(this.currentPlayingVideoId).getTitle()));

      } else {
        System.out.println("Cannot pause video: No video is currently playing");
      }
  }

  public void continueVideo() {
    if (this.currentPlayingVideoId != null && !this.currentPlayingVideoId.equals(" ")) {
      if (!currentVideoPaused) {
        System.out.println("Cannot continue video: Video is not paused");
      }
      else
        System.out.println(String.format("Continuing video: %s", this.videoLibrary.getVideo(this.currentPlayingVideoId).getTitle()));

    } else {
      System.out.println("Cannot continue video: No video is currently playing");
    }
  }

  public void showPlaying() {
    if (this.currentPlayingVideoId != null && !this.currentPlayingVideoId.equals(" ")) {
      Video currentVideo = this.videoLibrary.getVideo(this.currentPlayingVideoId);
      if (!currentVideoPaused)
        System.out.println(String.format("Currently playing: %s (%s) %s",
                currentVideo.getTitle(),
                currentVideo.getVideoId(),
                currentVideo.getTags().toString().replaceAll(",", "")));
      else
        System.out.println(String.format("Currently playing: %s (%s) %s - PAUSED",
                currentVideo.getTitle(),
                currentVideo.getVideoId(),
                currentVideo.getTags().toString().replaceAll(",", "")));
    } else {
      System.out.println("No video is currently playing");
    }
  }

  public void createPlaylist(String playlistName) {

    this.playlistLibrary.createPlayList(playlistName);

  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    // get the video by videoId
    Video video = this.videoLibrary.getVideo(videoId);

    this.playlistLibrary.addVideoToPlayList(playlistName, video);

  }

  public void showAllPlaylists() {
    var videoPlayLists = this.playlistLibrary.getVideoPlayLists();
    videoPlayLists = videoPlayLists.stream().sorted(Comparator.comparing(VideoPlaylist::getTitle)).collect(Collectors.toList());

    if(videoPlayLists.size() == 0)
      System.out.println("No playlists exist yet");
    else {
      System.out.println("Showing all playlists:");
      for (VideoPlaylist videoPlayList: videoPlayLists) {
        System.out.println(String.format("%s", videoPlayList.getTitle()));
      }
    }
  }

  public void showPlaylist(String playlistName) {
    var playList = this.playlistLibrary.getVideoPlayList(playlistName);
    if(playList == null)
      System.out.println(String.format("Cannot show playlist %s: Playlist does not exist", playlistName));
    else {
      System.out.println(String.format("Showing playlist: %s", playlistName));
      var playlistVideos = playList.getVideosList();
      if (playlistVideos ==  null || playlistVideos.size() == 0)
        System.out.println("No videos here yet");
      else {
        List<Video> videosList = new ArrayList<>();
        for (String videoId: playlistVideos) {
          Video video = this.videoLibrary.getVideo(videoId);
          videosList.add(video);
        }
        this.sortAndListVideos(videosList, false);
      }
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    // get the video by videoId
    Video video = this.videoLibrary.getVideo(videoId);

    this.playlistLibrary.removeVideoFromPlayList(playlistName, video);
  }

  public void clearPlaylist(String playlistName) {
    this.playlistLibrary.removeAllVideosFromPlayList(playlistName);
  }

  public void deletePlaylist(String playlistName) {
    this.playlistLibrary.deletePlayList(playlistName);
  }

  public void searchVideos(String searchTerm) {
    var filteredVideoList = this.videoLibrary.searchVideosByTitle(searchTerm);
    if(filteredVideoList.size() == 0)
      System.out.println(String.format("No search results for %s", searchTerm));
    else {
      searchAndPlayVideoFromUserSelection(searchTerm, filteredVideoList, "Nope!");
    }
  }

  public void searchVideosWithTag(String videoTag) {
    var filteredVideoList = this.videoLibrary.searchVideosByTag(videoTag);
    if(filteredVideoList.size() == 0)
      System.out.println(String.format("No search results for %s", videoTag));
    else {
      searchAndPlayVideoFromUserSelection(videoTag, filteredVideoList, "No");
    }
  }

  private void searchAndPlayVideoFromUserSelection(String videoTag, List<Video> filteredVideoList, String no) {
    System.out.println(String.format("Here are the results for %s:", videoTag));
    for (int i = 0; i < filteredVideoList.size(); i++) {
      Video video = filteredVideoList.get(i);
      System.out.println(String.format("%s) %s (%s) %s",
              i + 1,
              video.getTitle(),
              video.getVideoId(),
              video.getTags().toString().replaceAll(",", "")));
    }

    try {
      Scanner scanner = new Scanner(System.in);
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.\n" +
              "If your answer is not a valid number, we will assume it's a no.");

      int selection = Integer.parseInt(scanner.nextLine());
      if (selection != 0 && selection <= filteredVideoList.size()) {
        // Handle input
        this.playVideo(filteredVideoList.get(selection - 1).getVideoId());
      } else {
        System.out.println(no);
      }
    } catch (Exception e) {
      System.out.println(no);
    }
  }

  public void flagVideo(String videoId) {
    if(this.currentPlayingVideoId != null)
      this.stopVideo();

    this.videoLibrary.flagVideo(videoId, null);
  }

  public void flagVideo(String videoId, String reason) {
    if(this.currentPlayingVideoId != null)
      this.stopVideo();

    this.videoLibrary.flagVideo(videoId, reason);
  }

  public void allowVideo(String videoId) {
    this.videoLibrary.allowVideo(videoId);
  }
}