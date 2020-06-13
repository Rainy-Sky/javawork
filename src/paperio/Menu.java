package paperio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

/**
 * Class responsible for displaying menu and receiving game settings. Uses an actionListener to forward actions from
 * Menu.
 */
public class Menu extends JPanel{

	private ImageIcon back;
	private ImageIcon button1_pic;
	private ImageIcon button2_pic;
	private ImageIcon setting;
	private ImageIcon record;
	private ImageIcon music;
	private JButton button_music;
	private JButton button1;
	private JButton button2;
	private JButton button_set;
	private JButton button_record;
    private JTextField p1NameFld; 
    private JTextField p2NameFld;
    private ActionListener actionListener;

    private String[] names = {"Bulbasaur", "Ivysaur", "Venusaur", "Charmander", "Charmeleon", "Charizard", "Squirtle", "Wartortle", "Blastoise", "Caterpie", "Metapod", "Butterfree", "Weedle", "Kakuna", "Beedrill", "Pidgey", "Pidgeotto", "Pidgeot", "Rattata", "Raticate", "Spearow", "Fearow", "Ekans", "Arbok", "Pikachu", "Raichu", "Sandshrew", "Sandslash", "Nidoran", "Nidorina", "Nidoqueen", "Nidoran", "Nidorino", "Nidoking", "Clefairy", "Clefable", "Vulpix", "Ninetales", "Jigglypuff", "Wigglytuff", "Zubat", "Golbat", "Oddish", "Gloom", "Vileplume", "Paras", "Parasect", "Venonat", "Venomoth", "Diglett", "Dugtrio", "Meowth", "Persian", "Psyduck", "Golduck", "Mankey", "Primeape", "Growlithe", "Arcanine", "Poliwag", "Poliwhirl"
    		, "Poliwrath", "Abra", "Kadabra", "Alakazam", "Machop", "Machoke", "Machamp", "Bellsprout", "Weepinbell", "Victreebel", "Tentacool", "Tentacruel", "Geodude", "Graveler", "Golem", "Ponyta", "Rapidash", "Slowpoke", "Slowbro", "Magnemite", "Magneton", "Farfetch'd", "Doduo", "Dodrio", "Seel", "Dewgong", "Grimer", "Muk", "Shellder", "Cloyster", "Gastly", "Haunter", "Gengar", "Onix", "Drowzee", "Hypno", "Krabby", "Kingler", "Voltorb", "Electrode", "Exeggcute", "Exeggutor", "Cubone", "Marowak", "Hitmonlee", "Hitmonchan", "Lickitung", "Koffing", "Weezing", "Rhyhorn", "Rhydon", "Chansey", "Tangela", "Kangaskhan", "Horsea", "Seadra", "Goldeen", "Seaking", "Staryu", "Starmie", "Mr. Mime", "Scyther", "Jynx", "Electabuzz", "Magmar"
    		, "Pinsir", "Tauros", "Magikarp", "Gyarados", "Lapras", "Ditto", "Eevee", "Vaporeon", "Jolteon", "Flareon", "Porygon", "Omanyte", "Omastar", "Kabuto", "Kabutops", "Aerodactyl", "Snorlax", "Articuno", "Zapdos", "Moltres", "Dratini", "Dragonair", "Dragonite", "Mewtwo", "Mew"};

    /**
     * Initializes a menu with actionListener responsible for reacting to game start
     * @param actionListener actionListener responsible for reacting to game start
     */
    Menu(ActionListener actionListener){
        this.actionListener = actionListener;

        this.setOpaque(false);
        back = new ImageIcon("img/background.jpg");
		JLabel imgLabel = new JLabel(back);
		imgLabel.setBounds(0, 0, back.getIconWidth(), back.getIconHeight());	
		
		button1_pic=new ImageIcon("img/play_Single.png");
		button2_pic=new ImageIcon("img/play_multi.png");
		record=new ImageIcon("img/record.jpg");
		music=new ImageIcon("img/music.jpg");
		button1=new JButton(button1_pic);
		button2=new JButton(button2_pic);
		button_record=new JButton(record);
		button_music=new JButton(music);
		
		setting=new ImageIcon("img/setting.jpg");
		button_set=new JButton(setting);
		
		this.setLayout(null);
        addComponents();
        add(imgLabel);
    }

