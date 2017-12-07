/*
 * Copyright (C) 2004-2017 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.tools.configurator;

import javax.annotation.Resource;

import com.l2jserver.tools.util.AbstractResourceBundleLoader;

/**
 * @author HorridoJoho
 */
public final class ConfiguratorResources extends AbstractResourceBundleLoader
{
	private static final String _RES_BUNDLE_NAME = "resources.tools.configurator.ConfiguratorResources";
	
	@Resource
	public final String AppAbout = null;
	@Resource
	public final String AppName = null;
	@Resource
	public final String AppCloseDirtyMessage = null;
	
	@Resource
	public final String LoadingConfigs = null;
	@Resource
	public final String LoadingConfigsSuccessful = null;
	@Resource
	public final String LoadingConfigsError = null;
	
	@Resource
	public final String SavingConfigs = null;
	@Resource
	public final String SavingConfigsSuccessful = null;
	@Resource
	public final String SavingConfigsError = null;
	
	@Resource
	public final String ClosingApp = null;
	
	ConfiguratorResources()
	{
		super(_RES_BUNDLE_NAME);
		load();
	}
}
