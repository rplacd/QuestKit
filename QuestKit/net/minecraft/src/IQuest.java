package net.minecraft.src;
import java.lang.Exception;
import java.util.List;

import net.minecraft.client.Minecraft;

/**
 * The public interface for quests.
 * 
 * Quests are conceptually finite state machines - the state is stored in an internal QuestState. They are fed input on very tick and when the parent QuestCollection is notified with a message. There are two terminal states available representing success and failure - see QuestState.
 * In addition to this they provide some basic property facilities and access to a log.
 * The main implementation - JavaQuestBase - does Some Reflection Magic for state despatching in Java and logging. At this point, though, the implementation possibilities are open since properties aren't hooked up to fields proper, and state dispatching isn't implemented here either.
 * If a scripting language should ever come into vogue there are bound to be more elegant ways to implement state dispatching.
 */
public interface IQuest extends INBTSerializable {
	/**
	 * Get the quest's state log.
	 * 
	 * Previous states are ordered by time, descending: the first item will be the last state, and the last item will be the first state.
	 *
	 * @return Previous QuestStates.
	 */
	public List<QuestState> getLog();	
	/**
	 * Get the quest's name.
	 */
	public String getName();	
	/**
	 * Get the quest's description.
	 */
	public String getDescription();	
	/**
	 * Get the quest's state.
	 */
	public QuestState getState();	 
	/**
	 * Process an update tick. Implementers should dispatch on state here.
	 * 
	 * @param minecraft  The current Minecraft instance, as per ModLoader.
	 * @return The quest's state.
	 */
	public void tick(Minecraft minecraft) throws Exception;	
	/**
	 * Process an notification passed to the QuestCollection. Implementers should dispatch on state here.
	 * 
	 * @param minecraft  The current Minecraft instance, as per ModLoader.
	 * @param msg The "message" parameter.
	 * @return The quest's state.
	 */
	public void notify(Minecraft minecraft, Object msg) throws Exception;
}
