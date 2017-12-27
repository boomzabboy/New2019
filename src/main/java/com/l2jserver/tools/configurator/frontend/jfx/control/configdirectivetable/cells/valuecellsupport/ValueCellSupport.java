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

import com.l2jserver.tools.configurator.model.ConfigDirective;

import javafx.scene.Node;
import javafx.scene.control.TableCell;

/**
 * @author HorridoJoho
 */
public interface ValueCellSupport
{
	void clear(TableCell<?, ?> cell, ConfigDirective oldItem, ConfigDirective newItem, boolean empty);
	
	void updateItem(TableCell<?, ?> cell, ConfigDirective oldItem, ConfigDirective newItem, boolean empty);
	
	Node getValueControl();
}
