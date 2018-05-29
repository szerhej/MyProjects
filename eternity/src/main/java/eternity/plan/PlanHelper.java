/*
 * Created on 24-Feb-2010
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eternity.plan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PlanHelper {

	private static IPlan next(IPlan plan) {
		if (plan instanceof IPlanChained) {
			IPlanChained chained = (IPlanChained) plan;
			return chained.getNext();
		}
		return null;
	}

	public static ILabelled[] getAllLabelled(IPlan first) {
		List list = new ArrayList();
		IPlan plan = first;
		if (first == null)
			throw new RuntimeException();
		while (plan != null) {
			if (plan instanceof ILabelled)
				list.add(plan);
			else if (plan instanceof PlanFork) {
				PlanFork fork = (PlanFork) plan;
				IPlan[] plans = fork.getFork();
				for (int i = 0; i < plans.length; i++) {
					IPlan plan2 = plans[i];
					final ILabelled[] labelleds = getAllLabelled(plan2);
					list.add(new ILabelled() {
						public String getLabel() {
							String rootDescription = "";
							for (int i = 0; i < labelleds.length; i++) {
								ILabelled labelled = labelleds[i];
								rootDescription += rootDescription.length() == 0 ? "FORK==="
										: ",";
								rootDescription += labelled.getLabel();
							}
							return "\n\t\t\t" + rootDescription;
						}
					});
				}
			}
			plan = next(plan);
		}
		return (ILabelled[]) list.toArray(new ILabelled[list.size()]);
	}

	public static void saveAllCounts(IPlan first, long counts[]) {
		if (first == null)
			throw new RuntimeException();
		IPlan plan = first;
		while (plan != null) {
			if (plan instanceof IPlanSimple) {
				IPlanSimple planSimple = (IPlanSimple) plan;
				counts[planSimple.getLevel()] += planSimple.getPlanCounter();
			} else if (plan instanceof PlanFork) {
				PlanFork fork = (PlanFork) plan;
				IPlan[] plans = fork.getFork();
				for (int i = 0; i < plans.length; i++) {
					IPlan plan2 = plans[i];
					saveAllCounts(plan2, counts);
				}
			}
			plan = next(plan);
		}
	}

	public static void saveTryCounts(IPlan first, long counts[]) {
		if (first == null)
			throw new RuntimeException();
		IPlan plan = first;
		while (plan != null) {
			if (plan instanceof IPlanSimple) {
				IPlanSimple planSimple = (IPlanSimple) plan;
				counts[planSimple.getLevel()] += planSimple.getTryCounter();
			} else if (plan instanceof PlanFork) {
				PlanFork fork = (PlanFork) plan;
				IPlan[] plans = fork.getFork();
				for (int i = 0; i < plans.length; i++) {
					IPlan plan2 = plans[i];
					saveTryCounts(plan2, counts);
				}
			}
			plan = next(plan);
		}
	}

}
