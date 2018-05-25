package nodomain.knu2018.bandutils.model.foodmodel;

import java.util.ArrayList;

public class Foods {

    ArrayList<Food> response;

    public Foods(ArrayList<Food> response) {
        this.response = response;
    }

    public ArrayList<Food> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<Food> response) {
        this.response = response;
    }


}
