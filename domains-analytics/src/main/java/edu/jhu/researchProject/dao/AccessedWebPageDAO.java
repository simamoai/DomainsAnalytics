package edu.jhu.researchProject.dao;
import edu.jhu.researchProject.model.*;

import java.util.List;

public interface AccessedWebPageDAO {
	public List<AccessedWebPage> getAccessedWebPages(String pageKey);
	public void insertAccessedWebPage(String key, AccessedWebPage accessedWebPage);
	public void deleteAccessedWebPage(String pageKey);
	public void updateAccessedWebPage();
}
