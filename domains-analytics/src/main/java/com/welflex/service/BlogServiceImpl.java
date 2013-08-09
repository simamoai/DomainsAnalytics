package com.welflex.service;

import java.util.List;
import java.util.UUID;

import me.prettyprint.hector.api.Keyspace;

import com.welflex.dao.AuthorDao;
import com.welflex.dao.AuthorDaoImpl;
import com.welflex.dao.BlogEntryDao;
import com.welflex.dao.BlogEntryDaoImpl;
import com.welflex.dao.CommentDao;
import com.welflex.dao.CommentDaoImpl;
import com.welflex.dao.DaoHelper;
import com.welflex.dao.TagActionDao;
import com.welflex.dao.TagActionDaoImpl;
import com.welflex.model.Author;
import com.welflex.model.BlogEntry;
import com.welflex.model.Comment;

public class BlogServiceImpl implements BlogService {
  private final BlogEntryDao blogEntryDao;
  private final TagActionDao tagActionDao;
  private final CommentDao commentDao;
  private final AuthorDao authorDao;
  
  public BlogServiceImpl(Keyspace keySpace) {
    blogEntryDao = new BlogEntryDaoImpl(keySpace);
    tagActionDao = new TagActionDaoImpl(keySpace);
    commentDao = new CommentDaoImpl(keySpace);
    authorDao = new AuthorDaoImpl(keySpace);
  }
  
  public void create(BlogEntry blogEntry) {
    blogEntryDao.create(blogEntry);
  
    UUID uuid = DaoHelper.getTimeUUID();
    
    for (String tag : blogEntry.getTagsSplit()) {
      tagActionDao.create(tag, blogEntry.getBlogEntryId(), uuid);
    }
    tagActionDao.create(TagActionDao.ALL_TAG_KEY, blogEntry.getBlogEntryId(), uuid);
    int currentBlogEntryCount = authorDao.getBlogEntryCount(blogEntry.getAuthorId());
    authorDao.updateBlogEntryCount(blogEntry.getAuthorId(), currentBlogEntryCount + 1);
  }
    
  public List<String> getAllBlogEntryIds() {
    return tagActionDao.getBlogEntriesForTag(TagActionDao.ALL_TAG_KEY);
  }

  public void commentOnBlogEntry(Comment comment) {
    commentDao.create(comment);
  }
  
  public List<Comment> getBlogEntryComments(String blogEntryId) {
    return commentDao.getBlogEntryComments(blogEntryId);
  }
  
  public List<String> getBlogEntriesForTag(String tagId) {
    return tagActionDao.getBlogEntriesForTag(tagId);
  }

  public void createAuthor(Author author) {
    authorDao.save(author);
  }

  public BlogEntry getBlogEntry(String blogEntryId) {
    return blogEntryDao.get(blogEntryId);
  }
}
