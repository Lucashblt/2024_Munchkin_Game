package Model.MunchkinCard;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;


public class MunchkinCard {

    private String name;
    private String description;
    private String category;
    private String type;
    private Image image;
    private int price;

    public MunchkinCard(String name, String description, String type, String category, Image image, int price) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.category = category;
        this.image = image;
        this.price = price;
    }

    public JButton DrawCard(int cardWidth, int cardHeight) {
        JButton card = new JButton(name);
        card.setPreferredSize(new Dimension(cardWidth, cardHeight));
        ImageIcon icon = image != null ? new ImageIcon(image) : new ImageIcon("src/Model/MunchkinCard/run.png");
        // Redimensionnez l'image selon la taille souhaitée
        Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(img);
        card.setIcon(resizedIcon);
        // Configurez le texte sous l'icône (si nécessaire)
        card.setHorizontalTextPosition(SwingConstants.CENTER);
        card.setVerticalTextPosition(SwingConstants.BOTTOM);
        card.setPreferredSize(new Dimension(cardWidth, cardHeight));
        card.setBackground(new Color(241, 222, 201));
                
        return card;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getType() {
        return this.type;
    }

    public String getCategory() {
        return this.category;
    }

    public Image getImage() {
        return this.image;
    }
    
    public int getPrice() {
        return price;
    }

}
