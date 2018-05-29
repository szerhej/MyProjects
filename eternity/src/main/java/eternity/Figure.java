/*
 * Created on 26-Sep-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eternity;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
@Slf4j
public class Figure {

    public final static Figure ETALONFIGURE = new Figure();

    public final static FigureVector ETALONMATCHFIGURE = FigureVector.get(
            ETALONFIGURE, 0);

    private int[] patterns = new int[4];

    private int maxTurn = 4;

    private int count = 1;

    private int id;

    private int[] ids;

    private int idIndex = 0;

    public Figure() {
    }

    public Figure(int id, int p1, int p2, int p3, int p4) {
        patterns[0] = p1;
        patterns[1] = p2;
        patterns[2] = p3;
        patterns[3] = p4;
        this.id = id;
        ids = new int[] { id };
    }

    public int getPattern(int i) {
        return patterns[i % 4];
    }

    public void setPattern(int index, int c) {
        patterns[index % 4] = c;
    }

    public boolean isMatch(Figure comparable, int index1, int index2) {
        return patterns[index1 % 4] == comparable.patterns[index2 % 4];
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        Figure figure = (Figure) obj;
        if (maxTurn != figure.maxTurn)
            return false;
        for (int i = 0; i < 4; i++) {
            boolean b = true;
            for (int j = 0; j < 4; j++) {
                if (figure.patterns[(i + j) % 4] != patterns[j]) {
                    b = false;
                    break;
                }
            }
            if (b)
                return b;
        }
        return false;
    }

    /**
     * @return Returns the count.
     */
    public int getFigureCount() {
        return count;
    }

    public static Figure[] removeDuplications(Figure[] figures) {

        for (int i = 0; i < figures.length; i++) {
            if (figures[i].patterns[0] == figures[i].patterns[1]
                    && figures[i].patterns[0] == figures[i].patterns[2]
                    && figures[i].patterns[0] == figures[i].patterns[3])
                figures[i].maxTurn = 1;
            else if (figures[i].patterns[0] == figures[i].patterns[2]
                    && figures[i].patterns[1] == figures[i].patterns[3])
                figures[i].maxTurn = 2;
        }

        for (int i = 0; i < figures.length - 1; i++) {
            for (int j = i + 1; j < figures.length; j++) {
                if (figures[i].count > 0 && figures[i].equals(figures[j])) {
                    figures[j].count += figures[i].count;
                    figures[i].count = 0;
                    for (int k = 0; k < figures[i].ids.length; k++) {
                        figures[j].ids = Utility.addNew(figures[j].ids,
                                figures[i].ids[k]);
                    }
                }
            }
        }

        Collection fig = new ArrayList();
        for (int i = 0; i < figures.length; i++) {
            if (figures[i].count > 0) {
                fig.add(figures[i]);
            }
        }

        return (Figure[]) fig.toArray(new Figure[0]);

    }

    public static boolean checkMatch(Field field, FigureVector matchFigure,
            FigureVector[] fieldOccupation) {
        Field[] fields = field.getNeighbouringFields();
        for (int i = 0; i < 4; i++) {
            // If the field is on the edge but the figure is not.
            if (fields[i] == null
                    && matchFigure.getFigure().getPattern(
                            4 + i - matchFigure.getIndex()) != 0)
                return false;
            // If the field is not on the edge but the figure is.
            if (fields[i] != null
                    && matchFigure.getFigure().getPattern(
                            4 + i - matchFigure.getIndex()) == 0)
                return false;
            // If there is a figure placed in the neighbourhood.
            if (fields[i] != null
                    && fieldOccupation[fields[i].getPos()] != null
                    && fieldOccupation[fields[i].getPos()] != ETALONMATCHFIGURE
                    && fieldOccupation[fields[i].getPos()]
                            .getFigure()
                            .getPattern(
                                    (6 + i - fieldOccupation[fields[i].getPos()]
                                            .getIndex()) % 4) != matchFigure
                            .getFigure().getPattern(
                                    4 + i - matchFigure.getIndex())) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return Returns the maxTurn.
     */
    public int getMaxTurn() {
        return maxTurn;
    }

    /**
     * @return Returns the patterns.
     */
    public int[] getPatterns() {
        return patterns;
    }

    //    private static String[] PATTERNS = { "*", "a", "b", "c", "d", "e", "f",
    //          "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
    //        "t", "u", "w", "x", "y", "z" };
    private static String[] PATTERNS = { "*", "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "x", "y", "z" };

    private static Pattern FIGUREPATTERN = Pattern
            .compile("^([\\*a-z])([\\*a-z])([\\*a-z])([\\*a-z])$");

    private static int conv(String str) {
        for (int i = 0; i < PATTERNS.length; i++) {
            if (PATTERNS[i].equals(str))
                return i;
        }
        throw new RuntimeException("Unparseable pattern!!!" + str);
    }

    public static Figure parse(int id, String str) {
        Matcher matcher = FIGUREPATTERN.matcher(str);
        if (matcher.find()) {
            return new Figure(id, conv(matcher.group(1)),
                    conv(matcher.group(2)), conv(matcher.group(3)),
                    conv(matcher.group(4)));
        }
        throw new RuntimeException("Not a figure!!!" + str);
    }

    public String toString() {
        String ret = "";
        int[] is = getPatterns();
        for (int i = 0; i < 4; i++) {
            ret += PATTERNS[is[i]];
        }
        idIndex = (idIndex + 1) % ids.length;
        return "(" + (ids[idIndex] + 1) + ")<" + ret + ">";
    }

    public static String toString(Figure[] figures) {

        String pr = null;
        for (int i = 0; i < figures.length; i++) {
            if (figures[i] != null) {
                for (int j = 0; j < figures[i].count; j++) {
                    if (pr == null)
                        pr = figures[i].toString();
                    else
                        pr += "," + figures[i];
                }
            }
        }
        return pr;

    }

    /**
     * @return Returns the id.
     */
    public int getId() {
        return id;
    }

    public static void printPatterns(Figure[] figures) {
        Map map = new HashMap();
        for (int i = 0; i < figures.length; i++) {
            for (int j = 0; j < 4; j++) {
                String patt = PATTERNS[figures[i].getPattern(j)];
                Integer integer = (Integer) map.get(patt);
                if (integer == null)
                    integer = new Integer(0);
                integer = new Integer(integer.intValue() + figures[i].count);
                map.put(patt, integer);
            }
        }
        for (int i = 0; i < map.size(); i++) {
            log.info("Pattern " + PATTERNS[i] + " count:"
                    + map.get(PATTERNS[i]));
            String str = "";
            for (int j = 0; j < figures.length; j++) {
                int pattCount = 0;
                for (int k = 0; k < 4; k++) {
                    if (figures[j].patterns[k] == i)
                        pattCount++;
                }
                if (pattCount > 0) {
                    for (int k = 0; k < figures[j].ids.length; k++) {
                        str += (str.length() == 0 ? "" : ",")
                                + figures[j].toString() + "{" + pattCount + "}";
                    }
                }
            }
            log.info("Pattern " + PATTERNS[i] + " figures:" + str);
        }

    }

}
