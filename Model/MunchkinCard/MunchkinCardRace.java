package Model.MunchkinCard;

import java.awt.Image;

public class MunchkinCardRace extends MunchkinCard {
    private String raceEffect;

    public MunchkinCardRace(String name, String description, Image image, int price, String raceEffect) {
        super(name, description, "donjon", "Race", image, price);
        this.raceEffect = raceEffect;
    }

    public String getRaceEffect() {
        return raceEffect;
    }
}
