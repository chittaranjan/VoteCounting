# VoteCounting
Preferential Vote counting System
In a preferential vote counting system, voters are allowed to vote for more than one item so that they can also express a second, third, etc. preference in the event that their first preference is unelectable. In a paper based system, the votes (also called preferences) are written on a ballot paper by the voter. A voter is given an empty ballot paper with a list of the candidates (also called options) like this:

A. Winery tour
B. Ten pin bowling
C. Movie night
D. Museum visit

The voter can then fill out the empty ballot paper with their choices by putting consecutive numbers against the candidates they wish to be elected in their order of preference. In our situation, we will allow users to nominate as many or as few choices as they wish.

A. Winery tour
B. Ten pin bowling 2
C. Movie night
D. Museum visit    1

In the above example, the voter has selected a museum visit as their first preference, then ten pin bowling as their second preference. They have not expressed a third or fourth preference because they’re not interested in any of the remaining options or don’t care which of them wins the election if they can’t have their first or second choice.

In our election, we are only selecting one winner. The rule we will apply is that the winner is the candidate that has more that half of the available votes. To start out the counting, we allocate ballot papers to each candidate according to the first preference written on it. At this point it is possible that no candidate has more than half the number of available votes (called the quota) yet. To resolve this, we find the candidate (or candidates if there are more than one with the same number of votes) with the least number of votes and eliminate them from the election. When a candidate is eliminated, all the ballot papers that have been assigned to them are re-allocated to the next available preference on each of those ballot papers. This process repeats in rounds until one candidate has more than half of the available votes.
There are a few special rules and edge cases to be aware of:
1) A ballot paper can become ‘exhausted’ if it has already been assigned to all of the candidates for which a preference has been provided or if all of the preferences voted for on the ballot paper have already been eliminated. When a ballot paper is exhausted, it can no longer be assigned to anyone, so the number of votes that are required to win the election (the quota) must be re-calculated.
2) If there is a tie between two or more leading candidates and there are no other candidates that can be eliminated, one candidate should be chosen at random for elimination.


How to build and run
1. To build and make jar 
gradlew clean build jar
2. Run the tests
gradlew test
3. Run the application
gradlew runJar
