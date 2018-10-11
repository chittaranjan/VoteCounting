import java.util.Map;
import java.util.Set;

public class Result {
    private Candidate candidateEliminated;
    private Candidate winner;
    private int quotaRequiredToWin;
    private Map<Character, Integer> currentVoteCount;

    public Candidate getCandidateEliminated() {
        return candidateEliminated;
    }

    public void setCandidateEliminated(Candidate candidateEliminated) {
        this.candidateEliminated = candidateEliminated;
    }

    public void setWinner(Candidate winner) {
        this.winner = winner;
    }

    public void setQuotaRequiredToWin(int quotaRequiredToWin) {
        this.quotaRequiredToWin = quotaRequiredToWin;
    }

    public void setCurrentVoteCount(Map<Character, Integer> currentVoteCount) {
        this.currentVoteCount = currentVoteCount;
    }

    public Candidate getWinner() {
        return winner;
    }

    public int getQuotaRequiredToWin() {
        return quotaRequiredToWin;
    }

    public Map<Character, Integer> getCurrentVoteCount() {
        return currentVoteCount;
    }

    @Override
    public String toString() {
        return "Result{" +
                "candidatesEliminated=" + candidateEliminated +
                ", winner=" + winner +
                ", quotaRequiredToWin=" + quotaRequiredToWin +
                ", currentVoteCount=" + currentVoteCount +
                '}';
    }
}
