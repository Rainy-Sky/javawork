package paperio;

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
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.*;
import java.util.Timer;

/**
 * 该类为本游戏主要的策略类
 * 主要初始化方块、玩家和游戏面板，并对他们进行状态跟踪以便于随时更改状态，来达到游戏的效果
 * 该类还结合 painter类画出计分板以及对该游戏的主要算法进行编写
 * 该类同样提供两种画板，即单人模式和多人模式，可以很好的提升玩家体验
 */
public class Board extends JPanel {

    private final int areaHeight;
    private final int areaWidth;
    private Tile[][] gameArea;
    private final int scale = 20;   // 缩放比例

    private int botNumber;  // 人机数量
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<HumanPlayer> humanPlayers = new ArrayList<>();
    private HashMap<Tile, Player> tilePlayerMap = new HashMap<>();

    private int tickCounter = 0;
    private final int tickReset;

    private ArrayList<Player> deadBots = new ArrayList<>();
    private boolean paused = true;
    private ActionListener actionListener;

    private ArrayList<Painter> painters = new ArrayList<>();
    private HashMap<Player, Painter> player_painter = new HashMap<>();

    private java.util.List<Color> colorList = new ArrayList<>(Arrays.asList(Color.magenta, Color.green, Color.red,
            Color.blue, Color.orange, Color.yellow, Color.pink, new Color(142,12,255),
            new Color(255,43,119), new Color(100,255,162)));

    /**
     * 单人模式面板
     * @param actionListener listener for key presses and state updates
     * @param p1name name of player
     * @param areaHeight height of game area
     * @param areaWidth width of game area
     * @param gameSpeed game speed between 1 and 5, 5 being the fastest
     * @param botNumber number of bots to have in game
     */
    Board(ActionListener actionListener, String p1name, int areaHeight, int areaWidth, int gameSpeed, int botNumber){
        this.actionListener = actionListener;
        this.areaHeight = areaHeight;
        this.areaWidth = areaWidth;
        this.botNumber = botNumber;
        int[] speeds = {12, 10, 8, 6, 4};
        tickReset = speeds[gameSpeed - 1];

        players.add(new HumanPlayer(areaHeight, areaWidth, new Color((int)(Math.random() * 0x1000000)), p1name));
        humanPlayers.add((HumanPlayer)players.get(0));

        initBoard();

        painters.add(new Painter(scale, this, humanPlayers.get(0), players));
        player_painter.put(humanPlayers.get(0), painters.get(0));
    }

    /**
     * 多人模式面板
     * @param actionListener listener for key presses and state updates
     * @param p1name name of player 1
     * @param p2name name of player 2
     * @param areaHeight height of game area
     * @param areaWidth width of game area
     * @param gameSpeed game speed between 1 and 5, 5 being the fastest
     * @param botNumber number of bots to have in game
     */
    Board(ActionListener actionListener, String p1name, String p2name, int areaHeight, int areaWidth, int  gameSpeed, int botNumber) {
        this.actionListener = actionListener;
        this.areaHeight = areaHeight;
        this.areaWidth = areaWidth;
        this.botNumber = botNumber;
        int[] speeds = {12, 10, 8, 6, 4};
        tickReset = speeds[gameSpeed - 1];

        players.add(new HumanPlayer(areaHeight, areaWidth, new Color((int)(Math.random() * 0x1000000)), p1name));
        players.add(new HumanPlayer(areaHeight, areaWidth, new Color((int)(Math.random() * 0x1000000)), p2name));
        humanPlayers.add((HumanPlayer)players.get(0));
        humanPlayers.add((HumanPlayer)players.get(1));

        initBoard();

        painters.add(new Painter(scale, this, humanPlayers.get(0), players));
        painters.add(new Painter(scale, this, humanPlayers.get(1), players));
        player_painter.put(humanPlayers.get(0), painters.get(0));
        player_painter.put(humanPlayers.get(1), painters.get(1));
    }

