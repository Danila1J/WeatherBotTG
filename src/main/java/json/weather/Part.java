package json.weather;

public class Part {
    public String part_name;
    public int temp_min;
    public int temp_avg;
    public int temp_max;
    public double wind_speed;
    public double wind_gust;
    public String wind_dir;
    public int pressure_mm;
    public int pressure_pa;
    public int humidity;
    public double prec_mm;
    public int prec_prob;
    public int prec_period;
    public String icon;
    public String condition;
    public int feels_like;
    public String daytime;
    public boolean polar;

    public String getPart_name() {
        return part_name;
    }

    public void setPart_name(String part_name) {
        this.part_name = part_name;
    }

    public int getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(int temp_min) {
        this.temp_min = temp_min;
    }

    public int getTemp_avg() {
        return temp_avg;
    }

    public void setTemp_avg(int temp_avg) {
        this.temp_avg = temp_avg;
    }

    public int getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(int temp_max) {
        this.temp_max = temp_max;
    }

    public double getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public double getWind_gust() {
        return wind_gust;
    }

    public void setWind_gust(double wind_gust) {
        this.wind_gust = wind_gust;
    }

    public String getWind_dir() {
        return wind_dir;
    }

    public void setWind_dir(String wind_dir) {
        this.wind_dir = wind_dir;
    }

    public int getPressure_mm() {
        return pressure_mm;
    }

    public void setPressure_mm(int pressure_mm) {
        this.pressure_mm = pressure_mm;
    }

    public int getPressure_pa() {
        return pressure_pa;
    }

    public void setPressure_pa(int pressure_pa) {
        this.pressure_pa = pressure_pa;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getPrec_mm() {
        return prec_mm;
    }

    public void setPrec_mm(double prec_mm) {
        this.prec_mm = prec_mm;
    }

    public int getPrec_prob() {
        return prec_prob;
    }

    public void setPrec_prob(int prec_prob) {
        this.prec_prob = prec_prob;
    }

    public int getPrec_period() {
        return prec_period;
    }

    public void setPrec_period(int prec_period) {
        this.prec_period = prec_period;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(int feels_like) {
        this.feels_like = feels_like;
    }

    public String getDaytime() {
        return daytime;
    }

    public void setDaytime(String daytime) {
        this.daytime = daytime;
    }

    public boolean isPolar() {
        return polar;
    }

    public void setPolar(boolean polar) {
        this.polar = polar;
    }
}
