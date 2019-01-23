/*
 * Copyright (C) 2004-2015 L2J Server
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
package com.l2jserver.gameserver.model.actor;

import com.l2jserver.gameserver.model.itemcontainer.ItemContainer;

/**
 * @author HorridoJoho
 */
public interface IItem extends IWorldSpawnable, IWorldOwnable
{
	/**
	 * Make the world character pickup this item.
	 * @param picker the world character
	 * @return true when success, false otherwise
	 * @throws IllegalStateException this item is not on the ground
	 */
	boolean pickup(IWorldCharacter picker);
	
	/**
	 * Make the world charater use this item.
	 * @param user the world character
	 * @return true when success, false otherwise
	 * @throws IllegalStateException this item is not in an item container
	 */
	boolean use(IWorldCharacter user);
	
	/**
	 * @return whatever this item is in an item container
	 */
	boolean isInContainer();
	
	/**
	 * @return the item container this item is in or null if not in a container
	 */
	ItemContainer getContainer();
}
