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
package com.l2jserver.tools.configurator.frontend.jfx.control.configdirectivetable.cells;

import java.net.InetAddress;

import com.l2jserver.tools.configurator.frontend.jfx.control.configdirectivetable.cells.valuecellsupport.BooleanValueCellSupport;
import com.l2jserver.tools.configurator.frontend.jfx.control.configdirectivetable.cells.valuecellsupport.ValueCellSupport;
import com.l2jserver.tools.configurator.frontend.jfx.control.configdirectivetable.cells.valuecellsupport.InetAddressValueCellSupport;
import com.l2jserver.tools.configurator.frontend.jfx.control.configdirectivetable.cells.valuecellsupport.StringValueCellSupport;
import com.l2jserver.tools.configurator.model.ConfigDirective;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;

/**
 * @author HorridoJoho
 */
public final class ValueCell extends TableCell<ConfigDirective, ConfigDirective>
{
	private ValueCellSupport _support;
	
	public ValueCell(boolean isDefaultDisplay)
	{
		setAlignment(Pos.CENTER);
		setDisable(isDefaultDisplay);
	}
	
	@Override
	public void updateItem(ConfigDirective item, boolean empty)
	{
		ConfigDirective oldItem = getItem();
		if (_support != null)
		{
			_support.clear(this, oldItem, item, empty);
			_support = null;
		}
		
		super.updateItem(item, empty);
		
		if (empty || (item == null))
		{
			setText(null);
			setGraphic(null);
		}
		else
		{
			Class<?> type = item.getType();
			if (type == Boolean.class)
			{
				_support = new BooleanValueCellSupport();
			}
			else if (type == InetAddress.class)
			{
				_support = new InetAddressValueCellSupport();
			}
			else
			{
				_support = new StringValueCellSupport();
			}
			
			_support.updateItem(this, oldItem, item, empty);
			setGraphic(_support.getValueControl());
		}
	}
}
