package edu.jhu.researchProject.mapReduceProcess;

import org.junit.Test;

import com.jhu.researchProject.mapReduceProcess.ExtractFromCompressedData;

public class ExtractFromCompressedDataTest {
//	@BeforeClass
//	public static void beforeClass() throws Exception {
//		BootStrap.init();
//	}
	
	@Test
	public void testExtractCompressedData() throws Exception{
		new ExtractFromCompressedData().run();
	}
}
