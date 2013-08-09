package com.welflex.dao;

import me.prettyprint.hector.api.Keyspace;

/**
 * Created by IntelliJ IDEA. User: sacharya Date: Oct 26, 2010 Time: 3:09:11 PM To change this
 * template use File | Settings | File Templates.
 */
public class AbstractDao {
  protected final Keyspace keySpace;
  public AbstractDao(Keyspace keySpace) {
    this.keySpace = keySpace;
  }
}
