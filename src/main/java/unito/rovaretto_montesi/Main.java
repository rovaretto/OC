package unito.rovaretto_montesi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> job = new ArrayList<>();
        job.add(1);
        job.add(2);
        job.add(3);
        job.add(4);
        job.add(5);
        job.add(6);
        job.add(7);
        job.add(8);

        HashMap<Integer,Integer> processTime = new HashMap<>();
        processTime.put(-1, 1000);
        processTime.put(1,121);
        processTime.put(2,79);
        processTime.put(3,147);
        processTime.put(4,83);
        processTime.put(5,130);
        processTime.put(6,102);
        processTime.put(7,96);
        processTime.put(8,88);


        HashMap<Integer,Integer> dueDate = new HashMap<>();
        dueDate.put(1,260);
        dueDate.put(2,266);
        dueDate.put(3,269);
        dueDate.put(4,336);
        dueDate.put(5,337);
        dueDate.put(6,400);
        dueDate.put(7,683);
        dueDate.put(8,719);

        HashMap<Integer,Integer> weight = new HashMap<>();
        weight.put(1,1);
        weight.put(2,1);
        weight.put(3,1);
        weight.put(4,1);
        weight.put(5,1);
        weight.put(6,1);
        weight.put(7,1);
        weight.put(8,1);

        JobSet.setJobSet( new JobSet(job,processTime,dueDate,weight));
        TardinessStructure ts = new TardinessStructure(JobSet.getJobSet().generateSubset(1,8,-1), 0);
        ts.constructStructure();
        System.out.println("AAA");
        for(TardinessStructure[] aa : ts.tardyMatrix){
            for(TardinessStructure j : aa){
                System.out.println(j);
            }
            System.out.println();
        }

//

        System.out.println(ts.resolveStructure());
        for(Integer [] a: ts.results){
            for(Integer i : a){
                System.out.print(i + " ");
            }
            System.out.println();
        }
    }
}