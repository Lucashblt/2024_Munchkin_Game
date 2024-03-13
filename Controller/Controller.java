package Controller;

import View.*;

public class Controller {
    private View vue;

    public Controller(View vue) {
        this.vue = vue;
        this.vue.setControleur(this);
    }
}
