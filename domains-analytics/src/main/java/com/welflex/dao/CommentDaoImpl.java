package com.welflex.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.beans.SuperSlice;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SuperSliceQuery;

import com.welflex.model.Comment;
/**
 * <pre>
 * <!--
    ColumnFamily: Comments
    We store all comments here

    Row key => row key of the BlogEntry
    SuperColumn name: TimeUUIDType

    Access: get all comments for an entry

    Comments : {
        // comments for scream-is-the-best-movie-ever
        scream-is-the-best-movie-ever : { // row key = row key of BlogEntry
            // oldest comment first
            timeuuid_1 : { // SC Name
                // all Columns in the SC are attribute of the comment
                commenterId: Joe Blow,
                email: joeb@example.com,
                comment: you're a dumb douche, the godfather is the best movie ever
                commentTime: 1250438004
            },

            ... more comments for scream-is-the-best-movie-ever

            // newest comment last
            timeuuid_2 : {
                commenterId: Some Dude,
                email: sd@example.com,
                comment: be nice Joe Blow this isnt youtube
                commentTime: 1250557004
            },
        },

        // comments for i-got-a-new-guitar
        i-got-a-new-guitar : {
            timeuuid_1 : { // SC Name
                // all Columns in the SC are attribute of the comment
                commenter: Johnny Guitar,
                email: guitardude@example.com,
                comment: nice axe dawg...
                commentTime: 1250438004
            },
        }

        ..
        // then more Super CF's for the other entries
    }
-->
</pre>
 * 
 */
public class CommentDaoImpl extends AbstractDao implements CommentDao {
  private static final String COL_FAMILY_NAME = "Comments";

  public CommentDaoImpl(Keyspace keyspace) {
    super(keyspace);
  }

  public void create(Comment comment) {
    Mutator<String> mutator = HFactory.createMutator(keySpace, StringSerializer.get());
    UUID timeUUID = DaoHelper.getTimeUUID();

    List<HColumn<String, String>> columns = DaoHelper.getStringCols(comment);

    HSuperColumn<UUID, String, String> sCol = HFactory.createSuperColumn(timeUUID, columns,
      UUIDSerializer.get(), StringSerializer.get(), StringSerializer.get());

    mutator.insert(comment.getBlogEntryId(), COL_FAMILY_NAME, sCol);
  }

  public List<Comment> getBlogEntryComments(String blogEntryId) {
    SuperSliceQuery<String, byte[], String, String> query = HFactory
        .createSuperSliceQuery(keySpace, StringSerializer.get(), BytesArraySerializer.get(),
          StringSerializer.get(), StringSerializer.get())
        .setRange(null, null, false, Integer.MAX_VALUE).setColumnFamily(COL_FAMILY_NAME)
        .setKey(blogEntryId);

    QueryResult<SuperSlice<byte[], String, String>> result = query.execute();

    SuperSlice<byte[], String, String> sc = result.get();

    List<Comment> retResults = new ArrayList<Comment>();

    for (HSuperColumn<byte[], String, String> col : sc.getSuperColumns()) {
      Comment comment = new Comment();
      DaoHelper.populateEntityFromCols(comment, col.getColumns());
      retResults.add(comment);
    }

    return retResults;
  }
}
