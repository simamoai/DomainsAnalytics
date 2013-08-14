package edu.jhu.researchProject.service;

import java.util.List;

import me.prettyprint.hector.api.Keyspace;

import edu.jhu.researchProject.dao.AccessedWebPageDAO;
import edu.jhu.researchProject.dao.AccessedWebPageDAOImpl;
import edu.jhu.researchProject.model.AccessedWebPage;

public class AccessedWebPageServiceImpl implements AccessedWebPageService{
	private final AccessedWebPageDAO accessedWebPageDAO;
	
	
	public AccessedWebPageServiceImpl(Keyspace keySpace) {
		this.accessedWebPageDAO = new AccessedWebPageDAOImpl(keySpace);
	}

	@Override
	public List<AccessedWebPage> getAccessedWebPages(String pageKey) {
		return this.accessedWebPageDAO.getAccessedWebPages(pageKey);
	}


	@Override
	public void insertAccessedWebPage(String key, AccessedWebPage accessedWebPage) {
		// TODO Auto-generated method stub
		this.accessedWebPageDAO.insertAccessedWebPage(key, accessedWebPage);
	}


	@Override
	public void deleteAccessedWebPage(String pageKey) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void updateAccessedWebPage() {
		// TODO Auto-generated method stub
		
	}
}
