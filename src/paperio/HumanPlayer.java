package paperio;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

/**
 * A HumanPlayer is a player controlled by a person. HumanPlayer adds reactions to key presses on top of abstract class
 * Player. HumanPlayer stores which key speed direction should be updated regarding to in the next tick as well.
 */
public class HumanPlayer extends Player {

    private int nextKey;

    /**
     * Constructs a HumanPlayer on a random spot on the game area with specified color
     * @param height height of game area player is constructed in
     * @param width width of game area player is constructed in
     * @param color the color of the player
     * @param name the name of player
     */
    HumanPlayer(int height, int width, Color color, String name) {
        super(height, width, color);
        this.name = name;
    }

    /**
     * Set key to change dx and dy in next tick
     * @param nextKey key to change dx and dy in next tick
     */
    void setNextKey(int nextKey) {
        this.nextKey = nextKey;
    }

    /**
     * Moves the player in different directions
     */
    @Override
    public void move(){
        x += dx;
        y += dy;
    }


    /**
     * Updates dx and dy regarding to key sent as input
     */
    void updateD() {
        //Left
        if((nextKey == KeyEvent.VK_LEFT || nextKey == KeyEvent.VK_A) && dx != 1) {
            dx = -1;
            dy = 0;
        }

        //Right
        if((nextKey == KeyEvent.VK_RIGHT || nextKey == KeyEvent.VK_D) && dx != -1) {
            dx = 1;
            dy = 0;
        }

        //Up
        if((nextKey == KeyEvent.VK_UP || nextKey == KeyEvent.VK_W) && dy != 1) {
            dx = 0;
            dy = -1;
        }

        //Down
        if((nextKey == KeyEvent.VK_DOWN || nextKey == KeyEvent.VK_S) && dy != -1) {
            dx = 0;
            dy = 1;
        }
    }
    
    void die() {
        isAlive = false;
        new Thread(()->{
        		playMusic();
        }).start();
        Data.addScores(this.getName(), this.getPercentOwned());
        ArrayList<Tile> ownedTilesCopy = (ArrayList<Tile>)tilesOwned.clone();
        ArrayList<Tile> contestedTilesCopy = (ArrayList<Tile>)tilesContested.clone();
        for(int i = 0; i < ownedTilesCopy.size(); i++){
            ownedTilesCopy.get(i).setOwner(null);
        }

        for(int i = 0; i < contestedTilesCopy.size(); i++){
            contestedTilesCopy.get(i).setContestedOwner(null);
        }
        tilesOwned.clear();
        tilesContested.clear();
        currentTile = null;

    }
    
    static void playMusic() {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File("music/Over.wav"));
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
			boolean flag=PaperIO.flag;
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
}
