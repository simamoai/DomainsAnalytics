package com.welflex.service;

import java.util.List;

import com.welflex.model.Author;
import com.welflex.model.BlogEntry;
import com.welflex.model.Comment;

public interface BlogService {
  
  public void create(BlogEntry blogEntry);
  
  public BlogEntry getBlogEntry(String blogEntryId);
  
  public List<String> getAllBlogEntryIds();
  
  public void commentOnBlogEntry(Comment comment);
  
  public List<Comment> getBlogEntryComments(String blogEntryId);
  
  public List<String> getBlogEntriesForTag(String tagId);
  
  public void createAuthor(Author author);
}
