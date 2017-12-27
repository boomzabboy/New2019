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
package com.l2jserver.tools.util.jfx.stage.msgbox;

import java.util.ResourceBundle;

/**
 * @author HorridoJoho
 */
public enum MsgBoxButton
{
	OK(MsgBoxResult.OK),
	YES(MsgBoxResult.OK),
	SAVE(MsgBoxResult.OK),
	
	RETRY(MsgBoxResult.RETRY),
	TRY_AGAIN(MsgBoxResult.RETRY),
	
	CONTINUE(MsgBoxResult.CONTINUE),
	IGNORE(MsgBoxResult.CONTINUE),
	
	CANCEL(MsgBoxResult.CANCEL),
	NO(MsgBoxResult.CANCEL),
	ABORT(MsgBoxResult.CANCEL),
	DO_NOT_SAVE(MsgBoxResult.CANCEL);
	
	private final String _localizationKey = "BUTTON_" + toString();
	private final MsgBoxResult _result;
	
	private MsgBoxButton(MsgBoxResult result)
	{
		_result = result;
	}
	
	public MsgBoxResult getResult()
	{
		return _result;
	}
	
	public String getButtonText(ResourceBundle resources)
	{
		return resources.getString(_localizationKey);
	}
}
