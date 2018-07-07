package nodomain.knu2018.bandutils.model.analysis;

import java.util.ArrayList;

public class WriteCount {

    ArrayList<Columns> response;

    public WriteCount(ArrayList<Columns> response) {
        this.response = response;
    }

    public ArrayList<Columns> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<Columns> response) {
        this.response = response;
    }

    public class Columns{

        String total;
        String today;
        String totalBs;
        String totalFit;
        String totalDrug;
        String totalMeal;
        String totalSleep;


        public Columns(String total, String today, String totalBs, String totalFit, String totalDrug, String totalMeal, String totalSleep) {
            this.total = total;
            this.today = today;
            this.totalBs = totalBs;
            this.totalFit = totalFit;
            this.totalDrug = totalDrug;
            this.totalMeal = totalMeal;
            this.totalSleep = totalSleep;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getToday() {
            return today;
        }

        public void setToday(String today) {
            this.today = today;
        }

        public String getTotalBs() {
            return totalBs;
        }

        public void setTotalBs(String totalBs) {
            this.totalBs = totalBs;
        }

        public String getTotalFit() {
            return totalFit;
        }

        public void setTotalFit(String totalFit) {
            this.totalFit = totalFit;
        }

        public String getTotalDrug() {
            return totalDrug;
        }

        public void setTotalDrug(String totalDrug) {
            this.totalDrug = totalDrug;
        }

        public String getTotalMeal() {
            return totalMeal;
        }

        public void setTotalMeal(String totalMeal) {
            this.totalMeal = totalMeal;
        }

        public String getTotalSleep() {
            return totalSleep;
        }

        public void setTotalSleep(String totalSleep) {
            this.totalSleep = totalSleep;
        }
    }

}
