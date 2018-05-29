/*
 * Created on 03-Oct-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eternity.plan;



import eternity.Config;
import eternity.Field;
import eternity.Figure;
import eternity.FieldVector;
import eternity.FigureVector;
import eternity.Utility;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
@Slf4j
class PlanSimple1 implements IPlanSimple {

	private PlanSet planSet;

	private long planCounter = 0;

	private long planTryCounter = 0;

	private boolean stopped;

	private Field field;

	private Field[] fields;

	private FieldVector fieldVector;

	private FigureVector[][] figureMap;

	private final static String PREPOS = "S1";

	public PlanSimple1(Field field, PlanSet planSet) {
		this.field = field;
		fields = new Field[] { field };
		this.planSet = planSet;
	}

	/**
	 * @return Returns the fieldVector.
	 */
	public FieldVector getMatchField() {
		return fieldVector;
	}

	/**
	 * @param fieldVector
	 *            The fieldVector to set.
	 */
	public void setMatchField(FieldVector fieldVector) {
		this.fieldVector = fieldVector;
	}

	public IRun compile(IndexMap indexMap, int level) {
		this.level = level;
		if (indexMap.get(PREPOS, fields, planSet.getFieldOccupation()) == null) {

			FigureVector[][] ret = new FigureVector[Config.getInstance()
					.getPatternCount() + 1][];
			int fieldIndex1 = (fieldVector.getIndex() + 2) % 4;
			Field field = fieldVector.getFieldTo();
			int indexCount = 0;

			for (int i = 0; i < planSet.getFigures().length; i++) {
				if (planSet.getFigures()[i] != null
						&& planSet.getFigureCount()[planSet.getFigures()[i]
								.getId()] > 0)
					for (int j = 0; j < planSet.getFigures()[i].getMaxTurn(); j++) {
						int pattern = planSet.getFigures()[i].getPatterns()[(fieldIndex1
								- j + 4) % 4];
						FigureVector matchFigure = FigureVector.get(planSet
								.getFigures()[i], j);
						if (Figure.checkMatch(field, matchFigure, planSet
								.getFieldOccupation())) {
							ret[pattern] = (FigureVector[]) Utility.addNew(
									ret[pattern], matchFigure);
							indexCount++;
						}
					}
			}

			figureMap = ret;
			log.debug(getName()
					+ " simple index generation is ready.  Element count:"
					+ indexCount);
			indexMap.put(PREPOS, fields, figureMap, planSet
					.getFieldOccupation());
		} else {
			figureMap = (FigureVector[][]) indexMap.get(PREPOS, fields, planSet
					.getFieldOccupation());
			log.debug(getName() + " simple index reused");
		}
		planSet.getFieldOccupation()[field.getPos()] = Figure.ETALONMATCHFIGURE;
		final IRun callable = next.compile(indexMap, level + 1);
		planSet.getFieldOccupation()[field.getPos()] = null;
		return () ->  {
				stopped = false;
				int[] figureCount = planSet.getFigureCount();
				FigureVector[] fieldOccupation = planSet.getFieldOccupation();

				FigureVector[] map = figureMap[fieldOccupation[fieldVector
						.getFieldFrom().getPos()]
						.getFigure()
						.getPattern(
								(4 + fieldVector.getIndex() - fieldOccupation[fieldVector
										.getFieldFrom().getPos()].getIndex()) % 4)];
				if (map != null) {
					for (int iterator = 0; iterator < map.length && !stopped; iterator++) {
						planTryCounter++;
						int figureId = map[iterator].getFigure().getId();
						if (figureCount[figureId] > 0) {
							figureCount[figureId]--;
							fieldOccupation[field.getPos()] = map[iterator];
							planCounter++;
							callable.call();
							figureCount[figureId]++;
						}
					}
					fieldOccupation[field.getPos()] = null;
				}
		};
	}

	public String getName() {
		return "S1-" + level + "-" + field.toStringg();
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

	/**
	 * @param fieldVector
	 *            The fieldVector to set.
	 */
	public void setFieldVector(FieldVector fieldVector) {
		this.fieldVector = fieldVector;
	}
}
