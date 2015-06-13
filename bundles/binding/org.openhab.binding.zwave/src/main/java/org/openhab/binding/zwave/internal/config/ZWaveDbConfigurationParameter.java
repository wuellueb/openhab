/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Implements the configuration parameters for the XML product database
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
public class ZWaveDbConfigurationParameter {
	public Integer Index;
	public String Type;
	public String Default;
	public Integer Size;
	public Integer Minimum;
	public Integer Maximum;
	public String Units;
	public Boolean ReadOnly;
	public Boolean WriteOnly;
	@XStreamImplicit
	public List<ZWaveDbLabel> Label;
	@XStreamImplicit
	public List<ZWaveDbLabel> Help;
	@XStreamImplicit
	public List<ZWaveDbConfigurationListItem> Item;
}

