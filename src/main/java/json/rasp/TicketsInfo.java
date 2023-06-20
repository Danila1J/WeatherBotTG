package json.rasp;

import java.util.List;

public class TicketsInfo {
    public boolean et_marker;
    public List<Place> places;

    public boolean isEt_marker() {
        return et_marker;
    }

    public void setEt_marker(boolean et_marker) {
        this.et_marker = et_marker;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }
}
