package com.welflex.dao;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TagActionDaoImpl extends AbstractDao implements TagActionDao {
  private static final String COL_FAMILY_NAME = "TaggedPosts";

  public TagActionDaoImpl(Keyspace keyspace) {
    super(keyspace);
  }

  public void create(String tagId, String blockEntryId, UUID timeUUID) {
    Mutator<String> mutator = HFactory.createMutator(keySpace, StringSerializer.get());
    
    HColumn<UUID, String> column = HFactory.createColumn(timeUUID, blockEntryId,
        UUIDSerializer.get(), StringSerializer.get());
    mutator.addInsertion(tagId, COL_FAMILY_NAME, column)
        .addInsertion(ALL_TAG_KEY, COL_FAMILY_NAME, column).execute();
  }

  public List<String> getBlogEntriesForTag(String tagId) {
    SliceQuery<String, UUID, String> query = HFactory
        .createSliceQuery(keySpace, StringSerializer.get(), UUIDSerializer.get(),
            StringSerializer.get());

    QueryResult<ColumnSlice<UUID, String>> result = query.setColumnFamily(COL_FAMILY_NAME)
        .setKey(tagId).setRange(null, null, false, Integer.MAX_VALUE).execute();

    List<String> blogEntryIds = new ArrayList<String>();

    for (HColumn<UUID, String> column : result.get().getColumns()) {
      blogEntryIds.add(column.getValue());
    }

    return blogEntryIds;
  }

  public void delete(String tagId) {
    Mutator<String> mutator = HFactory.createMutator(keySpace, StringSerializer.get());
    mutator.delete(tagId, COL_FAMILY_NAME, null, StringSerializer.get());
  }
}
