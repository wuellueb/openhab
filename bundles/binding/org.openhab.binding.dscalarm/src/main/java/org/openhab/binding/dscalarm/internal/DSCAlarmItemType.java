/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dscalarm.internal;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to map item types from the binding string to a ENUM value
 * 
 * @author Russell Stephens
 * @since 1.6.0
 */
public enum DSCAlarmItemType{

	PANEL_CONNECTION("panel_connection"),
	PANEL_MESSAGE("panel_message"),
	PANEL_COMMAND("panel_command"),
	PANEL_SYSTEM_ERROR("panel_system_error"),

	PANEL_TIME("panel_time"),
	PANEL_TIME_STAMP("panel_time_stamp"),
	PANEL_TIME_BROADCAST("panel_time_broadcast"),

	PANEL_FIRE_KEY_ALARM("panel_fire_key_alarm"),
	PANEL_PANIC_KEY_ALARM("panel_panic_key_alarm"),
	PANEL_AUX_KEY_ALARM("panel_aux_key_alarm"),
	PANEL_AUX_INPUT_ALARM("panel_aux_input_alarm"),
	
	PARTITION_STATUS("partition_status"),
	PARTITION_ARM_MODE("partition_arm_mode"),

	PARTITION_ARMED("partition_armed"),
	PARTITION_ENTRY_DELAY("partition_entry_delay"),
	PARTITION_EXIT_DELAY("partition_exit_delay"),
	PARTITION_IN_ALARM("partition_in_alarm"),
	
	ZONE_GENERAL_STATUS("zone_general_status"),
	ZONE_ALARM_STATUS("zone_alarm_status"),
	ZONE_TAMPER_STATUS("zone_tamper_status"),
	ZONE_FAULT_STATUS("zone_fault_status"),
	ZONE_BYPASS_MODE("zone_bypass_mode"),

	ZONE_IN_ALARM("zone_in_alarm"),
	ZONE_TAMPER("zone_tamper"),
	ZONE_FAULT("zone_fault"),
	ZONE_TRIPPED("zone_tripped"),
	
	KEYPAD_READY_LED("keypad_ready_led"),
	KEYPAD_ARMED_LED("keypad_armed_led"),
	KEYPAD_MEMORY_LED("keypad_memory_led"),
	KEYPAD_BYPASS_LED("keypad_bypass_led"),
	KEYPAD_TROUBLE_LED("keypad_trouble_led"),	
	KEYPAD_PROGRAM_LED("keypad_program_led"),	
	KEYPAD_FIRE_LED("keypad_fire_led"),	
	KEYPAD_BACKLIGHT_LED("keypad_backlight_led"),	
	KEYPAD_AC_LED("keypad_ac_led");	

	/**
	 * The string found in a binding configuration
	 */
	private String label;
	
	/**
	 * Lookup map to get a DSCAlarmItemType from its label
	 */
	private static Map<String, DSCAlarmItemType> labelToDSCAlarmItemType;

	/**
	 * Constructor
	 * 
	 * @param label
	 */
	private DSCAlarmItemType(String label) {
		this.label = label;
	}
	
	/**
	 * Initialize the lookup map that gets a DSCAlarmItemType value from a string label
	 */
	private static void initMapping() {
		labelToDSCAlarmItemType = new HashMap<String, DSCAlarmItemType>();
		for (DSCAlarmItemType s : values()) {
			labelToDSCAlarmItemType.put(s.label, s);
		}
	}

	/**
	 * Returns the label of the DSCAlarmItemType Values enumeration
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * Lookup function based on the binding type label
	 * Returns null if the binding type is not found
	 * 
	 * @param label
	 * @return enum value
	 */
	public static DSCAlarmItemType getDSCAlarmItemType(String label) {
		if (labelToDSCAlarmItemType == null) {
			initMapping();
		}
		return labelToDSCAlarmItemType.get(label);
	}
		
}

