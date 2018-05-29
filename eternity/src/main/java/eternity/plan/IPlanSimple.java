/*
 * Created on 24-Feb-2010
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eternity.plan;

import eternity.Field;

/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public interface IPlanSimple extends IPlanChainable {

	long getPlanCounter();

	long getTryCounter();

	Field getField();

	int getLevel();

	String getName();
}
