/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.powerdoglocalapi.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.powerdoglocalapi.PowerDogLocalApiBindingProvider;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author wuellueb
 * @since 1.7.0
 */
public class PowerDogLocalApiBinding extends AbstractActiveBinding<PowerDogLocalApiBindingProvider> {

	private static final Logger logger = 
		LoggerFactory.getLogger(PowerDogLocalApiBinding.class);

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is set in the activate()
	 * method and must not be accessed anymore once the deactivate() method was called or before activate()
	 * was called.
	 */
	private BundleContext bundleContext;

	/**
	 * the maximum duration of data in the cache (in ms, defaults to 1 minute)
	 */
	private int cacheDuration = 600000; // TODO necessary???
	
	/** 
	 * the refresh interval which is used to poll values from the PowerDogLocalApi
	 * server (optional, defaults to 300000ms)
	 */
	private long refreshInterval = 300000;
	
	private int tempCounter = 0; // TODO remove

	/**
	 *  RegEx to validate a config <code>'^(.*?)\\.(host|port)$'</code> 
	 */
	private static final Pattern EXTRACT_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(.*?)$");

	private Map<String, Long> lastUpdateMap = new HashMap<String, Long>();

	private Map<String, PowerDogLocalApiServerConfig> serverList = new HashMap<String, PowerDogLocalApiServerConfig>();

	public PowerDogLocalApiBinding() {
	}
		
	
	/**
	 * Called by the SCR to activate the component with its configuration read from CAS
	 * 
	 * @param bundleContext BundleContext of the Bundle that defines this component
	 * @param configuration Configuration properties for this component obtained from the ConfigAdmin service
	 */
	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
		this.bundleContext = bundleContext;

		logger.debug("activate() method is called!");

		// the configuration is guaranteed not to be null, because the component definition has the
		// configuration-policy set to require. If set to 'optional' then the configuration may be null
		this.bundleContext = bundleContext;
			
