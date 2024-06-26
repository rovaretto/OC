package unito.rovaretto_montesi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TardinessStructure {
    public TardinessStructure[][] tardyMatrix;

    public Integer[][] results;

    public SubSet initialSubset;

    public JobSet jobSet = JobSet.getJobSet();

    public Integer startingTime;

    public TardinessStructure(SubSet initialSubset, int startingTime) {
        this.initialSubset = initialSubset;
        this.startingTime = startingTime;
    }

    public void constructStructure(){
        if(initialSubset.jobList.isEmpty())return;
        if(initialSubset.jobList.size() == 1){return;}

        List<Integer> deltas = restrictionOfDelta();
        tardyMatrix = new TardinessStructure[deltas.size()][2];
        results = new Integer[deltas.size()][3];

        int kPrimo = initialSubset.getMaxJob();

        for(int i = 0; i < deltas.size(); i++){
            tardyMatrix[i][0] = new TardinessStructure(jobSet.generateSubset(initialSubset.i, initialSubset.k + deltas.get(i),kPrimo), startingTime);

            int ckPrimoDelta = calcolaCkPrimoDiDelta(tardyMatrix[i][0].initialSubset);

            results[i][1] = jobSet.weight.get(kPrimo) * Math.max(0, ckPrimoDelta - jobSet.getDueDate(kPrimo));

            tardyMatrix[i][1] = new TardinessStructure(jobSet.generateSubset(kPrimo + deltas.get(i) + 1, initialSubset.j, kPrimo), ckPrimoDelta);

        }
    }

    private Integer calcolaCkPrimoDiDelta(SubSet subSet) {
        return subSet.jobList.stream().
                map(jobSet.processingTime::get).
                reduce(this.startingTime, Integer::sum);
    }

    public List<Integer> restrictionOfDelta(){
        List<Integer> restrictions = new ArrayList<>();
        int dk = jobSet.getDueDate(initialSubset.getMaxJob());
        while (true){
            while(true){
                List<Integer> sPrimo = new ArrayList<>();

                for(Integer j : initialSubset.jobList){
                    if(jobSet.getDueDate(j) <= dk){
                        sPrimo.add(j);
                    }
                }
                int dkPrimo = this.startingTime + sPrimo.stream().
                                                        map(jobSet::getProcessingTime).
                                                        reduce(0, Integer::sum);
                if(dkPrimo > dk){
                    dk = dkPrimo;
                }else{
                    break;
                }
            }

            int maxJ = 0;
            for(Integer j : initialSubset.jobList){
                if(jobSet.getDueDate(j)<= dk && j > maxJ){
                    maxJ = j;
                }
            }
            restrictions.add(maxJ - initialSubset.getMaxJob());

            List<Integer> sSecondo = new ArrayList<>();
            for(Integer j : initialSubset.jobList){
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

    public Integer resolveStructure(){
        if(tardyMatrix == null) this.constructStructure();

        if(initialSubset.jobList.isEmpty())return 0;
        if(initialSubset.jobList.size() == 1){
            int job = initialSubset.jobList.getFirst();
            return jobSet.getWeight(job) * Math.max(0, startingTime + jobSet.getProcessingTime(job) - jobSet.getDueDate(job));
        }

        for(int i = 0; i < tardyMatrix.length; i++){
            for(int j = 0; j < tardyMatrix[i].length; j++){
                if(j == 0)
                    results[i][j] = tardyMatrix[i][j].resolveStructure();
                else
                    results[i][2] = tardyMatrix[i][j].resolveStructure();
            }
        }

        return Arrays.stream(results)
                .mapToInt(row -> Arrays.stream(row)
                        .mapToInt(Integer::intValue)
                        .sum())
                .min()
                .orElse(-1);
    }

    @Override
    public String toString() {
        return "TardinessStructure{" +
                "tardyMatrix=" + Arrays.toString(tardyMatrix) +
                ", results=" + Arrays.toString(results) +
                ", initialSubset=" + initialSubset +
                ", jobSet=" + jobSet +
                ", startingTime=" + startingTime +
                '}';
    }
}
