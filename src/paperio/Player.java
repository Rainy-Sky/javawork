package paperio;

import java.awt.*;
import java.util.ArrayList;

/**
 * An abstract class for a general player in the game. Human player and bot player differs a bit but their common logic
 * is specified here. It keeps track of players position, speed, color, owned and contested tiles and name. Two players
 * can also be compared that compares number of owned tiles of the player.
 */
abstract class Player implements Comparable<Player> {

    int x;
    int y;
    int dx;
    int dy;
    protected Color color;
    protected ArrayList<Tile> tilesOwned = new ArrayList<>();
    protected ArrayList<Tile> tilesContested = new ArrayList<>();
    int height;
    int width;
    String name;

    protected Boolean isAlive = true;

    protected Tile currentTile;

    /**
     * 在一个随机位置生成一个随机颜色的玩家
     * @param height 游戏区域的高度
     * @param width 游戏区域的宽度
     * @param color 玩家的颜色
     */
    Player(int height, int width, Color color){
        x = (int)(Math.random() * (width - 2) +1);
        y = (int)(Math.random() * (height - 2) +1);

        if(x < 5){
            x += 5;
        }else if(x > (width -5)){
            x-= 5;
        }
        if(y < 5){
            y+= 5;
        }else if(y > (height) - 5){
            y -= 5;
        }
        this.color = color;
        this.height = height;
        this.width = width;

        double rand = Math.random();
        if (rand < 0.25) {
            dx = 1;
            dy = 0;
        } else if (rand < 5) {
            dx = -1;
            dy = 0;
        } else if (rand < 0.75) {
            dx = 0;
            dy = 1;
        } else {
            dx = 0;
            dy = -1;
        }
    }

    /**
     * 获取玩家的x坐标
     * @return 玩家的x坐标
     */
    int getX(){
        return x;
    }

    /**
     * 获取玩家的x坐标
     * @return 玩家的x坐标
     */
    int getY(){
        return y;
    }

    /**
     * @return 玩家的颜色
     */
    Color getColor(){
        return color;
    }

    /**
     * 玩家移动的抽象方法
     */
    abstract void move();

    /**
     * 当玩家死亡时，把他的领地全部变成无色
     */
    void die() {
        isAlive = false;
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

    /**
     * 将一个地图块加入玩家领地
     * @param t 需要加入领地的地图快
     */
    void setTileOwned(Tile t){
        tilesOwned.add(t);
        t.setOwner(this);
        t.setContestedOwner(null);
    }

    /**
     * 从领地中删除地图块
     * @param t 需要删除的地图块
     */
    void removeTileOwned(Tile t){
        tilesOwned.remove(t);
    }

    /**
     * 获得玩家领地的地图块
     * @return 玩家领地的地图块
     */
    ArrayList<Tile> getTilesOwned(){
        return tilesOwned;
    }

    /**
     * Get as a percentage how much of the total game area a player owns
     * @return percentage of how much of the total game area a player owns
     */
    double getPercentOwned(){
        return 100 * getTilesOwned().size() / (double)(height*width);
    }

    /**
     * 将地图块添加到玩家竞争尾迹
     * @param t Tile to be added to players contested list
     */
    void setTileContested(Tile t){
        tilesContested.add(t);
        t.setContestedOwner(this);
    }

    /**
     * 获得玩家尾迹的地图块
     * @return 玩家的尾迹地图块
     */
    ArrayList<Tile> getTilesContested(){
        return tilesContested;
    }


    /**
     * 将玩家的尾迹转换为领土
     */
    void contestToOwned(){
        for (Tile t : tilesContested) {
            setTileOwned(t);
        }
        tilesContested.clear();
    }

    /**
     * 玩家尾迹被其他人触碰
     * @param t 被触碰的尾迹
     */
    void checkCollision(Tile t){
        if(t.getContestedOwner() != null) {
            t.getContestedOwner().die();
        }
    }

    /**
     * 将一个地图块设为该玩家的当前所在块
     * @param currentTile 被设为当前块的地图块
     */
    void setCurrentTile(Tile currentTile) {
        this.currentTile = currentTile;
    }

    /**
     * 获得玩家的x方向速度
     * @return 玩家的x方向速度
     */
    int getDx() {
        return dx;
    }

    /**
     * 获得玩家的y方向速度
     * @return 玩家的y方向速度
     */
    int getDy() {
        return dy;
    }

    /**
     * 获得玩家名
     * @return 玩家名
     */
    String getName() {
        return name;
    }

    /**
     * 获得玩家状态
     * @return 玩家的状态，是否存货
     */
    Boolean getAlive() {
        return isAlive;
    }

    /**
     * 设置玩家的存活状态
     * @param alive 玩家是否存活
     */
    public void setAlive(Boolean alive) {
        isAlive = alive;
    }

    /**
     * 比较两个玩家的领土大小
     * @param player 比较的目标
     * @return 1 玩家领土比目标更大, -1 玩家领土比目标更小
     */
    public int compareTo(Player player){
        return Integer.compare(player.getTilesOwned().size(), tilesOwned.size());
    }
}