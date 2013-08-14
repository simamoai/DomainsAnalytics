package edu.jhu.researchProject.service;

import java.util.List;

import edu.jhu.researchProject.model.AccessedWebPage;

public interface AccessedWebPageService {
	public List<AccessedWebPage> getAccessedWebPages(String pageKey);
	public void insertAccessedWebPage(String key, AccessedWebPage accessedWebPage);
	public void deleteAccessedWebPage(String pageKey);
	public void updateAccessedWebPage();
}
