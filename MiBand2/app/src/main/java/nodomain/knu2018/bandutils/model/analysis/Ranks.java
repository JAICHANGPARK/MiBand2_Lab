package nodomain.knu2018.bandutils.model.analysis;

import java.util.ArrayList;

public class Ranks {

    ArrayList<Rank> response;

    public Ranks(ArrayList<Rank> response) {
        this.response = response;
    }

    public ArrayList<Rank> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<Rank> response) {
        this.response = response;
    }

    public class Rank {

        String userName;
        String score;
        String ranking;

        public Rank() {
        }

        public Rank(String userName, String score, String ranking) {
            this.userName = userName;
            this.score = score;
            this.ranking = ranking;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getRanking() {
            return ranking;
        }

        public void setRanking(String ranking) {
            this.ranking = ranking;
        }
    }
}
