package edu.jhu.researchProject.mapReduceProcess;

import java.io.IOException;
import java.nio.ByteBuffer;

import me.prettyprint.cassandra.serializers.UUIDSerializer;

import org.apache.cassandra.hadoop.ConfigHelper;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.map.MultithreadedMapper;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;

public class ExtractFromCompressedData {
	public static enum MyCounter {
		TOTAL_RECORDS_PROCESSED_IN_MAP
	}

	public void run() throws IOException, InterruptedException,
			ClassNotFoundException {
		Job job = new Job();

		job.setJarByClass(ExtractFromCompressedData.class);

		job.setJobName("Extract From Compressed Data");

		FileInputFormat.setInputPaths(job, new Path(this.getClass()
				.getClassLoader().getResource("com.zone10.gz").toString()));
		FileOutputFormat.setOutputPath(job, new Path("output"));

		job.setMapperClass(DomainNamesMapper.class);
		job.setReducerClass(DomainNamesReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setNumReduceTasks(100);

		MultithreadedMapper.setMapperClass(job, DomainNamesMapper.class);
		MultithreadedMapper.setNumberOfThreads(job, 1000);
		/* ConfigHelper.setRpcPort(job.getConfiguration(), "9160");
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
	      ConfigHelper.setInputSlicePredicate(job.getConfiguration(), predicate);*/
		boolean success = job.waitForCompletion(true);
		System.exit(success ? 0 : 1);
	}

}
