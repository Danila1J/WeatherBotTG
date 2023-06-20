package org.example;

import java.util.*;

public class Properties {
    public static final StringBuilder str = new StringBuilder();    //Строка расписания

    private static List<String> s = new ArrayList<>();

    private static String name_from;
    private static String name_to;

    private static String nameCity;
    private static double tempOfCity;
    private static double wind;

    private static double morning_t;
    private static String morning_cond;

    private static double day_t;
    private static String day_cond;

    private static double evening_t;
    private static String evening_cond;

    private static double night_t;
    private static String night_cond;

    private static String condition;

    private static String weatherMessage;

    public static String getName_from() {
        return name_from;
    }

    public static void setName_from(String name_from) {
        Properties.name_from = name_from;
    }

    public static String getName_to() {
        return name_to;
    }

    public static void setName_to(String name_to) {
        Properties.name_to = name_to;
    }

    public static String getNameCity() {
        return nameCity;
    }

    public static void setNameCity(String nameCity) {
        Properties.nameCity = nameCity;
    }

    public static double getTempOfCity() {
        return tempOfCity;
    }

    public static void setTempOfCity(double tempOfCity) {
        Properties.tempOfCity = tempOfCity;
    }

    public static double getWind() {
        return wind;
    }

    public static void setWind(double wind) {
        Properties.wind = wind;
    }

    public static double getMorning_t() {
        return morning_t;
    }

    public static void setMorning_t(double morning_t) {
        Properties.morning_t = morning_t;
    }

    public static String getMorning_cond() {
        return morning_cond;
    }

    public static void setMorning_cond(String morning_cond) {
        Properties.morning_cond = morning_cond;
    }

    public static double getDay_t() {
        return day_t;
    }

    public static void setDay_t(double day_t) {
        Properties.day_t = day_t;
    }

    public static String getDay_cond() {
        return day_cond;
    }

    public static void setDay_cond(String day_cond) {
        Properties.day_cond = day_cond;
    }

    public static double getEvening_t() {
        return evening_t;
    }

    public static void setEvening_t(double evening_t) {
        Properties.evening_t = evening_t;
    }

    public static String getEvening_cond() {
        return evening_cond;
    }

    public static void setEvening_cond(String evening_cond) {
        Properties.evening_cond = evening_cond;
    }

    public static double getNight_t() {
        return night_t;
    }

    public static void setNight_t(double night_t) {
        Properties.night_t = night_t;
    }

    public static String getNight_cond() {
        return night_cond;
    }

    public static void setNight_cond(String night_cond) {
        Properties.night_cond = night_cond;
    }

    public static String getCondition() {
        return condition;
    }

    public static void setCondition(String condition) {
        Properties.condition = condition;
    }

    public static String getWeatherMessage() {
        return weatherMessage;
    }

    public static void setWeatherMessage(String weatherMessage) {
        Properties.weatherMessage = weatherMessage;
    }

    public static List<String> getS() {
        return s;
    }

    public static void setS(List<String> s) {
        Properties.s = s;
    }
}

