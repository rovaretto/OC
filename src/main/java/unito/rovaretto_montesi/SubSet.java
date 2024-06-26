package unito.rovaretto_montesi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubSet {
    int i;
    int j;
    int k;

    List<Integer> jobList;

    JobSet jobSet = JobSet.getJobSet();

    public  SubSet(int i, int j, int k){
        this.i = i;
        this.j = j;
        this.k = k;

        //initial time
        if(k == -1){
            this.jobList = jobSet.jobList.stream().toList();
            int maxJ = jobList.getFirst();
            for(Integer job : this.jobList){
                if(jobSet.getProcessingTime(job) >= jobSet.getProcessingTime(maxJ)){
                    maxJ = job;
                }
            }
            this.k = maxJ;
            return;
        }

        this.jobList = new ArrayList<>();
        for(Integer job : jobSet.jobList){
            if(i <= job && job <= j &&
                    jobSet.getProcessingTime(job) < jobSet.getProcessingTime(this.k)){
                this.jobList.add(job);
            }
        }
    }

    public Integer getMaxJob(){
        int maxJob = this.jobList.getFirst();
        for(Integer job : this.jobList){
            if(jobSet.getProcessingTime(job) > jobSet.getProcessingTime(maxJob)){
                maxJob = job;
            }
        }
        return maxJob;
    }

    @Override
    public String toString() {
        return "SubSet{" +
                "i=" + i +
                ", j=" + j +
                ", k=" + k +
                ", jobList=" + jobList +
                ", jobSet=" + jobSet +
                '}';
    }
}
