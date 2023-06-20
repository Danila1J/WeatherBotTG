package json.rasp;

public class Segment {
    public String arrival;
    public From from;
    public Thread thread;
    public String departure_platform;
    public String departure;
    public String stops;
    public String departure_terminal;
    public To to;
    public boolean has_transfers;
    public TicketsInfo tickets_info;
    public double duration;
    public String arrival_terminal;
    public String start_date;
    public String arrival_platform;

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public String getDeparture_platform() {
        return departure_platform;
    }

    public void setDeparture_platform(String departure_platform) {
        this.departure_platform = departure_platform;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getStops() {
        return stops;
    }

    public void setStops(String stops) {
        this.stops = stops;
    }

    public String getDeparture_terminal() {
        return departure_terminal;
    }

    public void setDeparture_terminal(String departure_terminal) {
        this.departure_terminal = departure_terminal;
    }

    public To getTo() {
        return to;
    }

    public void setTo(To to) {
        this.to = to;
    }

    public boolean isHas_transfers() {
        return has_transfers;
    }

    public void setHas_transfers(boolean has_transfers) {
        this.has_transfers = has_transfers;
    }

    public TicketsInfo getTickets_info() {
        return tickets_info;
    }

    public void setTickets_info(TicketsInfo tickets_info) {
        this.tickets_info = tickets_info;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getArrival_terminal() {
        return arrival_terminal;
    }

    public void setArrival_terminal(String arrival_terminal) {
        this.arrival_terminal = arrival_terminal;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getArrival_platform() {
        return arrival_platform;
    }

    public void setArrival_platform(String arrival_platform) {
        this.arrival_platform = arrival_platform;
    }
}
