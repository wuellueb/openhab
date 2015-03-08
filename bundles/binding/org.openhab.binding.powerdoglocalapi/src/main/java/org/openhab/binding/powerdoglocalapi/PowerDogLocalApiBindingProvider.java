/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.powerdoglocalapi;

import java.util.List;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and the Powerdog Local API.
 * 
 * @author wuellueb
 * @since 1.7.0
 */
public interface PowerDogLocalApiBindingProvider extends BindingProvider {

	/**
	 * Returns the Type of the Item identified by {@code itemName}
	 * 
	 * @param itemName the name of the item to find the type for
	 * @return the type of the Item identified by {@code itemName}
	 */
	Class<? extends Item> getItemType(String itemName);
	
	/**
	 * Return the Server IP or hostname for the PowerDog linked to the item
	 * 
	 * @param itemName the item for which to find the server
	 */
	//String getPowerDogHost(String itemName);

	/**
	 * Return the XML-RPC port for the PowerDog linked to the item
	 * 
	 * @param itemName the item for which to find the server port
	 */
	//int getPowerDogPort(String itemName);

	/**
	 * Return the PowerAPI password serverPassword for the PowerDog linked to the item
	 * 
	 * @param itemName the item for which to find the powerDogPassword
	 */
	//String getPowerDogPassword(String itemName);

	/**
	 * Return the IP serverId for PowerDog linked to the item
	 * 
	 * @param itemName the item for which to find the serverId
	 */
	String getServerId(String itemName);

	/**
	 * Return the PowerDog Value ID for the item (linked to the parameter)
	 * 
	 * @param itemName the item for which to find a ValueID
	 */
	String getValueId(String itemName);

	/**
	 * Return the parameter 'name' for this item. The variable 'name' is the PowerDog XML parameter
	 * used for the item. This is linked to the ValueID.
	 * 
	 * @param itemName the item for which to find the name
	 */
	String getName(String itemName);
	
	/**
	 * Return the parameter 'dataType' for this item. Can be either 'Number' or 'String'.
	 * 
	 * @param itemName the item for which to find the dataType
	 */
	String getDataType(String itemName);
	
	/**
	 * Returns the refresh interval to use according to <code>itemName</code>.
	 * Is used by PowerDog-In-Binding.
	 *  
	 * @param itemName the item for which to find a refresh interval
	 * 
	 * @return the matching refresh interval or <code>null</code> if no matching
	 * refresh interval could be found.
	 */
	int getRefreshInterval(String itemName);
	
	/**
	 * Returns all items which are mapped to a PowerDog-In-Binding
	 * @return item which are mapped to a PowerDog-In-Binding
	 */
	List<String> getInBindingItemNames();
}
