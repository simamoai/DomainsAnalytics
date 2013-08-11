package edu.jhu.researchProject.service;

import java.util.List;

import edu.jhu.researchProject.model.AccessedWebPage;

public interface AccessedWebPageService {
	public List<AccessedWebPage> getAccessedWebPageList();
	public AccessedWebPage getAccessedWebPage(String pageKey);
	public void insertAccessedWebPage(AccessedWebPage accessedWebPage);
	public void deleteAccessedWebPage(String pageKey);
	public void updateAccessedWebPage();
}
