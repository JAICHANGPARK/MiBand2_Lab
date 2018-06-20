package nodomain.knu2018.bandutils.model.analysis;

public class MyWriteCount {

    String type;
    String count;

    public MyWriteCount(String type, String count) {
        this.type = type;
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
