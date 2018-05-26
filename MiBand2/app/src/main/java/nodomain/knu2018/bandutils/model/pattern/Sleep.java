package nodomain.knu2018.bandutils.model.pattern;

public class Sleep {


    String Date;
    String Time;
    String StartDate;
    String StartTime;
    String EndDate;
    String EndTime;
    String Duration;
    String Type;
    String Satisfaction;

    public Sleep(String date, String time, String startDate, String startTime,
                 String endDate, String endTime, String duration, String type, String satisfaction) {
        Date = date;
        Time = time;
        StartDate = startDate;
        StartTime = startTime;
        EndDate = endDate;
        EndTime = endTime;
        Duration = duration;
        Type = type;
        Satisfaction = satisfaction;
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

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getSatisfaction() {
        return Satisfaction;
    }

    public void setSatisfaction(String satisfaction) {
        Satisfaction = satisfaction;
    }
}
