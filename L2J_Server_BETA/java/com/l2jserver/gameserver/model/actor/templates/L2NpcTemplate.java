/*
 * Copyright (C) 2004-2014 L2J Server
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
package com.l2jserver.gameserver.model.actor.templates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.gameserver.datatables.NpcData;
import com.l2jserver.gameserver.enums.AISkillType;
import com.l2jserver.gameserver.enums.AIType;
import com.l2jserver.gameserver.enums.NpcRace;
import com.l2jserver.gameserver.enums.QuestEventType;
import com.l2jserver.gameserver.enums.Sex;
import com.l2jserver.gameserver.model.L2MinionData;
import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.base.ClassId;
import com.l2jserver.gameserver.model.drops.DropListScope;
import com.l2jserver.gameserver.model.drops.IDropItem;
import com.l2jserver.gameserver.model.effects.L2EffectType;
import com.l2jserver.gameserver.model.holders.ItemHolder;
import com.l2jserver.gameserver.model.interfaces.IIdentifiable;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.skills.L2Skill;
import com.l2jserver.gameserver.model.skills.L2SkillType;

/**
 * NPC template.
 * @author Nos
 */
public final class L2NpcTemplate extends L2CharTemplate implements IIdentifiable
{
	private static final Logger _log = Logger.getLogger(L2NpcTemplate.class.getName());
	
	private int _id;
	private int _displayId;
	private byte _level;
	private String _type;
	private String _name;
	private boolean _usingServerSideName;
	private String _title;
	private boolean _usingServerSideTitle;
	private StatsSet _parameters;
	private NpcRace _race;
	private Sex _sex;
	private int _chestId;
	private int _rhandId;
	private int _lhandId;
	private int _weaponEnchant;
	private double _expRate;
	private double _sp;
	private double _raidPoints;
	private boolean _unique;
	private boolean _attackable;
	private boolean _targetable;
	private boolean _undying;
	private boolean _showName;
	private boolean _flying;
	private boolean _canMove;
	private boolean _noSleepMode;
	private boolean _passableDoor;
	private boolean _hasSummoner;
	private boolean _canBeSown;
	private int _corpseTime;
	private AIType _aiType;
	private int _aggroRange;
	private int _clanHelpRange;
	private int _dodge;
	private boolean _isChaos;
	private int _soulShot;
	private int _spiritShot;
	private int _soulShotChance;
	private int _spiritShotChance;
	private int _minSkillChance;
	private int _maxSkillChance;
	private int _primarySkillId;
	private int _shortRangeSkillId;
	private int _shortRangeSkillChance;
	private int _longRangeSkillId;
	private int _longRangeSkillChance;
	private Set<Integer> _clans;
	private Set<Integer> _enemyClans;
	private Map<DropListScope, List<IDropItem>> _dropLists;
	private double _collisionRadiusGrown;
	private double _collisionHeightGrown;
	
	private final Map<AISkillType, List<L2Skill>> _aiSkills = new ConcurrentHashMap<>();
	private final Map<Integer, L2Skill> _skills = new ConcurrentHashMap<>();
	private final List<L2MinionData> _minions = new ArrayList<>();
	private final List<ClassId> _teachInfo = new ArrayList<>();
	private final Map<QuestEventType, List<Quest>> _questEvents = new ConcurrentHashMap<>();
	
	/**
	 * Constructor of L2Character.
	 * @param set The StatsSet object to transfer data to the method
	 */
	public L2NpcTemplate(StatsSet set)
	{
		super(set);
	}
	
