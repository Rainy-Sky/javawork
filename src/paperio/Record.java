package paperio;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Record extends JPanel {
	private ActionListener actionListener;
	private HashMap<String, Double> scores;
	private JLabel achievement;
	private JLabel title;
	private JLabel[] topscore;
	private JLabel[] playername;
	private ImageIcon back;
	private ImageIcon returnM;
	private JButton button_back;
	Record(ActionListener actionListener,String p1name,String p2name){
		this.actionListener=actionListener;
		this.setOpaque(false);
		playername = new JLabel[2];
        back = new ImageIcon("img/achieve.jpg");
        returnM = new ImageIcon("img/setting_return.jpg");
        achievement=new JLabel(back);
		button_back = new JButton(returnM);
        achievement.setBounds(0, 0, back.getIconWidth(), back.getIconHeight());	
		topscore=new JLabel[2];
		this.setLayout(null);
		init(p1name,p2name);
    	add(topscore[0]);
    	add(topscore[1]);
    	add(playername[0]);
    	add(playername[1]);
        add(button_back);
		add(achievement);
	}
	/**
	 * 初始化界面 绘制历史记录内容
	 */
	private void init(String p1name,String p2name) {
		this.scores=Data.getScores();
		if(scores.containsKey(p1name)) {
			topscore[0]=new JLabel("最好成绩："+scores.get(p1name).toString()+"%");
			System.out.println(p1name+":"+scores.get(p1name));
		}
		else {
			topscore[0]=new JLabel("暂无游戏记录");
		}
		if(scores.containsKey(p2name)) {
			topscore[1]=new JLabel("最好成绩："+scores.get(p2name).toString()+"%");
		}
		else {
			topscore[1]=new JLabel("暂无游戏记录");
		}
		playername[0] = new JLabel("player1:"+p1name);
		playername[1] = new JLabel("player2:"+p2name);
		
		button_back.setBounds(20,7,returnM.getIconWidth(),returnM.getIconHeight());
        button_back.setActionCommand("menu");
        button_back.addActionListener(actionListener);
        button_back.setBackground(Color.DARK_GRAY);
        button_back.setFocusPainted(false);
        button_back.setBorderPainted(false);
        
        title=new JLabel("ACHIEVEMENT");
        title.setFont(new Font("Monospaced", Font.PLAIN, 36));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(JLabel.RIGHT);
        title.setBounds(550, 100, 500, 150);
        add(title);
		
        for(int i=0;i<2;i++) {
        	topscore[i].setFont(new Font("Monospaced", Font.PLAIN, 24));
        	topscore[i].setForeground(Color.WHITE);
        	topscore[i].setHorizontalAlignment(JLabel.RIGHT);
        	topscore[i].setBounds(800, 300+i*150, 350, 100);
        }
        
        for(int i=0;i<2;i++) {
        	playername[i].setFont(new Font("Monospaced", Font.PLAIN, 24));
        	playername[i].setForeground(Color.WHITE);
        	playername[i].setHorizontalAlignment(JLabel.LEFT);
        	playername[i].setBounds(650, 300+i*150, 350, 100);
        }
        
	}
	/**
	 * record更新后 重新绘制界面
	 */
	public void refresh(String p1name,String p2name) {
		init(p1name,p2name);
	}
}
