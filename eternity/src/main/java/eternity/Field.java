/*
 * Created on 24-Sep-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eternity;

import org.apache.commons.beanutils.PropertyUtils;

import eternity.plan.IPlan;
import eternity.plan.IPlanSimple;

/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Field implements Comparable {

	public final static Field[] NULLARRAY = {};

	private FigureVector figureVector;

	private FieldVector[] fieldVectors = new FieldVector[4];

	private Field[] neighbouringFields = new Field[4];

	private int pos;

	private Field(int pos) {
		this.pos = pos;
	}

	public static Field[] generateAllFields(Config config) {
		Field[] allFields = null;
		int dimX = config.getDimX();
		int dimY = config.getDimY();
		int dimAbsX = config.getDimAbsX();
		int dimMax = config.getDimMax();
		if (allFields != null)
			return allFields;
		allFields = new Field[dimAbsX * dimY];

		int[] st = { dimAbsX, 1, -dimAbsX, -1 };// The order is important

		for (int y = 0; y < dimY; y++) {
			for (int x = 0; x < dimX; x++) {
				int z = y * dimAbsX + x;
				if (z % dimAbsX < dimX)
					allFields[z] = new Field(z);
			}
		}

		for (int i = 0; i < allFields.length; i++) {
			if (allFields[i] != null) {
				for (int n = 0; n < st.length; n++) {
					int a = allFields[i].pos + st[n];
					if (a >= 0 && a < dimMax && allFields[a] != null) {
						allFields[i].fieldVectors[n] = new FieldVector(
								allFields[i], allFields[a], n);
						allFields[i].neighbouringFields[n] = allFields[a];
					} else {
						allFields[i].fieldVectors[n] = null;
						allFields[i].neighbouringFields[n] = null;
					}
				}
			}
		}
		return allFields;
	}

	/**
	 * @return Returns the figure.
	 */
	public FigureVector getVector() {
		return figureVector;
	}

	/**
	 *
	 * @param figureVector The figure to set.
	 */
	public void setVector(FigureVector figureVector) {
		this.figureVector = figureVector;
	}

	public Field getField(int index) {
		return neighbouringFields[index];
	}

	/**
	 * @return Returns the neighbouringFields.
	 */
	public Field[] getNeighbouringFields() {
		return neighbouringFields;
	}

	/**
	 * @return Returns the fieldVectors.
	 */
	public FieldVector[] getMatchFields() {
		return fieldVectors;
	}

	public static String[] COLUMNS = { "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"W", "X", "Y", "Z" };

	public String toString(FigureVector[] fieldOccupation) {
		int dimAbsX = Config.getInstance().getDimAbsX();
		String ret = COLUMNS[pos % dimAbsX] + (pos / dimAbsX + 1);
		if (fieldOccupation[pos] != null) {
			ret += fieldOccupation[pos].toString();
		}
		return ret;
	}

	public String toStringg() {
		int dimAbsX = Config.getInstance().getDimAbsX();
		String ret = COLUMNS[pos % dimAbsX] + (pos / dimAbsX + 1);
		return ret;
	}

	public String toString() {
		return toStringg();
	}

	public static String toString(Field[] fields, FigureVector[] fieldOccupation) {
		String pr = null;
		for (int i = 0; i < fields.length; i++) {
			if (fields[i] != null && fieldOccupation[fields[i].pos] != null) {
				if (pr == null)
					pr = fields[i].toString(fieldOccupation);
				else
					pr += "," + fields[i].toString(fieldOccupation);
			}
		}
		return pr;

	}

	public static String toStringByOrder(IPlan first,
			FigureVector[] fieldOccupation) {
		String pr = null;
		try {
			for (IPlan plan = first; plan != null
					&& PropertyUtils.isReadable(plan, "next"); plan = (IPlan) PropertyUtils
					.getSimpleProperty(plan, "next")) {
				if (plan instanceof IPlanSimple) {
					IPlanSimple planSimple=(IPlanSimple)plan;
					Field field = planSimple.getField();

					if (field != null && fieldOccupation[field.pos] != null) {
						if (pr == null)
							pr = field.toString(fieldOccupation);
						else
							pr += "," + field.toString(fieldOccupation);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return pr;

	}

	public static String toString(Field[] fields) {
		FigureVector[] fieldOccupation = new FigureVector[fields.length];
		for (int i = 0; i < fieldOccupation.length; i++) {
			if (fields[i] != null)
				fieldOccupation[i] = fields[i].getVector();
		}
		return toString(fields, fieldOccupation);
	}

	public static Field getField(String fieldStr, Field[] fields) {
		for (int i = 0; i < fields.length; i++) {
			if (fields[i] != null && fields[i].toStringg().equals(fieldStr))
				return fields[i];
		}
		throw new RuntimeException("Not existing field!!!" + fieldStr);
	}

	public int compareTo(Object arg0) {
		int thisVal = this.pos;
		int anotherVal = ((Field) arg0).pos;
		return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
	}

	/**
	 * @return Returns the pos.
	 */
	public int getPos() {
		return pos;
	}
}
