package paperio;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 这是继承自Player类的Bot玩家子类，由程序自动控制移动
 */
class BotPlayer extends Player {

    // TODO read all names from file
    private String[] names = { "Bulbasaur", "Ivysaur", "Venusaur", "Charmander", "Charmeleon", "Charizard", "Squirtle",
            "Wartortle", "Blastoise", "Caterpie", "Metapod", "Butterfree", "Weedle", "Kakuna", "Beedrill", "Pidgey",
            "Pidgeotto", "Pidgeot", "Rattata", "Raticate", "Spearow", "Fearow", "Ekans", "Arbok", "Pikachu", "Raichu",
            "Sandshrew", "Sandslash", "Nidoran", "Nidorina", "Nidoqueen", "Nidoran", "Nidorino", "Nidoking", "Clefairy",
            "Clefable", "Vulpix", "Ninetales", "Jigglypuff", "Wigglytuff", "Zubat", "Golbat", "Oddish", "Gloom",
            "Vileplume", "Paras", "Parasect", "Venonat", "Venomoth", "Diglett", "Dugtrio", "Meowth", "Persian",
            "Psyduck", "Golduck", "Mankey", "Primeape", "Growlithe", "Arcanine", "Poliwag", "Poliwhirl", "Poliwrath",
            "Abra", "Kadabra", "Alakazam", "Machop", "Machoke", "Machamp", "Bellsprout", "Weepinbell", "Victreebel",
            "Tentacool", "Tentacruel", "Geodude", "Graveler", "Golem", "Ponyta", "Rapidash", "Slowpoke", "Slowbro",
            "Magnemite", "Magneton", "Farfetch'd", "Doduo", "Dodrio", "Seel", "Dewgong", "Grimer", "Muk", "Shellder",
            "Cloyster", "Gastly", "Haunter", "Gengar", "Onix", "Drowzee", "Hypno", "Krabby", "Kingler", "Voltorb",
            "Electrode", "Exeggcute", "Exeggutor", "Cubone", "Marowak", "Hitmonlee", "Hitmonchan", "Lickitung",
            "Koffing", "Weezing", "Rhyhorn", "Rhydon", "Chansey", "Tangela", "Kangaskhan", "Horsea", "Seadra",
            "Goldeen", "Seaking", "Staryu", "Starmie", "Mr. Mime", "Scyther", "Jynx", "Electabuzz", "Magmar", "Pinsir",
            "Tauros", "Magikarp", "Gyarados", "Lapras", "Ditto", "Eevee", "Vaporeon", "Jolteon", "Flareon", "Porygon",
            "Omanyte", "Omastar", "Kabuto", "Kabutops", "Aerodactyl", "Snorlax", "Articuno", "Zapdos", "Moltres",
            "Dratini", "Dragonair", "Dragonite", "Mewtwo", "Mew" };

    /**
     * 构造一个BotPlayer
     * 
     * @param height 游戏区域的高度
     * @param width  游戏区域的宽度
     * @param color  Bot的颜色
     */
    BotPlayer(int height, int width, Color color) {
        super(height, width, color);
        this.name = names[new Random().nextInt(names.length)];
    }

    /**
     * 采用随机的方式让AI移动
     */
    @Override
    public void move() {
        x += dx;
        y += dy;
        double rand = Math.random();
        int old_dx=dx;
        int old_dy=dy;
        if (rand < 0.05 && dx != -1) {
            dx = 1;
            dy = 0;
        } else if (rand < 0.1 && dx != 1) {
            dx = -1;
            dy = 0;
        } else if (rand < 0.15 && dy != -1) {
            dx = 0;
            dy = 1;
        } else if (rand < 0.2 && dy != 1) {
            dx = 0;
            dy = -1;
        }
        if (!checkSuicide(dx, dy)) {
            if (old_dx != -1) {
                dx = 1;
                dy = 0;
                if (checkSuicide(dx, dy)) {
                    return;
                }
            }
            if (old_dx != 1) {
                dx = -1;
                dy = 0;
                if (checkSuicide(dx, dy)) {
                    return;
                }
            }
            if (old_dy != -1) {
                dx = 0;
                dy = 1;
                if (checkSuicide(dx, dy)) {
                    return;
                }
            }
            if (old_dy != 1) {
                dx = 0;
                dy = -1;
                if (checkSuicide(dx, dy)) {
                    return;
                }
            }
        }
        avoidOutOfBounds();
    }

    /**
     * 预测是否会碰撞自己的尾迹
     */
    private boolean checkSuicide(int ddx, int ddy) {
        ArrayList<Tile> tilesContested = this.getTilesContested();
        for (Tile tile : tilesContested) {
            int tx = tile.getX();
            int ty = tile.getY();
            if (x + ddx == tx && y + ddy == ty) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查是否会碰壁
     */
    private void avoidOutOfBounds() {
        if (x == 0 && y == height - 1) {
            if (dx == -1) {
                dx = 0;
                dy = -1;
            } else {
                dx = 1;
                dy = 0;
            }
        } else if (x == width - 1 && y == 0) {
            if (dx == 1) {
                dx = 0;
                dy = 1;
            } else {
                dx = -1;
                dy = 0;
            }
        } else if (x == width - 1 && y == height - 1) {
            if (dx == 1) {
                dx = 0;
                dy = -1;
            } else {
                dx = -1;
                dy = 0;
            }
        } else if (x == 0 && y == 0) {
            if (dx == -1) {
                dx = 0;
                dy = 1;
            } else {
                dx = 1;
                dy = 0;
            }
        } else if (x == 0 && dx == -1) {
            dx = 0;
            dy = 1;
        } else if (x == width - 1 && dx == 1) {
            dx = 0;
            dy = 1;
        } else if (y == 0 && dy == -1) {
            dx = 1;
            dy = 0;
        } else if (y == height - 1 && dy == 1) {
            dx = 1;
            dy = 0;
        }
    }

    /**
     * 重写die方法. 在死亡一段时间后Bot会复活
     */
    @Override
    void die() {
        super.die();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setAlive(true);
            }
        }, 5000);
    }
}