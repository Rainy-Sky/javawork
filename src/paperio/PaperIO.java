package  paperio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * PaperIO is the main class used to start window and keep track of current state and switch between states.
 */

@SuppressWarnings("serial")
public class PaperIO extends JFrame implements ActionListener{

    private Board board;
    private Menu menu;
    private Record record;
    private Setting setting;
    private JPanel cards;
    private Image im;
    private static boolean flag=true;

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
    	Data data;

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
        record = new Record(this,menu.getP1Name(), menu.getP2Name());
        cards = new JPanel(new CardLayout());
        cards.add(menu, "menu");
        cards.add(setting,"setting");
        cards.add(record,"record");
        add(cards);
        
        new Thread(()->{
        	while(true) {
        		playMusic();
        	}
        }).start();
    }

    /**
     * 枚举游戏状态：游戏进行（GAME）、菜单（MENU）、设置界面（SETTING）、历史记录(record)
     */
    private enum STATE{
        GAME,
        MENU,
        SETTING,
        RECORD
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
        }else if(s==STATE.RECORD) {
        	cards.add(new Record(this,menu.getP1Name(), menu.getP2Name()),"record"); 
        	cardLayout.show(cards, "record");
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
            	cards.remove(record);
            	setState(STATE.RECORD);
            	break;
            case "music":
            	setMusic(!flag);
            	break;
        }
    }
    
    
    /**
     * 设置游戏背景音乐
     */
	static void playMusic() {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File("music/PAPER_BGM.wav"));
			AudioFormat aif = ais.getFormat();
			final SourceDataLine sdl;
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);
			sdl = (SourceDataLine) AudioSystem.getLine(info);
			sdl.open(aif);
			sdl.start();
			FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
			/**
			 * value可以用来设置音量，从0-2.0
			 */

		    double value=2.0;
			float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) / Math.log(10.0) * 20.0);
			fc.setValue(dB);
			int nByte = 0;
			int writeByte = 0;
			final int SIZE = 1024 * 64;
			byte[] buffer = new byte[SIZE];
			/**
			 * 判断 播放/暂停 状态
			 */
			while (nByte != -1) {
				if(flag) {
					nByte = ais.read(buffer, 0, SIZE);
					sdl.write(buffer, 0, nByte);
				}else {
					nByte = ais.read(buffer, 0, 0);
				}
			}
			sdl.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 设置播放状态
	 */
	public static void setMusic(boolean val){
		flag=val;
		
	}
    	
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "True");

        EventQueue.invokeLater(PaperIO::new);
    }

}