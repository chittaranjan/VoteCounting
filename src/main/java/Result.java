import java.util.Map;

public class Result {
    private Candidate candidate;
    private int quotaRequiredToWin;
    private Map<Character, Integer> currentVoteCount;

    public Result(Candidate candidate, int quotaRequiredToWin, Map<Character, Integer> currentVoteCount) {
        this.candidate = candidate;
        this.quotaRequiredToWin = quotaRequiredToWin;
        this.currentVoteCount = currentVoteCount;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public int getQuotaRequiredToWin() {
        return quotaRequiredToWin;
    }

    public Map<Character, Integer> getCurrentVoteCount() {
        return currentVoteCount;
    }
}
