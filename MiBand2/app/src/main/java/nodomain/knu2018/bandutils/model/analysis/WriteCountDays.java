package nodomain.knu2018.bandutils.model.analysis;

import java.util.ArrayList;

public class WriteCountDays {

    ArrayList<CountDay> response;

    public WriteCountDays(ArrayList<CountDay> response) {
        this.response = response;
    }

    public ArrayList<CountDay> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<CountDay> response) {
        this.response = response;
    }

    public class CountDay{

        String userName;
        String rgstDate;
        String count;


        public CountDay(String userName, String rgstDate, String count) {
            this.userName = userName;
            this.rgstDate = rgstDate;
            this.count = count;
        }

        public CountDay(String userName, String count) {
            this.userName = userName;
            this.count = count;
        }


        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getRgstDate() {
            return rgstDate;
        }

        public void setRgstDate(String rgstDate) {
            this.rgstDate = rgstDate;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }
}
