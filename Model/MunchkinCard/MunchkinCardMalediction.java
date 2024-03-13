package Model.MunchkinCard;

import java.awt.Image;

import Model.Joueur;

public class MunchkinCardMalediction extends MunchkinCard {
    private String bonusMalus;

    public MunchkinCardMalediction(String name, String description, String bonusMalus, Image image, int price) {
        super(name, description, "Donjon", "Malediction", image, price);
        this.bonusMalus = bonusMalus;
    }

    public String getBonusMalus() {
        return bonusMalus;
    }

    // Applique le bonus/malus au joueur en fonction du nom de la carte de malédiction
    public static void applyBonusMalus(Joueur currentPlayer, MunchkinCard card) {
        String cardName = card.getName();
        
        switch (cardName) {
            case "Partiel (race=etudiant)":
                // Change la race du joueur en "Etudiant"
                currentPlayer.setRace("Etudiant");
                break;
            case "Mauvaise note (-1 item)":
                // Fait perdre 1 item au joueur s'il en possède
                if (!currentPlayer.getItems().isEmpty()) {
                    currentPlayer.removeRandomItem();
                }
                break;
            case "Semestre supérieur (-1 level)":
                // Fait perdre 1 niveau au joueur
                currentPlayer.setLevel(currentPlayer.getLevel() - 1);
                break;
            default:

                break;
        }
    }
}
