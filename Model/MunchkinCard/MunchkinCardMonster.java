package Model.MunchkinCard;

import java.awt.Image;
import java.util.Random;

public class MunchkinCardMonster extends MunchkinCard {
    private int level;

    public MunchkinCardMonster(String name, String description, int level, Image image, int price) {
        super(name, description, "Donjon", "Monstre", image, price);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public int randomNumberForFightMonster() {

        Random random = new Random();

        if (this.getLevel() > 0) {
            // double weight = Math.pow(1.2, this.getLevel());
            // int randomNumber = (int) (Math.floor(11 * Math.pow(random.nextDouble(),
            // weight)));
            // return Math.min(randomNumber, 10);

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
        return -1;
    }
}
