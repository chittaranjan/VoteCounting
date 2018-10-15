import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Solution {

    public static void main(String... args) {
        VoteCountingApplication voteCountingApplication = new VoteCountingApplication();
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

        List<String> candidates = new ArrayList<>();
        while(input.hasNext()){
            String candidateName = input.nextLine();
            candidates.add(candidateName);
        }
        voteCountingApplication.registerCandidates(candidates);
        voteCountingApplication.showCandidatesToBeElected();

        Scanner in = new Scanner(System.in);
        String userEntry = null;
        while (userEntry == null || !userEntry.equals("tally")) {

            System.out.print(">");

            userEntry = in.nextLine();
            if (userEntry.equals("tally")) {
                break;
            }
            voteCountingApplication.takeUserPreferences(userEntry);
        }

        if (userEntry.equals("tally")) {
           voteCountingApplication.countVotesAndPrintResult();
        }


    }
}
