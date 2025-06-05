package view.CitizenManagement;

public class StatisticsResult {
    private long total;
    private long countPermanent;
    private long countAway;
    private long countTemporary;

    private long countUnknown;
    public StatisticsResult(long total, long countPermanent, long countAway, long countTemporary, long countUnknown) {
        this.total = total;
        this.countPermanent = countPermanent;
        this.countAway = countAway;
        this.countTemporary = countTemporary;
        this.countUnknown = countUnknown;
    }

    public long getCountPermanent() {
        return countPermanent;
    }

    public long getCountTemporary() {
        return countTemporary;
    }

    public long getCountAway() {
        return countAway;
    }

    public long getTotal() {
        return total;
    }

    public long getCountUnknown() {
        return countUnknown;
    }
}
