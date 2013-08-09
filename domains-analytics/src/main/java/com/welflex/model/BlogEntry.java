package com.welflex.model;

import java.util.Arrays;
import org.apache.commons.lang.StringUtils;

public class BlogEntry {
  private String blogEntryId;

  private String authorId;

  private String title;

  private String body;

  private String tags;

  private Long pubDate;

  public String getBlogEntryId() {
    return blogEntryId;
  }

  public void setBlogEntryId(String blogEntryId) {
    this.blogEntryId = blogEntryId;
  }

  public String getAuthorId() {
    return authorId;
  }

  public void setAuthorId(String authorId) {
    this.authorId = authorId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  public Long getPubDate() {
    return pubDate;
  }

  public void setPubDate(Long pubDate) {
    this.pubDate = pubDate;
  }

  public Iterable<String> getTagsSplit() {
    return Arrays.asList(StringUtils.split(tags, ','));
  }

  @Override
  public String toString() {
    return "BlogEntry [blogEntryId=" + blogEntryId + ", authorId=" + authorId + ", title=" + title
      + ", body=" + body + ", tags=" + tags + ", pubDate=" + pubDate + "]";
  }
}
