package com.jhu.researchProject.mapReduceAnalytics;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DomainContentReducer extends Reducer<Text, Text, Text, Text> {

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		String pageCode = "";
		String ipAddress = "";
		String content = "";

		for (Text value : values) {
			String[] valuesArr = value.toString().split("~");
			if (valuesArr.length == 3) {
				pageCode = valuesArr[0];
				ipAddress = valuesArr[1];
				content = valuesArr[2];
			}
		}

		context.write(key, new Text(pageCode + " " + ipAddress + "" + content));
	}

}
