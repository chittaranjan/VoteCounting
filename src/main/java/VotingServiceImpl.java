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
    private Set<Candidate> candidatesEliminatedAtCurrentRound;
    private Set<Candidate> candidatesEliminated;

    private Map<Character, Integer> currentVoteCount;

    public VotingServiceImpl() {
        //ToDo proper initialisation
        validBallots = new HashMap<>();
        currentVoteCount = new HashMap<>();
        candidatesEliminatedAtCurrentRound = new HashSet<>();
        candidatesEliminated = new HashSet<>();
    }

    @Override
    public void castVote(Ballot ballot) {
        if (null == this.ballots) {
            this.ballots = new ArrayList<>();
        }
        this.ballots.add(ballot);

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
        }
        Result resultAtThisRound = new Result();
        resultAtThisRound.setCurrentVoteCount(currentVoteCount);
        resultAtThisRound.setQuotaRequiredToWin(currentQuota);

        aWinnerIsDecided = checkCurrentQuotaAndReallocate();
        if (!aWinnerIsDecided) {
            resultAtThisRound.setCandidatesEliminated(candidatesEliminatedAtCurrentRound);
            return  resultAtThisRound;
        }
        resultAtThisRound.setWinner(Candidate.getCandidateUsingOption(winnerOption));
        return resultAtThisRound;
    }

    private boolean checkCurrentQuotaAndReallocate() {

        System.out.println(validBallots);


        //Compute the vote counts
        Map<Character, Integer> voteCounts = validBallots
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e-> e.getValue().size()));
        currentVoteCount.clear();
        currentVoteCount.putAll(voteCounts);


        //Check if there is a winner already
        Set<Character> winners = voteCounts.entrySet()
                .stream()
                .filter(voteCount -> voteCount.getValue().intValue() >= currentQuota)
                .map(voteCount -> voteCount.getKey())
                .collect(Collectors.toSet());

        if (winners != null && winners.isEmpty() == false) {
            aWinnerIsDecided = true;
            /**
             * In case more than one candidate emerge out to be winner
             * Need to select one randomly
             */
            if (winners.size() > 1) {
                int randomNumber = new Random().nextInt(winners.size());
                winnerOption = (Character)winners.stream().toArray()[randomNumber];

            } else {
                winnerOption = winners.stream().findFirst().get();
            }
            return true;
        } else {
            //Look for candidate with minimum vote
            Optional<Integer> minimumPreferenceValue = voteCounts.values().stream().min(Integer::compare);
            if (minimumPreferenceValue.isPresent()) {
                Set<Candidate> candidatesWithMinimumVotes = voteCounts.entrySet()
                        .stream()
                        .filter(voteCount -> voteCount.getValue().equals(minimumPreferenceValue.get()))
                        .map(this::getCandidateUsingVoteCount)
                        .collect(Collectors.toSet());

                candidatesWithMinimumVotes.stream().forEach(candidatesWithMinimumVote -> {
                    reAllocateBallots(candidatesWithMinimumVote.getOption());
                });
                candidatesEliminated.addAll(candidatesWithMinimumVotes);
                candidatesEliminatedAtCurrentRound.clear();
                candidatesEliminatedAtCurrentRound.addAll(candidatesWithMinimumVotes);
            }
        }
        return false;
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
        List<Ballot> ballotsAssignedToCandidate = validBallots.get(candidatesWithMinimumVote);
        ballotsAssignedToCandidate.stream().forEach(ballot -> ballot.reOrderPreferences());
        allocateAccordingToPreference();
    }


    @Override
    public int getCurrentQuota() {
        return currentQuota;
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