	@Override
	public void set(StatsSet set)
	{
		super.set(set);
		_id = set.getInt("id");
		_displayId = set.getInt("displayId", _id);
		_level = set.getByte("level", (byte) 70);
		_type = set.getString("type", "L2Npc");
		_name = set.getString("name", "");
		_usingServerSideName = set.getBoolean("usingServerSideName", false);
		_title = set.getString("title", "");
		_usingServerSideTitle = set.getBoolean("usingServerSideTitle", false);
		_race = set.getEnum("race", NpcRace.class, NpcRace.ETC);
		_sex = set.getEnum("sex", Sex.class, Sex.ETC);
		
		_chestId = set.getInt("chestId", 0);
		_rhandId = set.getInt("rhandId", 0);
		_lhandId = set.getInt("lhandId", 0);
		_weaponEnchant = set.getInt("weaponEnchant", 0);
		
		_expRate = set.getDouble("expRate", 0);
		_sp = set.getDouble("sp", 0);
		_raidPoints = set.getDouble("raidPoints", 0);
		
		_unique = set.getBoolean("unique", false);
		_attackable = set.getBoolean("attackable", true);
		_targetable = set.getBoolean("targetable", true);
		_undying = set.getBoolean("undying", true);
		_showName = set.getBoolean("showName", true);
		_flying = set.getBoolean("flying", false);
		_canMove = set.getBoolean("canMove", true);
		_noSleepMode = set.getBoolean("noSleepMode", false);
		_passableDoor = set.getBoolean("passableDoor", false);
		_hasSummoner = set.getBoolean("hasSummoner", false);
		_canBeSown = set.getBoolean("canBeSown", false);
		
		_corpseTime = set.getInt("corpseTime", Config.DEFAULT_CORPSE_TIME);
		
		_aiType = set.getEnum("aiType", AIType.class, AIType.FIGHTER);
		_aggroRange = set.getInt("aggroRange", 0);
		_clanHelpRange = set.getInt("clanHelpRange", 0);
		_dodge = set.getInt("dodge", 0);
		_isChaos = set.getBoolean("isChaos", false);
		
		_soulShot = set.getInt("soulShot", 0);
		_spiritShot = set.getInt("spiritShot", 0);
		_soulShotChance = set.getInt("shotShotChance", 0);
		_spiritShotChance = set.getInt("spiritShotChance", 0);
		
		_minSkillChance = set.getInt("minSkillChance", 7);
		_maxSkillChance = set.getInt("maxSkillChance", 15);
		_primarySkillId = set.getInt("primarySkillId", 0);
		_shortRangeSkillId = set.getInt("shortRangeSkillId", 0);
		_shortRangeSkillChance = set.getInt("shortRangeSkillChance", 0);
		_longRangeSkillId = set.getInt("longRangeSkillId", 0);
		_longRangeSkillChance = set.getInt("longRangeSkillChance", 0);
		
		_collisionRadiusGrown = set.getDouble("collisionRadiusGrown", 0);
		_collisionHeightGrown = set.getDouble("collisionHeightGrown", 0);
	}
	
	@Override
	public int getId()
	{
		return _id;
	}
	
	public int getDisplayId()
	{
		return _displayId;
	}
	
	public byte getLevel()
	{
		return _level;
	}
	
	public String getType()
	{
		return _type;
	}
	
	public boolean isType(String type)
	{
		return getType().equalsIgnoreCase(type);
	}
	
	public String getName()
	{
		return _name;
	}
	
	public boolean isUsingServerSideName()
	{
		return _usingServerSideName;
	}
	
	public String getTitle()
	{
		return _title;
	}
	
	public boolean isUsingServerSideTitle()
	{
		return _usingServerSideTitle;
	}
	
	public boolean hasParameters()
	{
		return _parameters != null;
	}
	
	public StatsSet getParameters()
	{
		return _parameters;
	}
	
	public void setParameters(StatsSet set)
	{
		_parameters = set;
	}
	
	public NpcRace getRace()
	{
		return _race;
	}
	
	public Sex getSex()
	{
		return _sex;
	}
	
	public int getChestId()
	{
		return _chestId;
	}
	
	public int getRHandId()
	{
		return _rhandId;
	}
	
	public int getLHandId()
	{
		return _lhandId;
	}
	
	public int getWeaponEnchant()
	{
		return _weaponEnchant;
	}
	
	public double getExpRate()
	{
		return _expRate;
	}
	
	public double getSP()
	{
		return _sp;
	}
	
	public double getRaidPoints()
	{
		return _raidPoints;
	}
	
	public boolean isUnique()
	{
		return _unique;
	}
	
	public boolean isAttackable()
	{
		return _attackable;
	}
	
	public boolean isTargetable()
	{
		return _targetable;
	}
	
	public boolean isUndying()
	{
		return _undying;
	}
	
	public boolean isShowName()
	{
		return _showName;
	}
	
	public boolean isFlying()
	{
		return _flying;
	}
	
	public boolean canMove()
	{
		return _canMove;
	}
	
