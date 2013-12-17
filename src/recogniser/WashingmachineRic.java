package recogniser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import main.CoreMain;

import MLP.MLP;

public class WashingmachineRic {	

 	MLP net;
 	double[][] nf;	

 	public WashingmachineRic() throws ClassNotFoundException, IOException{
 				
		FileInputStream fi_nf = new FileInputStream(CoreMain.trainedNetPath + "reti_addestrate_lavatrice/trained.nf");
	    ObjectInputStream is_nf = new ObjectInputStream(fi_nf);
	    nf = (double[][])is_nf.readObject();
	
		FileInputStream fi = new FileInputStream(CoreMain.trainedNetPath + "reti_addestrate_lavatrice/trained.net");
	    ObjectInputStream is = new ObjectInputStream(fi);
	    net = (MLP)is.readObject();
	   	    
 	}
 	
 	public static double[] printSingleResult(double[] pattern, MLP net) {
        double outx[] = net.runMLP(pattern);
        
        for(int i=0; i<outx.length; i++)
        	System.out.print(outx[i]+ " --- "); 
        	System.out.println("\n");
        	System.out.print("Washingmachine answer: ");
        return outx;
    }
	
 	public boolean recognizeWashingmachine(ArrayList<Float> dataArray, ArrayList<Integer> hoursArray, ArrayList<Float> dataArray2){
 		boolean rispostaLavatrice;
        double[] out;
 		double[] pattern=PreProcessing.PatternWashingmachine(dataArray,hoursArray,dataArray2);
 		
 		pattern = normalizeData(pattern, nf);
               
 		System.out.println("\n--- Test set simulation (reloaded ANN) ---");
	    out=printSingleResult(pattern,net);
	    if (out[0]>0.95 && out[1]<0.05){
	    	rispostaLavatrice=true;
	    } else if (out[0]<0.05 && out[1]>0.95){
	    	rispostaLavatrice=false;
	    } else {
	    	System.out.println("Warning - uncertain answer: output too far from 0 and 1");
	    	rispostaLavatrice=false;
	    }
	    	    
 		return rispostaLavatrice;
 	}
 	
 	
 	public double[] normalizeData(double[] x, double[][] f) {

        for(int j=0;j<x.length;j++)
            x[j] = (x[j]-f[0][j])/f[1][j];
        
        return x;
    }
	


}
