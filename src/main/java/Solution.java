import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Solution {

    public static void main(String... args) {
        System.out.println(args[1]);
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
        Set<Candidate> candidates = new HashSet<>();
        while(input.hasNext()){
            String candidateName = input.nextLine();
            Candidate candidate = new Candidate(option++, candidateName);
            candidates.add(candidate);
        }

        candidates.stream().forEach(candidate -> System.out.println(candidate.getOption() + ". " + candidate.getName()));

        Scanner in = new Scanner(System.in);
        String userEntry = null;
        Map<Character, Integer> preference = new HashMap<>();
        while (userEntry == null || !userEntry.equals("tally")) {

            userEntry = in.nextLine();
            if (userEntry.isEmpty()) {
                continue;
            }
            String[] userVotes = userEntry.split("\\s+");
            Set<String> uniqueVotes = new LinkedHashSet<String>(Arrays.asList(userVotes));
            Set<Character> options = new LinkedHashSet<>(uniqueVotes.stream().map(vote -> vote.charAt(0)).collect(Collectors.toList()));
            Set<Character> availableOptions = candidates.stream().map(candidate -> candidate.getOption()).collect(Collectors.toSet());

            if (!availableOptions.containsAll(options)) {
                options.removeIf(optionToValidate -> !availableOptions.contains(optionToValidate));
            }

            if (options.size() > 0) {
                int preferenceOrder = 0;
                for (Character optionToMap : options) {
                    preference.put(optionToMap, new Integer(++preferenceOrder));
                }
                Ballot ballot = new Ballot(candidates);
                ballot.setVotePreference(preference);
                votingService.castVote(ballot);
            }
        }

        if (userEntry.equals("tally")) {
            Result result = votingService.countVotes();
            while (result.getWinner() == null) {
                result = votingService.countVotes();
                System.out.println("Current vote count: " + result.getCurrentVoteCount());
                System.out.println("Quota required to win: "+ result.getQuotaRequiredToWin());
                System.out.println("Candidates eliminated in this round: "+ result.getCandidatesEliminated());

            }
            System.out.println("Winner: " + result.getWinner().getOption());
        }


    }
}
