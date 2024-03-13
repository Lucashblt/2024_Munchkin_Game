import Model.*;
import View.*;

import javax.swing.*;

import Controller.*;

public class Main {
    //Jeu Munchkin
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            Deck deck = new Deck();
            View view = new View(deck);
            Controller controller = new Controller(view);

            view.setControleur(controller);
            view.afficher();

            deck.initDeck();
        });
    }
}
