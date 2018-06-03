/*
 * Created on 30-Jan-2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package fg.eternity.plan;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * @author gxfulop
 * <p>
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PlanOnce implements IPlanChainable {

    @Setter @Getter
    private IPlan next;
    @Setter @Getter
    private IPlan first;

    public IRun compile(IndexMap indexMap, final int level) {

        final IPlan thisPlan = this;
        return () -> {
            IRun run = next.compile(new IndexMap(), level);
            run.call();
            try {
                for (IPlan plan = first; plan != thisPlan; plan = (IPlan) PropertyUtils
                        .getSimpleProperty(plan, "next")) {
                    plan.stop();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    public void stop() {
    }

}
