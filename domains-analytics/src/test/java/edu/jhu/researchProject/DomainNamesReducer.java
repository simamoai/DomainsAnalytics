package edu.jhu.researchProject;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

public class DomainNamesReducer extends
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
