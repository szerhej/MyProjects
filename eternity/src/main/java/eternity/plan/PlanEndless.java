/*
 * Created on 30-Jan-2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eternity.plan;

import java.util.Random;



import eternity.Field;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gxfulop
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
@Slf4j
public class PlanEndless implements IPlanChainable {

	public String getName() {
		return "ENDLESS";
	}

	public IRun compile(final IndexMap map, int level) {
		final IRun run = next.compile(map, level);
		return new IRun() {

			public void call() {
				Random random = new Random();
				while (true) {
					log.info("Endless Plan: Start New Iteration");
					int shuffle = map.shuffle(random);
					log.info("Shuffle Number:" + shuffle);
					run.call();
				}
			}

		};
	}

	public String toString() {
		return getName();
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

}
