/*
 * Created on 06-Nov-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package fg.eternity.bo;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Config implements Serializable {
	private int dimX = 0;

	private int dimY = 0;

	private int dimAbsX = 0;

	private int dimMax = 0;

	private int patternCount = 0;
	public static int MAXLEVEL = 500;

	private Properties properties;

	public void init() {
		this.dimAbsX = dimX + 2;
		this.dimMax = (dimX + 2) * dimY - 2;
	}

	/**
	 * @return Returns the dimAbsX.
	 */
	public int getDimAbsX() {
		return dimAbsX;
	}

	/**
	 * @return Returns the dimMax.
	 */
	public int getDimMax() {
		return dimMax;
	}

	/**
	 * @return Returns the dimX.
	 */
	public int getDimX() {
		return dimX;
	}

	/**
	 * @return Returns the dimY.
	 */
	public int getDimY() {
		return dimY;
	}

	/**
	 * @return Returns the patternCount.
	 */
	public int getPatternCount() {
		return patternCount;
	}

	public int getIndexRebuildDiff() {
		return 8;
	}

	/**
	 * @return Returns the properties.
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            The properties to set.
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * @param dimX The dimX to set.
	 */
	public void setDimX(int dimX) {
		this.dimX = dimX;
	}
	/**
	 * @param dimY The dimY to set.
	 */
	public void setDimY(int dimY) {
		this.dimY = dimY;
	}
	/**
	 * @param patternCount The patternCount to set.
	 */
	public void setPatternCount(int patternCount) {
		this.patternCount = patternCount;
	}
}
