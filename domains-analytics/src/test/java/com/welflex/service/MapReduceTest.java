package com.welflex.service;

import java.util.Random;

import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.welflex.BootStrap;
import com.welflex.dao.WordCountDao;
import com.welflex.dao.WordCountDaoImpl;
import com.welflex.model.Author;
import com.welflex.model.BlogEntry;
import com.welflex.model.Comment;

public class MapReduceTest {
  private static final String BLOG_CLUSTER = "Test Cluster";

  private static final String KEY_SPACE = "Keyspace1";

  private Cluster cluster;

  private Keyspace keySpace;

  private BlogService blogService;

  private final Random random = new Random();

  @BeforeClass
  public static void beforeClass() throws Exception {
    BootStrap.init();
  }

  @Before
  public void before() {
    // Connect to service
    cluster = HFactory.getOrCreateCluster(BLOG_CLUSTER, "localhost:9160");
    keySpace = HFactory.createKeyspace(KEY_SPACE, cluster);
    blogService = new BlogServiceImpl(keySpace);
 }

  private static final String COMMENTERS[] = { "sanjayacharya", "donaldduck", "rogerrabbit",
      "jamesbond", "nickcarter", "rambo", "neo", "nemesis", "factorypilot", "fasterMan",
      "shiveringzombies" };

  private void createAuthors(String... authorIds) {
    for (String authorId : authorIds) {
      Author author = new Author();
      author.setUserName(authorId);
      blogService.createAuthor(author);
    }
  }
  
  private static String[] WORDS_TO_GET_COUNT_OF = {"james", "2012", "cave", "walther", "bond"};

  @Test
  public void testMapReduce() throws Exception {
    createAuthors(COMMENTERS);
    String blogEntryId = "cassandraMapReduce";
    
    BlogEntry entry = new BlogEntry();
    entry.setAuthorId(COMMENTERS[0]);
    entry.setBlogEntryId(blogEntryId);
    entry.setBody("Cassandra and Map Reduce is what I am blogging about Blah Blah Blah ahasdadsa.");
    entry.setTags("cassandra");
    entry.setTitle("A Blog on Cassandra and Hector");
    entry.setPubDate(System.currentTimeMillis());
    blogService.create(entry);
  
    createComments(blogEntryId, 5000);
    new CommentWordCountJob(WORDS_TO_GET_COUNT_OF).run();    
    printWordCounts();
  }
  
  private void printWordCounts() {
    WordCountDao wcDao = new WordCountDaoImpl(keySpace);
    for (int i = 0; i < WORDS_TO_GET_COUNT_OF.length; i++) {
      System.out.println("The word [" + WORDS_TO_GET_COUNT_OF[i] + "] has occured ["
        + wcDao.getWordCount(WORDS_TO_GET_COUNT_OF[i]) + "] times");
    }
  }

  private void createComments(String blogEntryId, int count) {
    for (int i = 0; i < count; i++) {
      Comment comment = new Comment();
      comment.setBlogEntryId(blogEntryId);
      comment.setComment(COMMENTS[random.nextInt(COMMENTS.length)]);
      comment.setCommenterId(COMMENTERS[random.nextInt(COMMENTERS.length)]);
      System.out.println("Adding comment:" + comment);
      blogService.commentOnBlogEntry(comment);
    }
  }

  private String[] COMMENTS = { "Only a cave man could do this", "The world is a cave. James bond lives in a Cave.",
      "Fred Flintstone is in his cave", "Roger Rabit wanted to be on Geico",
      "Java is dead, long live Java", "James Bond would be dead without Q to help him.",
      "Bond uses Walther PPK", "Felix Lighter and James Bond work well together as they are cave men",
      "Geico has great customer service. Even Bond uses it.", 
      "2012 comes after 2011 but before 2013.",
      "2012 cannot be the end of the world, not as long as James and Rambo do not fight"};
  
}
