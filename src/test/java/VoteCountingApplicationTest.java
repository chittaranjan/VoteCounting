import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class VoteCountingApplicationTest {

    private final String fileWithOneCandidate = "FileWithOneCandidate.txt";
    private final String fileWithEmptyLines = "FileWithEmptyLines.txt";
    private final String fileWithWhiteSpaceAtEndOfLine = "FileWithWhiteSpaceAtTheEndOfLine.txt";
    private final String fileWithCandidateList = "Candidates.txt";

    @Test
    public void whenAFileWithCandidateListIsProvidedCandidateListIsPopulatedWithNameAndUniqueLetterPrefix() {
        List<String> candidates = readFile(fileWithOneCandidate);
        registerCandidateAndValidateDetails(candidates);
    }

    @Test
    public void testBlankLinesAreIgnored() {
        List<String> candidates = readFile(fileWithEmptyLines);
        registerCandidateAndValidateDetails(candidates);
    }

    @Test
    public void testEmptyLinesProvidedAtTheBeginningOrEndOfALineAreIgnored() {
        List<String> candidates = readFile(fileWithWhiteSpaceAtEndOfLine);
        registerCandidateAndValidateDetails(candidates);
    }

    @Test
    public void whenValidPreferenceAreSetShouldReturnTrue() {
        List<String> candidates = readFile(fileWithCandidateList);
        VoteCountingApplication voteCountingApplication = new VoteCountingApplication();
        voteCountingApplication.registerCandidates(candidates);
        String validVote = "ABCD";
        Assert.assertTrue(voteCountingApplication.takeUserPreferences(validVote));
    }

    @Test
    public void whenInvalidPreferenceAreSetShouldReturnFalse() {
        List<String> candidates = readFile(fileWithCandidateList);
        VoteCountingApplication voteCountingApplication = new VoteCountingApplication();
        voteCountingApplication.registerCandidates(candidates);
        String invalidVote = "DoNotExist";
        Assert.assertFalse(voteCountingApplication.takeUserPreferences(invalidVote));
    }

    @Test
    public void whenWhiteSpacesAreProvidedInPreferenceTheyShouldBeIgnored() {
        List<String> candidates = readFile(fileWithCandidateList);
        VoteCountingApplication voteCountingApplication = new VoteCountingApplication();
        voteCountingApplication.registerCandidates(candidates);
        String validVote = "ABB";
        Assert.assertFalse(voteCountingApplication.takeUserPreferences(validVote));
    }

    @Test
    public void whenOptionsAreEnteredTwiceInPreferenceShouldReturnFalse() {
        List<String> candidates = readFile(fileWithCandidateList);
        VoteCountingApplication voteCountingApplication = new VoteCountingApplication();
        voteCountingApplication.registerCandidates(candidates);
        String validVote = "ABCD ";
        Assert.assertTrue(voteCountingApplication.takeUserPreferences(validVote));
    }



    private void registerCandidateAndValidateDetails(List<String> candidates) {
        VoteCountingApplication voteCountingApplication = new VoteCountingApplication();
        voteCountingApplication.registerCandidates(candidates);
        Set<Candidate> candidateSet = voteCountingApplication.getCandidates();
        Assert.assertEquals(1, candidateSet.size());
        Optional<Candidate> optionalCandidate = candidateSet.stream().findFirst();
        Assert.assertTrue(optionalCandidate.isPresent());
        Assert.assertEquals('A', optionalCandidate.get().getOption().charValue());
        Assert.assertEquals("Winery tour", optionalCandidate.get().getName());
    }

    private List<String> readFile(String fileName) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        List<String> candidates = new ArrayList<>();
        try {
            String candidateName = "";
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                while ( (candidateName = reader.readLine()) != null) {
                    candidates.add(candidateName);
                }
            }
        } catch(IOException ex) {
            System.out.println("Unable to read file" + ex);
        } finally {
            try {
                inputStream.close();
            } catch (Throwable ignore) {

            }
        }
        return candidates;
    }
}
