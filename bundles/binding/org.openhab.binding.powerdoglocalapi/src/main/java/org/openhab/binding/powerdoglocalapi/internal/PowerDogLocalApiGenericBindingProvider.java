/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.powerdoglocalapi.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.powerdoglocalapi.PowerDogLocalApiBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>
 * This class parses the EcoData PowerDog LocalAPI item binding data. It registers as a 
 * {@link PowerDogLocalAPIGenericBindingProvider} service as well.
 * </p>
 * 
 * <p>Here are some examples for valid binding configuration strings:
 * <ul>
 * 	<li><code>{ powerdoglocalapi="<serverId:arithmetic_1234567890:Current_Value:Number:300000" }</code></li>
 * 	<li><code>{ powerdoglocalapi="<powerdog:pv_global_1234567890:Current_Value:Number:300000" }</code></li>
 * 	<li><code>{ powerdoglocalapi="<powerdog:pv_global_1234567890:Unit_1000000:String:300000" }</code></li>
* 	<li><code>{ powerdoglocalapi=">powerdog:remotecounter_1234567890:Current_Value:Number" }</code></li>
 * </ul>
 * 
 * The 'serverId' referenced in the binding string is configured in the openhab.cfg file -:
 * powerdoglocalapi:serverId.host = powerdog
 * 
 * 'serverId' can be any alphanumeric string as long as it is the same in the binding and
 * configuration file. <b>NOTE</b>: The parameter is case sensitive!
 * 
 * @author wuellueb
 * @since 1.7.0
 */
public class PowerDogLocalApiGenericBindingProvider extends AbstractGenericBindingProvider implements PowerDogLocalApiBindingProvider {

	static final Logger logger = LoggerFactory.getLogger(PowerDogLocalApiGenericBindingProvider.class);

	/** {@link Pattern} which matches a binding configuration part */
	private static final Pattern BASE_CONFIG_PATTERN =
		Pattern.compile("(<|>)([0-9._a-zA-Z]+:[0-9._a-zA-Z]+:[0-9._a-zA-Z]+:[a-zA-Z]+:[0-9]+)");

	/** {@link Pattern} which matches an In-Binding */
	private static final Pattern IN_BINDING_PATTERN =
		Pattern.compile("([0-9._a-zA-Z]+):([0-9._a-zA-Z]+):([0-9._a-zA-Z]+):([a-zA-Z]+):([0-9]+)");
	
	/** {@link Pattern} which matches an Out-Binding */
	private static final Pattern OUT_BINDING_PATTERN =
		Pattern.compile("([0-9._a-zA-Z]+):([0-9._a-zA-Z]+):([0-9._a-zA-Z]+):([a-zA-Z]+)"); // TODO
	
	/** 
	 * Artificial command for the PowerDog configuration
	 */
	protected static final Command IN_BINDING_KEY = StringType.valueOf("IN_BINDING");

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "powerdoglocalapi";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		//if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		//	throw new BindingConfigParseException("item '" + item.getName()
		//			+ "' is of type '" + item.getClass().getSimpleName()
		//			+ "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		//}
		logger.debug("PowerDogLocalApi:validateItemType called"); // TODO
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		logger.debug("PowerDogLocalApi:processBindingConfiguration called");
		super.processBindingConfiguration(context, item, bindingConfig);
		//PowerDogLocalApiBindingConfig config = new PowerDogLocalApiBindingConfig(); // TODO remove
		
		if (bindingConfig != null) {
			PowerDogLocalApiBindingConfig config = parseBindingConfig(item, bindingConfig);
			logger.debug("bindingConfig adeed (config=" + config.toString() + ")");
			addBindingConfig(item, config);
		}
		else {
			logger.warn("bindingConfig is NULL (item=" + item + ") -> process bindingConfig aborted!");
		}
		
