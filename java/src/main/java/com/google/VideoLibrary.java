package com.google;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A class used to represent a Video Library.
 */
class VideoLibrary {

  private final HashMap<String, Video> videos;

  VideoLibrary() {
    this.videos = new HashMap<>();
    try {
      File file = new File(this.getClass().getResource("/videos.txt").getFile());

      Scanner scanner = new Scanner(file);
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        String[] split = line.split("\\|");
        String title = split[0].strip();
        String id = split[1].strip();
        List<String> tags;
        if (split.length > 2) {
          tags = Arrays.stream(split[2].split(",")).map(String::strip).collect(
              Collectors.toList());
        } else {
          tags = new ArrayList<>();
        }
        this.videos.put(id, new Video(title, id, tags, false, null));
      }
    } catch (FileNotFoundException e) {
      System.out.println("Couldn't find videos.txt");
      e.printStackTrace();
    }
  }

  List<Video> getVideos() {
    return new ArrayList<>(this.videos.values());
  }

  /**
   * Get a video by id. Returns null if the video is not found.
   */
  Video getVideo(String videoId) {
    return this.videos.get(videoId);
  }

  /**
   * Search and list videos by title. Returns null if the video is not found.
   */
  List<Video> searchVideosByTitle(String title) {
    List<Video> videoList =
            this.getVideos().stream().filter(x -> !x.getFlagged()).sorted(Comparator.comparing(Video::getTitle))
                    .collect(Collectors.toList());

    List<Video> filteredVideosList = videoList.stream()
            .filter(x-> x.getTitle().toLowerCase().contains(title.toLowerCase())).collect(Collectors.toList());

    return filteredVideosList;
  }

  /**
   * Search and list videos by tag. Returns null if the video is not found.
   */
  List<Video> searchVideosByTag(String title) {
    List<Video> videoList =
            this.getVideos().stream().filter(x -> !x.getFlagged()).sorted(Comparator.comparing(Video::getTitle))
                    .collect(Collectors.toList());

    List<Video> filteredVideosList = videoList.stream()
            .filter(x-> {
              var anyMatch = x.getTags().stream()
                      .anyMatch(o -> o.equalsIgnoreCase(title));
              return anyMatch;
                    }
            ).collect(Collectors.toList());

    return filteredVideosList;
  }

  /**
   * Flag a video. Returns null if the video is not found.
   */
  boolean flagVideo(String videoId, String flagReason) {
    // get video
    var video = this.getVideo(videoId);

    // check if video exists
    if (video == null) {
      System.out.println("Cannot flag video: Video does not exist");
      return false;
    } else {
      // check if video is already flagged
      if (video.getFlagged())
      {
        System.out.println("Cannot flag video: Video is already flagged");
        return false;
      }

      // flag video
      video = new Video(video.getTitle(), video.getVideoId(), video.getTags(), true, flagReason);

      if(flagReason != null && !flagReason.isEmpty() && !flagReason.isBlank())
        System.out.println(String.format("Successfully flagged video: %s (reason: %s)",
                video.getTitle(), video.getFlaggedReason()));
      else
        System.out.println(String.format("Successfully flagged video: %s (reason: %s)",
                  video.getTitle(), "Not supplied)"));

      // update video list
      this.videos.put(videoId, video);
    }

    return true;
  }

  /**
   * Flag a video. Returns null if the video is not found.
   */
  boolean allowVideo(String videoId) {
    // get video
    var video = this.getVideo(videoId);

    // check if video exists
    if (video == null) {
      System.out.println("Cannot remove flag from video: Video does not exist");
      return false;
    } else {
      // check if video is not flagged
      if (!video.getFlagged())
      {
        System.out.println("Cannot remove flag from video: Video is not flagged");
        return false;
      }

      // allow video
      video = new Video(video.getTitle(), video.getVideoId(), video.getTags(), false, null);

      // update video list
      this.videos.put(videoId, video);
      System.out.println(String.format("Successfully removed flag from video: %s", video.getTitle()));
    }

    return true;
  }
}
