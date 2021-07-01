package com.google;

import com.google.inject.internal.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/** A class used to represent a video. */
class Video {

  private final String title;
  private final String videoId;
  private final List<String> tags;
  private final Boolean flagged;
  private final String flaggedReason;

  Video(String title, String videoId, List<String> tags, @Nullable Boolean flagged,
        @Nullable String flaggedReason) {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
    this.flagged = flagged;
    this.flaggedReason = flaggedReason;
  }

  /** Returns the title of the video. */
  String getTitle() {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId() {
    return videoId;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags() {
    return tags;
  }

  public String getFlaggedReason() {
    return flaggedReason;
  }

  public boolean getFlagged() {
    return flagged;
  }
}

///** A class used to represent a video. */
//class VideoExtended extends Video {
//
//  private final Object flagged;
//  private final Object flaggedReason;
//
//  VideoExtended(String title, String videoId, List<String> tags, @Nullable Boolean flagged,
//                @Nullable String flaggedReason) {
//    super(title, videoId, tags);
//    this.flagged = flagged;
//    this.flaggedReason = flaggedReason;
//  }
//
//  /** Returns a readonly collection of the tags of the video. */
//  public Object getFlaggedReason() {
//    return flaggedReason;
//  }
//}
