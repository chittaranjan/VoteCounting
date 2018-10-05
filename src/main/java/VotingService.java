/**
 * VotingService
 */
public interface VotingService {
    void castVote(Ballot ballot);
    void countVotes();
    int getCurrentQuota();
    Candidate getWinner();
}
