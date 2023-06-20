package json.rasp;

public class Thread {
    public String uid;
    public String title;
    public String number;
    public String short_title;
    public String thread_method_link;
    public Carrier carrier;
    public String transport_type;
    public Object vehicle;
    public TransportSubtype transport_subtype;
    public String express_type;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getShort_title() {
        return short_title;
    }

    public void setShort_title(String short_title) {
        this.short_title = short_title;
    }

    public String getThread_method_link() {
        return thread_method_link;
    }

    public void setThread_method_link(String thread_method_link) {
        this.thread_method_link = thread_method_link;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    public void setCarrier(Carrier carrier) {
        this.carrier = carrier;
    }

    public String getTransport_type() {
        return transport_type;
    }

    public void setTransport_type(String transport_type) {
        this.transport_type = transport_type;
    }

    public Object getVehicle() {
        return vehicle;
    }

    public void setVehicle(Object vehicle) {
        this.vehicle = vehicle;
    }

    public TransportSubtype getTransport_subtype() {
        return transport_subtype;
    }

    public void setTransport_subtype(TransportSubtype transport_subtype) {
        this.transport_subtype = transport_subtype;
    }

    public String getExpress_type() {
        return express_type;
    }

    public void setExpress_type(String express_type) {
        this.express_type = express_type;
    }
}
