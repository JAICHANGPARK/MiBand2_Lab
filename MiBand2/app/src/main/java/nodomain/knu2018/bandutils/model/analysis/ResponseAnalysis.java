package nodomain.knu2018.bandutils.model.analysis;

import java.util.ArrayList;

public class ResponseAnalysis {

    ArrayList<Analysis> response ;

    public ResponseAnalysis(ArrayList<Analysis> response) {
        this.response = response;
    }

    public ArrayList<Analysis> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<Analysis> response) {
        this.response = response;
    }
}
