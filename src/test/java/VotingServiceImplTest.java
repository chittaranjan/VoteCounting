import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class VotingServiceImplTest extends VotingCountBaseTest {
    private List<Ballot> ballots;
    private VotingService votingService;

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
        Result result = votingService.countVotes();
        Assert.assertEquals(5, result.getQuotaRequiredToWin());
    }

    @Test
    public void testEliminationOfCandidateWithMinimumVote() {
        ballots.forEach(ballot -> {
            votingService.castVote(ballot);
        });
        Result resultAfterFirstRound = votingService.countVotes();
        Assert.assertEquals(null, resultAfterFirstRound.getWinner());
        Assert.assertTrue(resultAfterFirstRound.getCandidateEliminated() == null);
        Result resultAfterSecondRound = votingService.countVotes();
        Assert.assertEquals('A', resultAfterSecondRound.getCandidateEliminated().getOption().charValue());
    }

    @Test
    public void testUsualScenarioWithValidBallotsAndCountResult() {
        /**
         * tests an usual scenario
         * verifies currentQuota to win, total number of non-exhausted votes and candidates eliminated and/or winner
         * at every rounds of counting
         */

        ballots.forEach(ballot -> {
            votingService.castVote(ballot);
        });

        //First Round
        Result resultAfterFirstRound = votingService.countVotes();
        Assert.assertEquals(5, resultAfterFirstRound.getQuotaRequiredToWin());
        Assert.assertEquals(8, resultAfterFirstRound.getCurrentVoteCount().values().stream().flatMapToInt(value -> IntStream.of(value.intValue())).sum());
        Assert.assertTrue(resultAfterFirstRound.getCandidateEliminated() == null && resultAfterFirstRound.getWinner() == null);
        //Second Round
        Result resultAfterSecondRound = votingService.countVotes();
        Assert.assertEquals(5, resultAfterSecondRound.getQuotaRequiredToWin());
        Assert.assertEquals(8, resultAfterSecondRound.getCurrentVoteCount().values().stream().flatMapToInt(value -> IntStream.of(value.intValue())).sum());
        Assert.assertEquals('A', resultAfterSecondRound.getCandidateEliminated().getOption().charValue());
        //3rd Round
        Result resultAfterThirdRound = votingService.countVotes();
        Assert.assertEquals(4, resultAfterThirdRound.getQuotaRequiredToWin());
        Assert.assertEquals(7, resultAfterThirdRound.getCurrentVoteCount().values().stream().flatMapToInt(value -> IntStream.of(value.intValue())).sum());
        Assert.assertEquals('D', resultAfterThirdRound.getCandidateEliminated().getOption().charValue());
        Assert.assertEquals('B', resultAfterThirdRound.getWinner().getOption().charValue());
    }


    @Test
    public void testEliminationOfCandidateAtRandomWhenThereAreMultipleLeadingCandidates() {
        /**
         * test case to test a scenario when there is a tie between 2 or more leading candidates
         * and there are no other candidates that can be eliminated
         * One candidate should be chosen at random for elimination.
         *
         * e.g
         * There are 2 ballots like below
         * A D C
         * B D C
         *
         * In this case, first preference in 2 ballots are A and B,
         * Either or A or B will be chosen at random to be eliminated
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
                        'C', 3);
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
        //Second round
        Result resultAfterSecondRound = votingService.countVotes();
        Assert.assertEquals(2, resultAfterSecondRound.getQuotaRequiredToWin());
        Character eliminatedCandidateAtSecondRound = resultAfterSecondRound.getCandidateEliminated().getOption();
        Assert.assertTrue(eliminatedCandidateAtSecondRound.equals('A') || eliminatedCandidateAtSecondRound.equals('B'));
        Result resultAfterThirdRound = votingService.countVotes();
        Character eliminatedCandidateAtThirdRound = resultAfterThirdRound.getCandidateEliminated().getOption();
        if (eliminatedCandidateAtSecondRound.equals('A')) {
            Assert.assertTrue(eliminatedCandidateAtThirdRound.equals('B') || eliminatedCandidateAtThirdRound.equals('D'));
        } else if (eliminatedCandidateAtSecondRound.equals('B')) {
            Assert.assertTrue(eliminatedCandidateAtThirdRound.equals('A') || eliminatedCandidateAtThirdRound.equals('D'));
        }

        if (eliminatedCandidateAtThirdRound.equals('A') || eliminatedCandidateAtThirdRound.equals('B')) {
            Assert.assertTrue(resultAfterThirdRound.getWinner().getOption().equals('D'));
        } else {
            Result resultAfterForthRound = votingService.countVotes();
            Character eliminatedCandidateAtForthRound = resultAfterForthRound.getCandidateEliminated().getOption();
            if (eliminatedCandidateAtSecondRound.equals('A')) {
                Assert.assertTrue(eliminatedCandidateAtForthRound.equals('B') || eliminatedCandidateAtForthRound.equals('C'));
            } else if (eliminatedCandidateAtSecondRound.equals('A')) {
                Assert.assertTrue(eliminatedCandidateAtForthRound.equals('A') || eliminatedCandidateAtForthRound.equals('C'));
            }

            if (eliminatedCandidateAtForthRound.equals('B') || eliminatedCandidateAtForthRound.equals('A')) {
                Assert.assertTrue(resultAfterForthRound.getWinner().getOption().equals('C'));
            }

        }

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