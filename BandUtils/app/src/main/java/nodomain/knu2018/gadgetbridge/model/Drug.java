package nodomain.knu2018.gadgetbridge.model;

public class Drug {


    String Date;
    String Time;
    String TypeTop;
    String TypeBottom;
    String Value;

    public Drug(String date, String time, String typeTop, String typeBottom, String value) {
        Date = date;
        Time = time;
        TypeTop = typeTop;
        TypeBottom = typeBottom;
        Value = value;
    }


    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTypeTop() {
        return TypeTop;
    }

    public void setTypeTop(String typeTop) {
        TypeTop = typeTop;
    }

    public String getTypeBottom() {
        return TypeBottom;
    }

    public void setTypeBottom(String typeBottom) {
        TypeBottom = typeBottom;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
