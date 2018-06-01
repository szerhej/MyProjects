package fg.eternity.plan;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import fg.eternity.bo.Field;
import fg.eternity.bo.Figure;
import fg.eternity.bo.FigureVector;
import fg.eternity.util.Utility;

public class IndexMap {

    private HashMap map = new HashMap();

    public IndexMap() {
    }

    public String keyGen(Field[] fields, FigureVector[] fieldOccupation) {
        Arrays.sort(fields);
        String ret = "";

        for (int i = 0; i < fields.length; i++) {
            Field[] neighbour = fields[i].getNeighbouringFields();
            for (int j = 0; j < neighbour.length; j++) {
                if (neighbour[j] == null)
                    ret += "X";
                else if (fieldOccupation[neighbour[j].getPos()] != null
                        && fieldOccupation[neighbour[j].getPos()] != Figure.ETALONMATCHFIGURE)
                    ret += neighbour[j].toString();
                else if (fieldOccupation[neighbour[j].getPos()] != null
                        && fieldOccupation[neighbour[j].getPos()] == Figure.ETALONMATCHFIGURE)
                    ret += "X";
                else
                    ret += "O";
            }
        }

        return ret;
    }

    public String subKeyGen(Field[] fields, FigureVector[] fieldOccupation) {
        Arrays.sort(fields);
        String ret = "";

        for (int i = 0; i < fields.length; i++) {
            Field[] neighbour = fields[i].getNeighbouringFields();
            for (int j = 0; j < neighbour.length; j++) {
                if (neighbour[j] == null)
                    ret += "X";
                else if (fieldOccupation[neighbour[j].getPos()] != null
                        && fieldOccupation[neighbour[j].getPos()] != Figure.ETALONMATCHFIGURE)
                    ret += neighbour[j].toString();
                else if (fieldOccupation[neighbour[j].getPos()] != null
                        && fieldOccupation[neighbour[j].getPos()] == Figure.ETALONMATCHFIGURE)
                    ret += "E";
                else
                    ret += "O";
            }
        }

        return ret;
    }

    public void put(String prepos, Field[] fields, Object object,
            FigureVector[] fieldOccupation) {
        String key = prepos + keyGen(fields, fieldOccupation);
        String subKey = prepos + subKeyGen(fields, fieldOccupation);
        Map m = (Map) map.get(key);
        if (m == null) {
            m = new HashMap();
            map.put(key, m);
        }
        m.put(subKey, object);
    }

    public Object get(String prepos, Field[] fields,
            FigureVector[] fieldOccupation) {
        String key = prepos + keyGen(fields, fieldOccupation);
        String subKey = prepos + subKeyGen(fields, fieldOccupation);
        Map m = (Map) map.get(key);
        if (m == null) {
            return null;
        }
        return m.get(subKey);
    }

    public int shuffle(Random random) {
        int ret = 0;
        for (Iterator iter = map.values().iterator(); iter.hasNext();) {
            Object element = iter.next();

            if (element instanceof Map) {
                for (Iterator iter1 = ((Map) element).values().iterator(); iter1
                        .hasNext();) {
                    Object element1 = iter1.next();
                    if (element1 instanceof Object[]) {
                        ret += shuffle((Object[]) element1, random);
                    }
                }

            }

        }
        return ret;
    }

    private int shuffle(Object[] objects, Random random) {
        int ret = 0;
        if (objects instanceof FigureVector[]) {
            Utility.shuffle(objects, random);
            ret++;
        } else {
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                if (object instanceof Object[]) {
                    ret += shuffle((Object[]) object, random);
                }
            }
        }
        return ret;
    }

}
