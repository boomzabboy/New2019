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

import java.util.LinkedList;
import java.util.Queue;

import com.l2jserver.tools.configurator.model.ConfigDirective;
import com.l2jserver.tools.util.jfx.StringConverters;

import javafx.beans.binding.Bindings;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;

/**
 * @author HorridoJoho
 */
public final class BooleanValueCellSupport implements ValueCellSupport
{
	private static final Queue<CheckBox> _boxQueue = new LinkedList<>();
	
	private CheckBox _box = null;
	
	@Override
	public void clear(TableCell<?, ?> cell, ConfigDirective oldItem, ConfigDirective newItem, boolean empty)
	{
		if (oldItem != null)
		{
			if (!cell.isDisable())
			{
				Bindings.unbindBidirectional(oldItem.valueProperty(), _box.selectedProperty());
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
				_box = new CheckBox();
			}
			_box.setSelected(Boolean.parseBoolean(newItem.getDefaultValue()));
			if (!cell.isDisable())
			{
				Bindings.bindBidirectional(newItem.valueProperty(), _box.selectedProperty(), StringConverters.BOOLEAN);
			}
		}
	}
	
	@Override
	public Control getValueControl()
	{
		return _box;
	}
}
