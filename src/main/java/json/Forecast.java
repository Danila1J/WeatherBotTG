package json;

import java.util.List;

public class Forecast {
    public String date;
    public int date_ts;
    public int week;
    public String sunrise;
    public String sunset;
    public int moon_code;
    public String moon_text;
    public List<Part> parts;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDate_ts() {
        return date_ts;
    }

    public void setDate_ts(int date_ts) {
        this.date_ts = date_ts;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public int getMoon_code() {
        return moon_code;
    }

    public void setMoon_code(int moon_code) {
        this.moon_code = moon_code;
    }

    public String getMoon_text() {
        return moon_text;
    }

    public void setMoon_text(String moon_text) {
        this.moon_text = moon_text;
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }
}
