/*
 * Created on 21-Oct-2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package fg.eternity;

import fg.eternity.plan.IPlan;
import fg.eternity.plan.IndexMap;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Main {

	public static void main(String[] args) {
		IndexMap indexMap = new IndexMap();
		ClassPathXmlApplicationContext rootContext = new ClassPathXmlApplicationContext(
				new String[] { "classpath:fg/eternity/config/eternity-context.xml" });
		((IPlan) rootContext.getBean("mainRoute")).compile(indexMap,1).call();
	}
}
