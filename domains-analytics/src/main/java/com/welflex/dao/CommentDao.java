package com.welflex.dao;

import java.util.List;

import com.welflex.model.Comment;

public interface CommentDao {
  /**
   * @param comment to create
   */
  public void create(Comment comment);
  
  /**
   * @param blogEntryId Blog Entry Id for which to get the comments
   * @return            List of Comments
   */
  public List<Comment> getBlogEntryComments(String blogEntryId);
}
