package View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;

import java.awt.*;

import Model.Joueur;
import Model.Deck;
import Controller.GamePhase;
import Model.MunchkinCard.*;

public class GameBoard extends JFrame {
    private GamePhase gamePhase;
    private List<Joueur> joueurs;
    private JLabel PlayerLabel;
    private int currentPlayerIndex = 0;
    private boolean isPhase0 = true;
    private boolean pickCardTresor = false;
    private JButton btnNextPlayer;
    private JPanel panelMiddle;
    private JPanel piocheAndBin;
    private JPanel pickCard;
    private List<MunchkinCard> cardPick = new ArrayList<>();
    private JPanel playerItem;
    private JPanel panelSouth;
    private Deck deck;
    private int cardWidth = 150;
    private int cardHeight = 200;

    public GameBoard(GamePhase gamePhase, Deck deck) {
        this.deck = deck;
        this.gamePhase = gamePhase;

        setTitle("Munchkin Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(200, 255, 200));
        setLayout(new BorderLayout()); // Utilisez un BorderLayout pour organiser les composants

        setVisible(true);

        joueurs = Joueur.creerJoueurs();
        gamePhase.startPhase0(joueurs, deck);

        // panel pour le moment pour avoir un bouton pour passer au joueur suivant
        JPanel panel = new JPanel();
        panel.setBackground(new Color(200, 255, 200));
        PlayerLabel = new JLabel();
        panel.add(PlayerLabel, BorderLayout.NORTH); // Ajoute le PlayerLabel directement au GameBoard
        updatePlayerLabels(); // Met à jour les étiquettes au début du jeu

        // Ajoutez un bouton pour passer au joueur suivant
        btnNextPlayer = new JButton("Joueur suivant");
        btnNextPlayer.setPreferredSize(new Dimension(200, 40));
        panel.add(btnNextPlayer);
        add(panel, BorderLayout.NORTH);

        panelMiddle = new JPanel(new GridLayout());
        piocheAndBin = addPiocheAndBin(cardWidth, cardHeight);
        panelMiddle.add(piocheAndBin, BorderLayout.NORTH);
        pickCard = addCardPick(cardWidth, cardHeight);
        panelMiddle.add(pickCard, BorderLayout.CENTER);
        playerItem = addPlayerItem(cardWidth, cardHeight, joueurs.get(currentPlayerIndex).getItems());
        panelMiddle.add(playerItem, BorderLayout.SOUTH);
        add(panelMiddle, BorderLayout.CENTER);

        panelSouth = new JPanel(new GridLayout());
        JPanel playerCard = addPlayerCard(cardWidth, cardHeight, joueurs.get(currentPlayerIndex).getCartes());
        panelSouth.add(playerCard, BorderLayout.SOUTH);
        add(panelSouth, BorderLayout.SOUTH);

        revalidate();
        repaint();

        btnNextPlayer.addActionListener(e -> {
            if (joueurs.get(currentPlayerIndex).getCartes().size() > 5 
                || (joueurs.get(currentPlayerIndex).getItems().size() > 6 
                && joueurs.get(currentPlayerIndex).getRace().equals("Puant"))) {
                JOptionPane.showMessageDialog(null, "Vous ne pouvez pas passer au joueur suivant vous avez trop de cartes");
            }else{
                nextplayer();
                if (!isPhase0) {
                    pick(cardWidth, cardHeight);                  
                }
            }
        });

    }
    private void pick(int cardWidth, int cardHeight){
        // pioche une carte donjon
        MunchkinCard tempDonjon = deck.getRandomDonjonCard();
        cardPick.add(tempDonjon);
        pickCard.removeAll();
        // affiche la carte piochee (nom et image)
        JButton btnPickDonjon = createButton(tempDonjon.getName(), cardPick, cardWidth, cardHeight,
                tempDonjon.getImage(), true);
        pickCard.add(btnPickDonjon);
        revalidate();
        repaint();
        // joue la carte piochee
        gamePhase.playCardPick(joueurs.get(currentPlayerIndex), tempDonjon, joueurs, cardPick, deck);
        checkIfPlayerWin(joueurs.get(currentPlayerIndex));
        if (tempDonjon.getCategory().equals("Monstre") || tempDonjon.getCategory().equals("Malediction")) {
            if (tempDonjon.getCategory().equals("Monstre")) {
                pickCardTresor = true;
            } else {
                pickCardTresor = false;
            }
            updatePlayerLabels();
            updatePlayerItems();
            updatePiocheAndBin();
            updatePlayerCards();
            chekIfDead();
        } else {
            pickCard.removeAll();
            JButton btnPickDonjon2 = createButton("Carte pioche", cardPick, cardWidth, cardHeight,
                    Toolkit.getDefaultToolkit().getImage(""), false);
            pickCard.add(btnPickDonjon2);
            updatePiocheAndBin();
            updatePlayerCards();
        }
    }
    private void checkIfPlayerWin(Joueur currentPlayer) {
        if (currentPlayer.getLevel() >= 10 || currentPlayer.getCredits() >= 300) {
            if(currentPlayer.getLevel() >= 10) {
                JOptionPane.showMessageDialog(null, "Le joueur " + currentPlayer.getUsername() + " a gagné ! (Niveau 10 atteint)");
            } else {
                JOptionPane.showMessageDialog(null, "Le joueur " + currentPlayer.getUsername() + " a gagné ! (300 crédits atteints)");
            }
            System.exit(0);
        }     
    }

    private void chekIfDead(){
        if(gamePhase.isDead){
            nextplayer();
            pick(cardWidth, cardHeight);
            JOptionPane.showMessageDialog(null, "Au tour du joueur " + joueurs.get(currentPlayerIndex).getUsername() + " de jouer");
            gamePhase.isDead = false;
        }
    }

    private void nextplayer() {
        pickCardTresor = false;
        currentPlayerIndex = (currentPlayerIndex + 1) % joueurs.size();
        if (currentPlayerIndex == 0) {
            isPhase0 = false;
        }
        updatePlayerLabels();
        updatePlayerItems();
        updatePlayerCards();
    }

    private JButton createButton(String text, List<MunchkinCard> cards, int cardWidth, int cardHeight, Image image,
            Boolean isFaceup) {

        JButton button = new JButton(text);
        ImageIcon icon = new ImageIcon(image);
        // Redimensionnez l'image selon la taille souhaitée
        Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(img);
        button.setIcon(resizedIcon);
        // Configurez le texte sous l'icône (si nécessaire)
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setPreferredSize(new Dimension(cardWidth, cardHeight));

        if (cards.size() != 0 && !isFaceup) {
            button.setBackground(new Color(183, 116, 46));
        } else if (cards.size() != 0 && isFaceup) {
            button.setBackground(new Color(241, 222, 201));
        } else {
            button.setBackground(new Color(200, 255, 200));
        }
        return button;
    }

    private JPanel addPiocheAndBin(int cardWidth, int cardHeight) {
        JButton btnBinDonjon = createButton("Bin donjon", deck.getBinDonjon(), cardWidth, cardHeight,
                Toolkit.getDefaultToolkit().getImage("ressources/image/bin.png"), false);
        JButton btnPiocheDonjon = createButton("Donjon", deck.getDeckDonjon(), cardWidth, cardHeight,
                Toolkit.getDefaultToolkit().getImage("ressources/image/donjon_image.png"), false);
        JButton btnPiocheTresor = createButton("Tresor", deck.getDeckTresor(), cardWidth, cardHeight,
                Toolkit.getDefaultToolkit().getImage("ressources/image/tresor_image.png"), false);
        JButton btnBinTresor = createButton("Bin tresor", deck.getBinTresor(), cardWidth, cardHeight,
                Toolkit.getDefaultToolkit().getImage("ressources/image/bin.png"), false);

        JPanel panelPiocheAndBin = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panelPiocheAndBin.setBackground(new Color(200, 255, 200));
        panelPiocheAndBin.add(btnBinDonjon);
        panelPiocheAndBin.add(btnPiocheDonjon);
        panelPiocheAndBin.add(btnPiocheTresor);
        panelPiocheAndBin.add(btnBinTresor);

        return panelPiocheAndBin;
    }

    private JPanel addCardPick(int cardWidth, int cardHeight) {
        JButton btnPickDonjon = createButton("Carte pioche", cardPick, cardWidth, cardHeight,
                Toolkit.getDefaultToolkit().getImage(""), false);

        JPanel panelCardPick = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panelCardPick.setBackground(new Color(200, 255, 200));
        panelCardPick.add(btnPickDonjon);

        return panelCardPick;
    }

    private JPanel addPlayerItem(int cardWidth, int cardHeight, List<MunchkinCard> playerItems) {
        JButton btnItemHead = createButton("Equipement Tete", playerItems, 150, 200,
                Toolkit.getDefaultToolkit().getImage("ressources/image/head_item.png"), false);
        JButton btnItemBody = createButton("Equipement Corps", playerItems, 150, 200,
                Toolkit.getDefaultToolkit().getImage("ressources/image/body_item.png"), false);
        JButton btnItemWeapon = createButton("Equipement Arme", playerItems, 150, 200,
                Toolkit.getDefaultToolkit().getImage("ressources/image/weapons_item.png"), false);

        JPanel panelPlayerItem = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panelPlayerItem.setBackground(new Color(200, 255, 200));
        panelPlayerItem.add(btnItemHead);
        panelPlayerItem.add(btnItemBody);
        panelPlayerItem.add(btnItemWeapon);

        return panelPlayerItem;
    }

    private JPanel addPlayerCard(int cardWidth, int cardHeight, List<MunchkinCard> playerCards) {
        JPanel panelPlayerCard = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panelPlayerCard.setBackground(new Color(200, 255, 200));
        for (MunchkinCard card : playerCards) {
            JButton button = card.DrawCard(cardWidth, cardHeight);
            Joueur currentPLayer = joueurs.get(currentPlayerIndex);
            button.addActionListener(e -> {
                if (isPhase0) {
                    if (card.getCategory() == "Equipement" || card.getCategory() == "Race" ||
                            card.getCategory() == "Classe" || card.getCategory() == "Credits") {

                        int result = JOptionPane.showConfirmDialog(null,
                                "Voulez-vous jouer cette carte : " + card.getName(), "Confirmation",
                                JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.YES_OPTION) {
                            playCard(currentPLayer, card);
                        } else {
                            System.out.println("Le joueur a annulé la carte : " + card.getName());
                        }
                    } else {
                        // vous ne pouvez pas jouer cette carte au premier tour
                        JOptionPane.showMessageDialog(null, "Vous ne pouvez pas jouer cette carte au premier tour");
                    }
                } else {
                    askWhatToDoWithCard(currentPLayer, card);
                }
            });
            panelPlayerCard.add(button);
        }
        return panelPlayerCard;
    }

    private void playCard(Joueur currentPlayer, MunchkinCard card) {
        if (card.getCategory().equals("Race")) {
            gamePhase.applyRaceEffect(joueurs.get(currentPlayerIndex), card);
            updatePlayerLabels();
        } else if (card.getCategory().equals("Classe")) {
            gamePhase.applyClassEffect(joueurs.get(currentPlayerIndex), card);
            updatePlayerLabels();
        } else if (card.getCategory().equals("Credits")) {
            gamePhase.applyCreditEffect(joueurs.get(currentPlayerIndex));
            checkIfPlayerWin(currentPlayer);
            updatePlayerLabels();
        } else if (card.getCategory().equals("Equipement")) {
            joueurs.get(currentPlayerIndex).addItem(card);
            updatePlayerItems();
        } else if (card.getCategory().equals("Malediction")) {
            // demande a quel joueur il veut appliquer la malediction
            List<Joueur> tempPlayers = new ArrayList<>(joueurs);
            tempPlayers.remove(currentPlayerIndex);
            // trie les joueurs par niveau
            Collections.sort(tempPlayers, Comparator.comparingInt(Joueur::getLevel));
            Collections.reverse(tempPlayers);
            boolean isApply = false;
            for (Joueur player : tempPlayers) {
                if (isApply) {
                    break;
                }
                int result = JOptionPane.showConfirmDialog(null,
                        "Voulez vous appliquer la malediction" + card.getName() + " au joueur " + player.getUsername(),
                        "Confirmation", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    isApply = true;
                    updatePiocheAndBin();
                    updatePlayerItems();
                    updatePlayerLabels();
                    updatePlayerCards();
                }
            }
        } else if (card.getCategory().equals("Monstre")) {
            gamePhase.fightWithMonster(currentPlayer, (MunchkinCardMonster) card, joueurs, deck);
            updatePiocheAndBin();
            updatePlayerItems();
            updatePlayerLabels();
            updatePlayerCards();
            checkIfPlayerWin(currentPlayer);
        }
        if (card.getCategory().equals("Monstre")){
            pickCardTresor = true;
        } else {
            pickCardTresor = false;
        }
        // ajoute la carte a la bin donjon ou tresor selon la categorie
        // la carte equipement est ajoutee au joueur
        if (card.getCategory().equals("Credits")) {
            deck.getBinTresor().add(card);
        } else if (card.getCategory().equals("Race") ||
                card.getCategory().equals("Classe")) {
            deck.getBinDonjon().add(card);
        }
        joueurs.get(currentPlayerIndex).getCartes().remove(card);

        chekIfDead();

        updatePiocheAndBin();
        updatePlayerCards();
    }

    private void askWhatToDoWithCard(Joueur currentPlayer, MunchkinCard card) {
        if (pickCardTresor) {
            // si le joueur a pioche une carte tresor il ne peut plus jouer de carte
            String[] options = {"Donner", "Ne rien faire" };
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Que voulez-vous faire avec la carte " + card.getName() + "?",
                    "Choix de carte de categorie " + card.getCategory(),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            switch (choice) {
                case 0: // Donne la carte au joueur le plus faible
                    giveCard(currentPlayer, joueurs, card);
                    break;
                case 1: // ne rien faire
                    break;
                default:
                    break;
            }
        } else {
            String[] options = { "Utiliser", "Vendre", "Ne rien faire" };
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Que voulez-vous faire avec la carte " + card.getName() + "?",
                    "Choix de carte de categorie " + card.getCategory(),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            switch (choice) {
                case 0: // Utiliser la carte
                    playCard(currentPlayer, card);
                    break;
                case 1: // Vendre la carte
                    askWhoWantToBuyCard(currentPlayer, card, joueurs);
                    break;
                case 2: // Ne rien faire
                    break;
                default:
                    break;
            }
        }
    }

    private void giveCard(Joueur currentPlayer, List<Joueur> players, MunchkinCard card) {
        List<Joueur> lowplayers = new ArrayList<>(players);
        lowplayers.remove(currentPlayerIndex);
        // trie les joueurs par niveau croissant
        Collections.sort(lowplayers, Comparator.comparingInt(Joueur::getLevel));
        Collections.reverse(lowplayers);
        //donne la carte au joueur le plus faible
        lowplayers.get(0).addCard(card);
        joueurs.get(currentPlayerIndex).getCartes().remove(card);
        updatePlayerCards();
    }

    private void askWhoWantToBuyCard(Joueur currentPlayer, MunchkinCard card, List<Joueur> joueurs) {
        List<Joueur> tempPlayers = new ArrayList<>(joueurs);
        tempPlayers.remove(currentPlayerIndex);
        // trie les joueurs par niveau
        Collections.sort(tempPlayers, Comparator.comparingInt(Joueur::getLevel));
        Collections.reverse(tempPlayers);
        boolean isBuy = false;
        for (Joueur player : tempPlayers) {
            if (isBuy) {
                break;
            }
            int result = JOptionPane.showConfirmDialog(null,
                    "Joueur : " + player.getUsername() + " Voulez-vous acheter cette carte : " + card.getName()
                            + " pour le prix de "
                            + card.getPrice(),
                    "Confirmation", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                //si le joueur qui veut acheter et de la classe genie mecanique il a 1/2 chance de voler l'item
                if(player.getClasse().equals("Genie mecanique")) {
                    int random = (int) (Math.random() * 2);
                    if(random == 1) {
                        JOptionPane.showMessageDialog(null, "Le joueur " + player.getUsername() + " a volé l'item grâce à sa classe Genie mecanique");
                        // le joueur achete la carte et le joueur actuel la vend
                        player.addCard(card);
                        joueurs.get(currentPlayerIndex).getCartes().remove(card);
                        updatePlayerItems();
                        updatePlayerLabels();
                        updatePlayerCards();
                        isBuy = true;
                    }
                }else if (player.getCredits() > card.getPrice()) {
                    // le joueur achete la carte et le joueur actuel la vend
                    player.addCard(card);
                    player.setCredits(player.getCredits() - card.getPrice());
                    //si le joueur qui vend est de la race Alternant il gagne 2 fois le prix de l'item
                    if(currentPlayer.getRace().equals("Alternant")) {
                        currentPlayer.setCredits(currentPlayer.getCredits() + card.getPrice()*2);
                    } else {
                        currentPlayer.setCredits(currentPlayer.getCredits() + card.getPrice());
                    }
                    joueurs.get(currentPlayerIndex).getCartes().remove(card);
                    updatePlayerItems();
                    updatePlayerLabels();
                    updatePlayerCards();
                    isBuy = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Vous n'avez pas assez de credits");
                }
            }
        }
    }

    private void updatePlayerLabels() {
        Joueur joueurActuel = joueurs.get(currentPlayerIndex);
        Joueur prochainJoueur = joueurs.get((currentPlayerIndex + 1) % joueurs.size());

        PlayerLabel.setText("<html> Joueur actuel : " + joueurActuel.getUsername() + " (" +
                joueurActuel.getRace() + ", " + joueurActuel.getClasse() + ", Niveau " + joueurActuel.getLevel() +
                ", Crédits " + joueurActuel.getCredits() + ")" + "<br>" +
                "Prochain joueur : " + prochainJoueur.getUsername() + " (" +
                prochainJoueur.getRace() + ", " + prochainJoueur.getClasse() + ", Niveau " + prochainJoueur.getLevel() +
                ", Crédits " + prochainJoueur.getCredits() + ") </html>");
    }

    private void updatePiocheAndBin() {
        piocheAndBin.removeAll();
        int cardHeight = 200;
        int cardWidth = 150;
        // ajoute les bouttons de la pioche et bin
        // on devrai reutiliser la methode addPiocheAndBin mais cela fait un bug visuel
        // il n'est plus a la meme place
        // piocheAndBin.add(addPiocheAndBin(cardWidth, cardHeight));
        JButton btnBinDonjon = createButton("Bin donjon", deck.getBinDonjon(), cardWidth, cardHeight,
                Toolkit.getDefaultToolkit().getImage("ressources/image/bin.png"), false);
        JButton btnPiocheDonjon = createButton("Donjon", deck.getDeckDonjon(), cardWidth, cardHeight,
                Toolkit.getDefaultToolkit().getImage("ressources/image/donjon_image.png"), false);
        JButton btnPiocheTresor = createButton("Tresor", deck.getDeckTresor(), cardWidth, cardHeight,
                Toolkit.getDefaultToolkit().getImage("ressources/image/tresor_image.png"), false);
        JButton btnBinTresor = createButton("Bin tresor", deck.getBinTresor(), cardWidth, cardHeight,
                Toolkit.getDefaultToolkit().getImage("ressources/image/bin.png"), false);
        piocheAndBin.add(btnBinDonjon);
        piocheAndBin.add(btnPiocheDonjon);
        piocheAndBin.add(btnPiocheTresor);
        piocheAndBin.add(btnBinTresor);
    }

    private void updatePlayerCards() {
        panelSouth.removeAll(); // Nettoyer le panel des cartes du joueur actuel
        JPanel newPlayerCardPanel = addPlayerCard(150, 200, joueurs.get(currentPlayerIndex).getCartes());
        panelSouth.add(newPlayerCardPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void updatePlayerItems() {
        // Nettoyer le panel des items du joueur actuel et ajouter les nouveaux items
        List<MunchkinCard> playerItems = joueurs.get(currentPlayerIndex).getItems();

        playerItem.removeAll();

        // Vérifiez si le joueur a des items et mettez à jour les boutons correspondants
        if (!playerItems.isEmpty()) {
            List<MunchkinCard> headItems = playerItems.stream()
                    .filter(item -> item.getName().split("\\s+")[0].equalsIgnoreCase("Helmet"))
                    .collect(Collectors.toList());

            List<MunchkinCard> bodyItems = playerItems.stream()
                    .filter(item -> item.getName().split("\\s+")[0].equalsIgnoreCase("Body"))
                    .collect(Collectors.toList());

            List<MunchkinCard> weaponItems = playerItems.stream()
                    .filter(item -> item.getName().split("\\s+")[0].equalsIgnoreCase("Weapons"))
                    .collect(Collectors.toList());

            // tete item
            if (!headItems.isEmpty()) {
                if (headItems.size() > 1) {
                    // ajoute l'ancien carte dans la bin tresor
                    deck.getBinTresor().add(headItems.get(0));
                    playerItems.remove(headItems.get(0));
                    headItems.remove(0);
                    updatePiocheAndBin();
                }
                MunchkinCard HeadItem = headItems.get(0);
                JButton btnItemHead = createButton(HeadItem.getName(), headItems, 150, 200, HeadItem.getImage(), true);
                playerItem.add(btnItemHead);
            } else {
                // redessine les bouttons vides
                JButton btnItemHead = createButton("Equipement Tete", headItems, 150, 200,
                        Toolkit.getDefaultToolkit().getImage("ressources/image/head_item.png"), false);
                playerItem.add(btnItemHead);
            }
            // corps item
            if (!bodyItems.isEmpty()) {
                if (bodyItems.size() > 1) {
                    // ajoute l'ancien carte dans la bin tresor
                    deck.getBinTresor().add(bodyItems.get(0));
                    playerItems.remove(bodyItems.get(0));
                    bodyItems.remove(0);
                    updatePiocheAndBin();
                }
                MunchkinCard BodyItem = bodyItems.get(0);
                JButton btnItemBody = createButton(BodyItem.getName(), bodyItems, 150, 200, BodyItem.getImage(), true);
                playerItem.add(btnItemBody);
            } else {
                // redessine les bouttons vides
                JButton btnItemBody = createButton("Equipement Corps", bodyItems, 150, 200,
                        Toolkit.getDefaultToolkit().getImage("ressources/image/body_item.png"), false);
                playerItem.add(btnItemBody);
            }
            // arme item
            if (!weaponItems.isEmpty()) {
                if (weaponItems.size() > 1) {
                    // ajoute l'ancien carte dans la bin tresor
                    deck.getBinTresor().add(weaponItems.get(0));
                    //remove l'item de la liste player item
                    playerItems.remove(weaponItems.get(0));
                    weaponItems.remove(0);
                    updatePiocheAndBin();
                }
                MunchkinCard WeaponItem = weaponItems.get(0);
                JButton btnItemWeapon = createButton(WeaponItem.getName(), weaponItems, 150, 200, WeaponItem.getImage(),
                        true);
                playerItem.add(btnItemWeapon);
            } else {
                // redessine les bouttons vides
                JButton btnItemWeapon = createButton("Equipement Arme", weaponItems, 150, 200,
                        Toolkit.getDefaultToolkit().getImage("ressources/image/weapons_item.png"), false);
                playerItem.add(btnItemWeapon);
            }
        } else {
            // redessine les bouttons vides
            JButton btnItemHead = createButton("Equipement Tete", playerItems, 150, 200,
                    Toolkit.getDefaultToolkit().getImage("ressources/image/head_item.png"), false);
            JButton btnItemBody = createButton("Equipement Corps", playerItems, 150, 200,
                    Toolkit.getDefaultToolkit().getImage("ressources/image/body_item.png"), false);
            JButton btnItemWeapon = createButton("Equipement Arme", playerItems, 150, 200,
                    Toolkit.getDefaultToolkit().getImage("ressources/image/weapons_item.png"), false);
            playerItem.add(btnItemHead);
            playerItem.add(btnItemBody);
            playerItem.add(btnItemWeapon);
        }
    }
}