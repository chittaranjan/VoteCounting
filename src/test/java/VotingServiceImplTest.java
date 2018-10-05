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
    public void validBallotsTest() {
        ballots.forEach(ballot -> {
            votingService.castVote(ballot);
        });
        Assert.assertEquals(5, votingService.getCurrentQuota());
    }

    @After
    public void tearDown() throws Exception {
        ballots = null;
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