package nodomain.knu2018.bandutils.model.selectdrug;

public class Drugs {

    String valueUnit;
    String drugName;
    String date;
    String time;

    public Drugs(String valueUnit, String drugName, String date, String time) {
        this.valueUnit = valueUnit;
        this.drugName = drugName;
        this.date = date;
        this.time = time;
    }

    public Drugs(String drugName,String valueUnit) {
        this.drugName = drugName;
        this.valueUnit = valueUnit;

    }

    public String getValueUnit() {
        return valueUnit;
    }

    public void setValueUnit(String valueUnit) {
        this.valueUnit = valueUnit;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
