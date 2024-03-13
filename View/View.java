package View;

import Model.Deck;
import Controller.GamePhase;
import Controller.Controller;
import javax.swing.*;
import java.awt.*;

public class View extends JFrame{
    private JFrame MunchkinMenu;
    private JLabel NameGameLabel;
    private JButton btnGame;
    private JButton btnQuit;
    private JPanel panelBoutons;
    private Deck deck;

    private boolean isGameStarted = false;

    public View(Deck deck) {
        this.deck = deck;
        
        MunchkinMenu = new JFrame ("Munchkin Game");
        MunchkinMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MunchkinMenu.setSize(400, 300);
        MunchkinMenu.setLocationRelativeTo(null);

        NameGameLabel = new JLabel("Munchkin Game", JLabel.CENTER);
        NameGameLabel.setFont(new Font("Arial", Font.BOLD, 40));
        MunchkinMenu.add(NameGameLabel); 
    
        // Définissez le panel
        panelBoutons = new JPanel();
        btnGame = new JButton("Jouer");
        btnQuit = new JButton("Quitter"); 

        //taille bouttons
        btnGame.setPreferredSize(new Dimension(100, 40));
        btnQuit.setPreferredSize(new Dimension(100, 40));    
        // Ajouter les boutons au frame
        panelBoutons.add(btnGame); 
        panelBoutons.add(btnQuit);
         
        // Ajouter label et panel au frame
        MunchkinMenu.setLayout(new GridLayout(2, 1));
        MunchkinMenu.add(panelBoutons);
    }
    
    public void setControleur(Controller controleur) {
        btnGame.addActionListener(e -> {
            if (!isGameStarted) {
                GamePhase gamePhase = new GamePhase();
                GameBoard gameBoard = new GameBoard(gamePhase, deck);
                gameBoard.setVisible(true);
                isGameStarted = true;
                MunchkinMenu.dispose();
            }
        });
        btnQuit.addActionListener(e -> System.exit(0));
    }  
    
    public void afficher() {
        MunchkinMenu.setVisible(true);
    }
}

