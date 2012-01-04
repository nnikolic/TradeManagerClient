package skin;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class ColorButton extends JButton {
	private static final long serialVersionUID = -2957496052120195014L;
	private Color paintColor;

	public void setPaintColor(Color paintColor) {
		this.paintColor = paintColor;
	}

	public Color getPaintColor() {
		return paintColor;
	}

	public ColorButton() {
		setBorder(BorderFactory.createBevelBorder(0));
	}
	
	public ColorButton(Color paintColor) {
		setBorder(BorderFactory.createBevelBorder(0));
		this.paintColor = paintColor;
	}


	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(paintColor);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
