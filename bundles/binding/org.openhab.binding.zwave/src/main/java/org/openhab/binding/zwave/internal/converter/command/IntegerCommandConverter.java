/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.converter.command;

import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;

/**
 * Converts from {@link DecimalType} command to a Z-Wave value.
 * @author Chris Jackson
 * @since 1.7.0
 */
public class IntegerCommandConverter extends
		ZWaveCommandConverter<DecimalType, Integer> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Integer convert(Item item, DecimalType command) {
		return command.intValue();
	}

}
