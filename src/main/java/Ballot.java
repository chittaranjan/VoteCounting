import java.util.*;

public class Ballot {
    private Set<Candidate> candidates;
    boolean isValid = true;
    boolean isExhausted = false;

    public Ballot(Set<Candidate> candidates) {
        this.candidates = new HashSet<>();
        this.candidates.addAll(candidates);
    }

    public static Ballot ballotWithListOfCandidates(Set<Candidate> candidates) {
        return new Ballot(candidates);
    }

    public Set<Candidate> getCandidates() {
        return candidates;
    }

    /**
     * Moves the current preference up by 1
     */
    public void reOrderPreferences() {
        candidates.stream()
                .filter(candidate -> candidate.getCurrentPreference() > 0)
                .forEach(candidate -> candidate.reOrderPreference());
    }

    /**
     * setVotePreference
     *
     * Vote preference in the format
     * <Option, preferenceNumber>
     * e.g. <A, 1>
     */
    public void setVotePreference(Map<Character, Integer> votePreference) {
        System.out.println(votePreference);
        if (!isValidPreferenceSet(votePreference.values())) {
            isValid = false;
        }
        for (Map.Entry<Character, Integer> entry : votePreference.entrySet()) {
            Optional<Candidate> candidateOptional = candidates.stream().filter(candidate1 -> candidate1.getOption().equals(entry.getKey())).findAny();
            if (candidateOptional.isPresent()) {
                Candidate candidate = candidateOptional.get();
                candidate.setCurrentPreference(entry.getValue());
            } else {
                isValid = false;
            }

        }
        System.out.println(candidates);
    }

    public boolean isValid() {
        return isValid;
    }

    public boolean isExhausted() {
        return isExhausted;
    }

    public void setExhausted(boolean exhausted) {
        isExhausted = exhausted;
    }

    private boolean isValidPreferenceSet(Collection<Integer> preferenceValues) {
        /*
         * Preference must have been set by putting consecutive numbers starting with 1
         * And it must not go beyond the number of candidates available to vote
         */
        Object[] preferences = preferenceValues.toArray();
        if (preferences.length > candidates.size()) {
            return false;
        }
        Arrays.sort(preferences);
        if (((Integer)preferences[0]).intValue() != 1) {
            return false;
        }
        //Test for consecutive numbers
        for (int i=1; i< preferences.length; i++){
            if (((Integer)preferences[i]).intValue() != ((Integer)preferences[i-1]).intValue()+1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Ballot{" +
                "candidates=" + candidates +
                //", isValid=" + isValid +
                //", isExhausted=" + isExhausted +
                "}\n";
    }
}
