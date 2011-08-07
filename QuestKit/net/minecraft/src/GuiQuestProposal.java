package net.minecraft.src;

/**
* A GUI notification that asks whether the user wants to accept or reject a quest.
*/
public class GuiQuestProposal extends GuiQuestDialog {
	private IQuest quest;
		
	//created by an ItemQuestProposal
	public GuiQuestProposal(IQuest newQuest) {
		super("New quest:", newQuest.getName(), newQuest.getDescription());
		this.quest = newQuest;
	}
	
	public void initGui() {
		controlList.clear();
		//first param is ID.
	    controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 120, 90, 20, "Reject"));
	    controlList.add(new GuiButton(1, width / 2 + 10, height / 4 + 120, 90, 20, "Accept"));
	}
	
	public void drawScreen(int _i, int _j, float _f) {
		super.drawScreen(_i, _j, _f);
	}
	
	protected void actionPerformed(GuiButton guibutton)
    {
        if(!guibutton.enabled) {
            return;
        }
        if(guibutton.id == 0) {
            mc.displayGuiScreen(null);
        } else if(guibutton.id == 1){
        	mc.displayGuiScreen(null);
        	ModLoader.getMinecraftInstance().theWorld.questCollection.addQuest(quest);
        }
    }
}
