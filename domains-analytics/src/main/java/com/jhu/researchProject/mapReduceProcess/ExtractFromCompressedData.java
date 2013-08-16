package com.jhu.researchProject.mapReduceProcess;

import java.io.IOException;
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
		boolean success = job.waitForCompletion(true);
		System.exit(success ? 0 : 1);
	}

}
