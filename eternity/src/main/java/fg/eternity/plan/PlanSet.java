/*
 * Created on 26-Nov-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package fg.eternity.plan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fg.eternity.bo.Config;
import fg.eternity.bo.Field;
import fg.eternity.bo.Figure;
import fg.eternity.bo.FigureVector;
import fg.eternity.spring.FieldSet;
import fg.eternity.spring.FigureSet;

/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PlanSet {
	private int[] figureCount;

	private FigureVector[] fieldOccupation;

	private Field[] fields;

	private Figure[] figures;

	private Config config;

	private String defaults;

	/**
	 * @return Returns the fields.
	 */
	public Field[] getFields() {
		return fields;
	}

	/**
	 * @param fields
	 *            The fields to set.
	 */
	public void setFields(Field[] fields) {
		this.fields = fields;
	}

	/**
	 * @return Returns the figures.
	 */
	public Figure[] getFigures() {
		return figures;
	}

	/**
	 * @param figures
	 *            The figures to set.
	 */
	public void setFigures(Figure[] figures) {
		this.figures = figures;
	}

	/**
	 * @return Returns the figureCount.
	 */
	public int[] getFigureCount() {
		return figureCount;
	}

	/**
	 * @return Returns the fieldOccupation.
	 */
	public FigureVector[] getFieldOccupation() {
		return fieldOccupation;
	}

	public void init() {
		figureCount = new int[config.getDimMax()];

		fieldOccupation = new FigureVector[config.getDimMax()];

		for (int i = 0; i < figures.length; i++) {
			figureCount[figures[i].getId()] = figures[i].getFigureCount();
		}
		for (int i = 0; i < fields.length; i++) {
			if (fields[i] != null)
				fieldOccupation[fields[i].getPos()] = fields[i].getVector();
		}

		initDefaults();
	}

	protected Object clone() {
		PlanSet planSet = new PlanSet();
		planSet.figureCount = figureCount;
		planSet.fieldOccupation = fieldOccupation;
		planSet.fields = fields;
		planSet.figures = figures;
		return planSet;
	}

	/**
	 * @return Returns the config.
	 */
	public Config getConfig() {
		return config;
	}

	/**
	 * @param config
	 *            The config to set.
	 */
	public void setConfig(Config config) {
		this.config = config;
	}

	public void setFieldSet(FieldSet fieldSet) {
		this.fields = fieldSet.getFields();
	}

	public void setFigureSet(FigureSet figureSet) {
		this.figures = figureSet.getFigures();
	}

	private static Pattern pattern = Pattern
			.compile("([A-Z]\\d{1,2})(\\<[a-z][a-z][a-z][a-z]\\>)\\^([0-3])");

	public void setDefaults(String defaults) {
		this.defaults = defaults;
	}

	private void initDefaults() {
		Matcher matcher = pattern.matcher(defaults);
		while (matcher.find()) {
			String fieldName = matcher.group(1);
			String figureName = matcher.group(2);
			String figureIndex = matcher.group(3);
			Field field = null;
			Figure figure = null;
			field = Field.getField(fieldName.toUpperCase(), fields);
			for (int i = 0; i < figures.length; i++) {
				if (figures[i] != null
						&& figures[i].toString().toLowerCase().indexOf(
								figureName.toLowerCase()) >= 0) {
					figure = figures[i];
				}
			}

			if (figure == null)
				throw new RuntimeException("Figure does not exist!!! "
						+ figureName);

			if (figure.getFigureCount() == 0) {
				throw new RuntimeException("Figure is already reserved!!!"
						+ figureName);
			}

			field.setVector(FigureVector.get(figure, new Integer(figureIndex)
					.intValue()));
			figureCount[figure.getId()]--;
			fieldOccupation[field.getPos()] = FigureVector.get(figure,
					new Integer(figureIndex).intValue());

		}

	}

}
