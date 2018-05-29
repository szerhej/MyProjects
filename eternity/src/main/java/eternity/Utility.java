/*
 * Created on 17-Oct-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eternity;

import java.util.Random;

/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public abstract class Utility {

    public static Object[] addNew(Object[] objects, Object object) {

        int size = 0;
        if (objects != null)
            size = objects.length;

        Object[] r = (Object[]) java.lang.reflect.Array.newInstance(object
                .getClass(), size + 1);

        if (objects != null)
            System.arraycopy(objects, 0, r, 0, size);

        r[size] = object;

        return r;

    }

    public static int[] addNew(int[] objects, int object) {

        int size = 0;
        if (objects != null)
            size = objects.length;

        int[] r = new int[size + 1];

        if (objects != null)
            System.arraycopy(objects, 0, r, 0, size);

        r[size] = object;

        return r;

    }
    
    public static void shuffle(Object[] objects, Random random) {
        int u = 0;
        for (int i = 0; i < objects.length; i++) {
            if ((u = random.nextInt(objects.length)) == i) {
            } else {
                Object temp = objects[u];
                objects[u] = objects[i];
                objects[i] = temp;
            }
        }

    }

}