    /**
     * 对整个面板相关参数进行初始化，包括计时器、玩家、键盘操作等等
     */
    private void initBoard(){
        this.gameArea = new Tile[areaHeight][areaWidth];
        for(int i = 0; i < gameArea.length; i++){
            for(int j = 0; j < gameArea[i].length; j++){
                gameArea[i][j] = new Tile(j,i);
            }
        }

        specifyKeyActions();

        setBackground(Color.BLACK);

        // 添加人机，同时给他们画上随机的颜色
        for(int i = 0; i < botNumber; i++){
            if(i > 9){
                players.add(new BotPlayer(gameArea.length,gameArea[0].length,
                        new Color((int)(Math.random() * 0x1000000))));
            }else {
                players.add(new BotPlayer(gameArea.length, gameArea[0].length, colorList.get(i)));
            }
        }

        // 为每个玩家初始化区域，同时调用函数保证他们互相之间的距离
        for(int i = 0; i < players.size(); i++){
            // 如果相隔太近，就会重新创建新玩家来代替
            if(!checkSpawn(players.get(i))){
                players.remove(players.get(i));
                i--;
                if(botNumber > 9){
                    players.add(new BotPlayer(gameArea.length,gameArea[0].length,
                            new Color((int)(Math.random() * 0x1000000))));
                }else {
                    players.add(new BotPlayer(gameArea.length,gameArea[0].length, colorList.get(i)));
                }
                continue;
            }else {
                startingArea(players.get(i));
            }
        }

        // 启动计时器
        final int INITIAL_DELAY = 0;
        final int PERIOD_INTERVAL = 1000/60;
        java.util.Timer timer = new Timer();
        timer.scheduleAtFixedRate(new Board.ScheduleTask(),
                INITIAL_DELAY, PERIOD_INTERVAL);
    }


