package edu.jhu.researchProject.noSqlDBUtility;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.welflex.BootStrap;
import edu.jhu.researchProject.model.AccessedWebPage;
import edu.jhu.researchProject.service.AccessedWebPageServiceImpl;
import edu.jhu.researchProject.service.AccessedWebPageService;

import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;

public class AccessedWebPageCassandraTest {
	private static final String WEB_PAGE_CLUSTER = "Test Web Pages Cluster";

	private static final String KEY_SPACE = "WebPageKeyspace";

	private Cluster cluster;

	private Keyspace keySpace;

	private AccessedWebPageService accessedWebPageService;

	@BeforeClass
	public static void beforeClass() throws Exception {
		BootStrap.init();
	}

	@Before
	public void before() {
		// Connect to service
		cluster = HFactory.getOrCreateCluster(WEB_PAGE_CLUSTER,
				"localhost:9160");
		keySpace = HFactory.createKeyspace(KEY_SPACE, cluster);
		accessedWebPageService = new AccessedWebPageServiceImpl(keySpace);
	}

	@Test
	public void testInsertAccessedWebPageToCassandra() throws Exception {
		AccessedWebPage accessedWebPage = new AccessedWebPage();
		accessedWebPage.setPageKey("Domain1");
		accessedWebPage.setPageContent("some stupid content");

		accessedWebPageService.insertAccessedWebPage(accessedWebPage);
		
		assertEquals("accessed web page for Domain inserted successfully", 
				accessedWebPage, accessedWebPageService.getAccessedWebPage("Domain1"));
	}
}
