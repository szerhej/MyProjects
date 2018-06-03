/*
 * Created on 26-Sep-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package fg.eternity.bo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class FieldVector implements Serializable {

    @Setter @Getter
    private Field fieldFrom;

    @Setter @Getter
    private Field fieldTo;

    //regards fieldFrom
    @Setter @Getter
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


    @Override
    public String toString() {
        return fieldFrom.toString() + "-->" + fieldTo.toString() + "^" + index;
    }

}