	public boolean isNoSleepMode()
	{
		return _noSleepMode;
	}
	
	public boolean isPassableDoor()
	{
		return _passableDoor;
	}
	
	public boolean hasSummoner()
	{
		return _hasSummoner;
	}
	
	public boolean canBeSown()
	{
		return _canBeSown;
	}
	
	public int getCorpseTime()
	{
		return _corpseTime;
	}
	
	public AIType getAIType()
	{
		return _aiType;
	}
	
	public int getAggroRange()
	{
		return _aggroRange;
	}
	
	public int getClanHelpRange()
	{
		return _clanHelpRange;
	}
	
	public int getDodge()
	{
		return _dodge;
	}
	
	public boolean isChaos()
	{
		return _isChaos;
	}
	
	public int getSoulShot()
	{
		return _soulShot;
	}
	
	public int getSpiritShot()
	{
		return _spiritShot;
	}
	
	public int getSoulShotChance()
	{
		return _soulShotChance;
	}
	
	public int getSpiritShotChance()
	{
		return _spiritShotChance;
	}
	
	public int getMinSkillChance()
	{
		return _minSkillChance;
	}
	
	public int getMaxSkillChance()
	{
		return _maxSkillChance;
	}
	
	public int getPrimarySkillId()
	{
		return _primarySkillId;
	}
	
	public int getShortRangeSkillId()
	{
		return _shortRangeSkillId;
	}
	
	public int getShortRangeSkillChance()
	{
		return _shortRangeSkillChance;
	}
	
	public int getLongRangeSkillId()
	{
		return _longRangeSkillId;
	}
	
	public int getLongRangeSkillChance()
	{
		return _longRangeSkillChance;
	}
	
	public Set<Integer> getClans()
	{
		return _clans;
	}
	
	/**
	 * @param clans A sorted array of clan ids
	 */
	public void setClans(Set<Integer> clans)
	{
		_clans = clans != null ? Collections.unmodifiableSet(clans) : null;
	}
	
