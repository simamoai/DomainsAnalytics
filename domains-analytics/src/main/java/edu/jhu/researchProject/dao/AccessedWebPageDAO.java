package edu.jhu.researchProject.dao;
import edu.jhu.researchProject.model.*;

import java.util.List;

public interface AccessedWebPageDAO {
	public List<AccessedWebPage> getAccessedWebPageList();
	public AccessedWebPage getAccessedWebPage(String pageKey);
	public void insertAccessedWebPage(AccessedWebPage accessedWebPage);
	public void deleteAccessedWebPage(String pageKey);
	public void updateAccessedWebPage();
}
