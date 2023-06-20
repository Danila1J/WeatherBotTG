package json.rasp;

public class Place {
    public String currency;
    public Price price;
    public Object name;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }
}
