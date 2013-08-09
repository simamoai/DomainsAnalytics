package com.welflex.dao;

import com.welflex.model.BlogEntry;

public interface BlogEntryDao {
  public void create(BlogEntry blogEntry);
  public BlogEntry get(String blogEntryId);
}
