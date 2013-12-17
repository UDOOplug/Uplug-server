/*
 * SigmoidNeuron.java
 *
 * Modello di neurone con combinazione lineare degli ingressi
 * e funzione di uscita sigmoidale (funzione logistica)
 */

package MLP;

public class SigmoidNeuron extends Neuron {

    
    /** Creates a new instance of SigmoidNeuron */
    public SigmoidNeuron(int nin) {
        super(nin);
    }
    
   
    public double x(double xin[]) {
        double a=b;
        for(int i=0;i<w.length;i++)
            a+=xin[i]*w[i];
        return (1.0/(1.0+Math.exp(-a)));
    }
    

    public void fw(Neuron in[]) {
        double a=b;
        for(int i=0;i<w.length;i++)
            a+=in[i].out*w[i];
        out = 1.0/(1.0+Math.exp(-a));
    }

    
    public void bp(Neuron[] in) {
      
        delta *= out*(1-out); 
     
        for(int i=0; i<w.length;i++) {
            in[i].delta += delta*w[i];
            g[i] += delta*in[i].out;
        }
     
        db += delta;
    } 
}
