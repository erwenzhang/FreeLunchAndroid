package com.application.Kevin_Wenwen.FreeLunch;

/**
 * Created by xiangkundai on 12/13/15.
 */
public class DateTimeProcess {
    private String datetime;

    DateTimeProcess(String s) {
        datetime = s;
    }

    public String year() {
        return datetime.substring(0, 4);
    }

    public String month() {
        switch (datetime.substring(5, 7)) {
            case "01": return "Jan";
            case "02": return "Feb";
            case "03": return "Mar";
            case "04": return "Apr";
            case "05": return "May";
            case "06": return "Jun";
            case "07": return "Jul";
            case "08": return "Aug";
            case "09": return "Sep";
            case "10": return "Oct";
            case "11": return "Nov";
            case "12": return "Dec";
            default: return "Invalid month";
        }
    }

    public String day() {
        return datetime.substring(8, 10);
    }

    public String date() {
        return month() + " " + day() + ", " + year();
    }

    public String hourMinute() {
        String h = datetime.substring(11, 13);
        String m = datetime.substring(14, 16);
        Integer hour = Integer.parseInt(h);
        if (hour > 12) {
            return ((Integer)(hour - 12)).toString() + ":" + m + "PM";
        } else if (0 < hour && hour < 12) {
            return hour.toString() + ":" + m + "AM";
        } else if (hour == 0) {
            return "12:" + m + "AM";
        } else {
            return "12:" + m + "PM";
        }
    }
}