    /**
     * 把键盘操作转化为对应的动作以及函数
     */
    private void specifyKeyActions(){
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        if(humanPlayers.size() == 1){
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveUP");
            am.put("moveUP", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(0).setNextKey(KeyEvent.VK_UP);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDOWN");
            am.put("moveDOWN", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {humanPlayers.get(0).setNextKey(KeyEvent.VK_DOWN);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLEFT");
            am.put("moveLEFT", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(0).setNextKey(KeyEvent.VK_LEFT);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRIGHT");
            am.put("moveRIGHT", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(0).setNextKey(KeyEvent.VK_RIGHT);
                }
            });
        }else if(humanPlayers.size() == 2){
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveP1UP");
            am.put("moveP1UP", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(1).setNextKey(KeyEvent.VK_UP);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveP1DOWN");
            am.put("moveP1DOWN", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {humanPlayers.get(1).setNextKey(KeyEvent.VK_DOWN);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveP1LEFT");
            am.put("moveP1LEFT", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(1).setNextKey(KeyEvent.VK_LEFT);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveP1RIGHT");
            am.put("moveP1RIGHT", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(1).setNextKey(KeyEvent.VK_RIGHT);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "moveP2UP");
            am.put("moveP2UP", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(0).setNextKey(KeyEvent.VK_W);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "moveP2DOWN");
            am.put("moveP2DOWN", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {humanPlayers.get(0).setNextKey(KeyEvent.VK_S);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "moveP2LEFT");
            am.put("moveP2LEFT", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(0).setNextKey(KeyEvent.VK_A);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "moveP2RIGHT");
            am.put("moveP2RIGHT", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(0).setNextKey(KeyEvent.VK_D);
                }
            });
        }


        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "pause");
        am.put("pause", new AbstractAction() {
            public void actionPerformed(ActionEvent evt) {
                ActionEvent action = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "pause");
                actionListener.actionPerformed(action);
            }
        });
    }

    /**
     * 初始化每个玩家开始的区域，并设置好颜色
     * @param player player to generate starting area for
     */
    private void startingArea(Player player){
        int x = player.getX();
        int y = player.getY();
        if(!checkSpawn(player)){
            Player playerCopy = new BotPlayer(gameArea.length,gameArea[0].length, player.getColor());
            startingArea(playerCopy);
        }
        for(int i = x-1; i <= x+1; i++){
            for(int j = y-1; j <= y+1; j++){
                player.setTileOwned(getTile(i,j));
            }
        }
    }

    /**
     * 检查小范围内是否存在其他玩家
     * @param player Player that you want to check surroundings for other players
     * @return  True if nobody is close, False otherwise
     */
    private boolean checkSpawn(Player player){
        int x = player.getX();
        int y = player.getY();
        for(int i = x-3; i <= x+3; i++) {
            for (int j = y - 3; j <= y + 3; j++) {
                if (getTile(i, j).getOwner() != null || getTile(i, j).getContestedOwner() != null ) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 重写方法，自定义画图
     * @param g Graphics element used to draw elements on screen
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int i = 0; i < painters.size(); i++){
            //Set clipping area for painter
            g.setClip(getWidth()/painters.size() * i,0,getWidth()/painters.size(),getHeight());

            //Move graphics to top-left of clipping area
            g.translate(getWidth()/painters.size() * i,0);

            //Painter paints area
            painters.get(i).draw(g);

            //Move graphics back to top-left of window
            g.translate(-getWidth()/painters.size() * i,0);
        }
        try {
            drawScoreboard(g);
        } catch(IndexOutOfBoundsException ignored){
        }
        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * 画出计分板
     * @param g Graphics object received as argument in paintComponent method
     */
    private void drawScoreboard(Graphics g) {
        g.setFont(new Font("Monospaced", Font.PLAIN, 16));
        FontMetrics fontMetrics = g.getFontMetrics();
        int fontHeight = fontMetrics.getHeight();
        int barWidth;
        int barHeight = fontHeight + 4;

        Player player;
        String string;
        Color color;

        double highestPercentOwned = players.get(0).getPercentOwned();
        Collections.sort(players);
        for(int i = 0; i < Integer.min(5, players.size()); i++){
            player = players.get(i);
            string = String.format("%.2f%% - " + player.getName(), player.getPercentOwned());
            color = player.getColor();

            barWidth = (int)((player.getPercentOwned() / highestPercentOwned)*(getWidth()/4));
            g.setColor(player.getColor());
            g.fillRect(getWidth() - barWidth,  barHeight*i, barWidth,barHeight);
            // 调整颜色
            if(0.299*color.getRed() + 0.587*color.getGreen() + 0.114*color.getBlue() < 127){
                g.setColor(Color.WHITE);
            }else{
                g.setColor(Color.BLACK);
            }
            g.drawString(string, 2+getWidth() -  barWidth,  barHeight*i + fontHeight);
        }
    }

    /**
     * 主要游戏逻辑，包括开始、暂停、死亡、以及游戏的进行
     */
    private void tick(){
        Player player;
        tilePlayerMap.clear();
        for (int i = 0; i < players.size(); i++) {
            player = players.get(i);
            player.move();
            // 撞到边界
            if(player.getX() < 0 || player.getX() >= areaWidth || player.getY() < 0 || player.getY() >= areaHeight){
                player.die();
            }else{
                Tile tile = getTile(player.getX(), player.getY());
                player.checkCollision(tile);
                player.setCurrentTile(tile);
                findCollision(player, tile);

                // 不在自己领地内
                if (tile.getOwner() != player && player.getAlive()) {
                    player.setTileContested(tile);
                    // 玩家回到自己领地，就可以开始画图
                } else if (player.getTilesContested().size() > 0) {
                    player.contestToOwned();
                    fillEnclosure(player);
                }
            }
            // 人机死亡
            if(player instanceof BotPlayer && !player.getAlive()){
                deadBots.add(player);
            }
        }
        respawnBots();

        boolean allKilled = true;
        for(HumanPlayer humanPlayer : humanPlayers){
            humanPlayer.updateD();
            // 如果玩家死了，就停止画
            player_painter.get(humanPlayer).setDraw(humanPlayer.getAlive());
            allKilled = allKilled && !humanPlayer.getAlive();
        }
        if(allKilled){
            endGame();
        }

        // 将死去的玩家从列表清除
        players.removeIf(p -> !p.getAlive());
    }

    /**
     * 结束游戏
     */
    private void endGame(){
        JOptionPane.showMessageDialog(this,
                "You lost, game over", "GAME OVER", JOptionPane.PLAIN_MESSAGE);
        
        actionListener.actionPerformed(new ActionEvent(this, 0, "End Game"));
    }

    /**
     * 使得人机在一定时间后复活
     */
    private void respawnBots(){
        for(int i = 0; i < deadBots.size(); i++){
            if(deadBots.get(i).getAlive()){
                Player player = new BotPlayer(gameArea.length,gameArea[0].length,
                        new Color((int)(Math.random() * 0x1000000)));
                startingArea(player);
                players.add(player);
                deadBots.remove(deadBots.get(i));
            }
        }
    }

    /**
     * 判断游戏中玩家之前的碰撞
     * @param player Player you want to check collision for
     * @param tile   Tile that Player currently is on
     */
    private void findCollision(Player player, Tile tile) {
        // If corresponding tile is found in tilePlayerMap
        if(tilePlayerMap.containsKey(tile)) {

            // Iterate through all entries in tilePlayerMap, if the Tile in entry matches Tile in input,
            // compare sizes between players and destroy one of them. The player with the largest tiles contested
            // survives. If both players have the same amount of tiles contested, the player with the most tiles
            // owned survives. If both players have the same amount of tiles contested and tiles owned,
            // the first player added to Players list dies.
            for(Map.Entry<Tile, Player> entry : tilePlayerMap.entrySet()) {
                if (entry.getKey() == tile) {
                    if (entry.getValue().getTilesContested().size() > player.getTilesContested().size()) {
                        entry.getValue().die();
                    } else if (entry.getValue().getTilesContested().size() < player.getTilesContested().size()) {
                        player.die();
                    } else if (entry.getValue().getTilesContested().size() == player.getTilesContested().size()) {
                        if (entry.getValue().getTilesOwned().size() > player.getTilesOwned().size()) {
                            entry.getValue().die();
                        } else {
                            player.die();
                        }
                    }
                }
            }
        }else { // If no corresponding tile is found, add tile and player to tilePlayerMap
            tilePlayerMap.put(tile, player);
        }
        // Remove dead players
        players.removeIf(p -> !p.getAlive());
    }

    /**
     * 控制计时器
     */
    private void updateTick(){
        tickCounter++;
        tickCounter %= tickReset;
    }

    /**
     * 在需要填充的时候进行填充，即在玩家回到自己领地时进行填充
     * 使用DFS算法来保证填充的正确
     * 首先确定最大的矩形作为边界
     * 使用DFS对所有玩家未拥有的块进行DFS
     * 如果存在一块区域没有包含边界，则该区域需要被填满
     * @param player The player whose enclosure to be filled
     */
    private void fillEnclosure(Player player) {
        // Set boundary
        int maxX = 0;
        int minX = gameArea[0].length;
        int maxY = 0;
        int minY = gameArea.length;
        for (Tile t : player.getTilesOwned()) {
            if(t.getX() > maxX) maxX = t.getX();
            if(t.getX() < minX) minX = t.getX();
            if(t.getY() > maxY) maxY = t.getY();
            if(t.getY() < minY) minY = t.getY();
        }

        // 所需列表
        ArrayList<Tile> outside = new ArrayList<>();
        ArrayList<Tile> inside  = new ArrayList<>();
        ArrayList<Tile> visited = new ArrayList<>();
        HashSet<Tile> toCheck = new HashSet<>();

        // 需要检查的tile放入列表
        int y;
        int x;
        for(Tile t : player.getTilesOwned()){
            y = t.getY();
            x = t.getX();
            if(y -1 >= 0) toCheck.add(gameArea[y-1][x]);
            if(y + 1 < gameArea.length) toCheck.add(gameArea[y+1][x]);
            if(x - 1 >= 0) toCheck.add(gameArea[y][x-1]);
            if(x + 1 < gameArea[y].length) toCheck.add(gameArea[y][x+1]);
        }


        // 循环所有tile并进行dfs
        for(Tile t : toCheck){
            if(!inside.contains(t)){
                Stack<Tile> stack = new Stack<>();
                boolean cont = true;
                Tile v;
                visited.clear();

                // DFS algorithm
                stack.push(t);
                while((!stack.empty()) && cont){
                    v = stack.pop();
                    if(!visited.contains(v) && (v.getOwner() != player)){
                        y = v.getY();
                        x = v.getX();
                        if(outside.contains(v) //If already declared as outside
                                || x < minX || x > maxX || y < minY || y > maxY //If outside of boundary
                                || x == gameArea[0].length -1 || x == 0 || y == 0 || y == gameArea.length -1){ // If it is a edge tile
                            cont = false;
                        }else{
                            visited.add(v);
                            if(y -1 >= 0) stack.push(gameArea[y-1][x]);
                            if(y + 1 < gameArea.length) stack.push(gameArea[y+1][x]);
                            if(x - 1 >= 0) stack.push(gameArea[y][x-1]);
                            if(x + 1 < gameArea[y].length) stack.push(gameArea[y][x+1]);
                        }
                    }
                }
                if(cont){ // 如果没有边界
                    inside.addAll(visited);
                }else{
                    outside.addAll(visited);
                }
            }
        }

        // 填充
        for(Tile t : inside){
            player.setTileOwned(t);
        }
        new Thread(()->{
    		playMusic();
        }).start();
    }

    /**
     * 暂停
     * @param b True if game should be paused, false otherwise
     */
    void setPaused(Boolean b){
        paused = b;
    }

    /**
     * 获取游戏区域高度
     * @return height of game area
     */
    int getAreaHeight() {
        return areaHeight;
    }

    /**
     * 获取游戏区域宽度
     * @return width of game area
     */
    int getAreaWidth() {
        return areaWidth;
    }

    /**
     * 获取计时器
     * @return current tick counter
     */
    int getTickCounter() {
        return tickCounter;
    }

    /**
     * 获取重设计时器
     * @return how often tick is reset
     */
    int getTickReset() {
        return tickReset;
    }

    /**
     * 获得tile的坐标
     * @param x x position of tile
     * @param y y position of tile
     * @return tile at position (x,y)
     */
    Tile getTile(int x, int y){
        return gameArea[y][x];
    }

    /**
     * ScheduleTask is responsible for receiving and responding to timer calls
     */
    private class ScheduleTask extends TimerTask {

        /**
         * Gets called by timer at specified interval. Calls tick at specified rate and repaint each time
         */
        @Override
        public void run() {
            if(!paused) {
                updateTick();
                if (tickCounter == 0) {
                    tick();
                }
                repaint();
            }
        }
    }
    static void playMusic() {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File("music/fill.wav"));
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
			boolean flag=true;
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
