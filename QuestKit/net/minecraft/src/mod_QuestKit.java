package net.minecraft.src;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import net.minecraft.client.Minecraft;

/**
* The ModLoader mod that provides support utilities for hooking into keybinds and displaying notifications. A singleton instance.
*/
public class mod_QuestKit extends BaseMod {
	private GuiQuestNotification gui;
	private KeyBinding reservedBinding = new KeyBinding("QuestKit Reserved", org.lwjgl.input.Keyboard.KEY_L);
	private KeyBinding guiBinding = new KeyBinding("Quests", org.lwjgl.input.Keyboard.KEY_H);
	private LinkedBlockingQueue<GuiScreen> notificationGuiQueue = new LinkedBlockingQueue<GuiScreen>();
	
	private static mod_QuestKit instance;
	/**
	* Get or create the singleton instance.
	* 
	* @return The singleton instance.
	*/
	public static mod_QuestKit getModLoadedInstance() {
		if(instance != null) {
			return instance;
		} else {
	        List<BaseMod> allMods = ModLoader.getLoadedMods();
			for(Iterator<BaseMod> it = allMods.iterator(); it.hasNext();) {
				BaseMod curr = it.next();
				if(curr.toString() == "QuestKit")
					instance = (mod_QuestKit)curr;
			}
			if(instance != null) {
				return instance;
			} else {
				throw new IllegalStateException("ModLoader hasn't loaded our QuestKit mod.");
			}
		}
	}
	
	/**
	* For ModLoader's eyes only.
	*/
	public mod_QuestKit() {		
		ModLoader.RegisterEntityID(ItemQuestProposal.class, "New quest", ModLoader.getUniqueEntityId());
		new ItemQuestProposal(0); //force MC to recognize our entity.
		
		gui = new GuiQuestNotification(ModLoader.getMinecraftInstance());
		ModLoader.SetInGameHook(this, true, false);
		ModLoader.SetInGUIHook(this, true, false);
		//ModLoader.RegisterKey(this, reservedBinding, false);
		ModLoader.RegisterKey(this, guiBinding, false);
	}
	
	private Boolean questBrowserIsOpen = false;
	public void KeyboardEvent(KeyBinding keybinding) {
		if(keybinding == reservedBinding) {
			World theWorld = ModLoader.getMinecraftInstance().theWorld;
			theWorld.questCollection.proposeQuest(new DebugQuest());
			theWorld.questCollection.proposeQuest(new ToolCollectorQuest());
			/*
			theWorld.createExplosion(null, 7.8D, 70.62D, -60.63D, 4F);
			
			ItemQuestProposal item = new ItemQuestProposal(0);
			item.setQuest(new DebugQuest());
			EntityItem entity = new EntityItem(theWorld, 7.8D, 70.62D, -60.63D, new ItemStack(item));
			theWorld.entityJoinedWorld(entity);
			
			
			ItemQuestProposal item2 = new ItemQuestProposal(0);
			item2.setQuest(new ToolCollectorQuest());
			EntityItem entity2 = new EntityItem(theWorld, 7.8D, 70.62D, -60.63D, new ItemStack(item2));
			theWorld.entityJoinedWorld(entity2); */
			
		} else if(keybinding == guiBinding) {
			if(questBrowserIsOpen) {
				questBrowserIsOpen = false;
				ModLoader.getMinecraftInstance().displayGuiScreen(null);
			} else {
				Minecraft mc = ModLoader.getMinecraftInstance();
				questBrowserIsOpen = true;
				Deque<IQuest> allQuests = mc.theWorld.questCollection.getReadOnlyQuests();
				mc.displayGuiScreen(new GuiQuestBrowserPaginated(allQuests));
			}
		}
	}
	
	@Override
	public String Version() {
		return "QuestKit pre-alpha";
	}
	@Override
	public String toString() {
		return "QuestKit";
	}
	
	public boolean OnTickInGame(Minecraft game) {
		if(game.currentScreen == null || game.currentScreen instanceof GuiUnused) {
			GuiScreen notification = notificationGuiQueue.poll();
			if(notification != null)
				game.displayGuiScreen(notification);
		}
        //gui.updateWindow();
        return true;
	}
	public boolean OnTickInGUI(Minecraft game) {
		//gui.updateWindow();
		return true;
	}
	
	/**
	* Notify via GUI that a quest has been added.
	* 
	* @param quest The quest in question.
	*/
	public void notifyQuestAdd(IQuest quest) {
		notificationGuiQueue.offer(new GuiQuestProgress(quest));
	}	
	/**
	* Notify via GUI that a quest has been updated.
	* 
	* @param quest The quest in question.
	*/
	public void notifyQuestChanged(IQuest quest) {
		notificationGuiQueue.offer(new GuiQuestProgress(quest));
	}
	/**
	* Propose a quest to the user via GUI.
	* 
	* @param quest The quest in question.
	*/
	public void proposeQuest(IQuest quest) {
		notificationGuiQueue.offer(new GuiQuestProposal(quest));
	}
}
