/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plex.internal;

import static org.openhab.binding.plex.internal.PlexBindingConstants.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.openhab.binding.plex.internal.annotations.ItemMapping;
import org.openhab.binding.plex.internal.annotations.ItemPlayerStateMapping;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;

/**
* A session for a single Plex client.     
* 
* @author Jeroen Idserda
* @since 1.7.0
*/
public class PlexSession {
	
	private String machineIdentifier;

	private String sessionKey;
	
	@ItemMapping(property = PROPERTY_STATE, type=StringType.class, stateMappings = 
		{@ItemPlayerStateMapping(state=PlexPlayerState.Playing, property=PROPERTY_PLAY),
		 @ItemPlayerStateMapping(state=PlexPlayerState.Stopped, property=PROPERTY_STOP),
		 @ItemPlayerStateMapping(state=PlexPlayerState.Paused, property=PROPERTY_PAUSE)})
	private PlexPlayerState state = PlexPlayerState.Stopped;
	
	@ItemMapping(property = PROPERTY_TITLE, type=StringType.class)
	private String title = "";
	
	@ItemMapping(property = PROPERTY_TYPE, type=StringType.class)
	private String type = "";
	
	@ItemMapping(property = PROPERTY_VOLUME, type=PercentType.class)
	private Integer volume = 100;
	
	@ItemMapping(property = PROPERTY_PROGRESS, type=PercentType.class)
	private BigDecimal progress = BigDecimal.ZERO;
	
	private int duration = 0;
	
	private int viewOffset = 0;
	
	public String getMachineIdentifier() {
		return machineIdentifier;
	}

	public void setMachineIdentifier(String machineIdentifier) {
		this.machineIdentifier = machineIdentifier;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public PlexPlayerState getState() {
		return state;
	}

	public void setState(PlexPlayerState state) {
		this.state = state;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	
	public BigDecimal getProgress() {
		return progress;
	}

	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getViewOffset() {
		return viewOffset;
	}

	public void setViewOffset(Integer viewOffset) {
		if (viewOffset != null) {
			this.viewOffset = viewOffset;
			updateProgress();
		}
	}
	
	private void updateProgress() {
		if (duration > 0) {
			BigDecimal progress = new BigDecimal("100")
									.divide(new BigDecimal(duration), new MathContext(100, RoundingMode.HALF_UP))
									.multiply(new BigDecimal(viewOffset))
									.setScale(2, RoundingMode.HALF_UP);
			
			progress = BigDecimal.ZERO.max(progress);
			progress = new BigDecimal("100").min(progress);
			
			this.progress = progress;
		}
	}
}
