package unito.rovaretto_montesi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JobSet {
    List<Integer> jobList;
    HashMap<Integer,Integer> processingTime;
    HashMap<Integer,Integer> dueDate;
    HashMap<Integer,Integer> weight;

    Integer kMax;


    private static JobSet jobSet;

    public static JobSet getJobSet(){
        return jobSet;
    }
    public static void setJobSet(JobSet job){
        jobSet = job;
    }

    public JobSet() {
        jobList = new ArrayList<>();
        processingTime = new HashMap<>();
        dueDate = new HashMap<>();
    }

    public JobSet(List<Integer> jobList, HashMap<Integer, Integer> processingTime, HashMap<Integer, Integer> dueDate, HashMap<Integer,Integer> weight) {
        this.jobList = jobList;
        this.processingTime = processingTime;
        this.dueDate = dueDate;
        this.weight = weight;
        setMax();
    }

    public void setMax(){
        if(jobList.isEmpty()) return;

        int max = jobList.getFirst();
        for(int i = 0; i < jobList.size(); i++){
            if(this.processingTime.get(jobList.get(i)) > this.processingTime.get(max)){
                max = jobList.get(i);
            }
        }
        kMax = max;
    }

    public SubSet generateSubset(int i, int j, int k){
        return new SubSet(i,j,k);
    }

    public Integer getProcessingTime(int job){
        return processingTime.get(job);
    }

    public Integer getDueDate(int job){
        return dueDate.get(job);
    }

    public Integer getWeight(int job){
        return weight.get(job);
    }

    @Override
    public String toString() {
        return "JobSet{" +
                "jobList=" + jobList +
                ", processingTime=" + processingTime +
                ", dueDate=" + dueDate +
                ", weight=" + weight +
                ", kMax=" + kMax +
                '}';
    }
}
