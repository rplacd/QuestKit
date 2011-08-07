package net.minecraft.src;

/**
* Create a dialog with a white "category" title, a inner title, and an inner body supporting multiline text.
*/
public class GuiQuestDialog extends GuiScreen {	
	private GuiMultilineText description;
	
	private String title;
	private String dialogTitle;
	private String dialogBody;
	
	protected void setContents(String newTitle, String newDTitle, String newDBody) {
		title = newTitle;
		dialogTitle = newDTitle;
		dialogBody = newDBody;
	}

	public GuiQuestDialog() {
		this("", "", "");
	}

	public GuiQuestDialog(String newTitle, String newDTitle, String newDBody) {
		super();
		title = newTitle;
		dialogTitle = newDTitle;
		dialogBody = newDBody;
	}
	
	public void initGui() {
		controlList.clear();
	}

	public void drawScreen(int _i, int _j, float _f) {
		drawDefaultBackground();
	    drawCenteredString(fontRenderer, title, width / 2, 40, 0xffffff);
	    
	    int titleWidth = fontRenderer.getStringWidth(dialogTitle);
	    int largerWidth;
	    if(titleWidth > 100)
	    	largerWidth = titleWidth;
	    else
	    	largerWidth = 100;
	    int padding = 10;
	    int left = (width/2) - largerWidth - padding;
	    int right = (width/2) + largerWidth + padding;
		description = new GuiMultilineText(this, dialogBody, left + padding, 100, right - padding, 0x805E00);
	    int top = 70;
	    int bottom = top + padding + 9 + padding + description.getHeight() + padding;
	    
	    drawGradientRect(left, top, right, bottom, 0xFFFEFFD4, 0xFFFEFFD4);
	    drawGradientRect(left, top, right, top+10, 0xFF757561, 0x00FEFFD4);
	    drawGradientRect(left, bottom-10, right, bottom, 0x00FEFFD4, 0xFF757561);
	    fontRenderer.drawString(dialogTitle, left + padding, top+padding, 0x594100);
	    description.drawMultilineText();
	    
		super.drawScreen(_i, _j, _f);
	}
	
	protected void actionPerformed(GuiButton guibutton)
    {
        if(!guibutton.enabled) {
            return;
        }
    }

}