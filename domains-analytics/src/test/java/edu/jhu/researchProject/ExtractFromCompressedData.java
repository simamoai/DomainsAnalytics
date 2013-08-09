package edu.jhu.researchProject;

import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;

public class ExtractFromCompressedData {

	public void run() throws IOException, InterruptedException,
			ClassNotFoundException {
		Job job = new Job();

		job.setJarByClass(ExtractFromCompressedData.class);

		job.setJobName("Extract From Compressed Data");

		FileInputFormat.setInputPaths(job, new Path(this.getClass()
				.getClassLoader().getResource("com.zone1.gz").toString()));
		FileOutputFormat.setOutputPath(job, new Path(
				"output/extractCompressedData.out"));

		job.setMapperClass(DomainNamesMapper.class);
		job.setReducerClass(DomainNamesReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		boolean success = job.waitForCompletion(true);
		System.exit(success ? 0 : 1);
	}

	public static enum MyCounter {
	    TOTAL_RECORDS_PROCESSED_IN_MAP
	}

	public static class DomainNamesMapper extends
			Mapper<LongWritable, Text, Text, Text> {
		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String line = value.toString();
			String[] arr = line.split(" ");
			Text domainKey = new Text();
			Text values = new Text();
			
			if (arr.length == 3) {
				domainKey.set(arr[0]);
				values.set(arr[1]+"~"+arr[2]);
				context.write(domainKey, values);
			}
			Counter totalRecordsProcessedCounter = context.getCounter(MyCounter.TOTAL_RECORDS_PROCESSED_IN_MAP);
	        totalRecordsProcessedCounter.increment(1);
		}
	}

	public static class DomainNamesReducer extends
			Reducer<Text, Text, Text, Text> {
		@Override
		public void reduce(Text key, Iterable<Text> values,
				Context context) throws IOException, InterruptedException {
			String number = "";
			String ipAddress = "";
			
			for (Text value :  values) {
				String[] valuesArr = value.toString().split("~");
				if (valuesArr.length == 2) {
					number = valuesArr[0];
					ipAddress = valuesArr[1];
				}
			}
			String outputString = number+" "+ipAddress;
			context.write(key, new Text(outputString));
		}
	}
}
