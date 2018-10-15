
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class VoteCountingApplication {
    private Set<Candidate> candidates;
    private VotingService votingService;

    public VoteCountingApplication() {
        this.candidates = new LinkedHashSet<>();
        this.votingService = new VotingServiceImpl();
    }

    public void registerCandidates(List<String> candidates) {
        Set<String> uniqueCandidates = candidates.stream().collect(Collectors.toSet());
        char option = 'A';
        for (String candidateName : uniqueCandidates) {
            if (candidateName != null && candidateName.isEmpty() == false) {
                //Add candidate after removing leading/trailing spaces, if any
                this.candidates.add(new Candidate(option++, candidateName.trim()));
            }
        }
    }

    public void showCandidatesToBeElected() {
        System.out.println("\n*************** Candidates to elect " + "******************");
        this.candidates.stream().forEach(candidate -> System.out.println(candidate.getOption() + ". " + candidate.getName()));
        System.out.println("\n*********************************************************");
        System.out.println("\nEnter your choice in the order of preference");
    }

    /**
     * takes user preferences, creates an instance of ballot and set the preferences on the ballot
     * @param userVotes
     * @return true if ballot is valid
     *         false otherwise
     */
    public boolean takeUserPreferences(String userVotes) {
        //Ignore Leading/Trailing white spaces
        String validVote = userVotes.trim();
        if (validVote.isEmpty()) {
            //Do Nothing
            return false;
        }

        //Get fresh set of candidates to be added to newly created ballot
        Set<Candidate> candidatesToBeElected = new LinkedHashSet<>();
        this.candidates.forEach(candidate -> candidatesToBeElected.add(candidate.clone()));

        Set<Character> uniqueVotes = new LinkedHashSet<>();
        Set<Character> duplicateVotes = getStreamOfCharacters(validVote.toCharArray()).filter(userVote -> !uniqueVotes.add(userVote)).collect(Collectors.toSet());
        Set<Character> availableCandidateOptions = candidatesToBeElected.stream()
                .map(candidate -> candidate.getOption())
                .collect(Collectors.toSet());
        boolean invalidEntriesPresentInBallot = getStreamOfCharacters(validVote.toCharArray()).anyMatch(userVote -> !availableCandidateOptions.contains(userVote));
        if (invalidEntriesPresentInBallot) {
            System.out.println("Informal ballot since unknown entries are present; so discarded");
            return false;
        }
        if (!duplicateVotes.isEmpty()) {
            System.out.println("Informal ballot since duplicate entries are present; so discarded");
            return false;
        }

        Map<Character, Integer> preference = new LinkedHashMap<>();
        if (uniqueVotes.size() > 0) {
            int preferenceOrder = 0;
            for (Character optionToMap : uniqueVotes) {
                preference.put(optionToMap, new Integer(++preferenceOrder));
            }
        }

        Ballot ballot = new Ballot(candidatesToBeElected);
        ballot.setVotePreference(preference);
        if (!ballot.isValid()) {
            return false;
        }

        votingService.castVote(ballot);
        return true;
    }

    public void countVotesAndPrintResult() {
        int round = 1;
        Result result;
        do {
            System.out.println("\n\n*************** Round " + round + "****************");

            result = votingService.countVotes();
            System.out.println("---------------------------------------");
            System.out.println("Current vote count: ");
            System.out.println("---------------------------------------");
            result.getCurrentVoteCount().entrySet().stream().forEach(voteCount -> {
                Candidate candidate = Candidate.getCandidateUsingOption(voteCount.getKey());
                System.out.println(candidate.getName() + ":" + voteCount.getValue());
            });
            System.out.println("---------------------------------------");
            System.out.println("Quota required to win: "+ result.getQuotaRequiredToWin());
            System.out.println("---------------------------------------");
            if (result.getCandidateEliminated() != null) {
                System.out.println("Candidate eliminated:" + result.getCandidateEliminated().getName());
            }
            round++;
        } while (result.getWinner() == null);

        System.out.println("---------------------------------------");
        System.out.println("Winner: " + result.getWinner().getName());
    }

    private Stream<Character> getStreamOfCharacters(char[] chars) {
        return IntStream.range(0, chars.length).mapToObj(i -> chars[i]);
    }



    //For Test
    public Set<Candidate> getCandidates() {
        return candidates;
    }

}
