/* MLP.java
 *
 * implementa una rete MLP con un numero qualsiasi di strati
 * Lo strato 0 � lo strato di ingresso e non viene aggiornato
 * per cui pu� essere di qualsiasi tipo.
 * L'ultimo strato � lo strato di uscita.
 *
 */

package MLP;

import java.io.PrintStream;
import java.io.Serializable;

public class MLP implements Serializable {
   
    
    Layer[] l;
    
    int indim;
    int outdim;
    
    
    //learning rate
    double lrate=0.01;
    
    double Ecrit = 0.01;   
    int maxepochs = 10000; 
    
    
    public MLP(int[] neuronXlayer, Class[] layerType) throws Exception {
        l = new Layer[neuronXlayer.length];

        l[0] = new Layer(neuronXlayer[0],layerType[0],0);
        indim = neuronXlayer[0];
        
        for(int i=1;i<l.length;i++)
            l[i] = new Layer(neuronXlayer[i],layerType[i],neuronXlayer[i-1]);

        outdim = neuronXlayer[l.length-1];
    }
    
    
    public void forward(double[] in) {
        for(int i=0;i<l[0].n.length;i++)
            l[0].n[i].out=in[i];
    
        for(int i=1;i<l.length;i++)
            l[i].forward(l[i-1]);
    }
    
   
    public void backward() {
        resetHiddenDelta();
        
        for(int i=l.length-1;i>0;i--)
            l[i].backward(l[i-1]);
    }
    
    double computeTargetError(double[] d) {
        Neuron[] outx = l[l.length-1].n;
        double e=0,ei=0;
        
        for(int i=0;i<outx.length;i++) {
        	
            ei = outx[i].out-d[i];
            e += ei*ei;
            outx[i].delta = ei;
        }
        return e;
    }
    
    public void trainBatch(DataSet ls, PrintStream log) {
        boolean loop = true;
        double E=0;
        int epoch=0;
        
        newG();
        
        while(loop) {
            resetGW();
            E = 0;
                        
            for(int e=0;e<ls.x.length;e++) {
                forward(ls.x[e]);
                E += computeTargetError(ls.d[e]);
                backward();
            }
            
            E /= ls.x.length;
            
            if(E<=Ecrit) break;
            if(epoch>maxepochs) break;

            updateW(lrate);
            epoch++;
        }
    }
    
    /*
     * Metodo per calcolare il vettore di uscita
     * dato quello di ingresso
     *
     **/
    
    public double[] runMLP(double[] in) {
        // copia l'inresso nello strato 0'
        for(int i=0;i<l[0].n.length;i++)
            l[0].n[i].out=in[i];
    
        // esegue il forward sulla rete
        for(int i=1;i<l.length;i++)
            l[i].forward(l[i-1]);
        
        // crea il vettore di uscita copiandolo
        // dalllo strato di uscita
        double out[] = new double[outdim];
        Neuron[] outn = l[l.length-1].n;
        
        for(int i=0;i<outn.length;i++)
            out[i] = outn[i].out;
        
        return out;
    }
    
    /*
     * Azzera il gradiente per tutti i pesi
     **/
    
    public void resetGW() {
        for(int i=1;i<l.length;i++)
            l[i].resetGW();
    }
    

    /*
     * Alloca il gradiente per tutti i pesi
     */
    
    public void newG() {
        for(int i=1;i<l.length;i++)
            l[i].newG();
     }

    /*
     * aggiorna tutti i pesi
     */
    
    public void updateW(double eta) {
        for(int i=1;i<l.length;i++)
            l[i].updateW(eta);
    }
    /*
     * azzera gli errori dei neuroni degli strati
     * nascosti (sono esclusi solo lo strato di uscita
     * e quello di ingresso)
     */
    
    public void resetHiddenDelta() {
        for(int i=1;i<l.length-1;i++)
            l[i].resetDelta();
    }
}
