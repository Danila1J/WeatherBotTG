package json.schedule;

import java.util.List;

public class Root {
    public List<Object> interval_segments;
    public Pagination pagination;
    public List<Segment> segments;
    public Search search;

    public List<Object> getInterval_segments() {
        return interval_segments;
    }

    public void setInterval_segments(List<Object> interval_segments) {
        this.interval_segments = interval_segments;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }
}
