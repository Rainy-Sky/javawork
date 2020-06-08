package  paperio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * PaperIO is the main class used to start window and keep track of current state and switch between states.
 */

@SuppressWarnings("serial")
public class PaperIO extends JFrame implements ActionListener{

    private Board board;
    private Menu menu;
    private Setting setting;
    private JPanel cards;
    private Image im;

    /**
     * 游戏界面初始化
     */
    private PaperIO(){
        initUI();
    }

    /**
     * 设置窗口参数
     */
    private void initUI(){

        setSize(1280, 720);
        setLocation(600,600);
        setResizable(false);
        setVisible(true);
        setTitle("paperIO-ֽ纸团大作战");
        ImageIcon ig = new ImageIcon("img/icon.png");//这里放上你要设置图标图片
		im = ig.getImage();
		setIconImage(im);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        menu = new Menu(this);
        setting = new Setting(this);
        cards = new JPanel(new CardLayout());
        cards.add(menu, "menu");
        cards.add(setting,"setting");
        add(cards);
    }

    /**
     * 枚举游戏状态：游戏进行（GAME）、菜单（MENU）、设置界面（SETTING）
     */
    private enum STATE{
        GAME,
        MENU,
        SETTING
    }

    /**
     * Sets game state to specified state
     * @param s STATE game should be set to
     */
    private void setState(STATE s){
        CardLayout cardLayout = (CardLayout) cards.getLayout();
        if(s == STATE.GAME){
            cardLayout.show(cards, "board");
            board.setPaused(false);
        }else if(s == STATE.MENU){
            cardLayout.show(cards, "menu");
            if(board!=null)
            	board.setPaused(true);
        }else if(s==STATE.SETTING) {
        	cardLayout.show(cards, "setting");
        	if(board!=null)
        		board.setPaused(true);
        }
    }

    /**
     * Reacts to game actions such as game start and game paused
     * @param e event to react to
     */
    @Override
    public void actionPerformed(ActionEvent e) {
    	System.out.println(e.getActionCommand());
        switch (e.getActionCommand()) {
            case "Play Singleplayer":
                board = new Board(this, menu.getP1Name(), setting.getAreaHeight(), setting.getAreaWidth(), setting.getGameSpeed(), setting.getBotNumber());
                cards.add(board, "board");
                setState(STATE.GAME);
                break;
            case "Play Multiplayer":
                board = new Board(this, menu.getP1Name(), menu.getP2Name(), setting.getAreaHeight(), setting.getAreaWidth(), setting.getGameSpeed(), setting.getBotNumber());
                cards.add(board, "board");
                setState(STATE.GAME);
                break;
            case "End Game":
                setState(STATE.MENU);
                break;
            case "Setting":
            	setState(STATE.SETTING);
            	break;
            case "menu":
            	setState(STATE.MENU);
            	break;
            case "record":
            	
            	break;
        }
    }
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "True");

        EventQueue.invokeLater(PaperIO::new);
    }

}