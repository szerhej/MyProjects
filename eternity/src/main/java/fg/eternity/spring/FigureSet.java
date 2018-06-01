/*
 * Created on 17-Feb-2010
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package fg.eternity.spring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;



import fg.eternity.bo.Figure;

/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class FigureSet {
	private Map figureMap;

	private Figure[] figures;
	
	public void setFigureMap(Map figureMap) {
		this.figureMap = figureMap;
	}

	public Figure[] getFigures() {
		return figures == null ? figures = loadFigures() : figures;
	}

	private Figure[] loadFigures() {
		final Random random = new Random();
		List ret = new ArrayList();
		for (Iterator iter = figureMap.keySet().iterator(); iter.hasNext();) {
			Integer id = (Integer) iter.next();
			Figure figure = Figure.parse(id.intValue(), (String) figureMap
					.get(id));
			ret.add(figure);
		}

		int u = 0;
		Figure[] figures = (Figure[]) ret.toArray(new Figure[0]);

		// Do not mix them
		if (false)
			for (int i = 0; i < figures.length; i++) {
				while ((u = random.nextInt(figures.length)) == i)
					;
				Figure temp = figures[u];
				figures[u] = figures[i];
				figures[i] = temp;
			}
		
		figures=Figure.removeDuplications(figures);
		Figure.printPatterns(figures);
		
		return figures;
	}

}