		//addBindingConfig(item, config);		 // TODO remove
	}
	
	/**
	 * Delegates parsing the <code>bindingConfig</code> with respect to the
	 * first character (<code>&lt;</code> or <code>&gt;</code>) to the 
	 * specialized parsing methods
	 * 
	 * @param item
	 * @param bindingConfig
	 * 
	 * @throws BindingConfigParseException
	 */
	protected PowerDogLocalApiBindingConfig parseBindingConfig(Item item, String bindingConfig) throws BindingConfigParseException {
		
		logger.debug("PowerDogLocalAPI:parseBindingConfig called");

		PowerDogLocalApiBindingConfig config = new PowerDogLocalApiBindingConfig();
		config.itemType = item.getClass();
		
		Matcher matcher = BASE_CONFIG_PATTERN.matcher(bindingConfig);
		
		if (!matcher.matches()) {
			throw new BindingConfigParseException("PowerDogLocalAPI:bindingConfig '" + bindingConfig + "' doesn't contain a valid binding configuration");
		}
		matcher.reset();
				
		while (matcher.find()) {
			String direction = matcher.group(1);
			String bindingConfigPart = matcher.group(2);
			
			if (direction.equals("<")) {
				config = parseInBindingConfig(item, bindingConfigPart, config);
			}
			else if (direction.equals(">")) {
				// for future use
			}
			else {
				throw new BindingConfigParseException("Unknown command given! Configuration must start with '<' or '>' ");
			}
		}
		
		return config;
	}
	
	/**
	 * Parses a PowerDog LocalAPI in configuration by using the regular expression
	 * <code>([0-9.a-zA-Z]+:[0-9.a-zA-Z]+:[0-9._a-zA-Z]+:[a-zA-Z]+:[0-9]+)</code>. Where the groups should 
	 * contain the following content:
	 * <ul>
	 * <li>1 - Server ID</li>
	 * <li>2 - PowerDog Value ID</li>
	 * <li>3 - Variable name</li>
	 * <li>4 - Data type (String or Number)</li>
	 * <li>5 - Refresh Interval</li>
	 * </ul>
	 * 
	 * @param item 
	 * @param bindingConfig the config string to parse
	 * @param config
	 * 
	 * @return the filled {@link PowerDogLocalAPIBindingConfig}
	 * @throws BindingConfigParseException if the regular expression doesn't match
	 * the given <code>bindingConfig</code>
	 */
	protected PowerDogLocalApiBindingConfig parseInBindingConfig(Item item, String bindingConfig, PowerDogLocalApiBindingConfig config) throws BindingConfigParseException {

		logger.debug("PowerDogLocalAPI:parseInBindingConfig called");
		Matcher matcher = IN_BINDING_PATTERN.matcher(bindingConfig);
		
		if (!matcher.matches()) {
			throw new BindingConfigParseException("bindingConfig '" + bindingConfig + "' doesn't represent a valid in-binding-configuration. A valid configuration is matched by the RegExp '"+IN_BINDING_PATTERN+"'");
		}
		matcher.reset();
				
		PowerDogLocalApiBindingConfigElement configElement;

		while (matcher.find()) {
			configElement = new PowerDogLocalApiBindingConfigElement();
			configElement.serverId = matcher.group(1);
			configElement.valueId = matcher.group(2);
			configElement.name = matcher.group(3);
			configElement.dataType = matcher.group(4);
			configElement.refreshInterval = Integer.valueOf(matcher.group(5)).intValue();

			logger.debug("PowerDogLocalAPI: "+configElement);
			config.put(IN_BINDING_KEY, configElement);
		}
		
		return config;
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public Class<? extends Item> getItemType(String itemName) {
		PowerDogLocalApiBindingConfig config = (PowerDogLocalApiBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.itemType : null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getServerId(String itemName) {
		PowerDogLocalApiBindingConfig config = (PowerDogLocalApiBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(IN_BINDING_KEY) != null ? config.get(IN_BINDING_KEY).serverId : null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getValueId(String itemName){
		PowerDogLocalApiBindingConfig config = (PowerDogLocalApiBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(IN_BINDING_KEY) != null ? config.get(IN_BINDING_KEY).valueId : null;
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public String getName(String itemName) {
		PowerDogLocalApiBindingConfig config = (PowerDogLocalApiBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(IN_BINDING_KEY) != null ? config.get(IN_BINDING_KEY).name : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDataType(String itemName) {
		PowerDogLocalApiBindingConfig config = (PowerDogLocalApiBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(IN_BINDING_KEY) != null ? config.get(IN_BINDING_KEY).dataType : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getRefreshInterval(String itemName) {
		PowerDogLocalApiBindingConfig config = (PowerDogLocalApiBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(IN_BINDING_KEY) != null ? config.get(IN_BINDING_KEY).refreshInterval : 0;
	}
		
	/**
	 * {@inheritDoc}
	 */
	public List<String> getInBindingItemNames() {
		List<String> inBindings = new ArrayList<String>();
		for (String itemName : bindingConfigs.keySet()) {
			PowerDogLocalApiBindingConfig pdConfig = (PowerDogLocalApiBindingConfig) bindingConfigs.get(itemName);
			if (pdConfig.containsKey(IN_BINDING_KEY)) {
				inBindings.add(itemName);
			}
		}
		return inBindings;
	}
	
	/**
	 * This is a helper class holding binding specific configuration details
	 * to map commands to {@link PowerDogLocalAPIBindingConfigElement }. There will be a map like 
	 * <code>ON->PowerDogLocalAPIBindingConfigElement</code>
	 * 
	 * @author wuellueb
	 * @since 1.7.0
	 */
	class PowerDogLocalApiBindingConfig extends HashMap<Command, PowerDogLocalApiBindingConfigElement> implements BindingConfig {
		private static final long serialVersionUID = -3746900828632519633L;
		Class<? extends Item> itemType;
	}
	
	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the binding provider.
	 */
	static class PowerDogLocalApiBindingConfigElement implements BindingConfig {
		public String serverId;
		public String valueId;
		public String name;
		public String dataType;
		public int refreshInterval;
		
		@Override
		public String toString() {
			return "PowerDogLocalAPIBindingConfigElement [serverId=" + serverId
					+ ", valueId=" + valueId + ", name=" + name + ", dataType=" + dataType + ", refreshInterval=" + refreshInterval + "]";
		}
	}
	
}
