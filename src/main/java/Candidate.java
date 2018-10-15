import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Candidate
 */
public class Candidate {
    private String name;
    private Character option;
    private int currentPreference = 0;

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

    public void setCurrentPreference(int currentPreference) {
        this.currentPreference = currentPreference;
    }

    public void reOrderPreference() {
        this.currentPreference--;
    }

    public int getCurrentPreference() {
        return currentPreference;
    }

    public Candidate clone() {
        Candidate candidate = new Candidate(this.option, this.name);
        candidate.currentPreference = this.currentPreference;
        return candidate;
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
        return "[" + this.option + " - "+
                this. name+ " - " +
                this.currentPreference +"]";
    }

    //For Testcase
    public static void clearCache() {
        optionToNameMap.clear();
    }

    public static Candidate getCandidateUsingOption(Character option) {
        if (!optionToNameMap.containsKey(option)) {
            throw new IllegalArgumentException("Candidate with option "+ option + " does not exist");
        }
        return new Candidate(option, optionToNameMap.get(option));
    }
}
