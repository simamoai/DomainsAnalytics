package com.jhu.researchProject.mapReduceProcess;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Mapper;

import com.jhu.researchProject.mapReduceProcess.ExtractFromCompressedData.MyCounter;


public class DomainNamesMapper extends Mapper<LongWritable, Text, Text, Text> {

	private HtmlUnitDomainNamesProcessor htmlUnitDomainNamesProcessor;

	public DomainNamesMapper() {
		super();
		this.htmlUnitDomainNamesProcessor = new HtmlUnitDomainNamesProcessor();
	}

	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		String line = value.toString();
		String[] arr = line.split(" ");
		Text domainKey = new Text();
		Text values = new Text();
		Counter totalRecordsProcessedCounter = context
				.getCounter(MyCounter.TOTAL_RECORDS_PROCESSED_IN_MAP);
		if (arr.length == 3) {
			String domainName = arr[0];
			domainKey.set(domainName);
			try {
				String content = getHtmlUnitDomainNamesProcessor()
						.getWebPageContent(
								"http://"
										+ domainName.substring(domainName
												.indexOf(".") + 1) + ".com");
				values.set(arr[1] + "~" + arr[2] + "~" + content);
				context.write(domainKey, values);
				totalRecordsProcessedCounter.increment(1);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

	}

	public synchronized HtmlUnitDomainNamesProcessor getHtmlUnitDomainNamesProcessor() {
		return this.htmlUnitDomainNamesProcessor;
	}

	public synchronized void setHtmlUnitDomainNamesProcessor(
			HtmlUnitDomainNamesProcessor htmlUnitDomainNamesProcessor) {
		this.htmlUnitDomainNamesProcessor = htmlUnitDomainNamesProcessor;
	}

}
