package com.welflex.model;

public class Comment {
  private String blogEntryId;

  private String commenterId;

  private String comment;

  public String getBlogEntryId() {
    return blogEntryId;
  }

  public void setBlogEntryId(String blogEntryId) {
    this.blogEntryId = blogEntryId;
  }

  public String getCommenterId() {
    return commenterId;
  }

  public void setCommenterId(String commenterId) {
    this.commenterId = commenterId;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  @Override
  public String toString() {
    return "Comment [blogEntryId=" + blogEntryId + ", commenterId=" + commenterId + ", comment="
      + comment + "]";
  }
}
