package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.swing.*;

import Model.MunchkinCard.MunchkinCard;
import Model.MunchkinCard.MunchkinCardMonster;

import java.awt.*;

public class Joueur {
    private String username;
    private String race;
    private String classe;
    private int level;
    private int playerDamage;
    private int credits;
    private List<MunchkinCard> items;
    private List<MunchkinCard> cartes;

    public Joueur(String username, String race, String classe, int level, int playerDamage, int credits) {
        this.username = username;
        this.race = race;
        this.classe = classe;
        this.level = level;
        this.playerDamage = playerDamage;
        this.credits = credits;
        this.items = new ArrayList<>();
        this.cartes = new ArrayList<>();
    }

    public static List<Joueur> creerJoueurs() {
        List<Joueur> joueurs = new ArrayList<>();
        askNumberOfPlayers(joueurs);
        return joueurs;
    }

    private static void askNumberOfPlayers(List<Joueur> players) {
        int numberOfPlayers = 0;
        boolean validNumberOfPlayers = false;
        boolean validUsernames = false;

        while (!validNumberOfPlayers || !validUsernames) {
            do {
                String input = JOptionPane.showInputDialog("Combien de joueurs veulent jouer ?");
                if (input == null) {
                    int result = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment quitter le jeu ?",
                            "Quitter le jeu",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (result == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                } else {
                    try {
                        numberOfPlayers = Integer.parseInt(input);
                        if (numberOfPlayers >= 2 && numberOfPlayers <= 6) {
                            validNumberOfPlayers = true;
                        } else {
                            JOptionPane.showMessageDialog(null, "Veuillez entrer un nombre de joueurs entre 2 et 6.",
                                    "Nombre de joueurs invalide",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Veuillez entrer un nombre valide.", "Erreur de saisie",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } while (!validNumberOfPlayers);

            JPanel panel = new JPanel(new GridLayout(numberOfPlayers + 1, 2)); // Panel pour les champs de texte
            JTextField[] textFields = new JTextField[numberOfPlayers]; // Tableau pour stocker les champs de texte

            panel.add(new JLabel("Entrez les noms des joueurs :")); // Titre

            for (int i = 0; i < numberOfPlayers; i++) {
                panel.add(new JLabel("Joueur " + (i + 1) + ":"));
                do {
                    textFields[i] = new JTextField(20); // Création des champs de texte
                    int result = JOptionPane.showConfirmDialog(null, textFields[i],
                            "Entrez le nom du Joueur " + (i + 1),
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        if (textFields[i].getText().length() >= 1) {
                            validUsernames = true;
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Veuillez entrer un nom contenant au moins un caractère.",
                                    "Nom de joueur invalide", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        int option = JOptionPane.showConfirmDialog(null,
                                "Voulez-vous annuler l'entrée des noms des joueurs ?", "Annuler l'entrée",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (option == JOptionPane.YES_OPTION) {
                            validUsernames = false;
                            break;
                        }
                    }
                } while (!validUsernames);
            }

            if (validUsernames) {
                players.clear(); // Nettoyer la liste des joueurs pour une nouvelle saisie

                // Récupération des noms des joueurs depuis les champs de texte
                for (int i = 0; i < numberOfPlayers; i++) {
                    String username = textFields[i].getText();
                    Joueur joueur = new Joueur(username, "Etudiant", "aucune", 1, 1, 0);
                    joueur.applyRaceEffect();
                    players.add(joueur); // Ajout du joueur à la liste
                }

            } else {
                System.out.println("Saisie annulée par l'utilisateur.");
            }
        }
    }

    public void applyRaceEffect() {
        switch (race) {
            case "Puant":
                // Effectuer les actions spécifiques aux puants
                break;
            case "Geek":
                // Appliquer les effets spécifiques aux Geeks
                playerDamage += 1;
                break;
            case "Alternant":
                // Appliquer les effets spécifiques aux Alternants
                credits = (int) (credits * 1.5);
                break;
            default:
                // Pour la race par défaut (étudiant), ne rien faire
                break;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPlayerDamage() {
        return playerDamage;
    }

    public void setPlayerDamage(int playerDamage) {
        this.playerDamage = playerDamage;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public List<MunchkinCard> getItems() {
        return this.items;
    }

    public void setItems(MunchkinCard item) {
        this.items.add(item);
    }

    public void addItem(MunchkinCard items) {
        this.items.add(items);
    }

    public void addCard(MunchkinCard card) {
        cartes.add(card);
    }

    public List<MunchkinCard> getCartes() {
        return cartes;
    }

    /**
     * This function is used to ask the player what does he want to do when there is
     * an interaction with the game
     * 
     * @param question This is the question that we are asking
     * @return true if the user answer yes and false else (This function is only for
     *         yes or no questions)
     */
    public boolean askQuestion(String question) {
        // Wait half a second before asking the user
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean userAnswer;
        int result = JOptionPane.showConfirmDialog(null, question, "Question",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            userAnswer = true;
        } else {
            userAnswer = false;
        }
        return userAnswer;
    }

    /**
     * This function is used to remove a random item to a player that loose a fight
     */
    public void removeRandomItem() {
        if (this.items.size() > 0) {
            Random random = new Random();
            int randomNumber = random.nextInt(items.size());
            items.remove(randomNumber);
        }
    }

    /**
     * This is the treatment for a player who die during a fight
     * 
     * @return The list of all his items in order to give them to the other players
     */
    public List<MunchkinCard> isDead(Deck deck) {
        // recuper les items du joueur mort dans une liste et les renvoie
        List<MunchkinCard> deadItems = new ArrayList<>();
        for (MunchkinCard item : this.items) {
            deadItems.add(item);
            System.out.println(item.getName());
        }
        this.credits = 0;
        this.level = 1;
        for (MunchkinCard card : this.cartes) {
            if (card.getCategory().equals("Monster") ||
                    card.getCategory().equals("Malediction") ||
                    card.getCategory().equals("Classe") ||
                    card.getCategory().equals("Race")) {
                deck.getBinDonjon().add(card);
            } else {
                deck.getBinTresor().add(card);
            }
        }
        this.cartes.removeAll(cartes);
        this.items.removeAll(items);
        return deadItems;
    }

    /**
     * This function is used to determine if a player want to betray the one his
     * supposed to help
     * 
     * @return true if he want, false else
     */
    public boolean isBetrayal(String username) {
        return askQuestion(username +"Voulez-vous trahir le joueur que vous aviez prévue d'aider ?");
       
    }

    /**
     * This function is generating a random number in order to determine the chance
     * of winning the fight
     * 
     * @param isCoop      This boolean is used to tell the function if this player
     *                    ask for help or not
     * @param levelHelper This is to know the level of the player that his eventualy
     *                    helping him
     * @return The random value
     */
    public int randomNumberForFightPlayer(boolean isCoop, int levelHelper) {

        Random random = new Random();
        // double alpha = 1.5;
        int level = this.getLevel();

        if (isCoop) {
            level = level + levelHelper;
        }

        if (this.getLevel() > 0) {     
            if (isCoop) {
                switch (this.getLevel()) {
                    case 1:
                        return random.nextInt(2 * 2);
                    case 2:

                        return random.nextInt(3 * 2);
                    case 3:

                        return random.nextInt(4 * 2);
                    case 4:

                        return random.nextInt(5 * 2);
                    case 5:

                        return random.nextInt(6 * 2);
                    case 6:

                        return random.nextInt(7 * 2);
                    case 7:

                        return random.nextInt(8 * 2);
                    case 8:

                        return random.nextInt(9 * 2);
                    case 9:

                        return random.nextInt(10 * 2);
                    default:
                        break;
                }
            } else {
                switch (this.getLevel()) {
                    case 1:
                        return random.nextInt(2);
                    case 2:

                        return random.nextInt(3);
                    case 3:

                        return random.nextInt(4);
                    case 4:

                        return random.nextInt(5);
                    case 5:

                        return random.nextInt(6);
                    case 6:

                        return random.nextInt(7);
                    case 7:

                        return random.nextInt(8);
                    case 8:

                        return random.nextInt(9);
                    case 9:

                        return random.nextInt(10);
                    default:
                        break;
                }
            }
        }
        return -1;
    }

    /**
     * This function is used to ask the user if he want to help the user that ask
     * 
     * @return the player if he said yes, return null if the user don't want to help
     *         him
     */
    public Joueur askForHelp(List<Joueur> players) {
        for (Joueur tempPlayer : players) {
            boolean answer = askQuestion(tempPlayer.getUsername() + " voulez-vous aider ce joueur dans son combat ?");
            if (answer) {
                if (tempPlayer.isBetrayal(tempPlayer.getUsername())) {
                    return null;
                } else {
                    return tempPlayer;
                }
            }
        }
        return null;
    }
    
    /**
     * This function is used to know if the player has chance or not and if his
     * going to escape or not
     * 
     * @return boolean true if he escpaes, false else
     */
    public boolean isEscape() {
        double alpha = 1.2;
        Random random = new Random();
        if (this.level > 5) {
            alpha = 0.5;
        }
        double choice = Math.pow(random.nextDouble(), alpha);
        if (choice == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This function is used to know if the player want to fight with a monster from
     * his own deck
     * 
     * @return boolean true if he want, false else
     */
    public boolean askIfWantToFight() {
        if ((this.getCartes().stream().filter(MunchkinCardMonster.class::isInstance)
                .map(MunchkinCardMonster.class::cast)
                .collect(Collectors.toList()) != null)) {
            return askQuestion(", voulez-vous vous battre avec un monstre dans votre deck ?");
        }
        return false;
    }

    /**
     * This function is used to ask the player if he want to fight in order to
     * obtain the dead player items
     * 
     * @return true if the player want to fight, false else
     */
    public boolean askPlayerForItemsFight() {
        return askQuestion(
                this.getUsername() + " voulez-vous vous battre pour récuperer les équipements du defunt joueur ?");
    }

    /**
     * This function is used to ask the other user of the game if they want to
     * exchange with the current player
     * 
     * @return true if the user want, false else
     */
    public boolean askIfWantToExchange() {
        return askQuestion("Voulez-vous échanger une carte ?");
    }

    /**
     * This function is used to return the value of the player damage for the
     * current user
     * 
     * @return sum of the user player damage
     */
    public int sumPlayerDamage() {
        int sum = 0;
        if (!this.items.isEmpty()) {
            for (MunchkinCard item : items) {
                switch (item.getName()) {
                    case "Helmet of domination":
                        sum += 1;
                        break;

                    case "Helmet for Bald":
                        sum += 2;
                        break;

                    case "Helmet Glasses":
                        sum += 3;
                        break;

                    case "Body Teemo sweat":
                        sum += 1;
                        break;

                    case "Body Sweaty shirt":
                        sum += 2;
                        break;

                    case "Body Starcraft tshirt":
                        sum += 3;
                        break;

                    case "Weapons gun pen":
                        sum += 1;
                        break;

                    case "Weapons nvidia gpu book":
                        sum += 2;
                        break;

                    case "Weapons Cyberweapon":
                        sum += 3;
                        break;

                    default:
                        sum += 0;
                        break;
                }
            }
        }
        sum += this.getLevel();
        if (this.getRace() == "Geek") {
            sum += 1;
        }
        return sum;
    }
}
