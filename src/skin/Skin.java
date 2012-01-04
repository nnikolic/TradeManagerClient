package skin;

import java.awt.Color;

public class Skin {
	private int id;
	private String name;
	private Color nimbusFocus, nimbusSelectedText, nimbusSelectionBackground,
			nimbusBlueGrey, nimbusBase, control, text, nimbusLightBackground;

	public Skin() {
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Color getNimbusFocus() {
		return nimbusFocus;
	}

	public void setNimbusFocus(Color nimbusFocus) {
		this.nimbusFocus = nimbusFocus;
	}

	public Color getNimbusSelectedText() {
		return nimbusSelectedText;
	}

	public void setNimbusSelectedText(Color nimbusSelectedText) {
		this.nimbusSelectedText = nimbusSelectedText;
	}

	public Color getNimbusSelectionBackground() {
		return nimbusSelectionBackground;
	}

	public void setNimbusSelectionBackground(Color nimbusSelectionBackground) {
		this.nimbusSelectionBackground = nimbusSelectionBackground;
	}

	public Color getNimbusBlueGrey() {
		return nimbusBlueGrey;
	}

	public void setNimbusBlueGrey(Color nimbusBlueGrey) {
		this.nimbusBlueGrey = nimbusBlueGrey;
	}

	public Color getNimbusBase() {
		return nimbusBase;
	}

	public void setNimbusBase(Color nimbusBase) {
		this.nimbusBase = nimbusBase;
	}

	public Color getControl() {
		return control;
	}

	public void setControl(Color control) {
		this.control = control;
	}

	public Color getText() {
		return text;
	}

	public void setText(Color text) {
		this.text = text;
	}

	public Color getNimbusLightBackground() {
		return nimbusLightBackground;
	}

	public void setNimbusLightBackground(Color nimbusLightBackground) {
		this.nimbusLightBackground = nimbusLightBackground;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
