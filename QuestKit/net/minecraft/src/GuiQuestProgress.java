package net.minecraft.src;

/**
* A GUI notification that appears when a quest is updated.
*/
public class GuiQuestProgress extends GuiQuestDialog {
	public GuiQuestProgress(IQuest quest) {
		QuestState state = quest.getState();
		String title;
		if(state.isTerminalSuccess()) {
			title = "You succeed in your quest:";
		} else if(state.isTerminalFailure()) {
			title = "You fail in your quest:";
		} else {
			title = "Your quest continues...";
		}
		setContents(title, quest.getName(), state.getDescription());
	}

}
