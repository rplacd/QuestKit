package net.minecraft.src;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayDeque;
import net.minecraft.client.Minecraft;

/**
* Contains a bunch of quests - ostensibly linked to a player.
* 
* Provides an interface to the container for adding, removing, and "spring cleaning" quests, as well as ticking and notifying them.
* Currently hooked up to Minecraft's World class because of the Player class' habit of clearing itself out on death.
*/
public class QuestCollection extends Object implements INBTSerializable{
	private mod_QuestKit questKit;
	private List<IQuest> quests;
	
	/**
	 * Create a new quest collection with a mod_QuestKit instance.
	 *
	 * @param newQuestKit The mod_QuestKit instance to be kept.
	 */
	public QuestCollection(mod_QuestKit newQuestKit) {
		questKit = newQuestKit;
		quests = new LinkedList<IQuest>();
	}
	
	/**
	 * Ask the player to accept or reject a quest.
	 *
	 * @param newQuest Quest to add.
	 */
	public void proposeQuest(IQuest newQuest) {
		mod_QuestKit.getModLoadedInstance().proposeQuest(newQuest);
	}
	/**
	 * Add a quest to the collection.
	 *
	 * @param newQuest Quest to add.
	 */
	public void addQuest(IQuest newQuest) {
		quests.add(newQuest);
		//notify here
		questKit.notifyQuestAdd(newQuest);
	}	
	/**
	 * Remove quests that have a terminal state set.
	 */
	public void springCleanQuests() {
		for(Iterator<IQuest> it = quests.iterator(); it.hasNext();) {
			IQuest curr = it.next();
			if(curr.getState().isTerminal())
				it.remove();
		}
	}	
	/**
	 * Remove all quests.
	 */
	public void clearQuests() {
		quests.clear();
	}	
	/**
	 * Get a readonly copy of the collection's quests in deque form for easy iteration.
	 * 
	 * @return A deque of the quests available.
	 */
	public ArrayDeque<IQuest> getReadOnlyQuests() {
		return new ArrayDeque<IQuest>(quests);
	}
	
	/**
	 * Notify contained quests of a specific type with a message and the current Minecraft instance. This is one of two ways to provide stimulus to quest FSMs.
	 * 
	 * @param questType A Class representing the type of quest we want to notify, or null for a wildcard.
	 * @param msg A object that contains message information.
	 */
	public void notifyQuests(Class questType, Object msg) {
		Minecraft mc = ModLoader.getMinecraftInstance();
		try {
		for(IQuest quest : quests) {
			if(questType == null || quest.getClass() == questType) {
				if(!quest.getState().isTerminal()) {
					QuestState formerState = quest.getState();
					quest.notify(mc, msg);
					QuestState latterState = quest.getState();
					if(!formerState.equals(latterState)) {
						//notify here
						questKit.notifyQuestChanged(quest);
					}
				}
			}
		}
		} catch (Exception e) {
			ModLoader.ThrowException("A quest threw an exception while accepting a message", e);
		}
	}	
	
	/**
	 * Tick quests contained in this collection with a provided Minecraft instance. This is one of two ways of providing stimulus to quest FSMs.
	 * 
	 * @param game A Minecraft instance.
	 */
	public boolean updateQuests(Minecraft game) {
		try {
			for(IQuest quest : quests) {
				if(!quest.getState().isTerminal()) {
					QuestState formerState = quest.getState();
					quest.tick(game);
					QuestState latterState = quest.getState();
					if(!formerState.equals(latterState)) {
						//notify here
						questKit.notifyQuestChanged(quest);
					}
				}
			}
		} catch (Exception e) {
			ModLoader.ThrowException("A quest threw an exception while updating itself.", e);
		}
		return true;
	}
	
	//the idea: by modifying the player tag in WorldInfo we hook on silently as Player compound gets written to disk.
	//any pair in our quest pairs - any key, yes, because we're treating our compound as an array - is an eligible quest.
	public void readFromNBT(NBTTagCompound source) {
		int numQuests = source.getInteger("QuestIndices");
		NBTTagCompound allQuests = source.getCompoundTag("Quests");
		NBTTagCompound questClasses = source.getCompoundTag("QuestClasses");
		
		//dear oracle, checked exceptions are evil.
		if(numQuests > 0) {
			for(Integer i = 1; i <= numQuests; ++i) {
				String string_class = questClasses.getString(i.toString());
				try {
					Class questClass = Class.forName(string_class);
					IQuest newQuest = (IQuest) questClass.getDeclaredConstructor().newInstance();
					newQuest.readFromNBT(allQuests.getCompoundTag(i.toString()));
					quests.add(newQuest);
				} catch (ClassNotFoundException e) {
					ModLoader.ThrowException("The save has a quest that hasn't been installed.", e);
				} catch (SecurityException e) {
					ModLoader.ThrowException("The save has quest that's not allowing me to construct it from a save.", e);
				} catch (NoSuchMethodException e) {
					ModLoader.ThrowException("The save has a quest that hasn't been programmed to be constructed from a save.", e);
				} catch (IllegalArgumentException e) {
					ModLoader.ThrowException("The save has a quest that hasn't been programmed to be constructed from a save.", e);
				} catch (InstantiationException e) {
					ModLoader.ThrowException("The save has a quest that hasn't been programmed to be constructed from a save.", e);
				} catch (IllegalAccessException e) {
					ModLoader.ThrowException("The save has a quest that hasn't been programmed to be constructed from a save.", e);
				} catch (InvocationTargetException e) {
					ModLoader.ThrowException("The save has a quest that hasn't been programmed to be constructed from a save.", e);
				}
			}
		}
	}
	public void writeToNBT(NBTTagCompound target) {
		NBTTagCompound allQuests = new NBTTagCompound();
		NBTTagCompound questClasses = new NBTTagCompound();
		
		if(quests.size() > 0) {
			Integer index = 1;
			for(IQuest quest : quests) {
				questClasses.setString(index.toString(), quest.getClass().getName());
				NBTTagCompound questLocal = new NBTTagCompound();
				quest.writeToNBT(questLocal);
				allQuests.setCompoundTag(index.toString(), questLocal);
				index++;
			}
		}
		
		target.setInteger("QuestIndices", quests.size());
		target.setCompoundTag("Quests", allQuests);
		target.setCompoundTag("QuestClasses", questClasses);
	}
}