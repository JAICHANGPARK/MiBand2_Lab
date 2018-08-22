package nodomain.knu2018.bandutils.model.foodmodel;

public class AppVersion {
    String success;
    String dbVersion;

    public AppVersion(String success, String dbVersion) {
        this.success = success;
        this.dbVersion = dbVersion;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getDbVersion() {
        return dbVersion;
    }

    public void setDbVersion(String dbVersion) {
        this.dbVersion = dbVersion;
    }


}
