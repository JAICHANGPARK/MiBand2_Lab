package nodomain.knu2018.bandutils.model.pattern;

public class PatternGlobal {

    String kind;
    String date;
    String time;
    String type;
    String value;

    public PatternGlobal(String kind, String date, String time, String type, String value) {
        this.kind = kind;
        this.date = date;
        this.time = time;
        this.type = type;
        this.value = value;
    }

    public PatternGlobal(String date, String time, String type, String value) {
        this.date = date;
        this.time = time;
        this.type = type;
        this.value = value;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
