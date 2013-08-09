package edu.jhu.researchProject;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
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

		job.setMapperClass(WordMapper.class);
		job.setReducerClass(SumReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		boolean success = job.waitForCompletion(true);
		System.exit(success ? 0 : 1);
	}

	public static class WordMapper extends
			Mapper<LongWritable, Text, Text, IntWritable> {
		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String line = value.toString();

			for (String word : line.split("\\W+")) {
				if (word.length() > 0) {
					context.write(new Text(word), new IntWritable(1));
				}
			}
		}
	}

	public static class SumReducer extends
			Reducer<Text, IntWritable, Text, IntWritable> {
		@Override
		public void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			int wordCount = 0;

			for (IntWritable value : values) {
				wordCount += value.get();
			}
			context.write(key, new IntWritable(wordCount));
		}
	}
}
