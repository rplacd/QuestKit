package net.minecraft.src;
import java.util.Deque;

public abstract class GuiQuestBrowserBase extends GuiScreen {
	protected Deque<IQuest> allQuests;

	protected int padding;
	protected int left;
	protected int right;
	protected int top;
	protected int bottom;
	protected int center;
	protected int controlStripTop;
	protected int bottomControlStripTop;
	
	public GuiQuestBrowserBase(Deque<IQuest> newAllQuests) {
		this.allQuests = newAllQuests;
	}
	
	public void initGui() {
		padding = 10;
		left = width / 2 - 100;
		right = width / 2 + 100;
		top = (int) (padding * 2.5);
		bottom = height;
		center = width / 2;
		controlStripTop = top + padding - 5;
		bottomControlStripTop = bottom - (padding * 4) + 10;
		
		
		controlList.clear();
		//first param is ID.
		controlList.add(new GuiButton(-1, width / 2 - 190 - padding, controlStripTop - 25, 90, 20, "Remove finished"));
		controlList.add(new GuiButton(-2, width / 2 + 100 + padding, controlStripTop - 25, 90, 20, "Remove all"));
		
	    controlList.add(new GuiButton(0, width / 2 - 190 - padding, controlStripTop, 90, 20, "Previous"));
	    controlList.add(new GuiButton(1, width / 2 + 100 + padding, controlStripTop, 90, 20, "Next"));
	    
	    controlList.add(new GuiButton(3, width / 2 - 80 - padding, bottomControlStripTop, 20, 20, "<"));
	    controlList.add(new GuiButton(4, width / 2 + 60 + padding, bottomControlStripTop, 20, 20, ">"));
	}
	
	private String noMsg = "You don't have any quests.";
	private String noMsg2 = "The world is your oyster -";
	private String noMsg3 = "go find one!";
	public void drawScreen(int _i, int _j, float _f) {		
		drawDefaultBackground();
		drawGradientRect(left, top, right, bottom, 0xFFFEFFD4, 0xFFFEFFD4);
	    drawGradientRect(left, top, right, top+10, 0xFF757561, 0x00FEFFD4);
	    drawGradientRect(left, bottom-10, right, bottom, 0x00FEFFD4, 0xFF757561);
	    drawCenteredString(fontRenderer, "All quests", center, padding, 0xffffff);
	    
	    if(allQuests.size() < 1) {
	    	fontRenderer.drawString(noMsg, center - (fontRenderer.getStringWidth(noMsg) / 2), height/2 - padding, 0x594100);
	    	fontRenderer.drawString(noMsg2, center - (fontRenderer.getStringWidth(noMsg2) / 2), height/2, 0x594100);
	    	fontRenderer.drawString(noMsg3, center - (fontRenderer.getStringWidth(noMsg3) / 2), height/2 + padding, 0x594100);
	    } else {
	    	drawPaginatedContent();				
	    	//int titleHeight = top + padding;
	    	//String progressTop = String.format("%d/%d", index, allQuests.size());
	    	//fontRenderer.drawString(progressTop, right - padding - (fontRenderer.getStringWidth(progressTop)), titleHeight, 0x594100);
	    	//fontRenderer.drawString(selectedQuest.getName(), left + padding, titleHeight, 0x594100);
			//GuiMultilineText description = new GuiMultilineText(this, selectedQuest.getDescription(), left + padding, titleHeight + padding, right - padding, 0x805E00);
			//description.drawMultilineText();
    	    
	    	//finally, draw the buttons on top.
	    	super.drawScreen(_i, _j, _f);
	    }
	}
	
	public abstract void drawPaginatedContent();
}
