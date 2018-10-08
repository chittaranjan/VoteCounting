import java.util.Map;
import java.util.Set;

public class Result {
    private Set<Candidate> candidatesEliminated;
    private Candidate winner;
    private int quotaRequiredToWin;
    private Map<Character, Integer> currentVoteCount;
    private boolean isEliminated;
    private boolean isWinner;

    public Set<Candidate> getCandidatesEliminated() {
        return candidatesEliminated;
    }

    public void setCandidatesEliminated(Set<Candidate> candidatesEliminated) {
        this.candidatesEliminated = candidatesEliminated;
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

    public boolean isEliminated() {
        return isEliminated;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setEliminated(boolean eliminated) {
        isEliminated = eliminated;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }

}