		// to override the default refresh interval one has to add a 
		// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
		String refreshIntervalString = (String) configuration.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
		}

		parseConfiguration(configuration);

		setProperlyConfigured(true);
	}
	
	/**
	 * Called by the SCR when the configuration of a binding has been changed through the ConfigAdmin service.
	 * @param configuration Updated configuration properties
	 */
	public void modified(final Map<String, Object> configuration) {
		// update the internal configuration accordingly
		logger.debug("modified() method is called!");
		
		parseConfiguration(configuration);
		
		setProperlyConfigured(true);
	}
	
	/**
	 * Called by the SCR to deactivate the component when either the configuration is removed or
	 * mandatory references are no longer satisfied or the component has simply been stopped.
	 * @param reason Reason code for the deactivation:<br>
	 * <ul>
	 * <li> 0 – Unspecified
     * <li> 1 – The component was disabled
     * <li> 2 – A reference became unsatisfied
     * <li> 3 – A configuration was changed
     * <li> 4 – A configuration was deleted
     * <li> 5 – The component was disposed
     * <li> 6 – The bundle was stopped
     * </ul>
	 */
	public void deactivate(final int reason) {
		this.bundleContext = null;
		// deallocate resources here that are no longer needed and 
		// should be reset when activating this binding again
	}

	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "PowerDogLocalApi Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		logger.debug("execute() method is called!");
		
		/*if (!bindingsExist()) {
			logger.debug("There is no existing PowerDogLocalApi binding configuration => refresh cycle aborted!");
			return;
		}*/

		for (PowerDogLocalApiBindingProvider provider : providers) {
			for (String itemName : provider.getInBindingItemNames()) {
				int refreshInterval = provider.getRefreshInterval(itemName);

				Long lastUpdateTimeStamp = lastUpdateMap.get(itemName);
				if (lastUpdateTimeStamp == null) {
					lastUpdateTimeStamp = 0L;
				}

				long age = System.currentTimeMillis() - lastUpdateTimeStamp;
				boolean needsUpdate = age >= refreshInterval;

				if (needsUpdate) {
					logger.debug("Item '{}' is about to be refreshed now", itemName);

					// Get the unit serverId from the binding, and relate that to the config
					String unit = provider.getServerId(itemName);
					PowerDogLocalApiServerConfig server = serverList.get(unit);
					needsUpdate = false;
					if (server == null) {
						needsUpdate = false;
						logger.error("Unknown PowerDog server referenced: "+unit);
						
						continue;
					}
					else {
						age = System.currentTimeMillis() - server.lastUpdate;
						if (age >= cacheDuration)
							needsUpdate = true;
					}

					String response = null;
					if(needsUpdate == true) {
						// get doc in response // TODO
					}
					else {
						logger.debug("Using PowerDogLocalApi cache");
						response = server.cache;
					}

					/*if(response != null) {
						String value = getVariable(response, provider.getRomId(itemName), provider.getName(itemName));
						if (value != null) {
							Class<? extends Item> itemType = provider.getItemType(itemName);
							State state = createState(itemType, value);
							eventPublisher.postUpdate(itemName, state);
						}
					}*/ // TODO
					tempCounter++;
					
					Class<? extends Item> itemType = provider.getItemType(itemName);
					State state = createState(itemType, String.valueOf(tempCounter));
					eventPublisher.postUpdate(itemName, state);

					lastUpdateMap.put(itemName, System.currentTimeMillis());
				}
			}
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate({},{}) is called!", itemName, newState);
	}

	/**
	 * Returns a {@link State} which is inherited from the {@link Item}s
	 * accepted DataTypes. The call is delegated to the {@link TypeParser}. If
	 * <code>item</code> is <code>null</code> the {@link StringType} is used.
	 * 
	 * @param itemType
	 * @param transformedResponse
	 * 
	 * @return a {@link State} which type is inherited by the {@link TypeParser}
	 *         or a {@link StringType} if <code>item</code> is <code>null</code>
	 */
	private State createState(Class<? extends Item> itemType,
			String transformedResponse) {
		try {
			if (itemType.isAssignableFrom(NumberItem.class)) {
				return DecimalType.valueOf(transformedResponse);
			} else if (itemType.isAssignableFrom(ContactItem.class)) {
				return OpenClosedType.valueOf(transformedResponse);
			} else if (itemType.isAssignableFrom(SwitchItem.class)) {
				return OnOffType.valueOf(transformedResponse);
			} else if (itemType.isAssignableFrom(RollershutterItem.class)) {
				return PercentType.valueOf(transformedResponse);
			} else {
				return StringType.valueOf(transformedResponse);
			}
		} catch (Exception e) {
			logger.debug("Couldn't create state of type '{}' for value '{}'",
					itemType, transformedResponse);
			return StringType.valueOf(transformedResponse);
		}
	}
	private void parseConfiguration(Map<String, Object> config){
		logger.debug("PowerDogLocalApi:parseConfiguration() method is called!");	
		if (config != null) {
			logger.debug("next step");	
			Set<String> keyset = config.keySet();
			logger.debug(keyset.toString());	

			// read further config parameters here ...
			if ( serverList == null ) {
				serverList = new HashMap<String, PowerDogLocalApiServerConfig>();
			}
			
			for (Iterator<String> keys = keyset.iterator(); keys.hasNext(); ) {
				String key = keys.next();
				logger.debug("key: " + key.toString());	

				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key)) {
					continue;
				}
				else if ("event.topics".equals(key)) {
					continue;
				}
				else if ("component.name".equals(key)) {
					continue;
				}
				else if ("component.id".equals(key)) {
					continue;
				}
				else if ("objectClass".equals(key)) {
					continue;
				}

				Matcher matcher = EXTRACT_CONFIG_PATTERN.matcher(key);

				if (!matcher.matches()) {
					continue;
				}

				matcher.reset();
				matcher.find();

				String serverId = matcher.group(1);

				logger.debug("serverID: " + serverId);	

				PowerDogLocalApiServerConfig deviceConfig = serverList.get(serverId);

				if (deviceConfig == null) {
					deviceConfig = new PowerDogLocalApiServerConfig();
					serverList.put(serverId, deviceConfig);
				}

				logger.debug("Current Server config: " + deviceConfig.toString());	

				String configKey = matcher.group(2);
				String value = (String) config.get(key);

				if ("host".equals(configKey)) {
					deviceConfig.host = value;
					logger.debug("value: " + value);	
				}
				else if ("port".equals(configKey)) {
					if (StringUtils.isNotBlank(value)) {
						deviceConfig.port = (int) Long.parseLong(value);
						logger.debug("value: " + value);	
					}
				}
				else if ("password".equals(configKey)) {
					deviceConfig.password = value;
				}
				else if ("refresh".equals(configKey)) {
					if (StringUtils.isNotBlank(value)) {
						deviceConfig.refresh = (int) Long.parseLong(value);
						logger.debug("value: " + value);	
					}
				}
				/*else {
					throw new ConfigurationException(configKey, "The given PowerDogLocalApi configKey '" + configKey + "' is unknown");
				}*/
				logger.debug("New Server config: " + deviceConfig.toString());	
			}

			/*String timeoutString = (String) config.get("timeout");
			if (StringUtils.isNotBlank(timeoutString)) {
				timeout = Integer.parseInt(timeoutString);
			}

			String granularityString = (String) config.get("granularity");
			if (StringUtils.isNotBlank(granularityString)) {
				granularity = Integer.parseInt(granularityString);
			}

			String cacheString = (String) config.get("cache");
			if (StringUtils.isNotBlank(cacheString)) {
				cacheDuration = Integer.parseInt(cacheString);
			}*/ // TODO
			
			refreshInterval = 1000; // TODO
			
			setProperlyConfigured(true);
			logger.debug("PowerDogLocalApi:parseConfiguration() method is terminated");	
		}
	}
	
	static class PowerDogLocalApiServerConfig {
		public String host;
		public int port;
		public String password;
		public int refresh;
		public Long lastUpdate;
		public String cache;

		PowerDogLocalApiServerConfig() {
			lastUpdate = (long) 0;
			
			// set defaults
			refresh = 300000;
			password = "";
			port = 20000;
			host = "powerdog";
		}
		
		@Override
		public String toString() {
			String displayPassword = "[not set]";
			if(StringUtils.isNotBlank(password)) {
				displayPassword = "[set]*****";
			}
			return "PowerDogLocalApiServerCache [host="+host+", password=" + displayPassword + ", lastUpdate=" + lastUpdate + ", cache=" + cache + "]";
		}
	}}
