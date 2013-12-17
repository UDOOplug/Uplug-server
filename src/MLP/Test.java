/* Test.java
 *
 * Test per un singolo neurone
 */

package MLP;

import java.io.PrintStream;

public class Test {
    
    static PrintStream out = System.out;
    
    public static void main(String argv[]) throws Exception {
        DataSet lset = new DataSet();        
        lset.labeledFromArray(new double[][] {{0,0},{1,0},{0,1},{1,1}},
                new double[][] {{-1},{+1},{+1},{-1}});
        
        MLP net = new MLP(new int[] {2,2,1},
                new Class[] {HypTanNeuron.class,
                             HypTanNeuron.class,
                             HypTanNeuron.class});
       
        net.lrate = 0.1;
        net.Ecrit = 0.001;

        net.trainBatch(lset,out);
        
        System.out.println("sono quiiii"); 
       
        out.println("--- Simulazione ---");
        for(int i=0;i<lset.x.length;i++) {
            double outx[] = net.runMLP(lset.x[i]);
            out.print(lset.XiToString(i)+" x=[");
            for(int j=0;j<outx.length-1;j++)
                out.print(outx[j]+",");
            out.println(outx[outx.length-1]+"]");
       }
    }    
}
