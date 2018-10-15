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

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void whenAnInvalidOptionIsUsedToGetCandidateIllegalArgumentExceptionMustBeThrown() {
        Candidate candidate = new Candidate('A', "TestCandidate");
        Candidate candidateUsingOption = Candidate.getCandidateUsingOption(new Character('B'));
    }
}
