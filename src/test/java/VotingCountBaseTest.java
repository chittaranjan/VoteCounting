import java.util.HashMap;
import java.util.Map;

public class VotingCountBaseTest {
    public Map<Character, String> generateCandidates() {
        Map<Character, String> candidates = new HashMap<>();
        candidates.put('A', "Winery tour");
        candidates.put('B', "Ten pin bowling");
        candidates.put('C', "Movie night");
        candidates.put('D', "Museum visit");
        return candidates;
    }

}
