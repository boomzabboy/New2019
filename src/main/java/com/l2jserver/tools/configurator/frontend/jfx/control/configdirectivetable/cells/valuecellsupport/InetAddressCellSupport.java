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
package com.l2jserver.tools.configurator.frontend.jfx.control.configdirectivetable.cells.valuecellsupport;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import com.l2jserver.tools.configurator.model.ConfigDirective;
import com.l2jserver.tools.util.jfx.control.inetaddresscontrol.InetAddressControl;

import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.TableCell;

/**
 * @author HorridoJoho
 */
public final class InetAddressCellSupport implements IValueCellSupport
{
	private static final Queue<InetAddressControl> _boxQueue = new LinkedList<>();
	
	private InetAddressControl _box = null;
	
	@Override
	public void clear(TableCell<?, ?> cell, ConfigDirective oldItem, ConfigDirective newItem, boolean empty)
	{
		if (oldItem != null)
		{
			if (!cell.isDisable())
			{
				Bindings.unbindBidirectional(oldItem.valueProperty(), _box.stringValueProperty());
			}
			
			_boxQueue.offer(_box);
			_box = null;
		}
	}
	
	@Override
	public void updateItem(TableCell<?, ?> cell, ConfigDirective oldItem, ConfigDirective newItem, boolean empty)
	{
		if (!empty && (newItem != null))
		{
			_box = _boxQueue.poll();
			if (_box == null)
			{
				try
				{
					_box = new InetAddressControl(true, false, true);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			_box.setStringValue(newItem.getValue());
			if (!cell.isDisable())
			{
				Bindings.bindBidirectional(newItem.valueProperty(), _box.stringValueProperty());
			}
		}
	}
	
	@Override
	public Node getValueControl()
	{
		return _box;
	}
}
