package Model;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;

import Model.MunchkinCard.*;

public class Deck {
    private List<MunchkinCard> deckDonjon;
    private List<MunchkinCard> deckTresor;
    private List<MunchkinCard> binDonjon;
    private List<MunchkinCard> binTresor;

    // Tableau de données pour les monstres
    static String[][] monsterData = {
            { "Prof De Maths (niv.9)",
                    "Ce monstre est capable de donner des équations différentielles si complexes que même chatGPT préfère ne pas te soutenir.",
                    "9", "ressources/image/mathteacher.png", "15" },
            { "Prof De Dev (niv.5)",
                    "Ce monstre est capable d'utiliser des mots super complexes comme multi-threading ou polymorphisme paramétrique de type tout ça pour exprimer des choses basiques.",
                    "5", "ressources/image/Gechter.jpg", "10" },
            { "Prof De Dev (niv.5)",
                    "Ce monstre est capable d'utiliser des mots super complexes comme multi-threading ou polymorphisme paramétrique de type tout ça pour exprimer des choses basiques.",
                    "5", "ressources/image/Gechter.jpg", "10" },
            { "Prof De Database (niv.3)",
                    "Ce monstre est capable de te contraindre à passer des heures à remplir des bases de données pour faire quelques analyses basiques en PowerBI.",
                    "3", "ressources/image/databaseteacher.png", "6" },
            { "Prof De Database (niv.3)",
                    "Ce monstre est capable de te contraindre à passer des heures à remplir des bases de données pour faire quelques analyses basiques en PowerBI.",
                    "3", "ressources/image/databaseteacher.png", "6" },
            { "Prof De Database (niv.3)",
                    "Ce monstre est capable de te contraindre à passer des heures à remplir des bases de données pour faire quelques analyses basiques en PowerBI.",
                    "3", "ressources/image/databaseteacher.png", "6" },
            { "Prof De Psycho (niv.1)",
                    "Ce monstre est capable de faire cours à une classe quasiment vide un lundi matin à 8h et ce comme si c'était normal.",
                    "1", "ressources/image/psycho.png", "3" },
            { "Prof De Psycho (niv.1)",
                    "Ce monstre est capable de faire cours à une classe quasiment vide un lundi matin à 8h et ce comme si c'était normal.",
                    "1", "ressources/image/psycho.png", "3" },
            { "Prof De Psycho (niv.1)",
                    "Ce monstre est capable de faire cours à une classe quasiment vide un lundi matin à 8h et ce comme si c'était normal.",
                    "1", "ressources/image/psycho.png", "3" },
            { "Prof De Web (niv.7)",
                    "Ce monstre est capable de présenter des frameworks incroyables et utilisés partout (Angular) et en même temps, garder un intérêt tout particulier pour des langages qui devraient disparaître (PHP)",
                    "7", "ressources/image/webteacher.png", "12" },
            { "Prof De Web (niv.7)",
                    "Ce monstre est capable de présenter des frameworks incroyables et utilisés partout (Angular) et en même temps, garder un intérêt tout particulier pour des langages qui devraient disparaître (PHP)",
                    "7", "ressources/image/webteacher.png", "12" }
    };

    static String[][] maledictionData = {
            { "Semestre supérieur (-1 level)", "Fait perdre un niveau au joueur", "il perd -1 level",
                    "ressources/image/semestresupp.png", "15" },
            { "Mauvaise note (-1 item)", "Fait perdre un objet au joueur", "il perd -1 item",
                    "ressources/image/mauvaisenote.png", "10" },
            { "Partiel (race=etudiant)", "Change la race du joueur et le rend etudiant", "il redevient etudiant",
                    "ressources/image/exam.png", "5" },
            { "Semestre supérieur (-1 level)", "Fait perdre un niveau au joueur", "il perd -1 level",
                    "ressources/image/semestresupp.png", "15" },
            { "Mauvaise note (-1 item)", "Fait perdre un objet au joueur", "il perd -1 item",
                    "ressources/image/mauvaisenote.png", "10" },
            { "Partiel (race=etudiant)", "Change la race du joueur et le rend etudiant", "il redevient etudiant",
                    "ressources/image/exam.png", "5" }
    };

