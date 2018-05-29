/*
 * Created on 26-Sep-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eternity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class FigureVector {
    private Figure figure;

    private int index;

    /**
     *  
     */
    private FigureVector(Figure figure, int index) {
        this.index = index;
        this.figure = figure;
        if (this.figure == null)
            throw new RuntimeException("figure must not be null!!!");
        if (index >= figure.getMaxTurn())
            throw new RuntimeException("figure is overturned!!!");
    }

    private static List list = new ArrayList();

    public static FigureVector get(Figure figure, int index) {
        FigureVector vector = new FigureVector(figure, index);
        if (list.contains(vector))
            return (FigureVector) list.get(list.indexOf(vector));
        list.add(vector);
        return vector;

    }

    /**
     * @return Returns the figure.
     */
    public Figure getFigure() {
        return figure;
    }

    /**
     * @param figure
     *            The figure to set.
     */
    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    /**
     * @return Returns the index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index
     *            The index to set.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        FigureVector match = (FigureVector) obj;
        return match.figure == this.figure && match.index == this.index;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return figure.toString() + "^" + index;
    }

    public String toIdString() {
        return Integer.toString(figure.getId());
    }

    private static void turn4(FigureVector[] vectors) {
        if (vectors.length != 4)
            throw new RuntimeException("Wrong Data!!!");
        FigureVector stack = vectors[0];
        vectors[0] = vectors[1];
        vectors[1] = vectors[3];
        vectors[3] = vectors[2];
        vectors[2] = stack;
        for (int i = 0; i < vectors.length; i++) {
            vectors[i] = get(vectors[i].getFigure(),
                    (vectors[i].getIndex() + 1) % 4);
        }
    }

    public static int turn4ToTheSmallest(FigureVector[] vectors) {
        if (vectors.length != 4)
            throw new RuntimeException("Wrong Data!!!");
        int turns = 0;

        int smallestId = 9999999;
        for (int i = 0; i < vectors.length; i++) {
            FigureVector vector = vectors[i];
            if (vector.getFigure().getId() < smallestId)
                smallestId = vector.getFigure().getId();
        }
        while (smallestId != vectors[0].getFigure().getId()) {
            turn4(vectors);
            turns++;
        }
        return turns;
    }

}
