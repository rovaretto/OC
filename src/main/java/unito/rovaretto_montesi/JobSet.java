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
        if(jobSet==null){
            List<Integer> job = new ArrayList<>();
            job.add(1);
            job.add(2);
            job.add(3);
            job.add(4);
            job.add(5);
            job.add(6);
            job.add(7);
            job.add(8);
            job.add(9);
            job.add(10);
            job.add(11);
            job.add(12);


            HashMap<Integer,Integer> processTime = new HashMap<>();
            processTime.put(1,121);
            processTime.put(2,79);
            processTime.put(3,147);
            processTime.put(4,83);
            processTime.put(5,130);
            processTime.put(6,102);
            processTime.put(7,96);
            processTime.put(8,88);
            processTime.put(9,50);
            processTime.put(10,123);
            processTime.put(11,102);
            processTime.put(12,140);



            HashMap<Integer,Integer> dueDate = new HashMap<>();
            dueDate.put(1,260);
            dueDate.put(2,266);
            dueDate.put(3,269);
            dueDate.put(4,336);
            dueDate.put(5,337);
            dueDate.put(6,400);
            dueDate.put(7,683);
            dueDate.put(8,719);
            dueDate.put(9,759);
            dueDate.put(10,1000);
            dueDate.put(11,1050);
            dueDate.put(12,1200);

            HashMap<Integer,Integer> weight = new HashMap<>();
            weight.put(1,1);
            weight.put(2,1);
            weight.put(3,1);
            weight.put(4,1);
            weight.put(5,1);
            weight.put(6,1);
            weight.put(7,1);
            weight.put(8,1);
            weight.put(9,1);
            weight.put(10,1);
            weight.put(11,1);
            weight.put(12,1);

            JobSet.setJobSet( new JobSet(job,processTime,dueDate,weight));
        }
        return jobSet;
    }
    public static void setJobSet(JobSet job){
        jobSet = job;
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
        for (Integer job : jobList) {
            if (this.processingTime.get(job) > this.processingTime.get(max)) {
                max = job;
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

    public Double getRatioWeigthOnProcessingTime(int job){
        return (double)getWeight(job)/getProcessingTime(job);
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

    public List<Integer> getJobList() {
        return jobList;
    }

    public HashMap<Integer, Integer> getProcessingTime() {
        return processingTime;
    }

}
