package com.welflex.dao;

import com.welflex.model.Author;

public interface AuthorDao {
  public void save(Author author);
  public int getBlogEntryCount(String userName);
  public void updateBlogEntryCount(String userName, int count);
  public Author find(String userName);
  public void delete(Author author);
}
