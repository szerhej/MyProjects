/*
 * Created on 07-Nov-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package fg.eternity.plan;

import fg.eternity.bo.Field;
import fg.eternity.bo.FieldVector;
import fg.eternity.bo.Figure;
import fg.eternity.validator.Validator;
import lombok.Getter;
import lombok.Setter;

/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PlanSimpleSkeleton implements IPlanSimple,ILabelled {
	private IPlanSimple plan = null;

	private PlanSet planSet;

	@Getter
	private Field field;

	private IPlanSimple[] planSimples = null;
	@Getter @Setter
	private IPlan next;

	public PlanSimpleSkeleton(Field field, PlanSet planSet) {
		this.field = field;
		this.planSet = planSet;
		planSimples = new IPlanSimple[] { new PlanSimple0(field, planSet),
				new PlanSimple1(field, planSet),
				new PlanSimple2(field, planSet) };
	}

	public IRun compile(IndexMap indexMap, int level) {
		Validator.isNull(planSet.getFieldOccupation()[field.getPos()],"The field is occupied!!!!" + field);

		Field[] neighbouringFields = field.getNeighbouringFields();
		FieldVector[] fieldVectors = new FieldVector[2];
		int fieldVectorsIndex = 0;

		for (int i = 0; i < 4; i++) {
			if (neighbouringFields[i] != null
					&& planSet.getFieldOccupation()[neighbouringFields[i]
							.getPos()] == Figure.ETALONMATCHFIGURE) {
				Validator.isFalse(fieldVectorsIndex >= fieldVectors.length,field.toStringg() + " Dependency more than " + fieldVectors.length);
				fieldVectors[fieldVectorsIndex++] = neighbouringFields[i]
						.getMatchFields()[(i + 2) % 4];
			}
		}

		Validator.isTrue(fieldVectorsIndex<3,"{} fieldVectorsIndex:{} ",field.toStringg(), fieldVectors.length);

		plan = planSimples[fieldVectorsIndex];
		if (fieldVectorsIndex == 2) {
			PlanSimple2 planSimple2 = (PlanSimple2) planSimples[2];
			planSimple2.setFieldVector1(fieldVectors[0]);
			planSimple2.setFieldVector2(fieldVectors[1]);
			planSimple2.setNext(next);
		} else if (fieldVectorsIndex == 1) {
			PlanSimple1 planSimple1 = (PlanSimple1) planSimples[1];
			planSimple1.setFieldVector(fieldVectors[0]);
			planSimple1.setNext(next);
		} else {
			PlanSimple0 planSimple0 = (PlanSimple0) planSimples[0];
			planSimple0.setNext(next);
		}
		return plan.compile(indexMap, level);
	}

	public String getName() {
		return plan.getName();// + id;
	}

	public long getPlanCounter() {
		return plan.getPlanCounter();
	}

	public long getTryCounter() {
		return plan.getTryCounter();
	}

	public void stop() {
		plan.stop();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see IPlanSimple#getLevel()
	 */
	public int getLevel() {
		return plan.getLevel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ILabelled#getLabel()
	 */
	public String getLabel() {
		return getName() + "[" + getPlanCounter() + "/" + getTryCounter() + "]";
	}
}
