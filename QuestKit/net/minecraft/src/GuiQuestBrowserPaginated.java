package net.minecraft.src;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

/**
* The ModLoader mod that provides support utilities for hooking into keybinds and displaying notifications. A singleton instance.
*/
public class GuiQuestBrowserPaginated extends GuiQuestBrowserBase {
	private class Page {
		public int titleColor;
		int titleTop;
		GuiMultilineText titleText;
		int descTop;
		GuiMultilineText descText;
		
		int subPageTop;
		LinkedList<LinkedList<GuiMultilineText>> subPages;
		
		public Page(IQuest quest) {			
			GuiQuestBrowserPaginated superThis = GuiQuestBrowserPaginated.this;
			if(quest.getState().isTerminalFailure()) {
				titleColor = 0xE01B4C;
			} else if(quest.getState().isTerminalSuccess()) {
				titleColor = 0x74E01B;
			} else {
				titleColor = 0x594100;
			}
			
			titleTop = top + padding;
			titleText = new GuiMultilineText(superThis, quest.getName(), left + padding, titleTop, right - padding, titleColor);
			descTop = titleTop + titleText.getHeight() + (padding/2); //only place we're doing padding/2
			descText = new GuiMultilineText(superThis, quest.getDescription(), left + padding, descTop, right - padding, 0x805E00);
			
			subPageTop = descTop + descText.getHeight() + padding;
			subPages = new LinkedList<LinkedList<GuiMultilineText>>();		
			
			{
				LinkedList<GuiMultilineText> accumulatingPage = new LinkedList<GuiMultilineText>();
				int subPageBottom = bottomControlStripTop;
				int subPageHeight = subPageBottom - subPageTop;
				int spaceLeft = subPageBottom - subPageTop;
				int spaceWidth = padding/2;
				
				LinkedList<QuestState> allStates = new LinkedList<QuestState>();
				allStates.addAll(quest.getLog());
				allStates.addFirst(quest.getState());
				
				for(QuestState state : allStates) {
					if(GuiMultilineText.predictHeight(superThis, state.getDescription(), left + padding, right - padding) + spaceWidth > subPageHeight) {
						if(subPages.size() > 0) {
							subPages.addLast(accumulatingPage);
							accumulatingPage = new LinkedList<GuiMultilineText>();
							spaceLeft = bottomControlStripTop - subPageTop;
						}
						accumulatingPage.addLast(new GuiMultilineText(superThis, state.getDescription(), left + padding, subPageBottom - spaceLeft, right - padding, 0x594100));
						
						subPages.addLast(accumulatingPage);
						accumulatingPage = new LinkedList<GuiMultilineText>();
						spaceLeft = bottomControlStripTop - subPageTop;
					} else if(GuiMultilineText.predictHeight(superThis, state.getDescription(), left + padding, right - padding) + spaceWidth > spaceLeft) {
						subPages.addLast(accumulatingPage);
						accumulatingPage = new LinkedList<GuiMultilineText>();
						spaceLeft = bottomControlStripTop - subPageTop;
						
						GuiMultilineText firstState = new GuiMultilineText(superThis, state.getDescription(), left + padding,  subPageBottom - spaceLeft, right - padding, 0x594100);
						accumulatingPage.addLast(firstState);
						spaceLeft = spaceLeft - firstState.getHeight() - spaceWidth;
					} else {
						GuiMultilineText newState = new GuiMultilineText(superThis, state.getDescription(), left + padding,  subPageBottom - spaceLeft, right - padding, 0x594100);
						accumulatingPage.addLast(newState);
						spaceLeft = spaceLeft - newState.getHeight() - spaceWidth;
					}
				}			
				if(accumulatingPage.size() != 0)
					subPages.add(accumulatingPage);
			}
		}
		
		public void drawPage(int subpage) {
			titleText.drawMultilineText();
			descText.drawMultilineText();
			try {
				LinkedList<GuiMultilineText> subPage = subPages.get(subpage);
				for(GuiMultilineText slot : subPage) {
					slot.drawMultilineText();
				}
			} catch(IndexOutOfBoundsException e) {
				String strangeMessage = "Very strange.";
				fontRenderer.drawString(strangeMessage, center - (fontRenderer.getStringWidth(strangeMessage) / 2), subPageTop, 0x594100);
			}
		}
	}
	private ArrayList<Page> pages = new ArrayList<Page>();
	int currentPage = 0;
	int currentSubPage = 0;
	
	public GuiQuestBrowserPaginated(Deque<IQuest> newAllQuests) {
		super(newAllQuests);
	}

	public void initGui() {	
		super.initGui();
		for(IQuest quest: allQuests) {
			pages.add(new Page(quest));
		}
	}
	
	@Override
	public void drawPaginatedContent() {
		try {
			Page page = pages.get(currentPage);
			page.drawPage(currentSubPage);
			
	    	String pageProgress = String.format("%d/%d quests", currentPage + 1, pages.size());
	    	fontRenderer.drawString(pageProgress, right - padding - (fontRenderer.getStringWidth(pageProgress)), top + padding, 0x594100);

	    	String subPageProgress = String.format("%d/%d", currentSubPage + 1, page.subPages.size());
	    	fontRenderer.drawString(subPageProgress, center - (fontRenderer.getStringWidth(subPageProgress) / 2), bottomControlStripTop + 5, 0x594100);
		} catch(IndexOutOfBoundsException e) {
			//we have pages, yet an invalid index.
			String strangeMessage = "Very strange.";
			fontRenderer.drawString(strangeMessage, center - (fontRenderer.getStringWidth(strangeMessage) / 2), height/2, 0x594100);
		}
	}
	
	protected void actionPerformed(GuiButton guibutton){
        if(!guibutton.enabled) {
            return;
        }
        // Page buttons...
        if(guibutton.id == 0) {
        	currentSubPage = 0;
            if(1 >= currentPage + 1) {
            	currentPage = pages.size() - 1;
            } else {
            	currentPage--;
            }
        } else if(guibutton.id == 1) {
        	currentSubPage = 0;
            if(pages.size() <= currentPage + 1) {
            	currentPage = 0;
            } else {
            	currentPage++;
            }
        } //Subpage buttons
        else if(guibutton.id == 3) {
        	if(1 >= currentSubPage + 1) {
            	currentSubPage = pages.get(currentPage).subPages.size() - 1;
            } else {
            	currentSubPage--;
            }
        } else if(guibutton.id == 4) {
            if(pages.get(currentPage).subPages.size() <= currentSubPage + 1) {
            	currentSubPage = 0;
            } else {
            	currentSubPage++;
            }
        } //finally, the Clear and Remove All buttons.
        else if(guibutton.id == -1) {
        	currentSubPage = 0;
        	ModLoader.getMinecraftInstance().theWorld.questCollection.springCleanQuests();
        	ModLoader.getMinecraftInstance().displayGuiScreen(null);
        } else if(guibutton.id == -2) {
        	ModLoader.getMinecraftInstance().theWorld.questCollection.clearQuests();
        	ModLoader.getMinecraftInstance().displayGuiScreen(null);
        }
	}
}
