/**
 * VotingService
 */
public interface VotingService {
    void castVote(Ballot ballot);
    Result countVotes();
    int getCurrentQuota();
}
