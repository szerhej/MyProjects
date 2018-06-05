/*
 * Created on 26-Nov-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package fg.eternity.plan;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fg.eternity.bo.Config;
import fg.eternity.bo.Field;
import fg.eternity.bo.Figure;
import fg.eternity.bo.FigureVector;
import fg.eternity.spring.FieldSet;
import fg.eternity.spring.FigureSet;
import fg.eternity.util.BoardParser;
import fg.eternity.validator.Validator;
import lombok.Getter;
import lombok.Setter;

/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PlanSet implements Serializable {

	@Getter
	private int[] figureCount;

	@Getter
	private FigureVector[] fieldOccupation;

	@Setter @Getter
	private Field[] fields;

	@Setter @Getter
	private Figure[] figures;

	@Setter @Getter
	private Config config;

	private String defaults;

	public void init() {
		figureCount = new int[config.getDimMax()];

		fieldOccupation = new FigureVector[config.getDimMax()];

		for (int i = 0; i < figures.length; i++) {
			figureCount[figures[i].getId()] = figures[i].getFigureCount();
		}
		for (int i = 0; i < fields.length; i++) {
			if (fields[i] != null)
				fieldOccupation[fields[i].getPos()] = fields[i].getFigureVector();
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

	public void setFieldSet(FieldSet fieldSet) {
		this.fields = fieldSet.getFields();
	}

	public void setFigureSet(FigureSet figureSet) {
		this.figures = figureSet.getFigures();
	}

	public void setDefaults(String defaults) {
		this.defaults = defaults;
	}

	private void initDefaults() {
		Matcher matcher = BoardParser.FIGURE_PARSER.matcher(defaults);
		while (matcher.find()) {
			String fieldName = matcher.group(1);
			String figureName = "<"+matcher.group(3)+">";
			String figureIndex = matcher.group(4);
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

			Validator.notNull(figure,"Figure does not exist!!! {}",figureName);
			Validator.isFalse(figure.getFigureCount() == 0,"Figure is already reserved!!! {}",figureName);

			field.setFigureVector(FigureVector.get(figure, Integer.valueOf(figureIndex)
					.intValue()));
			figureCount[figure.getId()]--;
			fieldOccupation[field.getPos()] = FigureVector.get(figure,
					Integer.valueOf(figureIndex).intValue());

		}

	}

}
