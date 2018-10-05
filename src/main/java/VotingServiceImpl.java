import java.util.*;
import java.util.stream.IntStream;

/**
 * VotingServiceImpl
 */
public class VotingServiceImpl implements VotingService {

    Map<Character, List<Ballot>> validBallots;
    private int currentQuota;
    private Candidate winner;

    public VotingServiceImpl() {
        //ToDo proper initialisation
        validBallots = new HashMap<>();
    }

    @Override
    public void castVote(Ballot ballot) {
        allocateAccordingToPreference(ballot);
    }

    @Override
    public void countVotes() {

    }

    @Override
    public Candidate getWinner() {
        return winner;
    }

    @Override
    public int getCurrentQuota() {
        return currentQuota;
    }

    private void updateCurrentQuota() {
        currentQuota = ((validBallots.values().stream().flatMapToInt(value -> IntStream.of(value.size())).sum())/2) +1;
    }

    private void allocateAccordingToPreference(Ballot ballot) {
        Set<Candidate> candidates = ballot.getCandidates();
        Candidate candidateWithHighestPreference = candidates
                .stream()
                .filter(candidate -> candidate.getVoteCount() > 0)
                .min(Comparator.comparing(Candidate::getVoteCount))
                .orElseThrow(NoSuchElementException::new);
        validBallots.computeIfAbsent(candidateWithHighestPreference.getOption(), ballotList -> new ArrayList<>())
                    .add(ballot);
        updateCurrentQuota();

    }
}
