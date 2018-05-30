/*
 * Created on 17-Feb-2010
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eternity.spring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eternity.Field;
import eternity.plan.IPlan;
import eternity.plan.IPlanChainable;
import eternity.plan.IPlanChained;
import eternity.plan.IPlanSimple;
import eternity.plan.IRun;
import eternity.plan.IndexMap;
import eternity.plan.PlanSet;
import eternity.plan.PlanSimpleSkeleton;
import lombok.Getter;
import lombok.Setter;

/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class RouteDefinition implements IPlanChained {

	private Collection route;

	@Getter @Setter
	private PlanSet planSet;

	private List<IPlan> elementList = null;

	public void setRoute(Collection route) {
		elementList = null;
		this.route = route;
	}

	private static Pattern patternSimple = Pattern
			.compile("\\{([A-Z]\\d{1,2})\\}");

	public Collection generatePlan(String route, PlanSet planSet) {
		String[] planDescs = route.split("\\s*,\\s*");
		Collection<IPlan> buff = new ArrayList<>();
		IPlan plan = null;

		for (int ix = 0; ix < planDescs.length; ix++) {

			Matcher matcherSimple = patternSimple.matcher(planDescs[ix]);

			if (matcherSimple.find()) {
				Field field = Field.getField(matcherSimple.group(1), planSet
						.getFields());

				plan = new PlanSimpleSkeleton(field, planSet);

			} else
				throw new RuntimeException("Unparseable route!!!"
						+ planDescs[ix]+"----"+route);

			buff.add(plan);
		}

		for (IPlan element:buff) {
			if (element instanceof IPlanSimple)
				try {
					IPlanSimple planSimple = (IPlanSimple) element;

					Field field = planSimple.getField();
					planSet.getFieldOccupation()[field.getPos()] = null;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
		}

		return buff;
	}

	public IRun compile(IndexMap indexMap, int level) {
		if (elementList == null) {
			elementList = new ArrayList<>();
			for (Object element:route) {
				if (element instanceof IPlan)
					elementList.add((IPlan)element);
				else if (element instanceof String)
					elementList.addAll(generatePlan((String) element, planSet));
				else
					throw new RuntimeException();
			}
			IPlanChainable prev = null;
			for (Iterator iter = elementList.iterator(); iter.hasNext();) {
				IPlan element = (IPlan) iter.next();
				if (prev != null)
					prev.setNext(element);
				if (iter.hasNext())
					prev = (IPlanChainable) element;
				else
					prev = null;
			}
		}
		return elementList.get(0).compile(indexMap, level);
	}


	public void stop() {
		for (Iterator iter = elementList.iterator(); iter.hasNext();) {
			IPlan element = (IPlan) iter.next();
			element.stop();
		}
	}

	public IPlan getNext() {
		return (IPlan) elementList.get(0);
	}
}
