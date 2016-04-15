package model;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

public class DrawUtils {
	
	private DrawUtils() {
	}

	public static int getMsgWidth(String msg, Font font, Graphics2D g) {
		g.setFont(font);
		Rectangle2D bounds = g.getFontMetrics().getStringBounds(msg, g);
		return (int) bounds.getWidth();
	}

	public static int getMsgHeight(String msg, Font font, Graphics2D g) {
		g.setFont(font);
		if (msg.length() == 0)
			return 0;
		TextLayout layout = new TextLayout(msg, font, g.getFontRenderContext());
		return (int) layout.getBounds().getHeight();
	}
}

