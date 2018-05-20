package nodomain.knu2018.bandutils.model;

public class Fitness {

    String Type;
    String Value;
    String Date;
    String Time;
    String load;

    public Fitness(String type, String value, String date, String time, String load) {
        Type = type;
        Value = value;
        Date = date;
        Time = time;
        this.load = load;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
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

    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }
}
