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
package com.l2jserver.gameserver.ai;

import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;

/**
 * @author HorridoJoho
 * @param <T> the npc type
 */
public interface INpcAi<T extends L2Npc> extends ICharacterAi<T>
{
	/**
	 * Suspends timers and actions of this ai.
	 * @return true: ai got suspended<br>
	 *         false: ai is already suspended
	 */
	public abstract boolean suspend();
	
	/**
	 * Resumes timers and actions of this ai.
	 * @return true: ai got resumed<br>
	 *         false: ai is already running
	 */
	public abstract boolean resume();
	
	public void addAttackIntend(L2Character target, boolean stationary, float priority);
	
	public void addAttackIntendEx(L2Character target, boolean stationary, boolean force, float priority);
	
	public void addDoNothingIntend(long seconds, float priority);
	
	public void addEffectActionIntend(L2Character target, int type, long millis, float priority);
	
	public void addEffectActionIntend2(L2Character target, int type, long millis, float priority, int ukn2);
	
	public void addFleeIntend(L2Character target, float priority);
	
	public void addFleeIntendEx(L2Character target, int distance, float priority);
	
	public void addFollowIntend(L2Character target, float priority);
	
	public void addFollowIntend2(L2Character target, int distance, int ukn, int ukn2, float priority, int ukn3);
	
	public void addGetItemIntend(L2ItemInstance item, float priority);
	
	public void addGetItemIntendEx(int itemObjectId, float priority);
}
