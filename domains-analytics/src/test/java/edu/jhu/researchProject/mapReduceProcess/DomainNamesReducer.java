package edu.jhu.researchProject.mapReduceProcess;

import java.io.IOException;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import edu.jhu.researchProject.model.AccessedWebPage;
import edu.jhu.researchProject.service.AccessedWebPageService;
import edu.jhu.researchProject.service.AccessedWebPageServiceImpl;

public class DomainNamesReducer extends Reducer<Text, Text, Text, Text> {
	private static final String WEB_PAGE_CLUSTER = "Test Web Pages Cluster";
	private static final String KEY_SPACE = "WebPageKeyspace";

	private Cluster cluster;
	private Keyspace keySpace;
	private AccessedWebPageService accessedWebPageService;
	private AccessedWebPage reducedAccessedWebPage;
	
	@Override
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		// Connect to service
		cluster = HFactory.getOrCreateCluster(WEB_PAGE_CLUSTER,
				"localhost:9160");
		keySpace = HFactory.createKeyspace(KEY_SPACE, cluster);
		accessedWebPageService = new AccessedWebPageServiceImpl(keySpace);

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
		this.reducedAccessedWebPage = new AccessedWebPage(key.toString(),
				content, ipAddress, pageCode);
		if (reducedAccessedWebPage != null) {
			accessedWebPageService.insertAccessedWebPage(reducedAccessedWebPage);
			context.write(key, new Text(reducedAccessedWebPage.toString()));
		}
		
	}
}
