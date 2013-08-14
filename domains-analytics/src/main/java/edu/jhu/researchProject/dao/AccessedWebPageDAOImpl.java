package edu.jhu.researchProject.dao;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.welflex.dao.AbstractDao;
import com.welflex.dao.DaoHelper;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.beans.SuperSlice;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SuperSliceQuery;
import edu.jhu.researchProject.model.AccessedWebPage;

public class AccessedWebPageDAOImpl extends AbstractDao implements AccessedWebPageDAO{
	private static final String COL_FAMILY_ACCESSEDWEBPAGES = "AccessedWebPages";

	
	public AccessedWebPageDAOImpl(Keyspace keyspace) {
		super(keyspace);
		
	}

	public List<AccessedWebPage> getAccessedWebPages(String pageKey){
		  SuperSliceQuery<String, byte[], String, String> query = HFactory
			        .createSuperSliceQuery(keySpace, StringSerializer.get(), BytesArraySerializer.get(),
			          StringSerializer.get(), StringSerializer.get())
			        .setRange(null, null, false, Integer.MAX_VALUE).setColumnFamily(COL_FAMILY_ACCESSEDWEBPAGES)
			        .setKey(pageKey);

			    QueryResult<SuperSlice<byte[], String, String>> result = query.execute();

			    SuperSlice<byte[], String, String> sc = result.get();

			    List<AccessedWebPage> retResults = new ArrayList<AccessedWebPage>();

			    for (HSuperColumn<byte[], String, String> col : sc.getSuperColumns()) {
			    	AccessedWebPage accessedPage = new AccessedWebPage();
			      DaoHelper.populateEntityFromCols(accessedPage, col.getColumns());
			      retResults.add(accessedPage);
			    }
			    
			    if (retResults.size() == 0) {
			    	return null;
			    }
			    return retResults;
		
	}
	
	public void insertAccessedWebPage(String key, AccessedWebPage accessedWebPage){
		Mutator<String> mutator = HFactory.createMutator(keySpace, StringSerializer.get());
		UUID timeUUID  = HelperDAO.getTimeUUID();
		List<HColumn<String,String>> columns = HelperDAO.getStringCols(accessedWebPage);
		 HSuperColumn<UUID, String, String> sCol = HFactory.createSuperColumn(timeUUID, columns,
			      UUIDSerializer.get(), StringSerializer.get(), StringSerializer.get());
		 mutator.insert(key, COL_FAMILY_ACCESSEDWEBPAGES, sCol);
		 
	}
	
	public void deleteAccessedWebPage(String pageKey){
		
	}
	
	public void updateAccessedWebPage(){
		
	}
}

