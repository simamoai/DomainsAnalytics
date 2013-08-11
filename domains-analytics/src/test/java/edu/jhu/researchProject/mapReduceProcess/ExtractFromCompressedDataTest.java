package edu.jhu.researchProject.mapReduceProcess;

import org.junit.BeforeClass;
import org.junit.Test;
import com.welflex.BootStrap;

public class ExtractFromCompressedDataTest {
	@BeforeClass
	public static void beforeClass() throws Exception {
		BootStrap.init();
	}
	
	@Test
	public void testExtractCompressedData() throws Exception{
		new ExtractFromCompressedData().run();
	}
}
