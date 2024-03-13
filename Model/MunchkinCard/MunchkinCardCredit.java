package Model.MunchkinCard;

import java.awt.Image;

public class MunchkinCardCredit extends MunchkinCard {
    private int creditBonus = 6;

    public MunchkinCardCredit(String name, String description, Image image, int price, int creditBonus) {
        super(name, description, "tresor", "Credits", image, price);
        this.creditBonus = creditBonus;
    }

    public int getCreditBonus() {
        return creditBonus;
    }
}
