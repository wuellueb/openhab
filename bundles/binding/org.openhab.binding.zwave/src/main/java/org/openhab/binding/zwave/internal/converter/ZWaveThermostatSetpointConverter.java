/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.converter;

import java.math.BigDecimal;
import java.util.Map;

import org.openhab.binding.zwave.internal.converter.command.BigDecimalCommandConverter;
import org.openhab.binding.zwave.internal.converter.command.ZWaveCommandConverter;
import org.openhab.binding.zwave.internal.converter.state.BigDecimalDecimalTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.ZWaveStateConverter;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveThermostatSetpointCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveThermostatSetpointCommandClass.SetpointType;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveThermostatSetpointCommandClass.ZWaveThermostatSetpointValueEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * ZWaveThermostatSetpointConverter class. Converter for communication with the 
 * {@link ZWaveThermostatSetpointCommandClass}. Implements polling of the setpoint
 * status and receiving of setpoint events.
 * @author Matthew Bowman
 * @author Dave Hock
 * @author Chris Jackson
 * @since 1.4.0
 */
public class ZWaveThermostatSetpointConverter extends
		ZWaveCommandClassConverter<ZWaveThermostatSetpointCommandClass> {
	
	private static final Logger logger = LoggerFactory.getLogger(ZWaveThermostatSetpointConverter.class);
	private static final int REFRESH_INTERVAL = 0; // refresh interval in seconds for the thermostat setpoint;

	/**
	 * Constructor. Creates a new instance of the {@link ZWaveThermostatSetpointConverter} class.
	 * @param controller the {@link ZWaveController} to use for sending messages.
	 * @param eventPublisher the {@link EventPublisher} to use to publish events.
	 */
	public ZWaveThermostatSetpointConverter(ZWaveController controller,
			EventPublisher eventPublisher) {
		super(controller, eventPublisher);
		this.addCommandConverter(new BigDecimalCommandConverter());
		this.addStateConverter(new BigDecimalDecimalTypeConverter());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	SerialMessage executeRefresh(ZWaveNode node,
			ZWaveThermostatSetpointCommandClass commandClass, int endpointId,
			Map<String, String> arguments) {
		logger.debug("NODE {}: Generating poll message for {}, endpoint {}", node.getNodeId(), commandClass.getCommandClass().getLabel(), endpointId);
		String setpointType = arguments.get("setpoint_type");

		if (setpointType != null) {
			return node.encapsulate(commandClass.getMessage(SetpointType.getSetpointType(Integer.parseInt(setpointType))), commandClass, endpointId);
		} else {
			return node.encapsulate(commandClass.getValueMessage(), commandClass, endpointId);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void handleEvent(ZWaveCommandClassValueEvent event, Item item,
			Map<String, String> arguments) {
		ZWaveStateConverter<?,?> converter = this.getStateConverter(item, event.getValue());
		String setpointType = arguments.get("setpoint_type");
		String scale = arguments.get("setpoint_scale");
		ZWaveThermostatSetpointValueEvent setpointEvent = (ZWaveThermostatSetpointValueEvent)event;
		
		if (converter == null) {
			logger.warn("NODE {}: No converter found for item = {} endpoint = {}, ignoring event.", event.getNodeId(), item.getName(), event.getEndpoint());
			return;
		}
		
		// Don't trigger event if this item is bound to another setpoint type
		if (setpointType != null && SetpointType.getSetpointType(Integer.parseInt(setpointType)) != setpointEvent.getSetpointType())
			return;
		
		Object val = event.getValue();
		// Perform a scale conversion if needed
		if (scale != null && Integer.parseInt(scale) != setpointEvent.getScale()) {
			// For temperature, there are only two scales, so we simplify the conversion
			if(setpointEvent.getScale() == 0) {
				// Scale is celsius, convert to fahrenheit
				double c = ((BigDecimal)val).doubleValue();
				val = new BigDecimal((c * 9.0 / 5.0) + 32.0 );
			}
			else if(setpointEvent.getScale() == 1) {
				// Scale is fahrenheit, convert to celsius
				double f = ((BigDecimal)val).doubleValue();
				val = new BigDecimal((f - 32.0) * 5.0 / 9.0 );					
			}
		}

		State state = converter.convertFromValueToState(val);
		this.getEventPublisher().postUpdate(item.getName(), state);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void receiveCommand(Item item, Command command, ZWaveNode node,
			ZWaveThermostatSetpointCommandClass commandClass, int endpointId,
			Map<String, String> arguments) {
		ZWaveCommandConverter<?,?> converter = this.getCommandConverter(command.getClass());
		String scaleString = arguments.get("setpoint_scale");
		String setpointType = arguments.get("setpoint_type");
		
		int scale = 0;
		if (scaleString !=null ) scale= Integer.parseInt(scaleString);

		logger.debug("NODE {}: Thermostat command received for {}", node.getNodeId(), command.toString());

		if (converter == null) {
			logger.warn("NODE {}: No converter found for item = {}, endpoint = {}, ignoring command.", node.getNodeId(), item.getName(), endpointId);
			return;
		}
		
		SerialMessage serialMessage;
		
		if (setpointType != null) {
			serialMessage = node.encapsulate(commandClass.setMessage(scale, SetpointType.getSetpointType(Integer.parseInt(setpointType)),(BigDecimal)converter.convertFromCommandToValue(item, command)), commandClass, endpointId);
		} else {
			serialMessage = node.encapsulate(commandClass.setMessage(scale, (BigDecimal)converter.convertFromCommandToValue(item, command)), commandClass, endpointId);
		}
		
		if (serialMessage == null) {
			logger.warn("NODE {}: Generating message failed for command class = {}, endpoint = {}", node.getNodeId(), commandClass.getCommandClass().getLabel(), endpointId);
			return;
		}

		logger.debug("NODE {}: Sending Message: {}", node.getNodeId(), serialMessage);
		this.getController().sendData(serialMessage);
		
		if (command instanceof State) {
			this.getEventPublisher().postUpdate(item.getName(), (State)command);
		}
		
		// Request an update so that OH knows when the setpoint has changed.
		if (setpointType != null) {
			serialMessage = node.encapsulate(commandClass.getMessage(SetpointType.getSetpointType(Integer.parseInt(setpointType))), commandClass, endpointId);
		} else {
			serialMessage = node.encapsulate(commandClass.getValueMessage(), commandClass, endpointId);
		}
		
		if (serialMessage == null) {
			logger.warn("NODE {}: Generating message failed for command class = {}, endpoint = {}", node.getNodeId(), commandClass.getCommandClass().getLabel(), endpointId);
			return;
		}

		logger.debug("NODE {}: Sending Message: {}", node.getNodeId(), serialMessage);
		this.getController().sendData(serialMessage);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	int getRefreshInterval() {
		return REFRESH_INTERVAL;
	}


}
