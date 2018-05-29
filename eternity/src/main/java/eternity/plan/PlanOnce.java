/*
 * Created on 30-Jan-2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eternity.plan;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PlanOnce implements IPlanChainable {

	public IRun compile(IndexMap indexMap, final int level) {

		final IPlan thisPlan = this;
		return new IRun() {

			public void call() {
				IRun run = next.compile(new IndexMap(), level);
				run.call();
				try {
					for (IPlan plan = first; plan != thisPlan; plan = (IPlan) PropertyUtils
							.getSimpleProperty(plan, "next")) {
						plan.stop();
					}
				} catch (Exception e) {
					throw new RuntimeException();
				}
			}

		};
	}

	private IPlan next;

	public IPlan getNext() {
		return next;
	}

	public void setNext(IPlan next) {
		this.next = next;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eternity.plan.Plan#stop()
	 */
	public void stop() {
	}

	private IPlan first;

	/**
	 * @return Returns the first.
	 */
	public IPlan getFirst() {
		return first;
	}

	/**
	 * @param first
	 *            The first to set.
	 */
	public void setFirst(IPlan first) {
		this.first = first;
	}
}
