/*
 * Created on 03-Oct-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eternity.plan;

import java.util.ArrayList;
import java.util.Collection;



import eternity.Field;
import eternity.Figure;
import eternity.FigureVector;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
@Slf4j
class PlanSimple0 implements IPlanSimple {
	private PlanSet planSet;

	private long planCounter = 0;

	private long planTryCounter = 0;

	private boolean stopped;

	private Field field;

	private Field[] fields;

	private FigureVector[] figureMap;

	private final static String PREPOS = "S0";

	public PlanSimple0(Field field, PlanSet planSet) {
		this.field = field;
		fields = new Field[] { field };
		this.planSet = planSet;
	}

	public IRun compile(final IndexMap indexMap, int level) {
		this.level = level;
		if (indexMap.get(PREPOS, fields, planSet.getFieldOccupation()) == null) {

			Collection ret = new ArrayList();
			
			int indexCount = 0;

			for (int i = 0; i < planSet.getFigures().length; i++) {
				if (planSet.getFigures()[i] != null
						&& planSet.getFigureCount()[planSet.getFigures()[i]
								.getId()] > 0)
					for (int j = 0; j < planSet.getFigures()[i].getMaxTurn(); j++) {
						FigureVector matchFigure = FigureVector.get(planSet
								.getFigures()[i], j);
						if (Figure.checkMatch(field, matchFigure, planSet
								.getFieldOccupation())) {
							ret.add(matchFigure);
							indexCount++;
						}
					}
			}
			figureMap = (FigureVector[]) ret.toArray(new FigureVector[0]);
			log.debug(getName()
					+ " simple index generation is ready.  Element count:"
					+ indexCount);
			indexMap.put(PREPOS, fields, figureMap, planSet
					.getFieldOccupation());
		} else {
			figureMap = (FigureVector[]) indexMap.get(PREPOS, fields, planSet
					.getFieldOccupation());
			log.debug(getName() + " simple index reused");
		}
		planSet.getFieldOccupation()[field.getPos()] = Figure.ETALONMATCHFIGURE;
		final IRun callable = next.compile(indexMap, level + 1);
		planSet.getFieldOccupation()[field.getPos()] = null;
		return () -> {
				stopped = false;
				int[] figureCount = planSet.getFigureCount();
				FigureVector[] fieldOccupation = planSet.getFieldOccupation();

				for (int iterator = 0; iterator < figureMap.length && !stopped; iterator++) {
					planTryCounter++;
					Figure figure = figureMap[iterator].getFigure();
					if (figureCount[figure.getId()] > 0) {
						figureCount[figure.getId()]--;
						fieldOccupation[field.getPos()] = figureMap[iterator];
						planCounter++;
						callable.call();
						figureCount[figure.getId()]++;
					}
				}
				fieldOccupation[field.getPos()] = null;
		};
	}

	public String getName() {
		return "S0-" + level + "-" + field.toStringg();
	}

	public long getPlanCounter() {
		return planCounter;
	}

	public long getTryCounter() {
		return planTryCounter;
	}

	public PlanSet getPlanSet() {
		return planSet;
	}

	public void setPlanSet(PlanSet planSet) {
		this.planSet = planSet;

	}

	public Field[] getFields() {
		return fields;
	}

	private int level;

	private IPlan next;

	public IPlan getNext() {
		return next;
	}

	public void setNext(IPlan next) {
		this.next = next;
	}

	public void stop() {
		stopped = true;
	}

	/**
	 * @return Returns the level.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            The level to set.
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eternity.plan.IPlanSimple#getField()
	 */
	public Field getField() {
		return field;
	}
}
