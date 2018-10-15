import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CandidateTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        Candidate.clearCache();
    }

    @Test
    public void whenCandidateIsCloned() {
        Candidate candidate = new Candidate('A', "TestCandidate");
        Assert.assertTrue(candidate.equals(candidate.clone()));
    }

    @Test
    public void whenCandidateIsGetUsingValidOption() {
        Candidate candidate = new Candidate('A', "TestCandidate");
        Candidate candidateUsingOption = Candidate.getCandidateUsingOption('A');
        Assert.assertTrue(candidate.equals(candidateUsingOption));
    }

    @Test()
    public void testTwoDifferentCandidatesWithSameValuesAreEqual() {
        Candidate candidate1 = new Candidate('A', "TestCandidate");
        Candidate candidate2 = new Candidate('A', "TestCandidate");

        Assert.assertEquals(candidate1, candidate2);
        Assert.assertTrue(candidate1.hashCode() == candidate2.hashCode());
    }

    @Test()
    public void testTwoDifferentCandidatesWithDifferentValuesAreNotEqual() {
        Candidate candidate1 = new Candidate('A', "TestCandidate");
        Candidate candidate2 = new Candidate('B', "TestCandidate");
        Assert.assertNotEquals(candidate1, candidate2);
        Assert.assertFalse(candidate1.hashCode() == candidate2.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenANonExistingCandidateIsRequestedGetCandidateUsingOptionShouldReturnNull() {
        Candidate candidate1 = new Candidate('A', "TestCandidate");
        Candidate candidateUsingOptionB = Candidate.getCandidateUsingOption(new Character('B'));
    }
}
