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
				.getClassLoader().getResource("com.zone10.gz").toString()));
		FileOutputFormat.setOutputPath(job, new Path(
				"output"));

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
		private HtmlUnitDomainNamesProcessor htmlUnitDomainNamesProcessor;
		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			this.htmlUnitDomainNamesProcessor = new HtmlUnitDomainNamesProcessor();
			String line = value.toString();
			String[] arr = line.split(" ");
			Text domainKey = new Text();
			Text values = new Text();
			Counter totalRecordsProcessedCounter = context.getCounter(MyCounter.TOTAL_RECORDS_PROCESSED_IN_MAP);
			if (arr.length == 3) {
				String domainName = arr[0];
				domainKey.set(domainName);
				try {
					String content = htmlUnitDomainNamesProcessor.getWebPageContent("http://"+domainName.substring(domainName.indexOf(".")+1)+".com");
					values.set(arr[1]+"~"+arr[2]+"~"+content);
					context.write(domainKey, values);
			        totalRecordsProcessedCounter.increment(1);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			
		}
	}

	public static class DomainNamesReducer extends
			Reducer<Text, Text, Text, Text> {
		@Override
		public void reduce(Text key, Iterable<Text> values,
				Context context) throws IOException, InterruptedException {
			String number = "";
			String ipAddress = "";
			String content = "";
			
			for (Text value :  values) {
				String[] valuesArr = value.toString().split("~");
				if (valuesArr.length == 3) {
					number = valuesArr[0];
					ipAddress = valuesArr[1];
					content = valuesArr[2];
				}
			}
			String outputString = number+" "+ipAddress+" "+content;
			context.write(key, new Text(outputString));
		}
	}
}
