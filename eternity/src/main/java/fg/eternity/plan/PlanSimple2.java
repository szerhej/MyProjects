/*
 * Created on 03-Oct-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package fg.eternity.plan;



import fg.eternity.Field;
import fg.eternity.Figure;
import fg.eternity.FieldVector;
import fg.eternity.FigureVector;
import fg.eternity.Utility;
import fg.eternity.validator.Validator;
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
class PlanSimple2 implements IPlanSimple {

	private final static String PREPOS = "S2";
	@Getter
	private PlanSet planSet;
	@Getter
	private long planCounter = 0;
	@Getter
	private long tryCounter = 0;
	private boolean stopped;
	@Getter
	private Field field;
	@Getter
	private Field[] fields;
	@Setter @Getter
	private FieldVector fieldVector1;
	@Getter @Setter
	private FieldVector fieldVector2;
	private FigureVector[][][] figureMap;
	@Getter
	private int level;
	@Getter @Setter
	private IPlan next;

	public PlanSimple2(Field field, PlanSet planSet) {
		this.field = field;
		fields = new Field[] { field };
		this.planSet = planSet;
	}

	public IRun compile(IndexMap indexMap, int level) {
		this.level = level;
		if (indexMap.get(PREPOS, fields, planSet.getFieldOccupation()) == null) {

			FigureVector[] empty = new FigureVector[0];
			FigureVector[][][] ret = new FigureVector[planSet.getConfig()
					.getPatternCount()][planSet.getConfig().getPatternCount()][];
			for (int i = 0; i < planSet.getConfig().getPatternCount(); i++) {
				for (int j = 0; j < planSet.getConfig().getPatternCount(); j++) {
					ret[j][i] = empty;
				}

			}
			int fieldIndex1 = (fieldVector1.getIndex() + 2) % 4;
			int fieldIndex2 = (fieldVector2.getIndex() + 2) % 4;
			int indexDiff = (fieldIndex2 - fieldIndex1 + 4) % 4;
			Field field = fieldVector1.getFieldTo();

			Validator.equals(field,fieldVector2.getFieldTo(),"'to' fields must to be match!!!");
			Validator.isFalse(indexDiff == 0,"Index must be different!!!");

			int indexCount = 0;

			for (int i = 0; i < planSet.getFigures().length; i++) {
				if (planSet.getFigures()[i] != null
						&& planSet.getFigureCount()[planSet.getFigures()[i]
								.getId()] > 0)
					for (int j = 0; j < planSet.getFigures()[i].getMaxTurn(); j++) {
						if (planSet.getFigures()[i].getPatterns()[(fieldIndex1
								- j + 4) % 4] > 0
								&& planSet.getFigures()[i].getPatterns()[(fieldIndex1
										- j + 4 + indexDiff) % 4] > 0) {
							FigureVector matchFigure = FigureVector.get(planSet
									.getFigures()[i], j);
							if (Figure.checkMatch(field, matchFigure, planSet
									.getFieldOccupation())) {
								int retIndexA = planSet.getFigures()[i]
										.getPatterns()[(fieldIndex1 - j + 4) % 4] - 1;
								int retIndexB = planSet.getFigures()[i]
										.getPatterns()[(fieldIndex1 - j + 4 + indexDiff) % 4] - 1;
								ret[retIndexA][retIndexB] = (FigureVector[]) Utility
										.addNew(ret[retIndexA][retIndexB],
												matchFigure);
								indexCount++;
							}
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
			figureMap = (FigureVector[][][]) indexMap.get(PREPOS, fields,
					planSet.getFieldOccupation());
			log.debug(getName() + " simple index reused");
		}
		planSet.getFieldOccupation()[field.getPos()] = Figure.ETALONMATCHFIGURE;
		final IRun callable = next.compile(indexMap, level + 1);
		planSet.getFieldOccupation()[field.getPos()] = null;
		return new IRun() {
			final int[] figureCount = planSet.getFigureCount();
			final FigureVector[] fieldOccupation = planSet.getFieldOccupation();
			final int pos1 = fieldVector1.getFieldFrom().getPos();
			final int pos2 = fieldVector2.getFieldFrom().getPos();
			final int idx1 = fieldVector1.getIndex();
			final int idx2 = fieldVector2.getIndex();

			public void call() {

				stopped = false;

				int index1 = fieldOccupation[pos1].getFigure().getPattern(
						(4 + idx1 - fieldOccupation[pos1].getIndex()) % 4) - 1;
				int index2 = fieldOccupation[pos2].getFigure().getPattern(
						(4 + idx2 - fieldOccupation[pos2].getIndex()) % 4) - 1;
				FigureVector[] map = figureMap[index1][index2];
				int iterator = 0;
				for (; iterator < map.length && !stopped; iterator++) {
					int figureId = map[iterator].getFigure().getId();
					if (figureCount[figureId] > 0) {
						figureCount[figureId]--;
						fieldOccupation[field.getPos()] = map[iterator];
						planCounter++;
						callable.call();
						figureCount[figureId]++;
					}
				}
				tryCounter +=iterator;
				fieldOccupation[field.getPos()] = null;
			}
		};
	}

	public String getName() {
		return "S2-" + level + "-" + field.toStringg();
	}

	public void stop() {
		stopped = true;
	}


}
