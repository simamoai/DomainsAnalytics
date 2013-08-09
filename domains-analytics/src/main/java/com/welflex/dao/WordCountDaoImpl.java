package com.welflex.dao;
import me.prettyprint.hector.api.Keyspace;

import com.welflex.model.WordCount;

public class WordCountDaoImpl extends AbstractColumnFamilyDao<String, WordCount>
  implements WordCountDao {

  public WordCountDaoImpl(Keyspace keyspace) {
    super(keyspace, String.class, WordCount.class, "output_words");
  }

  @Override
  public int getWordCount(String key) {
    WordCount c = super.find(key);
    return c == null ? 0 : c.getCount();
  }
}
