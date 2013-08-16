package com.jhu.researchProject.mapReduceAnalytics;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.SortedMap;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.cassandra.db.IColumn;
import org.apache.cassandra.utils.ByteBufferUtil;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public  class DomainContentMapper extends Mapper<ByteBuffer, SortedMap<ByteBuffer, IColumn>, Text, IntWritable>
{
	private static final Logger logger = LoggerFactory.getLogger(DomainContentMapper.class);
	private static final String CONF_COLUMN_NAME = "pageContent";
    

    static final String OUTPUT_COLUMN_FAMILY = "output_words";
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
    private ByteBuffer sourceColumn;

    @SuppressWarnings("rawtypes")
	protected void setup(org.apache.hadoop.mapreduce.Mapper.Context context)
    throws IOException, InterruptedException
    {
        sourceColumn = ByteBufferUtil.bytes(context.getConfiguration().get(CONF_COLUMN_NAME));
    }

    public void map(ByteBuffer key, SortedMap<ByteBuffer, IColumn> columns, Context context) throws IOException, InterruptedException
    {
    	
        IColumn column = columns.get(sourceColumn);
        if (column == null)
            return;
        String value = ByteBufferUtil.string(column.value());
        logger.debug("read " + key + ":" + value + " from " + context.getInputSplit());

        StringTokenizer itr = new StringTokenizer(value);
        while (itr.hasMoreTokens())
        {
            word.set(itr.nextToken());
            context.write(word, one);
        }
    }
}