package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class ToolCollectorQuest extends JavaQuestBase {
	public String getName() {
		return "Tool Collector";
	}
	public String getDescription() {
		return "Collect ALL the tools!";
	}
	
	public ToolCollectorQuest() {
		state = new QuestState("FourLeft", "Four tools left to go! Padding Padding Padding Padding Padding Padding Padding Padding Padding Padding Padding Padding Padding Padding");
	}
	

	boolean hasPickaxe = false;
	boolean hasSpade = false;
	boolean hasHoe = false;
	boolean hasAxe = false;
	private boolean shouldContinue(Object msg) {
		if(msg == null)
			return false;
		if(msg.getClass() != ItemStack.class)
			return false;
		Item item = ((ItemStack)msg).getItem();
		if(hasPickaxe == false && item.getClass() == ItemPickaxe.class) {
			hasPickaxe = true;
			return true;
		} else if(hasSpade == false && item.getClass() == ItemSpade.class) {
			hasSpade = true;
			return true;
		} else if(hasHoe == false && item.getClass() == ItemHoe.class) {
			hasHoe = true;
			return true;
		} else if(hasAxe == false && item.getClass() == ItemAxe.class) {
			hasAxe = true;
			return true;
		} else {
			return false;
		}
	}
	
	public void notify_FourLeft(Minecraft mc, Object msg) {
		if(shouldContinue(msg))
			state = new QuestState("ThreeLeft", "One down, three tools left! Padding Padding Padding Padding Padding Padding Padding Padding Padding Padding Padding Padding Padding Padding");
	}
	public void notify_ThreeLeft(Minecraft mc, Object msg) {
		if(shouldContinue(msg))
			state = new QuestState("TwoLeft", "Two tools left. OOooo");
	}
	public void notify_TwoLeft(Minecraft mc, Object msg) {
		if(shouldContinue(msg))
			state = new QuestState("OneLeft", "One tool left - I can't wait! Padding Padding Padding Padding Padding Padding Padding Padding Padding Padding Padding Padding Padding Padding");
	}
	public void notify_OneLeft(Minecraft mc, Object msg) {
		if(shouldContinue(msg))
			state = new QuestState("Success", "You, sir, are a bona fide Tool Collector. Reward? What reward?");
	}

	public void readFromNBT(NBTTagCompound source) {
		super.readFromNBT(source);
		hasPickaxe = source.getBoolean("HasPickaxe");
		hasSpade = source.getBoolean("HasSpade");
		hasHoe = source.getBoolean("HasHoe");
		hasAxe = source.getBoolean("HasAxe");
	}
	public void writeToNBT(NBTTagCompound target) {
		super.writeToNBT(target);
		target.setBoolean("HasPickaxe", hasPickaxe);
		target.setBoolean("HasSpade", hasSpade);
		target.setBoolean("HasHoe", hasHoe);
		target.setBoolean("HasAxe", hasAxe);
	}
}
