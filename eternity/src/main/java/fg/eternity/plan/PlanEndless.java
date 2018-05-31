/*
 * Created on 30-Jan-2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package fg.eternity.plan;

import java.util.Random;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gxfulop
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
@Slf4j
public class PlanEndless implements IPlanChainable {

	@Setter @Getter
	private IPlan next;

	public String getName() {
		return "ENDLESS";
	}

	public IRun compile(final IndexMap map, int level) {
		final IRun run = next.compile(map, level);
		return () ->  {
				Random random = new Random();
				while (true) {
					log.info("Endless Plan: Start New Iteration");
					int shuffle = map.shuffle(random);
					log.info("Shuffle Number:" + shuffle);
					run.call();
				}
		};
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public void stop() {
	}

}
