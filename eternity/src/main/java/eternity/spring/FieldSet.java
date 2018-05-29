/*
 * Created on 17-Feb-2010
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eternity.spring;

import eternity.Config;
import eternity.Field;

/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class FieldSet {

	Field[] fields;
	Config config ;

	public FieldSet(Config config) {
		fields = Field.generateAllFields(config);
	}

	/**
	 * @return Returns the fields.
	 */
	public Field[] getFields() {
		return fields;
	}
	/**
	 * @return Returns the config.
	 */
	public Config getConfig() {
		return config;
	}
	/**
	 * @param config The config to set.
	 */
	public void setConfig(Config config) {
		this.config = config;
	}
}
