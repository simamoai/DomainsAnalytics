package com.welflex.dao;

import java.util.List;
import java.util.UUID;

public interface TagActionDao {
  public static final String ALL_TAG_KEY = "__notag__";
  /**
   * @param tagId Tag Identifier
   * @param blockEntryId Blog Id
   */
  void create(String tagId, String blockEntryId, UUID timeUUID);

  /**
   * Gets a List of Blog Entries for a particular tag
   * @param tagId Tag Identifier
   * @return   a List of Blog Entries for the tag specified
   */
  List<String> getBlogEntriesForTag(String tagId);

  void delete(String tagId);
}
