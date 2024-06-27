package unito.rovaretto_montesi;

import java.util.ArrayList;
import java.util.List;

public class BruteForce {
    public static JobSet j = JobSet.getJobSet();
    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8,9,10,11};
        List<List<Integer>> result = new ArrayList<>();
        permute(nums, 0, result);

        // Stampa tutte le permutazioni
        System.out.println(result.stream().map(BruteForce::calcolaTardy).min(Integer::compareTo).get());
    }

    public static Integer calcolaTardy(List<Integer> ar){
        int time = 0;
        int tardy = 0;
        for (Integer i : ar){
            time += j.getProcessingTime(i);
            tardy += Math.max(0,time - j.getDueDate(i));
        }
        return tardy;
    }

    private static void permute(int[] nums, int start, List<List<Integer>> result) {
        if (start == nums.length) {
            List<Integer> permutation = new ArrayList<>();
            for (int num : nums) {
                permutation.add(num);
            }
            result.add(permutation);
        } else {
            for (int i = start; i < nums.length; i++) {
                swap(nums, start, i);
                permute(nums, start + 1, result);
                swap(nums, start, i);
            }
        }
    }

    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
