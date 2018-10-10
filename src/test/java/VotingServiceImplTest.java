import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class VotingServiceImplTest extends VotingCountBaseTest {
    private List<Ballot> ballots;
    VotingService votingService;

    @Before
    public void setUp() throws Exception {
        ballots = createBallots();
        votingService = new VotingServiceImpl();
    }

    @Test
    public void testValidBallotsAndCurrentQuotaAfterFirstRound() {
        ballots.forEach(ballot -> {
            votingService.castVote(ballot);
        });
        votingService.countVotes();
        Assert.assertEquals(5, votingService.getCurrentQuota());
    }

    @Test
    public void eliminationOfCandidateWithMinimumVoteTest() {
        ballots.forEach(ballot -> {
            votingService.castVote(ballot);
        });
        Result resultAfterFirstRound = votingService.countVotes();
        Assert.assertEquals(null, resultAfterFirstRound.getWinner());
        Assert.assertTrue(resultAfterFirstRound.getCandidatesEliminated().isEmpty());
        Result resultAfterSecondRound = votingService.countVotes();
        Assert.assertEquals('A', ((Candidate)resultAfterSecondRound.getCandidatesEliminated().toArray()[0]).getOption().charValue());
    }

    @Test
    public void testUsualScenarioWithValidBallotsAndCountResult() {
        ballots.forEach(ballot -> {
            votingService.castVote(ballot);
        });

        //First Round
        Result resultAfterFirstRound = votingService.countVotes();
        System.out.println(resultAfterFirstRound);
        //Second Round
        Result resultAfterSecondRound = votingService.countVotes();
        System.out.println(resultAfterSecondRound);
        //3rd Round
        Result resultAfterThirdRound = votingService.countVotes();
        System.out.println(resultAfterThirdRound);
        Assert.assertEquals(4, resultAfterThirdRound.getQuotaRequiredToWin());
        Assert.assertEquals('B', resultAfterThirdRound.getWinner().getOption().charValue());
    }

    @Test
    public void allCandidatesEliminatedTest() {
        //ToDo check currentQuota when all candidates are eliminated
    }

    @Test
    public void testWhenThereIsATieWithFirstPreferenceDecideWinnerWithNextHigherPreference() {
        /**
         * test case to test a scenario when there is tie according to first preference in ballots
         * the winner can be determined according to next higher preference
         * e.g
         * There are 2 ballots like below
         * A D C
         * B D C
         *
         * In this case, first preference in 2 ballots are A and B, since there is tie, winner could be D
         *
         */
        Map<Character, String> candidates = generateCandidates();
        for (int i = 0; i <2; i++) {
            Set<Candidate> candidateSet = candidates
                    .entrySet()
                    .stream()
                    .map(entry -> new Candidate(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toSet());
            Ballot ballot = new Ballot(candidateSet);
            /**
             * Set preferences
             */
            Map<Character, Integer> preference = null;
            if (i == 0) {
                preference = Util.of('A', 1,
                        'D', 2,
                        'c', 3);
            } else if (i == 1) {
                preference = Util.of('B', 1,
                        'D', 2,
                        'C', 3);
            }
            ballot.setVotePreference(preference);
            votingService.castVote(ballot);
        }

        //First Round
        Result resultAfterFirstRound= votingService.countVotes();
        System.out.println(resultAfterFirstRound);
        //Second round
        Result resultAfterSecondRound = votingService.countVotes();
        System.out.println(resultAfterSecondRound);
        Assert.assertEquals(2, resultAfterSecondRound.getQuotaRequiredToWin());
        Assert.assertEquals('D', resultAfterSecondRound.getWinner().getOption().charValue());
    }

    @After
    public void tearDown() throws Exception {
        ballots.clear();
        ballots = null;
        votingService = null;
    }

    private List<Ballot> createBallots() {
        Map<Character, String> candidates = generateCandidates();
        List<Ballot> ballots = new ArrayList<>();

        for (int i =1; i<=8; i++) {
            Set<Candidate> candidateSet = candidates
                    .entrySet()
                    .stream()
                    .map(entry -> new Candidate(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toSet());
            Ballot ballot = new Ballot(candidateSet);
            Map<Character, Integer> preference = getPreference(i);
            ballot.setVotePreference(preference);
            ballots.add(ballot);
        }
        return ballots;
    }

    private Map<Character, Integer> getPreference(int number) {
        switch(number) {
            case 1:
                return Util.<Character, Integer>of(Character.valueOf('A'), Integer.valueOf(1),
                                                        Character.valueOf('B'), Integer.valueOf(2),
                                                        Character.valueOf('C'), Integer.valueOf(4),
                                                        Character.valueOf('D'), Integer.valueOf(3));
            case 2:
                return Util.<Character, Integer>of(Character.valueOf('A'), Integer.valueOf(2),
                        Character.valueOf('B'), Integer.valueOf(1),
                        Character.valueOf('D'), Integer.valueOf(3));
            case 3 :
                return Util.<Character, Integer>of(Character.valueOf('A'), Integer.valueOf(2),
                        Character.valueOf('B'), Integer.valueOf(3),
                        Character.valueOf('C'), Integer.valueOf(1),
                        Character.valueOf('D'), Integer.valueOf(4));
            case 4:
                return Util.<Character, Integer>of(Character.valueOf('A'), Integer.valueOf(3),
                        Character.valueOf('B'), Integer.valueOf(4),
                        Character.valueOf('C'), Integer.valueOf(1),
                        Character.valueOf('D'), Integer.valueOf(2));
            case 5:
                return Util.<Character, Integer>of(Character.valueOf('A'), Integer.valueOf(2),
                        Character.valueOf('D'), Integer.valueOf(1));
            case 6:
                return Util.<Character, Integer>of(
                        Character.valueOf('B'), Integer.valueOf(2),
                        Character.valueOf('D'), Integer.valueOf(1));
            case 7:
                return Util.<Character, Integer>of(Character.valueOf('A'), Integer.valueOf(2),
                        Character.valueOf('B'), Integer.valueOf(1),
                        Character.valueOf('C'), Integer.valueOf(3));
            case 8:
                return Util.<Character, Integer>of(Character.valueOf('A'), Integer.valueOf(3),
                        Character.valueOf('B'), Integer.valueOf(2),
                        Character.valueOf('C'), Integer.valueOf(1),
                        Character.valueOf('D'), Integer.valueOf(4));
        }
        return Collections.emptyMap();
    }



}