    static String[][] raceData = {
            { "Etudiant", "Etudiant lambda", "ressources/image/student.png",
                    "aucun", "1" },
            { "Geek", "Le geek afflige 1 de bonus damage à tous les monstres", "ressources/image/geek.png",
                    "+1 player damage", "20" },
            { "Puant", "Le puant peut avoir 6 cartes dans son inventaires", "ressources/image/puant.png",
                    "+1 cartes", "10" },
            { "Alternant", "L'Alternant gagne plus de credit à la vente de ses items",
                    "ressources/image/workstudy.png", "*2 prix items", "15" },
            { "Geek", "Le geek afflige 1 de bonus damage à tous les monstres", "ressources/image/geek.png",
                    "+1 player damage", "20" },
            { "Puant", "Le puant peut avoir 6 cartes dans son inventaires", "ressources/image/puant.png",
                    "+1 cartes", "10" },
            { "Alternant", "L'Alternant gagne plus de credit à la vente de ses items",
                    "ressources/image/workstudy.png", "*2 prix items", "15" }
    };

    static String[][] classData = {
            { "Informaticien", "A une chance /2 de tirer une carte tresor en plus apres avoir battu un monstre",
                    "ressources/image/informatique.png", "20" },
            { "génie Mecanique", "A une chance /2 de voler un objet a un autre joueur lors d'un echange",
                    "ressources/image/mechanic.png", "20" },
            { "génie Electrique", "Si il est de classe génie electrique augmente les players damage par 2",
                    "ressources/image/electrique.png", "30" },
            { "génie Industriel", "N'est pas affecter par les cartes malediction", "ressources/image/industrial.png",
                    "30" },
            { "Informaticien", "A une chance /2 de tirer une carte tresor en plus apres avoir battu un monstre",
                    "ressources/image/informatique.png", "20" },
            { "génie Mecanique", "A une chance /2 de voler un objet a un autre joueur lors d'un echange",
                    "ressources/image/mechanic.png", "20" },
            { "génie Electrique", "Si il est de classe génie electrique augmente les players damage par 2",
                    "ressources/image/electrique.png", "30" },
            { "génie Industriel", "N'est pas affecter par les cartes malediction", "ressources/image/industrial.png",
                    "30" }
};

    static String[][] creditData = {
            { "Credit", "Permet de gagner 6 credit au joueur tirant la carte", "ressources/image/Trésor/credits.png" },
            { "Credit", "Permet de gagner 6 credit au joueur tirant la carte", "ressources/image/Trésor/credits.png" },
            { "Credit", "Permet de gagner 6 credit au joueur tirant la carte", "ressources/image/Trésor/credits.png" },
            { "Credit", "Permet de gagner 6 credit au joueur tirant la carte", "ressources/image/Trésor/credits.png" },
            { "Credit", "Permet de gagner 6 credit au joueur tirant la carte", "ressources/image/Trésor/credits.png" },
            { "Credit", "Permet de gagner 6 credit au joueur tirant la carte", "ressources/image/Trésor/credits.png" }
    };

