import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * VotingServiceImpl
 */
public class VotingServiceImpl implements VotingService {

    private Map<Character, List<Ballot>> validBallots;
    private List<Ballot> ballots;
    private int currentQuota;
    private Character winnerOption;
    private boolean aWinnerIsDecided = false;
    private Candidate candidatesEliminatedAtCurrentRound;
    private Set<Candidate> candidatesEliminated;

    private Map<Character, Integer> currentVoteCount;

    public VotingServiceImpl() {
        validBallots = new HashMap<>();
        currentVoteCount = new LinkedHashMap<>();
        candidatesEliminatedAtCurrentRound = null;
        candidatesEliminated = new LinkedHashSet<>();
    }

    @Override
    public void castVote(Ballot ballot) {
        if (null == this.ballots) {
            this.ballots = new ArrayList<>();
        }
        if (ballot.isValid()) {
            this.ballots.add(ballot);
        }
    }

    @Override
    public Result countVotes() {
        /**
         * Perform a round of counting unless a winner is determined.
         *
         * At every round, candidate with least number of votes will be found and eliminated.
         * When candidate is eliminated, all the ballot papers that have been re-allocated to the next available preference on each of those ballot papers.
         * This process repeats in rounds until one candidate has more than half of the available votes.
         *
         */
        //First round of counting
        if (validBallots.isEmpty()) {
            allocateAccordingToPreference();
            return getResultAtCurrentRound();
        }
        //Else look for whether a winner is decided already
        if (aWinnerIsDecided == true) {
            //if the winner is already decided, then return the result immediately
            return getResultAtCurrentRound();
        }
        //Else reallocate
        checkCurrentQuotaAndReallocate();
        return getResultAtCurrentRound();
    }

    private Result getResultAtCurrentRound() {
        Result resultAtThisRound = new Result();
        resultAtThisRound.setCurrentVoteCount(currentVoteCount);
        resultAtThisRound.setQuotaRequiredToWin(currentQuota);
        resultAtThisRound.setCandidateEliminated(candidatesEliminatedAtCurrentRound);

        aWinnerIsDecided = isThereAWinnerYet();
        if (aWinnerIsDecided) {
            resultAtThisRound.setWinner(Candidate.getCandidateUsingOption(winnerOption));
        }
        return  resultAtThisRound;
    }

    private Map<Character, Integer> getCurrentVoteCount() {
        Map<Character, Integer> voteCounts = validBallots
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e-> e.getValue().size()));
        return voteCounts;
    }

    private boolean isThereAWinnerYet(){
        //Compute the vote counts
        Map<Character, Integer> voteCounts = getCurrentVoteCount();
        currentVoteCount.clear();
        currentVoteCount.putAll(voteCounts);

        //Check if there is a winner already
        Optional<Character> winner = voteCounts.entrySet()
                .stream()
                .filter(voteCount -> voteCount.getValue().intValue() >= currentQuota)
                .map(voteCount -> voteCount.getKey())
                .findAny();

        if (winner.isPresent()) {
            aWinnerIsDecided = true;
            winnerOption = winner.get();
            return true;
        }
        return false;
    }

    /**
     * ToDo add Javadoc
     */
    private void checkCurrentQuotaAndReallocate() {
        Map<Character, Integer> voteCounts = getCurrentVoteCount();
        Optional<Integer> minimumPreferenceValue = voteCounts.values().stream().min(Integer::compare);
        if (minimumPreferenceValue.isPresent()) {
            Set<Candidate> candidatesWithMinimumVotes = voteCounts.entrySet()
                    .stream()
                    .filter(voteCount -> voteCount.getValue().equals(minimumPreferenceValue.get()))
                    .map(this::getCandidateUsingVoteCount)
                    .collect(Collectors.toSet());

            /**
             * One candidate should be chosen at random
             */
            Candidate candidateForElimination;
            if (candidatesWithMinimumVotes.size() > 1) {
                int randomNumber = new Random().nextInt(candidatesWithMinimumVotes.size());
                candidateForElimination = (Candidate) candidatesWithMinimumVotes.stream().toArray()[randomNumber];

            } else {
                candidateForElimination = candidatesWithMinimumVotes.stream().findFirst().get();
            }
            reAllocateBallots(candidateForElimination.getOption());
            candidatesEliminated.add(candidateForElimination);
            candidatesEliminatedAtCurrentRound = candidateForElimination;
        }
    }

    private Candidate getCandidateUsingVoteCount(Map.Entry<Character, Integer> voteCount) {
        Candidate candidate = Candidate.getCandidateUsingOption(voteCount.getKey());
        candidate.setCurrentPreference(voteCount.getValue().intValue());
        return candidate;
    }

    /**
     * At every rounds of counting, candidates(s) with minimum number of votes are eliminated
     * And ballots assigned to the candidate need to be reallocated.
     *
     * This method just reorder the preferences of candidates in the ballot
     * before invoking the method to reallocate
     * @param candidatesWithMinimumVote
     */
    private void reAllocateBallots(Character candidatesWithMinimumVote) {
        if (validBallots.containsKey(candidatesWithMinimumVote)) {
            List<Ballot> ballotsAssignedToCandidate = validBallots.get(candidatesWithMinimumVote);
            ballotsAssignedToCandidate.stream().forEach(ballot -> ballot.reOrderPreferences());
        }
        allocateAccordingToPreference();
    }

    /**
     * Calculates the current quota required to win using the formula
     * (number of non-exhausted ballots / 2) + 1
     * At any point of time, sum of ballots in the validBallots map determines the number of non-exhausted votes
     */
    private void updateCurrentQuota() {
        currentQuota = ((validBallots.values().stream().flatMapToInt(value -> IntStream.of(value.size())).sum())/2) +1;
    }

    /**
     * Allocates the ballot to the candidate,
     * according to the highest preference to the candidate in each ballot
     *
     * if any candidate is eliminated in previous rounds of counting
     * the ballot is considered to be exhausted and  no need to allocate the ballot to the candidate
     */
    private void allocateAccordingToPreference() {
        validBallots.clear();
        validBallots = new HashMap<>();
        ballots.stream().forEach(ballot -> {
            Set<Candidate> candidates = ballot.getCandidates();
            Optional<Candidate> candidateWithHighestPreference = candidates
                    .stream()
                    .filter(candidate -> candidate.getCurrentPreference() > 0)
                    .min(Comparator.comparing(Candidate::getCurrentPreference));
            if (candidateWithHighestPreference.isPresent()) {
                Candidate candidate = candidateWithHighestPreference.get();

                //Compute validBallots by allocating ballots to candidates
                validBallots.computeIfAbsent(candidate.getOption(), ballotList -> new ArrayList<>())
                        .add(ballot);

                //Exhausted ballot; Remove form the validBallots
                if (candidatesEliminated.contains(candidate)) {
                    validBallots.remove(candidate.getOption());
                }
                updateCurrentQuota();
            }
        });
    }
}
