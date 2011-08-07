package net.minecraft.src;
import java.util.ArrayDeque;
import java.util.LinkedList;

/**
* A GUI widget that displays ragged-right multiline text.
*/
public class GuiMultilineText extends Gui {
	private class LineInfo {
		public StringBuilder text;
		public int top;
		public int left;
		public LineInfo(int le, int to) {
			text = new StringBuilder();
			top = to; left = le;
		}
	};
	

	private final int LineSkip = 9;
	int hexColor;
	ArrayDeque<GuiMultilineText.LineInfo> lines;
	GuiScreen screen;
	
	private int stringWidth(String str) {
		return screen.fontRenderer.getStringWidth(str);
	}
	
	public GuiMultilineText(GuiScreen screen, String string,  int left, int top, int right, int hexColor) {
		this.screen = screen;
		this.hexColor = hexColor;
		int width = right - left;
		lines = new ArrayDeque<GuiMultilineText.LineInfo>();
		
		//see http://en.wikipedia.org/wiki/Word_wrap for the algorithm in question.
		String[] wordsNoNewlines = string.split(" ");
		LinkedList<String> words = new LinkedList<String>();
		//make any newlines seperate "words" of their own.
		for(String word : wordsNoNewlines) {
			words.add(word);
		}
		
		int spaceWidth = stringWidth(" ");
		int spaceLeft = width;
		LineInfo accumulatingLine = new LineInfo(left, top);
		for(String word : words) {
			if(stringWidth(word) + spaceWidth  > width) { //too large for any line?
				if(lines.size() > 0) {
					lines.addLast(accumulatingLine);
					accumulatingLine = new LineInfo(left, lines.getLast().top + LineSkip);
					spaceLeft = width;
				}
				accumulatingLine.text.append(word);
				
				lines.addLast(accumulatingLine);
				accumulatingLine = new LineInfo(left, lines.getLast().top + LineSkip);
				spaceLeft = width;
			} else if(stringWidth(word) + spaceWidth > spaceLeft) { //too large for the existing line?
				lines.addLast(accumulatingLine);
				accumulatingLine = new LineInfo(left, lines.getLast().top + LineSkip);
				accumulatingLine.text.append(word);
				accumulatingLine.text.append(" ");
				
				spaceLeft = width - stringWidth(word);
			} else {
				accumulatingLine.text.append(word);
				accumulatingLine.text.append(" ");
				
				spaceLeft = spaceLeft - (stringWidth(word) + spaceWidth);
			}
		}
		if(accumulatingLine.text.length() != 0)
			lines.add(accumulatingLine);
	}
	
	public int getHeight() {
		int end = lines.getLast().top + 9;
		int start =  lines.getFirst().top;
		return end - start;
	}
	
	public static int predictHeight(GuiScreen screen, String string, int left, int right) {
		GuiMultilineText temp = new GuiMultilineText(screen, string, left, 0, right, 0);
		return temp.getHeight();
	}
	
	public void drawMultilineText() {
		for(GuiMultilineText.LineInfo line : lines) {
			screen.fontRenderer.drawString(line.text.toString(), line.left, line.top, hexColor);
		}
	}
}
