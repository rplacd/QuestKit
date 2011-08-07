package net.minecraft.src;
import net.minecraft.client.Minecraft;

public class ItemQuestProposal extends Item {
	private IQuest quest;
	public IQuest getQuest() {
		return quest;
	}
	public void setQuest(IQuest quest) {
		this.quest = quest;
	}

	public ItemQuestProposal(int index) {
		super(index);
		setItemName("New quest");
		setIconIndex(60);
	}
	
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
		//mod_QuestKit.getModLoadedInstance().notify("Poof!", "I'm gone.");
		itemstack.stackSize--;
		if(quest == null) {
			return itemstack;
		} else {
			Minecraft mc = ModLoader.getMinecraftInstance();
			mc.displayGuiScreen(new GuiQuestProposal(this.quest));
			return itemstack;
		}
	}
	
}