    static String[][] itemData = {
            { "Helmet of domination", "Augmente la resistance contre les monstres",
                    "ressources/image/Trésor/Equipement/knight_helmet.png", "+1 player damage", "5" },
            { "Helmet for Bald", "Augmente la resitstance contre les monstres",
                    "ressources/image/Trésor/Equipement/bald.png", "+2 player damage", "10" },
            { "Helmet Glasses", "Augmente la resitstance contre les monstres",
                    "ressources/image/Trésor/Equipement/glasses.png", "+3 player damage", "15" },
            { "Body Teemo sweat", "Augmente la resitstance contre les monstres",
                    "ressources/image/Trésor/Equipement/teemo.png", "+1 player damage", "5" },
            { "Body Sweaty shirt", "Augmente la resitstance contre les monstres",
                    "ressources/image/Trésor/Equipement/sweaty.png", "+2 player damage", "10" },
            { "Body Starcraft tshirt", "Augmente la resitstance contre les monstres",
                    "ressources/image/Trésor/Equipement/starcraft.png", "+3 player damage", "15" },
            { "Weapons gun pen", "Augmente les degats du joueur", "ressources/image/Trésor/Equipement/gunpen.png",
                    "+1 player damage",
                    "5" },
            { "Weapons nvidia gpu book", "Augmente les degats du joueur", "ressources/image/Trésor/Equipement/book.png",
                    "+2 player damage",
                    "10" },
            { "Weapons Cyberweapon", "Augmente les degats du joueur",
                    "ressources/image/Trésor/Equipement/cyberattack.png",
                    "+3 player damage", "15" },
            { "Helmet of domination", "Augmente la resistance contre les monstres",
                    "ressources/image/Trésor/Equipement/knight_helmet.png", "+1 player damage", "5" },
            { "Helmet for Bald", "Augmente la resitstance contre les monstres",
                    "ressources/image/Trésor/Equipement/bald.png", "+2 player damage", "10" },
            { "Helmet Glasses", "Augmente la resitstance contre les monstres",
                    "ressources/image/Trésor/Equipement/glasses.png", "+3 player damage", "15" },
            { "Body Teemo sweat", "Augmente la resitstance contre les monstres",
                    "ressources/image/Trésor/Equipement/teemo.png", "+1 player damage", "5" },
            { "Body Sweaty shirt", "Augmente la resitstance contre les monstres",
                    "ressources/image/Trésor/Equipement/sweaty.png", "+2 player damage", "10" },
            { "Body Starcraft tshirt", "Augmente la resitstance contre les monstres",
                    "ressources/image/Trésor/Equipement/starcraft.png", "+3 player damage", "15" },
            { "Weapons gun pen", "Augmente les degats du joueur", "ressources/image/Trésor/Equipement/gunpen.png",
                    "+1 player damage",
                    "5" },
            { "Weapons nvidia gpu book", "Augmente les degats du joueur", "ressources/image/Trésor/Equipement/book.png",
                    "+2 player damage",
                    "10" },
            { "Weapons Cyberweapon", "Augmente les degats du joueur",
                    "ressources/image/Trésor/Equipement/cyberattack.png",
                    "+3 player damage", "15" }
    };

    public Deck() {
        this.deckDonjon = new ArrayList<>();
        this.deckTresor = new ArrayList<>();
        this.binDonjon = new ArrayList<>();
        this.binTresor = new ArrayList<>();
    }

    /**
     * This function is used to initialize every deck from the data that correspond
     * to each deck
     */
    public void initDeck() {
        // Card initialization from the data
        // Monster card initialization
        for (String[] data : monsterData) {
            String name = data[0];
            String description = data[1];
            int strength = Integer.parseInt(data[2]);
            Image image = new ImageIcon(data[3]).getImage();
            int price = Integer.parseInt(data[4]);

            MunchkinCardMonster temp = new MunchkinCardMonster(name, description, strength, image, price);
            deckDonjon.add(temp);
        }

        // Malediction card initialization
        for (String[] data : maledictionData) {
            String name = data[0];
            String description = data[1];
            String bonusMalus = data[2];
            Image image = new ImageIcon(data[3]).getImage();
            int price = Integer.parseInt(data[4]);

            MunchkinCardMalediction temp = new MunchkinCardMalediction(name, description, bonusMalus, image, price);
            deckDonjon.add(temp);
        }

        // Race card initialization
        for (String[] data : raceData) {
            String name = data[0];
            String description = data[1];
            Image image = new ImageIcon(data[2]).getImage();
            String raceEffect = data[3];
            int price = Integer.parseInt(data[4]);

            MunchkinCardRace temp = new MunchkinCardRace(name, description, image, price, raceEffect);
            deckDonjon.add(temp);
        }

        // Class card initialization
        for (String[] data : classData) {
            String name = data[0];
            String description = data[1];
            Image image = new ImageIcon(data[2]).getImage();
            int price = Integer.parseInt(data[3]);
            MunchkinCardClass temp = new MunchkinCardClass(name, description, image, price);
            deckDonjon.add(temp);
        }

        // Credit card initialization
        for (String[] data : creditData) {
            String name = data[0];
            String description = data[1];
            Image image = new ImageIcon(data[2]).getImage();

            MunchkinCardCredit temp = new MunchkinCardCredit(name, description, image, 6, 6);
            deckTresor.add(temp);
        }

        // Equipment card initialization
        for (String[] data : itemData) {
            String name = data[0];
            String description = data[1];
            Image image = new ImageIcon(data[2]).getImage();
            String bonusString = data[3];
            int bonus = 0;

            if (bonusString.matches("[-+]?\\d+")) {
                bonus = Integer.parseInt(bonusString);
            } else {
                String[] parts = bonusString.split("\\s+");
                for (String part : parts) {
                    if (part.matches("[-+]?\\d+")) {
                        bonus = Integer.parseInt(part);
                        break;
                    }
                }
            }
            int price = Integer.parseInt(data[4]);
            MunchkinCardEquipement temp = new MunchkinCardEquipement(name, description, image, bonus, price);
            deckTresor.add(temp);
        }
    }

