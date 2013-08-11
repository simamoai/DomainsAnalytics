package edu.jhu.researchProject.dao;

import me.prettyprint.hector.api.Keyspace;

public class AbstractDAO {
	protected final Keyspace keySpace;

	public AbstractDAO(Keyspace keyspace) {
		super();
		this.keySpace = keyspace;
	}
	
}
