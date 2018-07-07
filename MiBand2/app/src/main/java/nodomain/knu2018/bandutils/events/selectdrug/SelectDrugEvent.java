package nodomain.knu2018.bandutils.events.selectdrug;

public class SelectDrugEvent {

    public  int valueUnit;
    public String drugName;
    public int position;

    public SelectDrugEvent(int valueUnit, String drugName, int position) {
        this.valueUnit = valueUnit;
        this.drugName = drugName;
        this.position = position;
    }

}
