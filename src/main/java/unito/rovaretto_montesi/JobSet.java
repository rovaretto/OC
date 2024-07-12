package unito.rovaretto_montesi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class JobSet {
    List<Integer> jobList;
    HashMap<Integer,Integer> processingTime;
    HashMap<Integer,Integer> dueDate;
    HashMap<Integer,Integer> weight;

    Integer kMax;

    public static int nElem = 109;


    private static JobSet jobSet;

    public static JobSet getJobSet(){
        if(jobSet==null){
            List<Integer> job = new ArrayList<>();
            for (int i = 1; i <= nElem; i++) {
                job.add(i);
            }


            HashMap<Integer,Integer> processTime = new HashMap<>();
            processTime.put(1,121);
            processTime.put(2,79);
            processTime.put(3,147);
            processTime.put(4,83);
            processTime.put(5,130);
            processTime.put(6,102);
            processTime.put(7,96);
            processTime.put(8,88);
            Random r = new Random();
            for (int i = 9; i <= nElem; i++) {
                int val = 50 + r.nextInt(150);
                if(!processTime.containsValue(val)){
                    processTime.put(i,val);
                }else {
                    i--;
                }
            }



            HashMap<Integer,Integer> dueDate = new HashMap<>();
            dueDate.put(1,260);
            dueDate.put(2,266);
            dueDate.put(3,269);
            dueDate.put(4,336);
            dueDate.put(5,337);
            dueDate.put(6,400);
            dueDate.put(7,683);
            dueDate.put(8,719);
            int base = 719;
            for (int i = 9; i <= nElem; i++) {
                base += 50 + r.nextInt(150);
                dueDate.put(i,base);
            }

            HashMap<Integer,Integer> weight = new HashMap<>();
            for (int i = 1; i <= nElem; i++) {
               int val = 1 + r.nextInt(5) ;
                weight.put(i,val);
            }


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

    public Integer calculateTardiness(List<Integer> jobList, int t){
        int tardiness = 0;
        for (Integer job : jobList) {
            t += processingTime.get(job);
            tardiness += Math.max(0, weight.get(job) * (t - dueDate.get(job)));
        }
        return tardiness;
    }

    public HashMap<Integer, Integer> getProcessingTime() {
        return processingTime;
    }

}
