import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * VotingServiceImpl
 */
public class VotingServiceImpl implements VotingService {

    Map<Character, List<Ballot>> validBallots;
    List<Ballot> ballots;
    private int currentQuota;
    private Result result;
    boolean aWinnerIsDecided = false;

    public VotingServiceImpl() {
        //ToDo proper initialisation
        validBallots = new HashMap<>();
    }

    @Override
    public void castVote(Ballot ballot) {
        if (null == this.ballots) {
            this.ballots = new ArrayList<>();
        }
        this.ballots.add(ballot);
        System.out.println("Total number of ballots:" + ballots.size());
        allocateAccordingToPreference(ballot);
    }

    @Override
    public Result countVotes() {
        /**
         * Performs rounds of counting until a winner is decided
         *
         * At every round, candidate with least number of votes will be found and eliminated.
         * When candidate is eliminated, all the ballot papers that have been re-allocated to the next available preference on each of those ballot papers.
         * This process repeats in rounds until one candidate has more than half of the available votes.
         *
         */
        if (!aWinnerIsDecided) {
            checkCurrentQuotaAndReallocate();
        }
        return null;
    }

    private void checkCurrentQuotaAndReallocate() {
        Map<Character, Integer> voteCounts = validBallots
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e-> e.getValue().size()));

        Set<Character> winners = voteCounts.entrySet()
                .stream()
                .filter(voteCount -> voteCount.getValue().intValue() >= currentQuota)
                .map(voteCount -> voteCount.getKey())
                .collect(Collectors.toSet());
        if (winners != null && winners.isEmpty() == false) {
            aWinnerIsDecided = true;
        } else {
            Integer minimumPreferenceValue = voteCounts.values().stream().min(Integer::compare).get();
            Set<Character> candidatesWithMinimumVotes = voteCounts.entrySet()
                    .stream()
                    .filter(voteCount -> voteCount.getValue().equals(minimumPreferenceValue))
                    .map(voteCount -> voteCount.getKey())
                    .collect(Collectors.toSet());


        }

    }

    @Override
    public Candidate getWinner() {
        return null;
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
        Optional<Candidate> candidateWithHighestPreference = candidates
                .stream()
                .filter(candidate -> candidate.getCurrentPreference() > 0)
                .min(Comparator.comparing(Candidate::getCurrentPreference));
        if (candidateWithHighestPreference.isPresent()) {
            Candidate candidate = candidateWithHighestPreference.get();
            System.out.println(candidate);
            validBallots.computeIfAbsent(candidate.getOption(), ballotList -> new ArrayList<>())
                    .add(ballot);
            checkCurrentQuotaAndReallocate();
            updateCurrentQuota();
        }
    }
}
