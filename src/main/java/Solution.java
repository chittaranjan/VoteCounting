import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Solution {

    public static void main(String... args) {
        VotingService votingService = new VotingServiceImpl();
        Scanner input = null;
        if (0 < args.length) {
            try {
                input = new Scanner(new File(args[1]));
            } catch (FileNotFoundException ex) {
                System.out.println("No File Found!");
                return;
            }

        } else {
            System.err.println("Invalid arguments count:" + args.length);
            System.exit(0);
        }

        char option = 'A';
        Set<Candidate> candidates = new LinkedHashSet<>();
        while(input.hasNext()){
            String candidateName = input.nextLine();
            Candidate candidate = new Candidate(option++, candidateName);
            candidates.add(candidate);
        }

        System.out.println("\n*************** Candidates to elect " + "******************");
        candidates.stream().forEach(candidate -> System.out.println(candidate.getOption() + ". " + candidate.getName()));
        System.out.println("\n*********************************************************");
        System.out.println("\nEnter your preference in order");

        Scanner in = new Scanner(System.in);
        String userEntry = null;
        while (userEntry == null || !userEntry.equals("tally")) {

            System.out.print(">");

            userEntry = in.nextLine();
            if (userEntry.isEmpty()) {
                continue;
            }
            String[] userVotes = userEntry.split("\\s+");

            Set<Candidate> freshSetOfCandidates = new LinkedHashSet<>();
            for (Candidate candidate : candidates) {
                freshSetOfCandidates.add(candidate.clone());
            }
            Set<Character> availableCandidateOptions = freshSetOfCandidates.stream().map(candidate -> candidate.getOption()).collect(Collectors.toSet());
            Set<String> uniqueVotes = new LinkedHashSet<>();
            Set<String> duplicateVotes = Arrays.stream(userVotes).filter(userVote -> !uniqueVotes.add(userVote)).collect(Collectors.toSet());
            boolean invalidEntriesPresentInBallot = Arrays.stream(userVotes)
                    .anyMatch(userVote -> !availableCandidateOptions.contains(userVote.charAt(0)));
            if (invalidEntriesPresentInBallot) {
                System.out.println("Informal ballot since unknown entries are present; so discarded");
                continue;
            }
            if (!duplicateVotes.isEmpty()) {
                System.out.println("Informal ballot since duplicate entries are present; so discarded");
                continue;
            }
            Set<Character> options = new LinkedHashSet<>(uniqueVotes.stream().map(vote -> vote.charAt(0)).collect(Collectors.toList()));
            Set<Character> availableOptions = freshSetOfCandidates.stream().map(candidate -> candidate.getOption()).collect(Collectors.toSet());

            if (!availableOptions.containsAll(options)) {
                options.removeIf(optionToValidate -> !availableOptions.contains(optionToValidate));
            }

            Map<Character, Integer> preference = new HashMap<>();
            if (options.size() > 0) {
                int preferenceOrder = 0;
                for (Character optionToMap : options) {
                    preference.put(optionToMap, new Integer(++preferenceOrder));
                }
                Set<Candidate> candidateSet = new LinkedHashSet<>();
                candidateSet.addAll(freshSetOfCandidates);

                Ballot ballot = new Ballot(candidateSet);
                ballot.setVotePreference(preference);
                votingService.castVote(ballot);
            }


        }

        if (userEntry.equals("tally")) {
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


    }
}
