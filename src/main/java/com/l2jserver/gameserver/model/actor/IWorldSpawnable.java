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

/**
 * @author HorridoJoho
 */
public interface IWorldSpawnable extends IWorldObject
{
	/**
	 * Spawns this world spawnable into the specified instance to the specified<br>
	 * position.
	 * @param x world x position
	 * @param y world y position
	 * @param z world z position
	 * @param instanceId the instanceId
	 * @throws IllegalStateException this world spawnable is already spawned
	 */
	void spawn(int x, int y, int z, int instanceId);
	
	/**
	 * Spawns this world spawnable into the main world instance to the specified<br>
	 * position.
	 * @param x world x position
	 * @param y world y position
	 * @param z world z position
	 * @throws IllegalStateException this world spawnable is already spawned
	 */
	void spawn(int x, int y, int z);
	
	/**
	 * Despawns this world spawnable from it's current instance.
	 * @throws IllegalStateException this world spawnable is not spawned
	 */
	void despawn();
	
	/**
	 * Teleports this world spawnable into the specified instance to the<br>
	 * specified position.<br>
	 * <br>
	 * When the world spawnable is not yet spawned, it is<br>
	 * spawned into the specified instance.
	 * @param x world x position
	 * @param y world y position
	 * @param z world z position
	 * @param heading
	 * @param instanceId the instance id
	 */
	void teleport(int x, int y, int z, int heading, int instanceId);
	
	/**
	 * Teleports this world spawnable inside it's current instance to the<br>
	 * specified position.
	 * @param x world x position
	 * @param y world y position
	 * @param z world z position
	 * @param heading
	 * @throws IllegalStateException this world spawnable is not yet spawned
	 */
	void teleport(int x, int y, int z, int heading);
	
	/**
	 * @return whatever this world spawnable is spawned
	 */
	boolean isSpawned();
	
	/**
	 * When this world sapwnable is spawned, this method returns a positive id<br>
	 * of the instance it is in or 0 for the main world instance.<br>
	 * <br>
	 * When this world spawnable is not spawned, this method returns -1.<br>
	 * <br>
	 * @see IWorldObject#getInstanceId()
	 */
	@Override
	int getInstanceId();
}
