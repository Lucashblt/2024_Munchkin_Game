package Model.MunchkinCard;

import java.awt.Image;

public class MunchkinCardEquipement extends MunchkinCard {
    private int bonus;

    public MunchkinCardEquipement(String name, String description, Image image, int bonus, int price) {
        super(name, description, "tresor", "Equipement", image, price);
        this.bonus = bonus;
    }

    public int getBonus() {
        return bonus;
    }

    public int calculateDamageToMonster(int playerDamage) {
        return playerDamage + bonus;
    }
}
