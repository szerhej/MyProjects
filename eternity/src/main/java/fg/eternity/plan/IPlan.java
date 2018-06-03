/*
 * Created on 17-Feb-2010
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package fg.eternity.plan;

import java.io.Serializable;

/**
 * @author gxfulop
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IPlan extends Serializable {
	IRun compile(IndexMap indexMap,int level);
	void stop();
}
