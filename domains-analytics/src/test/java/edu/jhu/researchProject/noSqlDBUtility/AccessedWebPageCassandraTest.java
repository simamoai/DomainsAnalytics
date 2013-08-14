package edu.jhu.researchProject.noSqlDBUtility;

import static org.junit.Assert.assertTrue;
import java.util.List;
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
		AccessedWebPage accessedWebPage2 = new AccessedWebPage();
		accessedWebPage2.setPageKey("Domain2");
		accessedWebPage2.setPageContent("some stupid content");

		accessedWebPageService.insertAccessedWebPage("domains", accessedWebPage);
		accessedWebPageService.insertAccessedWebPage("domains", accessedWebPage2);
		
		List<AccessedWebPage> listOfAccessedWebPages = accessedWebPageService.getAccessedWebPages("domains");
		assertTrue("AccessWebPages table contains Domain1", listOfAccessedWebPages.contains(accessedWebPage));
		assertTrue("AccessWebPages table contains Domain2", listOfAccessedWebPages.contains(accessedWebPage2));
	}
	
	@Test
	public void testGetAllAccessedWebPagesFromCassandra() throws Exception {
		List<AccessedWebPage> listOfAccessedWebPages = null;
		listOfAccessedWebPages = accessedWebPageService.getAccessedWebPages("domains");
		
		assertTrue("size of list is greater than 2", listOfAccessedWebPages.size() > 2);
	}
}
