/*
 * Neuron.java
 *
 * Classe base per implementare modelli di neuroni compatibili
 * con l'algoritmo backpropagation
 */

package MLP;

import java.io.Serializable;

public abstract class Neuron  implements Serializable {
  
	double w[];
    transient double g[];

    double b;
    transient double db; 
    
    transient double out;
    
    transient double delta; 
    transient double rndMin=-0.01;
    transient double rndMax=0.01;
    transient double range = rndMax-rndMin;
    
    
    public Neuron(int nin) {
        w = new double[nin];
        WRandomInit();
    }

    abstract public double x(double xin[]);
    
    
    public void WRandomInit(){
        for(int i=0;i<w.length;i++)
            w[i]=rndMin+Math.random()*range;
        b=rndMin+Math.random()*range;
    }
    

    public void setRndInterval(double min, double max){
        rndMax=max;
        rndMin=min;
        range=max-min;
    }

    public void setW(double newB, double newW[]) {
        w = newW.clone();
        b = newB;
    }
    
 
    abstract public void fw(Neuron in[]);
    
    public abstract void bp(Neuron[] in);
    
    public void updateW(double eta) {
        for(int i=0;i<w.length;i++)
            w[i] -= eta*g[i];
               b -= eta*db;       
    }
    
    public void resetGW() {
        for(int i=0;i<w.length;i++)
            g[i]=0;
        db=0;
    }

    
    public void newG() {
        g = new double[w.length];
    }

}