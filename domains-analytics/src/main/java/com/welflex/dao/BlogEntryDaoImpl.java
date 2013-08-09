package com.welflex.dao;
import me.prettyprint.hector.api.Keyspace;

import com.welflex.model.BlogEntry;
/**
 * <pre>
  <!--
    ColumnFamily: BlogEntries
    This is where all the blog entries will go:

    Row Key +> post's slug (the seo friendly portion of the uri)
    Column Name: an attribute for the entry (title, body, etc)
    Column Value: value of the associated attribute

    Access: grab an entry by blog Entry id (always fetch all Columns for Row)

    fyi: tags is a denormalization... its a comma separated list of tags.
    im not using json in order to not interfere with our
    notation but obviously you could use anything as long as your app
    knows how to deal w/ it

    BlogEntries : { // CF
        i-got-a-new-guitar : { // row key - the unique blog Entry Id of the entry.
            title: This is a blog entry about my new, awesome guitar,
            body: this is a cool entry. etc etc yada yada
            authorId: sachayra  // a row key into the Authors CF
            tags: life,guitar,music  // comma sep list of tags (basic denormalization)
            pubDate: 1250558004      // unixtime for publish date
            blogEntryId: i-got-a-new-guitar
        },
        // all other entries
        another-cool-guitar : {
            ...
            tags: guitar,
            blogEntryId: another-cool-guitar
        },
        scream-is-the-best-movie-ever : {
            ...
            tags: movie,horror,
            slug: scream-is-the-best-movie-ever
        }
    }
-->
  </pre>
 */
public class BlogEntryDaoImpl extends AbstractColumnFamilyDao<String, BlogEntry> implements
    BlogEntryDao {
  
  public BlogEntryDaoImpl(Keyspace keySpace) {
    super(keySpace, String.class, BlogEntry.class, "BlogEntries");
  }

  public void create(BlogEntry blogEntry) {
    save(blogEntry.getBlogEntryId(), blogEntry);
  }

  public BlogEntry get(String blogEntryId) {
    return find(blogEntryId);
  }
}
