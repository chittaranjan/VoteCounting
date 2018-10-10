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

        candidates.stream().forEach(candidate -> System.out.println(candidate.getOption() + ". " + candidate.getName()));

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
            Set<String> uniqueVotes = new LinkedHashSet<String>(Arrays.asList(userVotes));
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
                System.out.println("-------- Round " + round++ + "---------");

                result = votingService.countVotes();
                System.out.println(result);
                System.out.println("Current vote count: " + result.getCurrentVoteCount());
                System.out.println("Quota required to win: "+ result.getQuotaRequiredToWin());
                if (result.getWinner() == null) {
                    System.out.println("Candidates eliminated:" + result.getCandidatesEliminated());
                }

            } while (result.getWinner() == null);


            System.out.println("Winner: " + result.getWinner().getOption());
        }


    }

    private static void printCommonResult(Result result) {
        System.out.println("Current vote count: " + result.getCurrentVoteCount());
        System.out.println("Quota required to win: "+ result.getQuotaRequiredToWin());
    }
}
