/*
 * Created on 22-Oct-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eternity.plan;


import lombok.extern.slf4j.Slf4j;

/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
@Slf4j
public class Bingo extends PlanLogger {

	private long count = 0;

	/**
	 * @param field
	 */
	public Bingo() {
	}

	public String getName() {
		return "BINGO";
	}

	public String toString() {
		return "bingo[" + count + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eternity.plan.PlanLogger#compile(eternity.plan.IndexMap, int)
	 */
	public IRun compile(IndexMap indexMap, int level) {
		return new IRun() {

			public void call() {
				logPlan();
			}

		};
	}

}
