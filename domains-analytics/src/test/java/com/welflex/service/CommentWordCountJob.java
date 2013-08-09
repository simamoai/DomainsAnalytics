package com.welflex.service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.UUID;

import me.prettyprint.cassandra.serializers.IntegerSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;

import org.apache.cassandra.avro.Column;
import org.apache.cassandra.avro.ColumnOrSuperColumn;
import org.apache.cassandra.avro.Mutation;
import org.apache.cassandra.db.IColumn;
import org.apache.cassandra.hadoop.ColumnFamilyInputFormat;
import org.apache.cassandra.hadoop.ColumnFamilyOutputFormat;
import org.apache.cassandra.hadoop.ConfigHelper;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;
import org.apache.cassandra.utils.ByteBufferUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import com.google.common.collect.Maps;
import com.welflex.dao.DaoHelper;
public class CommentWordCountJob extends Configured {

  static final String KEYSPACE = "Keyspace1";

  static final String COLUMN_FAMILY = "Comments";

  static final String OUTPUT_COLUMN_FAMILY = "output_words";
  private final String[] expectedWords;
  
  public CommentWordCountJob(String... expectedWords) {
    this.expectedWords = expectedWords;
  }
  
  public static class TokenizerMapper extends
      Mapper<ByteBuffer, SortedMap<ByteBuffer, IColumn>, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);

    private Text word = new Text();
    private Set<String> expectedWords;
    private static final ByteBuffer COMMENT_COL_NAME = StringSerializer.get().toByteBuffer("comment");
    
    protected void setup(@SuppressWarnings("rawtypes") org.apache.hadoop.mapreduce.Mapper.Context context) throws IOException,
      InterruptedException {
      String[] expWords = context.getConfiguration().getStrings("expectedWords");
      expectedWords = new HashSet<String>(Arrays.asList(expWords));
    }
    
    public void map(ByteBuffer key, SortedMap<ByteBuffer, IColumn> columns, Context context) throws IOException,
      InterruptedException {
      
      for (Map.Entry<ByteBuffer, IColumn> entry : columns.entrySet()) {
        IColumn column = entry.getValue();
        if (column == null) {
          continue;
        }
        
        IColumn textCol = column.getSubColumn(COMMENT_COL_NAME);
        String value = ByteBufferUtil.string(textCol.value());
     
        StringTokenizer itr = new StringTokenizer(value);
        while (itr.hasMoreTokens()) {
          String nextWord = itr.nextToken().toLowerCase();
          // Only trap expected words
          if (expectedWords.contains(nextWord)) {
            word.set(nextWord);
            context.write(word, one);
          }
        }
      }
    }
  }
  
  public static class ReducerToCassandra extends
      Reducer<Text, IntWritable, ByteBuffer, List<Mutation>> {
   
    public void reduce(Text word, Iterable<IntWritable> values, Context context) throws IOException,
      InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      context.write(StringSerializer.get().toByteBuffer(word.toString()), Collections.singletonList(getMutation(word, sum)));
    }

    private static Mutation getMutation(Text word, int sum) {
      Column c = new Column();
      c.name = StringSerializer.get().toByteBuffer("count");
      c.value = IntegerSerializer.get().toByteBuffer(sum);
      c.timestamp = System.currentTimeMillis() * 1000;

      Mutation m = new Mutation();
      m.column_or_supercolumn = new ColumnOrSuperColumn();
      m.column_or_supercolumn.column = c;
      return m;
    }
  }
  
  public void run() throws IOException, InterruptedException, ClassNotFoundException {
    super.setConf(new Configuration());
    getConf().setStrings("expectedWords", expectedWords);
    
    Map<UUID, UUID> keyRangeMap = Maps.newHashMap();
    
    keyRangeMap.put(DaoHelper.uuidForDate(new Date(System.currentTimeMillis() - 50000)), 
      DaoHelper.uuidForDate(new Date(System.currentTimeMillis())));

    for (Map.Entry<UUID, UUID> entry : keyRangeMap.entrySet()) {
      Job job = new Job(getConf(), "wordcount");

      job.setJarByClass(CommentWordCountJob.class);
      job.setMapperClass(TokenizerMapper.class);
      job.setReducerClass(ReducerToCassandra.class);

      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(IntWritable.class);
      job.setOutputKeyClass(ByteBuffer.class);
      job.setOutputValueClass(List.class);

      job.setOutputFormatClass(ColumnFamilyOutputFormat.class);

      ConfigHelper.setOutputColumnFamily(job.getConfiguration(), KEYSPACE, OUTPUT_COLUMN_FAMILY);

      job.setInputFormatClass(ColumnFamilyInputFormat.class);

      ConfigHelper.setRpcPort(job.getConfiguration(), "9160");
      ConfigHelper.setInitialAddress(job.getConfiguration(), "localhost");
      ConfigHelper.setPartitioner(job.getConfiguration(),
        "org.apache.cassandra.dht.RandomPartitioner");
      ConfigHelper.setInputColumnFamily(job.getConfiguration(), KEYSPACE, COLUMN_FAMILY);
      ByteBuffer startKey = ByteBuffer.wrap(UUIDSerializer.get().toBytes(entry.getKey()));
      ByteBuffer endKey = ByteBuffer.wrap(UUIDSerializer.get().toBytes(entry.getValue()));
      SliceRange range = new SliceRange();
      range.setStart(startKey);
      range.setFinish(endKey);
      range.setCount(Integer.MAX_VALUE);
      
      SlicePredicate predicate = new SlicePredicate().setSlice_range(range);
      ConfigHelper.setInputSlicePredicate(job.getConfiguration(), predicate);
      job.waitForCompletion(true);
    }
  }
}
