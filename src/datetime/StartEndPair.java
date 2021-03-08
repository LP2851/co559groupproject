package datetime;

import java.util.Date;

public class StartEndPair {
    private Date start;
    private Date end;

    public StartEndPair(Date start, Date end) {
        this.start = start; this.end = end;
    }

    public Date getEnd() {
        return end;
    }

    public Date getStart() {
        return start;
    }
}
