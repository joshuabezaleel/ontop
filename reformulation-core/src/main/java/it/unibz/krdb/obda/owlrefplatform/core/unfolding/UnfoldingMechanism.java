package it.unibz.krdb.obda.owlrefplatform.core.unfolding;

import it.unibz.krdb.obda.model.DatalogProgram;
import it.unibz.krdb.obda.model.OBDAException;
import it.unibz.krdb.obda.model.Predicate;

import java.io.Serializable;

/**
 * This interface should be implemented by any class which implements an
 * unfolding Mechanism which should be integrated into a technique wrapper
 * 
 * @author Manfred Gerstgrasser
 * 
 */

public interface UnfoldingMechanism extends Serializable {

	/**
	 * unfolds the the given datalog program
	 * 
	 * @param query
	 *            the query
	 * @return the unfolded query
	 * @throws Exception
	 */
	public DatalogProgram unfold(DatalogProgram query, String targetPredicate)
			throws OBDAException;

	
}