package bot.service;

import java.util.ArrayList;
import java.util.List;

public class Properties {
    public StringBuilder getStr() {
        return str;
    }

    private final StringBuilder str = new StringBuilder();    //Строка расписания

    private List<String> s = new ArrayList<>();

    private String name_from;
    private String name_to;

    private String nameCity;
    private double tempOfCity;
    private double wind;

    private double morning_t;
    private String morning_cond;

    private double day_t;
    private String day_cond;

    private double evening_t;
    private String evening_cond;

    private double night_t;
    private String night_cond;

    private String condition;

    private String weatherMessage;

    public String getName_from() {
        return name_from;
    }

    public void setName_from(String name_from) {
        this.name_from = name_from;
    }

    public String getName_to() {
        return name_to;
    }

    public void setName_to(String name_to) {
        this.name_to = name_to;
    }

    public String getNameCity() {
        return nameCity;
    }

    public void setNameCity(String nameCity) {
        this.nameCity = nameCity;
    }

    public double getTempOfCity() {
        return tempOfCity;
    }

    public void setTempOfCity(double tempOfCity) {
        this.tempOfCity = tempOfCity;
    }

    public double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }

    public double getMorning_t() {
        return morning_t;
    }

    public void setMorning_t(double morning_t) {
        this.morning_t = morning_t;
    }

    public String getMorning_cond() {
        return morning_cond;
    }

    public void setMorning_cond(String morning_cond) {
        this.morning_cond = morning_cond;
    }

    public double getDay_t() {
        return day_t;
    }

    public void setDay_t(double day_t) {
        this.day_t = day_t;
    }

    public String getDay_cond() {
        return day_cond;
    }

    public void setDay_cond(String day_cond) {
        this.day_cond = day_cond;
    }

    public double getEvening_t() {
        return evening_t;
    }

    public void setEvening_t(double evening_t) {
        this.evening_t = evening_t;
    }

    public String getEvening_cond() {
        return evening_cond;
    }

    public void setEvening_cond(String evening_cond) {
        this.evening_cond = evening_cond;
    }

    public double getNight_t() {
        return night_t;
    }

    public void setNight_t(double night_t) {
        this.night_t = night_t;
    }

    public String getNight_cond() {
        return night_cond;
    }

    public void setNight_cond(String night_cond) {
        this.night_cond = night_cond;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getWeatherMessage() {
        return weatherMessage;
    }

    public void setWeatherMessage(String weatherMessage) {
        this.weatherMessage = weatherMessage;
    }

    public List<String> getS() {
        return s;
    }

    public void setS(List<String> s) {
        this.s = s;
    }
}

