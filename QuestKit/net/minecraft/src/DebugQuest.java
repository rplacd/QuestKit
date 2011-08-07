package net.minecraft.src;
import net.minecraft.client.Minecraft;

public class DebugQuest extends JavaQuestBase {
	public String getName() {
		return "Death Run";
	}
	public String getDescription() {
		return "A quest for the unknown. Enter a journey of self-discovery; die.";
	}
	
	public DebugQuest() {
		state = new QuestState("Must Die", "Seek the end");
	}
	
	public void notify_default(Minecraft mc, Object msg) {
		if(msg.toString() == "Death")
			state = new QuestState("Success", "You have left the mortal plane");
	}
}
