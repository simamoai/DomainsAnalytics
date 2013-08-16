package edu.jhu.researchProject.mapReduceAnalytics;

import org.junit.Test;

import com.jhu.researchProject.mapReduceAnalytics.ReadContentFromCassandraWithAnalytics;

public class ReadContentFromCassandraTest {
	
	@Test
	public void testReadContentFromCassandraMapReduceJob() throws Exception{
		new ReadContentFromCassandraWithAnalytics().run();
	}
}