    /**
     * Adds and styles all components in menu
     */
    private void addComponents(){ 
        add(new JLabel(" "));
        add(new JLabel(" "));

        /**
         *  Play buttons
         */
		add(button1);
		add(button2);
        button1.setBounds(700,495,button1_pic.getIconWidth(),button1_pic.getIconHeight());
        button1.setActionCommand("Play Singleplayer");
        button1.addActionListener(actionListener);
        button1.setBackground(Color.DARK_GRAY);
        button1.setFocusPainted(false);
        button1.setBorderPainted(false);

        button2.setBounds(700,570,button2_pic.getIconWidth(),button2_pic.getIconHeight());
        button2.setActionCommand("Play Multiplayer");
        button2.addActionListener(actionListener);
        button2.setBackground(Color.DARK_GRAY);
        button2.setFocusPainted(false);
        button2.setBorderPainted(false);

        /**
         *  Name labels
         */
        JLabel p1name = new JLabel("Player1 name:");
        JLabel p2name = new JLabel("Player2 name:");

        // Style and add labels
        p1name.setFont(new Font("Monospaced", Font.PLAIN, 20));
        p1name.setBounds(275, 500, 200, 40);
        p1name.setForeground(Color.WHITE);
        p1name.setHorizontalAlignment(JLabel.CENTER);
        p1name.setVerticalAlignment(JLabel.BOTTOM);
        add(p1name);
        p2name.setFont(new Font("Monospaced", Font.PLAIN, 20));
        p2name.setBounds(275, 575, 200, 40);
        p2name.setForeground(Color.WHITE);
        p2name.setHorizontalAlignment(JLabel.CENTER);
        p2name.setVerticalAlignment(JLabel.BOTTOM);
        add(p2name);

        /**
         *  Name text fields
         */
        p1NameFld = new JTextField(names[new Random().nextInt(names.length)]);
        p2NameFld = new JTextField(names[new Random().nextInt(names.length)]);

        /**
         * Styles and adds name text fields
         */
        p1NameFld.setFont(new Font("Monospaced", Font.PLAIN, 25));
        p1NameFld.setBounds(480, 500, 200, 60);
        p1NameFld.setBackground(Color.DARK_GRAY);
        p1NameFld.setForeground(Color.gray.brighter());
        p1NameFld.addMouseListener(new FieldMouseListener(p1NameFld));
        p1NameFld.setHorizontalAlignment(JTextField.CENTER);
        p1NameFld.setCaretColor(Color.WHITE);
        add(p1NameFld);
        p2NameFld.setFont(new Font("Monospaced", Font.PLAIN, 25));
        p2NameFld.setBounds(480, 575, 200, 60);
        p2NameFld.setBackground(Color.DARK_GRAY);
        p2NameFld.setForeground(Color.gray.brighter());
        p2NameFld.addMouseListener(new FieldMouseListener(p2NameFld));
        p2NameFld.setHorizontalAlignment(JTextField.CENTER);
        p2NameFld.setCaretColor(Color.WHITE);
        add(p2NameFld);
        
        button_set.setBounds(900,525,setting.getIconWidth(),setting.getIconHeight());
        button_set.setActionCommand("Setting");
        button_set.addActionListener(actionListener);
        button_set.setBackground(Color.DARK_GRAY);
        button_set.setFocusPainted(false);
        button_set.setBorderPainted(false);
        add(button_set);
        button_record.setBounds(170,515,record.getIconWidth(),record.getIconHeight());
        button_record.setActionCommand("record");
        button_record.addActionListener(actionListener);
        button_record.setBackground(Color.DARK_GRAY);
        button_record.setFocusPainted(false);
        button_record.setBorderPainted(false);
        add(button_record);
        button_music.setBounds(1100,150,record.getIconWidth(),record.getIconHeight());
        button_music.setActionCommand("music");
        button_music.addActionListener(actionListener);
        button_music.setBackground(Color.DARK_GRAY);
        button_music.setFocusPainted(false);
        button_music.setBorderPainted(false);
        add(button_music);
    }

    /**
     * Get player 1 name written in p1 name field
     * @return player 1 name written in p1 name field
     */
    public String getP1Name() {
        return p1NameFld.getText();
    }

    /**
     * Get player 2 name written in p1 name field
     * @return player 2 name written in p1 name field
     */
    public String getP2Name() {
        return p2NameFld.getText();
    }

    /**
     * Mouse listener responsible for reacting on name text field clicks and clearing placeholder
     */
    class FieldMouseListener implements MouseListener {

        private boolean changed = false;
        private JTextField textField;

        public FieldMouseListener(JTextField textField) {
            this.textField = textField;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(!changed) {
                textField.setText("");
                textField.setForeground(Color.WHITE);
            }
            changed = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }  

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
