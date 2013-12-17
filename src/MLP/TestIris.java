
/* TestIris.java
 *
 * Test sul dataset Iris
 */

package MLP;

import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class TestIris {
    
     static PrintStream out = System.out;
    
    public static void main(String argv[]) throws Exception {
        
        DataSet lset = new DataSet();        
        lset.readFromCSV("src/Tv-lcd_lset");
        
        
        double[][] nf= lset.computeNormalizeFactors();
        
        lset.normalizeData(nf);
               
        DataSet vset = new DataSet();        
        vset.readFromCSV("src/Tv-lcd_vset");
        vset.normalizeData(nf);

        
        
        MLP net = new MLP(new int[] {4,2,2}, //c'era 4,4,3 ci abbiamo messo uno perchè deve riconoscere solo il frigo
                new Class[] {HypTanNeuron.class,
        		             SigmoidNeuron.class,
                             SigmoidNeuron.class});
        net.lrate = 0.1;
        net.Ecrit = 0.001;
        net.maxepochs = 20000;
                
        net.trainBatch(lset,out);
         
        out.println("\n--- Simulazione sul Learning Set---");
        printResults(lset,net);
 
        out.println("\n--- Simulazione sul Test Set---");
        printResults(vset,net);
        
        FileOutputStream fo = new FileOutputStream("trained.net");
        ObjectOutputStream os = new ObjectOutputStream(fo);
        os.writeObject(net);
        os.close();
        
        FileOutputStream fo_nf = new FileOutputStream("trained.nf");
        ObjectOutputStream os_nf = new ObjectOutputStream(fo_nf);
        os_nf.writeObject(nf);
        os_nf.close();
                
        FileInputStream fi = new FileInputStream("trained.net");
        ObjectInputStream is = new ObjectInputStream(fi);
        net = (MLP)is.readObject();
       
        out.println("\n--- Simulazione sul Test Set (rete riletta) ---");
        printResults(vset,net);
              
    }
    
    public static void printResults(DataSet ds, MLP net) {
        double E = 0;
        int nerr = 0;
        for(int i=0;i<ds.x.length;i++) {
            double outx[] = net.runMLP(ds.x[i]);
            
            double dist=0;
            double maxD=Double.NEGATIVE_INFINITY,maxMLP=Double.NEGATIVE_INFINITY;
            int cD=0,cMLP=0;
            
            for(int j=0;j<outx.length;j++) {
                double diff;
                diff = outx[j]-ds.d[i][j];
                dist+=diff*diff;
                if(outx[j]>maxMLP) {
                    maxMLP=outx[j];
                    cMLP=j;
                }
                if(ds.d[i][j]>maxD) {
                    maxD=ds.d[i][j];
                    cD=j;
                }
            }
            E+=dist;
            out.print("Esempio "+i+": "+dist);
            if(cMLP!=cD) {
                nerr++;
                out.println(" KO");
            } else
                out.println(" OK");
            
       }
       out.println("** Costo totale "+E+" Errori "+nerr); 
    }
   
}