	/**
	 * @param clanName clan name to check if it belongs to this NPC template clans.
	 * @param clanNames clan names to check if they belong to this NPC template clans.
	 * @return {@code true} if at least one of the clan names belong to this NPC template clans, {@code false} otherwise.
	 */
	public boolean isClan(String clanName, String... clanNames)
	{
		// Using local variable for the sake of reloading since it can be turned to null.
		final Set<Integer> clans = _clans;
		
		if (clans == null)
		{
			return false;
		}
		
		int clanId = NpcData.getInstance().getClanId("ALL");
		if (clans.contains(clanId))
		{
			return true;
		}
		
		clanId = NpcData.getInstance().getClanId(clanName);
		if (clans.contains(clanId))
		{
			return true;
		}
		
		for (String name : clanNames)
		{
			clanId = NpcData.getInstance().getClanId(name);
			if (clans.contains(clanId))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param clans A set of clan names to check if they belong to this NPC template clans.
	 * @return {@code true} if at least one of the clan names belong to this NPC template clans, {@code false} otherwise.
	 */
	public boolean isClan(Set<Integer> clans)
	{
		// Using local variable for the sake of reloading since it can be turned to null.
		final Set<Integer> clanSet = _clans;
		
		if ((clanSet == null) || (clans == null))
		{
			return false;
		}
		
		int clanId = NpcData.getInstance().getClanId("ALL");
		if (clanSet.contains(clanId))
		{
			return true;
		}
		
		for (Integer id : clans)
		{
			if (clanSet.contains(id))
			{
				return true;
			}
		}
		return false;
	}
	
	public Set<Integer> getEnemyClans()
	{
		return _enemyClans;
	}
	
	/**
	 * @param enemyClans A sorted array of enemy clan ids
	 */
	public void setEnemyClans(Set<Integer> enemyClans)
	{
		_enemyClans = enemyClans != null ? Collections.unmodifiableSet(enemyClans) : null;
	}
	
	/**
	 * @param clanName clan name to check if it belongs to this NPC template enemy clans.
	 * @param clanNames clan names to check if they belong to this NPC template enemy clans.
	 * @return {@code true} if at least one of the clan names belong to this NPC template enemy clans, {@code false} otherwise.
	 */
	public boolean isEnemyClan(String clanName, String... clanNames)
	{
		// Using local variable for the sake of reloading since it can be turned to null.
		final Set<Integer> enemyClans = _enemyClans;
		
		if (enemyClans == null)
		{
			return false;
		}
		
		int clanId = NpcData.getInstance().getClanId("ALL");
		if (enemyClans.contains(clanId))
		{
			return true;
		}
		
		clanId = NpcData.getInstance().getClanId(clanName);
		if (enemyClans.contains(clanId))
		{
			return true;
		}
		
		for (String name : clanNames)
		{
			clanId = NpcData.getInstance().getClanId(name);
			if (enemyClans.contains(clanId))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param clans A set of clan names to check if they belong to this NPC template enemy clans.
	 * @return {@code true} if at least one of the clan names belong to this NPC template enemy clans, {@code false} otherwise.
	 */
	public boolean isEnemyClan(Set<Integer> clans)
	{
		// Using local variable for the sake of reloading since it can be turned to null.
		final Set<Integer> enemyClans = _enemyClans;
		
		if ((enemyClans == null) || (clans == null))
		{
			return false;
		}
		
		int clanId = NpcData.getInstance().getClanId("ALL");
		if (enemyClans.contains(clanId))
		{
			return true;
		}
		
		for (Integer id : clans)
		{
			if (enemyClans.contains(id))
			{
				return true;
			}
		}
		return false;
	}
	
	public Map<DropListScope, List<IDropItem>> getDropLists()
	{
		return _dropLists;
	}
	
	public void setDropLists(Map<DropListScope, List<IDropItem>> dropLists)
	{
		_dropLists = dropLists != null ? Collections.unmodifiableMap(dropLists) : null;
	}
	
	public List<IDropItem> getDropList(DropListScope dropListScope)
	{
		Map<DropListScope, List<IDropItem>> dropLists = _dropLists;
		return dropLists != null ? dropLists.get(dropListScope) : null;
	}
	
	public List<ItemHolder> calculateDrops(DropListScope dropListScope, L2Character victim, L2Character killer)
	{
		List<IDropItem> dropList = getDropList(dropListScope);
		if (dropList == null)
		{
			return null;
		}
		
		List<ItemHolder> calculatedDrops = null;
		for (IDropItem dropItem : dropList)
		{
			List<ItemHolder> drops = dropItem.calculateDrops(victim, killer);
			if ((drops == null) || drops.isEmpty())
			{
				continue;
			}
			
			if (calculatedDrops == null)
			{
				calculatedDrops = new ArrayList<>(drops.size());
			}
			
			calculatedDrops.addAll(drops);
		}
		
		return calculatedDrops;
	}
	
	public double getCollisionRadiusGrown()
	{
		return _collisionRadiusGrown;
	}
	
	public double getCollisionHeightGrown()
	{
		return _collisionHeightGrown;
	}
	
	public void resetSkills()
	{
		for (AISkillType type : AISkillType.values())
		{
			_aiSkills.put(type, new ArrayList<L2Skill>());
		}
		_skills.clear();
	}
	
	public static boolean isAssignableTo(Class<?> sub, Class<?> clazz)
	{
		// If clazz represents an interface
		if (clazz.isInterface())
		{
			// check if obj implements the clazz interface
			Class<?>[] interfaces = sub.getInterfaces();
			for (Class<?> interface1 : interfaces)
			{
				if (clazz.getName().equals(interface1.getName()))
				{
					return true;
				}
			}
		}
		else
		{
			do
			{
				if (sub.getName().equals(clazz.getName()))
				{
					return true;
				}
				
				sub = sub.getSuperclass();
			}
			while (sub != null);
		}
		return false;
	}
	
	/**
	 * Checks if obj can be assigned to the Class represented by clazz.<br>
	 * This is true if, and only if, obj is the same class represented by clazz, or a subclass of it or obj implements the interface represented by clazz.
	 * @param obj
	 * @param clazz
	 * @return {@code true} if the object can be assigned to the class, {@code false} otherwise
	 */
	public static boolean isAssignableTo(Object obj, Class<?> clazz)
	{
		return L2NpcTemplate.isAssignableTo(obj.getClass(), clazz);
	}
	
	public void addAtkSkill(L2Skill skill)
	{
		getAtkSkills().add(skill);
	}
	
	public void addBuffSkill(L2Skill skill)
	{
		getBuffSkills().add(skill);
	}
	
	public void addCOTSkill(L2Skill skill)
	{
		getCostOverTimeSkills().add(skill);
	}
	
	public void addDebuffSkill(L2Skill skill)
	{
		getDebuffSkills().add(skill);
	}
	
	public void addGeneralSkill(L2Skill skill)
	{
		getGeneralskills().add(skill);
	}
	
	public void addHealSkill(L2Skill skill)
	{
		getHealSkills().add(skill);
	}
	
	public void addImmobiliseSkill(L2Skill skill)
	{
		getImmobiliseSkills().add(skill);
	}
	
	public void addNegativeSkill(L2Skill skill)
	{
		getNegativeSkills().add(skill);
	}
	
	public void addQuestEvent(QuestEventType eventType, Quest q)
	{
		if (!_questEvents.containsKey(eventType))
		{
			_questEvents.put(eventType, new ArrayList<Quest>());
		}
		
		if (!eventType.isMultipleRegistrationAllowed() && !_questEvents.get(eventType).isEmpty())
		{
			_log.warning("Quest event not allowed in multiple quests.  Skipped addition of Event Type \"" + eventType + "\" for NPC \"" + _name + "\" and quest \"" + q.getName() + "\".");
		}
		else
		{
			_questEvents.get(eventType).add(q);
		}
	}
	
	public void removeQuest(Quest q)
	{
		for (Entry<QuestEventType, List<Quest>> entry : _questEvents.entrySet())
		{
			if (entry.getValue().contains(q))
			{
				Iterator<Quest> it = entry.getValue().iterator();
				while (it.hasNext())
				{
					Quest q1 = it.next();
					if (q1 == q)
					{
						it.remove();
					}
				}
				
				if (entry.getValue().isEmpty())
				{
					_questEvents.remove(entry.getKey());
				}
			}
		}
	}
	
	public void addMinionData(L2MinionData minion)
	{
		_minions.add(minion);
	}
	
	public void addRangeSkill(L2Skill skill)
	{
		if ((skill.getCastRange() <= 150) && (skill.getCastRange() > 0))
		{
			getShortRangeSkills().add(skill);
		}
		else if (skill.getCastRange() > 150)
		{
			getLongRangeSkills().add(skill);
		}
	}
	
	public void addResSkill(L2Skill skill)
	{
		getResSkills().add(skill);
	}
	
	public void addSkill(L2Skill skill)
	{
		if (!skill.isPassive())
		{
			if (skill.isSuicideAttack())
			{
				addSuicideSkill(skill);
			}
			else
			{
				addGeneralSkill(skill);
				
				if (skill.isContinuous())
				{
					if (!skill.isDebuff())
					{
						addBuffSkill(skill);
					}
					else
					{
						addDebuffSkill(skill);
						addCOTSkill(skill);
						addRangeSkill(skill);
					}
				}
				else if (skill.getSkillType() == L2SkillType.DUMMY)
				{
					if (skill.hasEffectType(L2EffectType.DISPEL, L2EffectType.DISPEL_BY_SLOT))
					{
						addNegativeSkill(skill);
						addRangeSkill(skill);
					}
					else if (skill.hasEffectType(L2EffectType.HEAL, L2EffectType.HEAL_PERCENT))
					{
						addHealSkill(skill);
					}
					else if (skill.hasEffectType(L2EffectType.PHYSICAL_ATTACK, L2EffectType.PHYSICAL_ATTACK_HP_LINK, L2EffectType.FATAL_BLOW, L2EffectType.ENERGY_ATTACK, L2EffectType.MAGICAL_ATTACK_MP, L2EffectType.MAGICAL_ATTACK, L2EffectType.DEATH_LINK, L2EffectType.HP_DRAIN))
					{
						addAtkSkill(skill);
						addUniversalSkill(skill);
						addRangeSkill(skill);
					}
					else if (skill.hasEffectType(L2EffectType.SLEEP))
					{
						addImmobiliseSkill(skill);
					}
					else if (skill.hasEffectType(L2EffectType.STUN, L2EffectType.ROOT))
					{
						addImmobiliseSkill(skill);
						addRangeSkill(skill);
					}
					else if (skill.hasEffectType(L2EffectType.MUTE, L2EffectType.FEAR))
					{
						addCOTSkill(skill);
						addRangeSkill(skill);
					}
					else if (skill.hasEffectType(L2EffectType.PARALYZE))
					{
						addImmobiliseSkill(skill);
						addRangeSkill(skill);
					}
					else if (skill.hasEffectType(L2EffectType.DMG_OVER_TIME, L2EffectType.DMG_OVER_TIME_PERCENT))
					{
						addRangeSkill(skill);
					}
					else if (skill.hasEffectType(L2EffectType.RESURRECTION))
					{
						addResSkill(skill);
					}
					else
					{
						addUniversalSkill(skill);
					}
				}
			}
		}
		
		_skills.put(skill.getId(), skill);
	}
	
	public void addSuicideSkill(L2Skill skill)
	{
		getSuicideSkills().add(skill);
	}
	
	public void addTeachInfo(List<ClassId> teachInfo)
	{
		_teachInfo.addAll(teachInfo);
	}
	
	public void addUniversalSkill(L2Skill skill)
	{
		getUniversalSkills().add(skill);
	}
	
	public boolean canTeach(ClassId classId)
	{
		// If the player is on a third class, fetch the class teacher
		// information for its parent class.
		if (classId.level() == 3)
		{
			return _teachInfo.contains(classId.getParent());
		}
		return _teachInfo.contains(classId);
	}
	
	/**
	 * @return the attack skills.
	 */
	public List<L2Skill> getAtkSkills()
	{
		return _aiSkills.get(AISkillType.ATTACK);
	}
	
	/**
	 * @return the buff skills.
	 */
	public List<L2Skill> getBuffSkills()
	{
		return _aiSkills.get(AISkillType.BUFF);
	}
	
	/**
	 * @return the cost over time skills.
	 */
	public List<L2Skill> getCostOverTimeSkills()
	{
		return _aiSkills.get(AISkillType.COT);
	}
	
	/**
	 * @return the debuff skills.
	 */
	public List<L2Skill> getDebuffSkills()
	{
		return _aiSkills.get(AISkillType.DEBUFF);
	}
	
	public Map<QuestEventType, List<Quest>> getEventQuests()
	{
		return _questEvents;
	}
	
	public List<Quest> getEventQuests(QuestEventType EventType)
	{
		return _questEvents.get(EventType);
	}
	
	/**
	 * @return the general skills.
	 */
	public List<L2Skill> getGeneralskills()
	{
		return _aiSkills.get(AISkillType.GENERAL);
	}
	
	/**
	 * @return the heal skills.
	 */
	public List<L2Skill> getHealSkills()
	{
		return _aiSkills.get(AISkillType.HEAL);
	}
	
	/**
	 * @return the immobilize skills.
	 */
	public List<L2Skill> getImmobiliseSkills()
	{
		return _aiSkills.get(AISkillType.IMMOBILIZE);
	}
	
	/**
	 * @return the long range skills.
	 */
	public List<L2Skill> getLongRangeSkills()
	{
		return _aiSkills.get(AISkillType.LONG_RANGE);
	}
	
	/**
	 * @return the list of all Minions that must be spawn with the L2NpcInstance using this L2NpcTemplate.
	 */
	public List<L2MinionData> getMinionData()
	{
		return _minions;
	}
	
	/**
	 * @return the negative skills.
	 */
	public List<L2Skill> getNegativeSkills()
	{
		return _aiSkills.get(AISkillType.NEGATIVE);
	}
	
	/**
	 * @return the resurrection skills.
	 */
	public List<L2Skill> getResSkills()
	{
		return _aiSkills.get(AISkillType.RES);
	}
	
	/**
	 * @return the short range skills.
	 */
	public List<L2Skill> getShortRangeSkills()
	{
		return _aiSkills.get(AISkillType.SHORT_RANGE);
	}
	
	/**
	 * @return the universal skills.
	 */
	public List<L2Skill> getUniversalSkills()
	{
		return _aiSkills.get(AISkillType.UNIVERSAL);
	}
	
	public List<L2Skill> getSuicideSkills()
	{
		return _aiSkills.get(AISkillType.SUICIDE);
	}
	
	@Override
	public Map<Integer, L2Skill> getSkills()
	{
		return _skills;
	}
	
	public List<ClassId> getTeachInfo()
	{
		return _teachInfo;
	}
}
