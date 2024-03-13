package Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.*;

import Model.MunchkinCard.*;
import Model.*;

public class GamePhase {
    public boolean isDead = false;
    /**
     * This function is used to initialize the game by giving the player every card
     * they need to start
     * 
     * @param players This is the list of all the players of this game
     * @param deck    This is the deck that contains the actual game
     *                configuration of deck
     */
    public void startPhase0(List<Joueur> players, Deck deck) {
        for (int i = 0; i < players.size(); i++) {
            Joueur player = players.get(i);
            for (int j = 0; j < 2; j++) {
                MunchkinCard tempDonjon = deck.getRandomDonjonCard();
                player.addCard(tempDonjon);
                MunchkinCard tempTresor = deck.getRandomTresorCard();
                player.addCard(tempTresor);
            }
        }
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
        // wait half a second before sending the message to the player
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

    public void playCardPick(Joueur currentPlayer, MunchkinCard card, List<Joueur> players, List<MunchkinCard> cardPick, Deck deck) {
        switch (card.getCategory()) {
            case "Monstre":
                System.out.println("You picked a monster, you have to fight with it !");
                applyMonster(currentPlayer, card, players, cardPick, deck);
                break;
            case "Malediction":
                System.out.println("You picked a malediction, this one were apply !");
                applyMaledictionEffect(currentPlayer, card, cardPick, deck);
                break;
            default:
                handleOtherCardType(currentPlayer, card, cardPick, deck);
                break;
        }
    }
    
    private void handleOtherCardType(Joueur currentPlayer, MunchkinCard card, List<MunchkinCard> cardPick, Deck deck) {
        boolean answer = askQuestion("Voulez vous cette carte dans votre main ?");
        if (answer) {
            currentPlayer.addCard(card);
            cardPick.remove(card);
        } else {
            handleCardNotChosen(currentPlayer, card, cardPick, deck);
        }
    }
    
    private void handleCardNotChosen(Joueur currentPlayer, MunchkinCard card, List<MunchkinCard> cardPick, Deck deck) {
        if (card.getCategory().equals("Equipement") || card.getCategory().equals("Credits")) {
            deck.getBinTresor().add(card);
        } else {
            deck.getBinDonjon().add(card);
        }
        cardPick.remove(card);
    }

    public void applyMaledictionEffect(Joueur currentPlayer, MunchkinCard card, List<MunchkinCard> cardPick,
            Deck deck) {
        if (currentPlayer.getClasse() != "génie Industriel") {
            MunchkinCardMalediction malediction = (MunchkinCardMalediction) card;
            JOptionPane.showMessageDialog(null,
                    currentPlayer.getUsername() + " recoit une malediction , " + malediction.getBonusMalus(),
                    "Malediction", JOptionPane.INFORMATION_MESSAGE);
            MunchkinCardMalediction.applyBonusMalus(currentPlayer, card);
            if (currentPlayer.getLevel() == 0) {
                JOptionPane.showMessageDialog(null, currentPlayer.getUsername() +" est mort, il perd vos items et vous recevez 2 cartes tresor et 2 cartes donjon", "Combat",
                            JOptionPane.INFORMATION_MESSAGE);
                currentPlayer.isDead(deck);
                distributeCardAfterDeath(deck, currentPlayer);  
            }
        } else {
            JOptionPane.showMessageDialog(null, currentPlayer.getUsername() +" est de la classe génie Industriel, il est immunisé contre les maledictions !", "Malediction",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        deck.getBinDonjon().add(card);
        cardPick.remove(card);
    }

    public void applyMonster(Joueur currentPlayer, MunchkinCard card, List<Joueur> players, List<MunchkinCard> cardPick,
            Deck deck) {
        List<Joueur> tempPlayers = new ArrayList<>(players);
        tempPlayers.remove(currentPlayer);
        fightWithMonster(currentPlayer, (MunchkinCardMonster) card, tempPlayers, deck);
        System.out.println(deck.getBinDonjon().size());
        System.out.println(cardPick.size());
        deck.getBinDonjon().add(card);
        cardPick.remove(card);
        System.out.println(deck.getBinDonjon().size());
        System.out.println(cardPick.size());
    }

    public void applyRaceEffect(Joueur currentPlayer, MunchkinCard card) {
        if (card.getName() == "Geek" && currentPlayer.getRace() != "Geek") {
            currentPlayer.setRace(card.getName());
            currentPlayer.setPlayerDamage(currentPlayer.getPlayerDamage() + 1);
        } else if (card.getName() == "Puant" || card.getName() == "Alternant"
                || card.getName() == "Etudiant") {
            if (currentPlayer.getRace() == "Geek") {
                currentPlayer.setPlayerDamage(currentPlayer.getPlayerDamage() - 1);
            }
            currentPlayer.setRace(card.getName());
        }
    }

    public void applyClassEffect(Joueur currentPlayer, MunchkinCard card) {
        currentPlayer.setClasse(card.getName());
    }

    public void applyCreditEffect(Joueur currentPlayer) {
        currentPlayer.setCredits(currentPlayer.getCredits() + 6);
    }


    /**
     * This function is used to determine which players want to fight for the dead
     * player's items
     * 
     * @param players This is the list which contains all the players in the game
     *                without the dead player
     * @return finalFightPlayers which is a list that contains all the players who
     *         want to fight for the dead player's item
     */
    private List<Joueur> askWhoWantFight(List<Joueur> players) {
        List<Joueur> finalFightPlayers = new ArrayList<>();
        for (Joueur player : players) {
            if (player.askPlayerForItemsFight()) {
                finalFightPlayers.add(player);
            }
        }
        return finalFightPlayers;
    }

    /**
     * This function is used to create the fight between to different players who
     * want the items of the dead player but which have the same level so it's
     * impossible to say which is the best between both
     * 
     * @param bestPlayer This is the actual best player
     * @param player     This is the player who want to become the best player
     * @return This function return the player who won the fight and who become the
     *         best player
     */
    private Joueur fightWithOtherPlayer(Joueur bestPlayer, Joueur player) {
        int bestPlayerValue;
        int playerValue;
        do {
            bestPlayerValue = bestPlayer.randomNumberForFightPlayer(false, 0);
            playerValue = player.randomNumberForFightPlayer(false, 0);
        } while (bestPlayerValue == playerValue);
        if (bestPlayer.sumPlayerDamage() < player.sumPlayerDamage()) {
            bestPlayerValue += 1;
        } else {
            playerValue += 1;
        }

        if (player.getClasse() == "Electricien" && player.getRace() == "Puant") {
            playerValue = playerValue * 2;
        }
        if (playerValue == bestPlayerValue) {
            Random random = new Random();
            int value = random.nextInt(1);
            if (value == 0) {
                bestPlayerValue += 1;
            } else {
                playerValue += 1;
            }
        }
        if (bestPlayerValue > playerValue) {
            return bestPlayer;
        } else {
            return player;
        }
    }

    /**
     * This function is called to manage the eventual fight between players who want
     * to fight for the items of the dead player
     * For this function, we are considering that if there is only one other player
     * in the game, there is no reason for him to refuse the dead player's items
     * 
     * @param deadPlayerItems This is the list of the dead player's items
     * @param players         This is the list of player in the game without the
     *                        current player
     */
    public void fightForItem(List<MunchkinCard> deadPlayerItems, List<Joueur> players, Joueur currentPlayer) {
        Joueur bestPlayer;
        List<Joueur> finalFightPlayers = new ArrayList<>();
        List<Joueur> otherPlayers = new ArrayList<>(players);
        otherPlayers.remove(currentPlayer);
        finalFightPlayers = askWhoWantFight(otherPlayers);
        if (finalFightPlayers.size() == 1) {
            System.out.println(deadPlayerItems.size());
            for (MunchkinCard item : deadPlayerItems) {
                System.out.println(item.getName());
                finalFightPlayers.get(0).getCartes().add(item);
            }
            deadPlayerItems.clear();
        } else if (finalFightPlayers.size() == 0) {
            deadPlayerItems.clear();
        } else {
            bestPlayer = finalFightPlayers.get(0);
            for (Joueur player : finalFightPlayers) {
                if (bestPlayer.getLevel() < player.getLevel()) {
                    bestPlayer = player;
                    // affiche un message pour dire que le joueur le plus fort a gagné
                } else if (bestPlayer.getLevel() == player.getLevel()) {
                    bestPlayer = fightWithOtherPlayer(bestPlayer, player);
                }
                JOptionPane.showMessageDialog(null, bestPlayer.getUsername() + "a gagné les items du joueur mort", "Combat",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            for (MunchkinCard item : deadPlayerItems) {
                System.out.println(item.getName());
                finalFightPlayers.get(0).getCartes().add(item);
                deadPlayerItems.clear();
            }
        }
    }

    /**
     * This function return true if the player win and false if the player loose the
     * fight
     * 
     * @param player  current player engage in the fight
     * @param monster the monster that the player has to fight with
     * @param players This is the list of all the user of the game without the
     *                current player
     * @param deck    This is the deck that contains the actual game
     *                configuration of deck
     * @return true or false
     */
    public void fightWithMonster(Joueur currentPlayer, MunchkinCardMonster monster, List<Joueur> players, Deck deck) {
        List<MunchkinCard> itemsDeadPlayer = new ArrayList<>();
        System.out.println("Vous avez engagé un combat avec un monstre de nom " + monster.getName() + " de niveau "
                + monster.getLevel());
        if (monster.getLevel() < currentPlayer.getLevel()) {
            int playerFightValue = currentPlayer.randomNumberForFightPlayer(false, 0);
            int monsterFightValue = monster.randomNumberForFightMonster();
            System.out.println("Monster value : " + monsterFightValue);
            System.out.println("Player value : " + playerFightValue);
            if (currentPlayer.getClasse() == "génie Electrique") {
                playerFightValue = playerFightValue * 2;
            }
            if (playerFightValue > monsterFightValue) {
                int nbcartes = 0;
                int nbNiveaux = 0;
                // affiche un message pour dire que le joueur a gagné le combat
                JOptionPane.showMessageDialog(null, "Vous avez gagné le combat !", "Combat",
                        JOptionPane.INFORMATION_MESSAGE);
                if (monster.getLevel() > 5) {
                    //pioche 2 carte tresor et gain de 2 niveaux
                    currentPlayer.setLevel(currentPlayer.getLevel() + 2);
                    nbNiveaux = 2;
                    MunchkinCard tempTresorCard = deck.getRandomTresorCard();
                    if (tempTresorCard != null) {
                        currentPlayer.addCard(tempTresorCard);
                        nbcartes++;
                    }
                    tempTresorCard = deck.getRandomTresorCard();
                    if (tempTresorCard != null) {
                        currentPlayer.addCard(tempTresorCard);
                        nbcartes++;
                    }
                    // si il est de la classe informaticien, il a une chance sur deux de gagner une carte tresor en plus
                    if (currentPlayer.getClasse() == "Informaticien") {
                        Random random = new Random();
                        int value = random.nextInt(1);
                        if (value == 1) {
                            if (tempTresorCard != null) {
                                currentPlayer.addCard(tempTresorCard);
                                nbcartes++;
                            }
                        }
                    }
                } else {
                    // pioche 1 carte tresor et gain de 1 niveau
                    currentPlayer.setLevel(currentPlayer.getLevel() + 1);
                    nbNiveaux = 1;
                    MunchkinCard tempTresorCard = deck.getRandomTresorCard();
                    if (tempTresorCard != null) {
                        currentPlayer.addCard(tempTresorCard);
                        nbcartes++;
                    }
                    if (currentPlayer.getClasse() == "Informaticien") {
                        Random random = new Random();
                        int value = random.nextInt(1);
                        if (value == 1) {
                            tempTresorCard = deck.getRandomTresorCard();
                            if (tempTresorCard != null) {
                                currentPlayer.addCard(tempTresorCard);
                                nbcartes++;
                            }
                        }
                    }
                }
                JOptionPane.showMessageDialog(null, "Vous avez gagné " + nbcartes + " cartes tresor et " + nbNiveaux +" niveaux !", "Combat",
                            JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Vous avez perdu le combat !", "Combat",
                        JOptionPane.INFORMATION_MESSAGE);
                if (currentPlayer.getCredits() >= 6 && !(currentPlayer.getItems().isEmpty())) {
                    currentPlayer.setCredits(currentPlayer.getCredits() - 6);
                    currentPlayer.removeRandomItem();
                } else {
                    JOptionPane.showMessageDialog(null, "Vous êtes mort, vous perdez vos items et vous recevez 2 cartes tresor et 2 cartes donjon", "Combat",
                            JOptionPane.INFORMATION_MESSAGE);
                    itemsDeadPlayer = currentPlayer.isDead(deck);
                    isDead = true;
                    distributeCardAfterDeath(deck, currentPlayer);
                    fightForItem(itemsDeadPlayer, players, currentPlayer);
                }
            }
        } else {
            List<Joueur> otherPlayers = new ArrayList<>(players);
            otherPlayers.remove(currentPlayer);
            Joueur helper = currentPlayer.askForHelp(otherPlayers);
            int nbcartes = 0;
            int nbcartesHelper = 0;
            int nbNiveaux = 0;
            if (helper != null) {
                int playersFightValue = currentPlayer.randomNumberForFightPlayer(true, helper.getLevel());
                int monsterFightValue = monster.randomNumberForFightMonster();
                System.out.println("players" + playersFightValue);
                System.out.println("monster" + monsterFightValue);
                if (currentPlayer.getClasse() == "Electricien" && currentPlayer.getRace() == "Puant") {
                    playersFightValue = playersFightValue * 2;
                }
                if (playersFightValue > monsterFightValue) {
                    // affiche un message pour dire que le joueur a gagné le combat
                    JOptionPane.showMessageDialog(null, "Vous avez gagné le combat !", "Combat",
                            JOptionPane.INFORMATION_MESSAGE);
                    currentPlayer.setLevel(currentPlayer.getLevel() + 1);
                    nbNiveaux = 1;
                    MunchkinCard tempTresorCard = deck.getRandomTresorCard();
                    if (tempTresorCard != null) {
                        currentPlayer.addCard(tempTresorCard);
                        nbcartes++;
                    }
                    tempTresorCard = deck.getRandomTresorCard();
                    if (tempTresorCard != null) {
                        helper.addCard(tempTresorCard);
                        nbcartesHelper++;
                    }
                    if (currentPlayer.getClasse() == "Informaticien") {
                        Random random = new Random();
                        int value = random.nextInt(1);
                        if (value == 1) {
                            tempTresorCard = deck.getRandomTresorCard();
                            if (tempTresorCard != null) {
                                currentPlayer.addCard(tempTresorCard);
                                nbcartes++;
                            }
                        }
                    }else if(helper.getClasse() == "Informaticien") {
                        Random random = new Random();
                        int value = random.nextInt(1);
                        if (value == 1) {
                            tempTresorCard = deck.getRandomTresorCard();
                            if (tempTresorCard != null) {
                                helper.addCard(tempTresorCard);
                                nbcartesHelper++;
                            }
                        }
                    }
                    JOptionPane.showMessageDialog(null, "Vous avez gagné " + nbcartes + " cartes tresor et " 
                        + nbNiveaux +" niveaux , " + helper.getUsername() + " recoit " + nbcartesHelper + " cartes tresor",
                         "Combat",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Vous avez perdu le combat !", "Combat",
                            JOptionPane.INFORMATION_MESSAGE);
                    if (currentPlayer.getCredits() >= 6 && !(currentPlayer.getItems().isEmpty())) {
                        JOptionPane.showMessageDialog(null, "Vous perdez 6 credits et un items", "Combat",
                                JOptionPane.INFORMATION_MESSAGE);
                        currentPlayer.setCredits(currentPlayer.getCredits() - 6);
                        currentPlayer.removeRandomItem();
                    } else {
                        JOptionPane.showMessageDialog(null, "Vous êtes mort, vous perdez vos items et vous recevez 2 cartes tresor et 2 cartes donjon", "Combat",
                                JOptionPane.INFORMATION_MESSAGE);
                        itemsDeadPlayer = currentPlayer.isDead(deck);
                        isDead = true;
                        distributeCardAfterDeath(deck, currentPlayer);
                        fightForItem(itemsDeadPlayer, players, currentPlayer);
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, "Vous tentez de vous enfuir !", "Combat",
                        JOptionPane.INFORMATION_MESSAGE);
                if (!currentPlayer.isEscape()) {
                    JOptionPane.showMessageDialog(null, "Vous n'avez pas reussi à vous enfuir !", "Combat",
                        JOptionPane.INFORMATION_MESSAGE);
                    if (currentPlayer.getCredits() >= 6 && !(currentPlayer.getItems().isEmpty())) {
                        JOptionPane.showMessageDialog(null, "Vous perdez 6 credits et un items", "Combat",
                                JOptionPane.INFORMATION_MESSAGE);
                        currentPlayer.setCredits(currentPlayer.getCredits() - 6);
                        currentPlayer.removeRandomItem();
                    } else {
                        JOptionPane.showMessageDialog(null, "Vous êtes mort, vous perdez vos items et vous recevez 2 cartes tresor et 2 cartes donjon", "Combat",
                            JOptionPane.INFORMATION_MESSAGE);
                        itemsDeadPlayer = currentPlayer.isDead(deck);
                        isDead = true;
                        distributeCardAfterDeath(deck, currentPlayer);
                        fightForItem(itemsDeadPlayer, players, currentPlayer);
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Vous vous enfuyez !", "Combat",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    private void distributeCardAfterDeath(Deck deck, Joueur currentPlayer) {
        for(int i = 0; i < 2; i++) {
            currentPlayer.getCartes().add(deck.getRandomDonjonCard());
            currentPlayer.getCartes().add(deck.getRandomTresorCard());
        }
    }
}
