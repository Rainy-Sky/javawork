package paperio;

import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class Record extends JPanel {
	private ActionListener actionListener;
	Record(ActionListener actionListener){
		this.actionListener=actionListener;
		
	}
}
