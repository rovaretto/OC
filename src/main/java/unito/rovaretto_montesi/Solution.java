package unito.rovaretto_montesi;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    List<Integer> jobs = new ArrayList<>();
    Integer tardiness;

    public void createSolution(List<Integer> jobs1, int kPrimo, List<Integer> jobs2) {
        jobs.addAll(jobs1);
        jobs.add(kPrimo);
        jobs.addAll(jobs2);
    }

    public Integer getTardiness() {
        return tardiness;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "jobs=" + jobs +
                ", tardiness=" + tardiness +
                '}';
    }
}
