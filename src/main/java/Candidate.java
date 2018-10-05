import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Candidate
 */
public class Candidate {
    private String name;
    private Character option;
    private int voteCount = 0;

    /**
     * A local cache to look for name of the candidate using the option
     */
    private static Map<Character, String> optionToNameMap = new HashMap<>();

    public Candidate(Character option, String name) {
        this.name = name;
        this.option = option;
        optionToNameMap.put(option, name);
    }

    public String getName() {
        return name;
    }

    public Character getOption() {
        return option;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getVoteCount() {
        return voteCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.option);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Candidate)) {
            return false;
        }
        Candidate that = (Candidate)obj;
        return (this.name.equals(that.name) && this.option.equals(that.option));
    }

    @Override
    public String toString() {
        return "[" + this.option + " - "+ this. name+ "-" + this.voteCount +"]";
    }

    public static Candidate getCandidateUsingOption(Character option) {
        if (!optionToNameMap.containsKey(option)) {
            throw new IllegalArgumentException("Candidate with option "+ option + " does not exist");
        }
        return new Candidate(option, optionToNameMap.get(option));
    }
}
