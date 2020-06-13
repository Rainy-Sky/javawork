 package paperio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Setting extends JPanel {
	private JLabel tip;
	private JButton button_back;
	private ImageIcon back;
	private ImageIcon returnM;
	private JSpinner areaHeightSpnr;
    private JSpinner areaWidthSpnr;
    private JSpinner gameSpeedSpnr;
    private JSpinner botNumberSpnr;
    private JSpinner musicSpnr;
	private ActionListener actionListener;
	/**
	 * 初始化设置界面
	 */
	Setting(ActionListener actionListener){
		this.actionListener = actionListener;
		this.setOpaque(false);
        back = new ImageIcon("img/setting_back.jpg");
        returnM = new ImageIcon("img/setting_return.jpg");
		JLabel imgLabel = new JLabel(back);
		button_back = new JButton(returnM);
		imgLabel.setBounds(0, 0, back.getIconWidth(), back.getIconHeight());	
		this.setLayout(null);
		addComponents();
		add(imgLabel);
	}
	private void addComponents() {
		tip=new JLabel("SETTING");
		JLabel musicVoice = new JLabel("Game music voice:");
		musicSpnr = new JSpinner(new SpinnerNumberModel(1,0,2,0.5));
		JLabel areaHeightLabel = new JLabel("Game area height:");
        areaHeightSpnr = new JSpinner(new SpinnerNumberModel(100, 25, 500, 5));
        JLabel areaWidthLabel = new JLabel("Game area width:");
        areaWidthSpnr = new JSpinner(new SpinnerNumberModel(100, 25, 500, 5));
        JLabel speedLabel = new JLabel("Game speed (1-5):");
        gameSpeedSpnr = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
        JLabel botNumberLabel = new JLabel("Number of bots:");
        botNumberSpnr = new JSpinner(new SpinnerNumberModel(10, 0, 25, 1));
        JTextField textField;

        button_back.setBounds(20,7,returnM.getIconWidth(),returnM.getIconHeight());
        button_back.setActionCommand("menu");
        button_back.addActionListener(actionListener);
        button_back.setBackground(Color.DARK_GRAY);
        button_back.setFocusPainted(false);
        button_back.setBorderPainted(false);
        /**
         *  Style setting labels
         */
        tip.setFont(new Font("Monospaced", Font.PLAIN, 36));
        tip.setForeground(Color.WHITE);
        tip.setHorizontalAlignment(JLabel.RIGHT);
        tip.setBounds(200, 0, 500, 150);
        areaHeightLabel.setFont(new Font("Monospaced", Font.PLAIN, 24));
        areaHeightLabel.setForeground(Color.WHITE);
        areaHeightLabel.setHorizontalAlignment(JLabel.RIGHT);
        areaHeightLabel.setBounds(400, 150, 300, 100);
        areaWidthLabel.setFont(new Font("Monospaced", Font.PLAIN, 24));
        areaWidthLabel.setForeground(Color.WHITE);
        areaWidthLabel.setHorizontalAlignment(JLabel.RIGHT);
        areaWidthLabel.setBounds(400, 250, 300, 100);
        speedLabel.setFont(new Font("Monospaced", Font.PLAIN, 24));
        speedLabel.setForeground(Color.WHITE);
        speedLabel.setHorizontalAlignment(JLabel.RIGHT);
        speedLabel.setBounds(400, 350, 300, 100);
        botNumberLabel.setFont(new Font("Monospaced", Font.PLAIN, 24));
        botNumberLabel.setForeground(Color.WHITE);
        botNumberLabel.setHorizontalAlignment(JLabel.RIGHT);
        botNumberLabel.setBounds(400, 450, 300, 100);
        musicVoice.setFont(new Font("Monospaced", Font.PLAIN, 24));
        musicVoice.setForeground(Color.WHITE);
        musicVoice.setHorizontalAlignment(JLabel.RIGHT);
        musicVoice.setBounds(400, 550, 300, 100);

        /**
         *  Style setting spinners
         */
        textField = (JTextField)areaHeightSpnr.getEditor().getComponent(0);
        areaHeightSpnr.setBounds(700, 175, 70 ,50);
        areaHeightSpnr.setFont(new Font("Monospaced", Font.PLAIN, 24));
        textField.setBackground(Color.pink);
        textField.setForeground(Color.WHITE);
        textField.setHorizontalAlignment(JTextField.LEFT);
        textField = (JTextField)areaWidthSpnr.getEditor().getComponent(0);
        areaWidthSpnr.setBounds(700, 275, 70 ,50);
        areaWidthSpnr.setFont(new Font("Monospaced", Font.PLAIN, 24));
        textField.setBackground(Color.pink);
        textField.setForeground(Color.WHITE);
        textField.setHorizontalAlignment(JTextField.LEFT);
        textField = (JTextField)gameSpeedSpnr.getEditor().getComponent(0);
        gameSpeedSpnr.setBounds(700, 375, 70 ,50);
        gameSpeedSpnr.setFont(new Font("Monospaced", Font.PLAIN, 24));
        textField.setBackground(Color.pink);
        textField.setForeground(Color.WHITE);
        textField.setHorizontalAlignment(JTextField.LEFT);
        textField = (JTextField)botNumberSpnr.getEditor().getComponent(0);
        botNumberSpnr.setBounds(700, 475, 70 ,50);
        botNumberSpnr.setFont(new Font("Monospaced", Font.PLAIN, 24));
        textField.setBackground(Color.pink);
        textField.setForeground(Color.WHITE);
        textField.setHorizontalAlignment(JTextField.LEFT);
        textField = (JTextField)musicSpnr.getEditor().getComponent(0);
        musicSpnr.setBounds(700, 575, 70 ,50);
        musicSpnr.setFont(new Font("Monospaced", Font.PLAIN, 24));
        textField.setBackground(Color.pink);
        textField.setForeground(Color.WHITE);
        textField.setHorizontalAlignment(JTextField.LEFT);
        
        JComponent[] settingComponents = {
        		tip,button_back,
        		areaHeightLabel, areaHeightSpnr,
                areaWidthLabel, areaWidthSpnr,
                speedLabel, gameSpeedSpnr,
                botNumberLabel, botNumberSpnr};

        /**
         *  Add setting labels and spinners
         */
        for(JComponent component : settingComponents){
            add(component);
        }
	}
	/**
     * Get game area height specified in area height spinner
     */
    public int getAreaHeight() {
        return Integer.valueOf(((JTextField)areaHeightSpnr.getEditor().getComponent(0)).getText());
    }

    /**
     * Get game area width specified in area width spinner
     */
    public int getAreaWidth() {
        return Integer.valueOf(((JTextField)areaWidthSpnr.getEditor().getComponent(0)).getText());
    }

    /**
     * Get game speed specified in game speed spinner
     */
    public int getGameSpeed() {
        return Integer.valueOf(((JTextField)gameSpeedSpnr.getEditor().getComponent(0)).getText());
    }

    /**
     * Get number of bots specified in bot number spinner
     */
    public int getBotNumber() {
        return Integer.valueOf(((JTextField)botNumberSpnr.getEditor().getComponent(0)).getText());
    }
    
    /**
     * 获取音量大小
     */
    public double getVoice() {
    	return Double.parseDouble(((JTextField)botNumberSpnr.getEditor().getComponent(0)).getText());
    }
}
