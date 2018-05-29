/*
 * Created on 13-Feb-2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eternity.plan;

import java.util.ArrayList;
import java.util.List;



import eternity.Field;

/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PlanFork implements IPlan {

	private IPlan next;

	private IPlan[] fork;

	public IRun compile(IndexMap indexMap, int level) {
		List list = new ArrayList();
		for (int i = 0; i < fork.length; i++) {
			IPlan element = fork[i];
			list.add(element.compile(indexMap, level));
		}
		final IRun[] runs = (IRun[]) list.toArray(new IRun[list.size()]);
		return () ->  {
				for (int i = 0; i < runs.length; i++) {
					next = fork[i];
					IRun run = runs[i];
					run.call();
				}
		};
	}

	public void setForkList(List fork) {
		this.fork = (IPlan[]) fork.toArray(new IPlan[fork.size()]);
	}

	public Field[] getFields() {
		return new Field[] {};
	}

	public void stop() {
	}

	/**
	 * @return Returns the fork.
	 */
	public IPlan[] getFork() {
		return fork;
	}
	/**
	 * @return Returns the next.
	 */
	public IPlan getNext() {
		return next;
	}
}
