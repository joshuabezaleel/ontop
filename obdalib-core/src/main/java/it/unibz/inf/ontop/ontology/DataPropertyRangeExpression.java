package it.unibz.inf.ontop.ontology;


/**
 * Represents the first argument of DataPropertyRange axiom
 * 
 * @author roman
 *
 */

public interface DataPropertyRangeExpression extends DataRangeExpression {

	public DataPropertyExpression getProperty();
	
}
