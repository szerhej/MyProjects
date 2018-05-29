/*
 * Created on 26-Sep-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eternity;

/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class FieldVector {
    private Field fieldFrom;

    private Field fieldTo;

    //regards fieldFrom
    private int index;

    /**
     *  
     */
    public FieldVector(Field fieldFrom, Field fieldTo, int index) {
        this.index = index;
        this.fieldFrom = fieldFrom;
        this.fieldTo = fieldTo;

        if (this.fieldFrom == null)
            throw new RuntimeException("fieldFrom must not be null!!!");
        if (this.fieldTo == null)
            throw new RuntimeException("fieldTo must not be null!!!");

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

    /**
     * @return Returns the fieldFrom.
     */
    public Field getFieldFrom() {
        return fieldFrom;
    }

    /**
     * @param fieldFrom
     *            The fieldFrom to set.
     */
    public void setFieldFrom(Field fieldFrom) {
        this.fieldFrom = fieldFrom;
    }

    /**
     * @return Returns the fieldTo.
     */
    public Field getFieldTo() {
        return fieldTo;
    }

    /**
     * @param fieldTo
     *            The fieldTo to set.
     */
    public void setFieldTo(Field fieldTo) {
        this.fieldTo = fieldTo;
    }

    public String toString() {
        return fieldFrom.toString() + "-->" + fieldTo.toString() + "^" + index;
    }

}
