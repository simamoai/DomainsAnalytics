package com.welflex.dao;

import me.prettyprint.cassandra.serializers.IntegerSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;

import com.welflex.model.Author;

public class AuthorDaoImpl extends AbstractColumnFamilyDao<String, Author> implements AuthorDao {
  public AuthorDaoImpl(Keyspace keySpace) {
    super(keySpace, String.class, Author.class, "Authors");
  }

  public void save(Author author) {
    super.save(author.getUserName(), author);
  }

  public void delete(Author author) {
    delete(author.getUserName());
  }

  public int getBlogEntryCount(String userName) {
    ColumnQuery<String, String, Integer> q = HFactory.createColumnQuery(keySpace,
      StringSerializer.get(), StringSerializer.get(), IntegerSerializer.get());

    QueryResult<HColumn<String, Integer>> r = q.setKey(userName).setName("numberOfPosts")
        .setColumnFamily(columnFamilyName).execute();
    HColumn<String, Integer> c = r.get();
    
    return c == null
        ? 0
        : c.getValue();
  }

  public void updateBlogEntryCount(String userName, int count) {
    Mutator<String> mutator = HFactory.createMutator(keySpace, StringSerializer.get());

    mutator.addInsertion(userName, columnFamilyName, HFactory.createColumn("numberOfPosts", count,
      StringSerializer.get(), IntegerSerializer.get())).execute();
  }
}
