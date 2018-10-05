import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BallotTest {
    private Ballot ballot;

    @Before
    public void setUp() throws Exception {
        ballot = Ballot.ballotWithListOfCandidates(generateCandidates(5));
    }

    @Test
    public void standardBallotTest() {
        Assert.assertEquals(5, ballot.getCandidates().size());
    }

    @Test
    public void validVotePreferenceTest() {
        Set<Candidate> candidates = ballot.getCandidates();
        Map<Character, Integer> vote = new HashMap<>();
        int count = 1;
        for(Candidate candidate : candidates) {
            vote.put(candidate.getOption(), count++);
        }
        ballot.setVotePreference(vote);
        Assert.assertTrue(ballot.isValid());
    }

    @Test
    public void preferenceNumberBeyondRangeInBallotTest() {
        Set<Candidate> candidates = ballot.getCandidates();
        Map<Character, Integer> vote = new HashMap<>();
        vote.put('A', 6);
        ballot.setVotePreference(vote);
        Assert.assertFalse(ballot.isValid());
    }

    @Test()
    public void invalidCandidateOptionInBallotTest() {
        Set<Candidate> candidates = ballot.getCandidates();
        Map<Character, Integer> vote = new HashMap<>();
        vote.put('F', 6);
        ballot.setVotePreference(vote);
        Assert.assertFalse(ballot.isValid());
    }

    private Set<Candidate> generateCandidates(int number) {
        Set<Candidate> candidates = new HashSet<>();
        char option = 'A';
        for (int i=0; i<number; i++) {
            Candidate candidate = new Candidate(new Character(option++), "Candidate"+(i+1));candidates.add(candidate);
        }
        return candidates;
    }
}