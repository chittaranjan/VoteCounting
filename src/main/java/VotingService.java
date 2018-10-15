/**
 * VotingService
 */
public interface VotingService {
    /**
     * API to cast vote
     * It ignores the ballot if found to be invalid
     * @param ballot
     */
    void castVote(Ballot ballot);

    /**
     * Performs one round of counting and and returns the result
     * @return
     */
    Result countVotes();

}
