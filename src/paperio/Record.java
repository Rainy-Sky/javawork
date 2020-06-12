package paperio;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Record extends JPanel {
	private ActionListener actionListener;
	private TreeSet<Double> scores;
	private JLabel achievement;
	private JLabel[] topscore;
	private ImageIcon back;
	private ImageIcon returnM;
	private JButton button_back;
	Record(ActionListener actionListener){
		this.actionListener=actionListener;
		this.setOpaque(false);
        back = new ImageIcon("img/achieve.jpg");
        returnM = new ImageIcon("img/setting_return.jpg");
        achievement=new JLabel(back);
		button_back = new JButton(returnM);
        achievement.setBounds(0, 0, back.getIconWidth(), back.getIconHeight());	
		topscore=new JLabel[5];
		this.setLayout(null);
		init();
		add(achievement);
	}
	/*
	 * 初始化界面 绘制历史记录内容
	 */
	private void init() {
		int size=0;
		this.scores=Data.getScores();
		Iterator<Double> score=scores.iterator();
		while(score.hasNext()) {
			topscore[size]=new JLabel("1. "+score.next().toString());
			size++;
		}
		
		button_back.setBounds(20,7,returnM.getIconWidth(),returnM.getIconHeight());
        button_back.setActionCommand("menu");
        button_back.addActionListener(actionListener);
        button_back.setBackground(Color.DARK_GRAY);
        button_back.setFocusPainted(false);
        button_back.setBorderPainted(false);
        
        achievement=new JLabel("ACHIEVEMENT");
        achievement.setFont(new Font("Monospaced", Font.PLAIN, 36));
        achievement.setForeground(Color.WHITE);
        achievement.setHorizontalAlignment(JLabel.RIGHT);
        achievement.setBounds(200, 100, 500, 150);
		
        for(int i=0;i<size;i++) {
        	topscore[i].setFont(new Font("Monospaced", Font.PLAIN, 24));
        	topscore[i].setForeground(Color.WHITE);
        	topscore[i].setHorizontalAlignment(JLabel.RIGHT);
        	topscore[i].setBounds(400, 100+i*100, 300, 100);
        }
	}
	/*
	 * record更新后 重新绘制界面
	 */
	public void refresh() {
		init();
	}
}