    public List<MunchkinCard> getDeckDonjon() {
        return this.deckDonjon;
    }

    public List<MunchkinCard> getDeckTresor() {
        return this.deckTresor;
    }

    public List<MunchkinCard> getBinDonjon() {
        return this.binDonjon;
    }

    public List<MunchkinCard> getBinTresor() {
        return this.binTresor;
    }

    /**
     * This function is used to obtain a random card from the deck Donjon in order
     * to distribute the cards
     * 
     * @return This return a random Donjon card from the deck Donjon
     */
    public MunchkinCard getRandomDonjonCard() {
        if (this.getDeckDonjon().isEmpty()) {
            System.out.println("Le deck donjon est vide");
            if (!this.getBinDonjon().isEmpty()) {
                System.out.println("Le bin donjon n'est pas vide");
                for (MunchkinCard card : this.getBinDonjon()) {
                    this.getDeckDonjon().add(card);
                }
                this.getBinDonjon().clear();
            } else {
                System.out.println("Le bin donjon est vide");
                return null;
            }
        }
        Random random = new Random();
        int nbAlea = random.nextInt(this.getDeckDonjon().size());
        MunchkinCard temp = this.getDeckDonjon().get(nbAlea);
        this.getDeckDonjon().remove(nbAlea);
        return temp;
    }

    /**
     * This function is used to obtain a random card from the deck Tresor in order
     * to distribute the cards
     * 
     * @return This return a random Donjon card from the deck Tresor
     */
    public MunchkinCard getRandomTresorCard() {
        if (this.getDeckTresor().isEmpty()) {
            System.out.println("Le deck tresor est vide");
            if (!this.getBinTresor().isEmpty()) {
                System.out.println("Le bin tresor n'est pas vide");
                for (MunchkinCard card : this.getBinTresor()) {
                    this.getDeckTresor().add(card);
                }
                this.getBinTresor().clear();
            } else {
                System.out.println("Le bin tresor est vide");
                return null;
            }
        }
        Random random = new Random();
        int nbAlea = random.nextInt(this.getDeckTresor().size());
        MunchkinCard temp = this.getDeckTresor().get(nbAlea);
        this.getDeckTresor().remove(nbAlea);
        return temp;
    }

    /**
     * This function is used to choose a random monster in the case we need one for
     * a room fight
     * 
     * @return This return a random monster that could be find in the deck or a
     *         medium one if there is no more monster in the deck
     */
    public MunchkinCardMonster getRandomMonster() {
        List<MunchkinCardMonster> tempMonsterList = getDeckDonjon().stream()
                .filter(MunchkinCardMonster.class::isInstance)
                .map(MunchkinCardMonster.class::cast)
                .collect(Collectors.toList());
        Random random = new Random();
        int randomValue = random.nextInt(tempMonsterList.size() + 1);
        // This monster is not removed from the deck, because it's not supposed to be in
        // the deck, in this case, we just want to check if there is still some monster
        // and in this case we use one as a model for the roomMonster
        if (tempMonsterList.size() > 0) {
            return tempMonsterList.get(randomValue);
        } else {
            // In the case that there is no more monster in the deck, we are creating a new
            // monster with a medium level
            MunchkinCardMonster newMonster = new MunchkinCardMonster("Prof De Dev",
                    "Ce monstre est capable d'utiliser des mots super complexes comme multi-threading ou polymorphisme paramétrique de type tout ça pour exprimer des choses basiques.",
                    5, new ImageIcon("ressources/image/Gechter.jpg").getImage(), 5);
            return newMonster;
        }
    }
}
