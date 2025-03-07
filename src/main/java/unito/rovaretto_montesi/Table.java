package unito.rovaretto_montesi;

import java.util.*;

public class Table {
    private final JobSet jobSet = JobSet.getJobSet();
    private final HashMap<List<Integer>,Solution[]> tardiness;

    /*conta i table hit e miss*/
    private int countTableHit =0;
    private int countTableMiss =0;

    /*conta le equazioni da risolvere in funzione di delta*/
    private int countEqResolved =0;

    private int countShortcuttedSolution = 0;

    public Table() {
        this.tardiness = new HashMap<>();
        int n = jobSet.getJobList().size();
        int P = jobSet.getProcessingTime().values().stream().mapToInt(Integer::intValue).sum() + 1;
        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= n; j++){
                for(int k = 1; k <= n; k++){
                    SubSet s = jobSet.generateSubset(i, j, k);
                    tardiness.put(s.jobList, new Solution[P]);
                }
            }
        }
        System.out.println(tardiness.size());
        tardiness.put(jobSet.getJobList(), new Solution[P]);
    }

    public void initialize(){
        for(List<Integer> s : tardiness.keySet()){
            if(s.isEmpty()){
                Solution solution = new Solution();
                solution.tardiness = 0;
                solution.jobs = new ArrayList<>();
                Arrays.fill(tardiness.get(s), solution);
            }

            if(s.size() == 1){
                int j = s.getFirst();
                for(int i = 0; i < tardiness.get(s).length; i++){
                    Solution solution = new Solution();
                    solution.tardiness = jobSet.getWeight(j) * Math.max(0, i + jobSet.getProcessingTime(j) - jobSet.getDueDate(j));
                    solution.jobs = new ArrayList<>();
                    solution.jobs.add(j);
                    tardiness.get(s)[i] = solution;
                }
            }
        }
    }


    private Solution resolve(SubSet s, int t) {
        if(tardiness.get(s.jobList)[t] !=null){
            countTableHit++;
            return tardiness.get(s.jobList)[t];
        }
        countTableMiss++;

        Solution shortcutResult = shortcutResolution(s,t);
        if(shortcutResult != null){
            countShortcuttedSolution++;
            return shortcutResult;
        }

        int n = jobSet.getJobList().size();
        int i = s.i;
        int j = s.j;
        int kPrimo = s.getMaxJob();

        List<Integer> delta = restrictionOfDelta(s,t);
//        List<Integer> delta = IntStream.rangeClosed(0,n-k).boxed().toList();

        List<Solution> result = new ArrayList<>();
        for(int d : delta){
            SubSet s1 = jobSet.generateSubset(i, kPrimo + d, kPrimo);
            Solution res1 = resolve(s1, t);

            int cKprimoDelta = t + s1.jobList.stream().map(jobSet::getProcessingTime).reduce(0, Integer::sum) + jobSet.getProcessingTime(kPrimo);
            Integer res2 = jobSet.getWeight(kPrimo) * Math.max(0, cKprimoDelta - jobSet.getDueDate(kPrimo));

            SubSet s2 = jobSet.generateSubset(kPrimo + d + 1, j, kPrimo);
            Solution res3 = resolve(s2, cKprimoDelta);

            Solution solutionFinal = new Solution();
            solutionFinal.tardiness =res1.tardiness+res2+res3.tardiness;
            solutionFinal.createSolution(res1.jobs,kPrimo,res3.jobs);
            result.add(solutionFinal);
        }
        countEqResolved += result.size();
        return result.stream().min(Comparator.comparing(Solution::getTardiness)).get();
    }

    public List<Integer> restrictionOfDelta(SubSet subSet, Integer t){
        List<Integer> restrictions = new ArrayList<>();
        int dk = jobSet.getDueDate(subSet.getMaxJob());
        while (true){
            while(true){
                List<Integer> sPrimo = new ArrayList<>();

                for(Integer j : subSet.jobList){
                    if(jobSet.getDueDate(j) <= dk){
                        sPrimo.add(j);
                    }
                }
                int dkPrimo = t + sPrimo.stream().
                                map(jobSet::getProcessingTime).
                                reduce(0, Integer::sum);
                if(dkPrimo > dk){
                    dk = dkPrimo;
                }else{
                    break;
                }
            }

            int maxJ = 0;
            for(Integer j : subSet.jobList){
                if(jobSet.getDueDate(j)<= dk && j > maxJ){
                    maxJ = j;
                }
            }
            restrictions.add(maxJ - subSet.getMaxJob());

            List<Integer> sSecondo = new ArrayList<>();
            for(Integer j : subSet.jobList){
                if(jobSet.getDueDate(j) > dk){
                    sSecondo.add(j);
                }
            }

            if(sSecondo.isEmpty()){
                break;
            }else{
                int djPrimo = jobSet.getDueDate(sSecondo.getFirst());
                for(Integer j : sSecondo){
                    if(jobSet.getDueDate(j) < djPrimo){
                        djPrimo = jobSet.getDueDate(j);
                    }
                }
                dk = djPrimo;
            }
        }
        return restrictions;
    }

    private Solution shortcutResolution(SubSet s, int t){
        List<Integer> earlierDeadline =  sortByNewDueDate(createEarlierDeadline(s,t));
        List<Integer> inducedOrderLaterDeadLine = sortByNewDueDate(createLaterDeadline(s,t));

        /*Applicabile theorem 4*/
        if(precondTheorem4(s.jobList, t)){
            System.out.println("teorema 4 normal");
            Solution solution = new Solution();
            solution.tardiness = jobSet.calculateTardiness(s.jobList,t);
            solution.jobs = s.jobList;
            return solution;
        }

        if(precondTheorem4(earlierDeadline,t)){
            System.out.println("teorema 4 earlier");
            Solution solution = new Solution();
            solution.tardiness = jobSet.calculateTardiness(earlierDeadline,t);
            solution.jobs = earlierDeadline;
            return solution;
        }

        if(precondTheorem4(inducedOrderLaterDeadLine,t)){
            System.out.println("teorema 4 later");
            Solution solution = new Solution();
            solution.tardiness = jobSet.calculateTardiness(inducedOrderLaterDeadLine,t);
            solution.jobs = inducedOrderLaterDeadLine;
            return solution;
        }

        /*Applicabile theorema 5*/
        if(precondTheorem5(s.jobList, t)){
            System.out.println("teorema 5 normal");
            Solution solution = new Solution();
            solution.tardiness = jobSet.calculateTardiness(s.jobList,t);
            solution.jobs = s.jobList;
            return solution;
        }

        if(precondTheorem5(earlierDeadline,t)){
            System.out.println("teorema 5 earlier");
            Solution solution = new Solution();
            solution.tardiness = jobSet.calculateTardiness(earlierDeadline,t);
            solution.jobs = earlierDeadline;
            return solution;
        }

        if(precondTheorem5(inducedOrderLaterDeadLine,t)){
            System.out.println("teorema 5 later");
            Solution solution = new Solution();
            solution.tardiness = jobSet.calculateTardiness(inducedOrderLaterDeadLine, t);
            solution.jobs = inducedOrderLaterDeadLine;
            return solution;
        }

        return null;
    }

    private HashMap<Integer, Integer> createEarlierDeadline(SubSet s, int t) {
        HashMap<Integer, Integer> earlierDeadline = new HashMap<>();
        List<Integer> ordered = s.jobList.stream().
                        sorted((i,j) -> jobSet.getProcessingTime(i) - jobSet.getProcessingTime(j)).
                        toList();
        for(int k = ordered.size(); k > 0; k --){
            int job = ordered.get(k - 1);
            earlierDeadline.put(job, jobSet.getDueDate(job));
            while (true){
                List<Integer> sK = ordered.stream().
                        filter(actualJob -> jobSet.getDueDate(actualJob) >= earlierDeadline.get(job) &&
                                jobSet.getProcessingTime(actualJob) > jobSet.getDueDate(job)).
                        toList();

                int cK = t + s.jobList.stream().
                                        filter(elem -> !sK.contains(elem)).
                                        map(jobSet::getProcessingTime).
                                        reduce(0, Integer::sum);

                if(cK < earlierDeadline.get(job)){
                    earlierDeadline.put(job, cK);
                }else{
                    break;
                }
            }
        }
        return earlierDeadline;
    }

    private HashMap<Integer, Integer> createLaterDeadline(SubSet s, int t) {
        HashMap<Integer, Integer> deadLine = new HashMap<>();

        for(int k : s.jobList){
            if(k == s.jobList.getFirst())deadLine.put(k, jobSet.getDueDate(k));
            deadLine.put(k, jobSet.getDueDate(k));
            while (true){
                List<Integer> sK = new ArrayList<>();
                for(Integer j : s.jobList){
                    if(jobSet.getDueDate(j) <= deadLine.get(k) && jobSet.getProcessingTime(j) < jobSet.getProcessingTime(k)){
                        sK.add(j);
                    }
                }
                int cK = t + jobSet.getProcessingTime(k) + sK.stream().map(jobSet::getProcessingTime).reduce(0, Integer::sum);
                if(cK > deadLine.get(k)){
                    deadLine.put(k, cK);
                }else{
                    break;
                }
            }
        }

        return deadLine;
    }

    private List<Integer> sortByNewDueDate(HashMap<Integer, Integer> deadLine){
        return deadLine.
                entrySet().
                stream().
                sorted(Map.Entry.comparingByValue()).
                map(Map.Entry::getKey).
                toList();
    }

    private boolean precondTheorem4(List<Integer> jobList, int t){
        /*non-increasing order*/
        List<Double> ratio = jobList.stream().map(jobSet::getRatioWeigthOnProcessingTime).toList();
        for(int i = 0; i < ratio.size() - 1; i++){
            if( ratio.get(i) < ratio.get(i + 1)){
                return false;
            }
        }

        /*check if all tardy*/
        int time = t;
        boolean allTardy = true;
        for(Integer job : jobList){
            time += jobSet.getProcessingTime(job);
            if(time <= jobSet.getDueDate(job)){
                allTardy = false;
            }
        }
        return allTardy;
    }

    private boolean precondTheorem5(List<Integer> jobList, int t){
        int time = t;
        int countTardy = 0;
        for(Integer job : jobList){
            time += jobSet.getProcessingTime(job);
            if(time > jobSet.getDueDate(job)){
                countTardy++;
            }
        }

        return countTardy <= 1;
    }

    public static void main(String[] args) {
        Table t = new Table();
        t.initialize();

        SubSet s = t.jobSet.generateSubset(1, JobSet.nElem,-1);
        System.out.println("---------------");
        Solution solution = t.resolve(s, 0);
        System.out.println("Total Tardiness: " + solution);
        System.out.println("tardiness: " + t.jobSet.calculateTardiness(solution.jobs,0));
        System.out.println("Table Hit: " + t.countTableHit);
        System.out.println("Table Miss: " + t.countTableMiss);
        System.out.println("Equation Resolved: " + t.countEqResolved);
        System.out.println("Shortcutted Solutions: " + t.countShortcuttedSolution);

//        SubSet ss = t.jobSet.generateSubset(7,8,3);
//        System.out.println(t.createLaterDeadline(ss,662));
//        System.out.println(t.sortByNewDueDate(t.createLaterDeadline(ss,662)));
//        System.out.println(t.shortcutResolution(ss,662));
    }
}

/*
* Con la restrizione di delta risolviamo 110 equazioni
* senza ne risolve 5769
*
* le computazioni nuove effettuate(table miss) sono 2475, quelle risparmiate (table hit) 9064
* */
