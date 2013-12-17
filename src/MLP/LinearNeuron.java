/*
 * LinearNeuron.java
 *
 * Modello di neurone con combinazione lineare degli ingressi
 * e funzione di uscita lineare
 */

package MLP;

public class LinearNeuron extends Neuron {
   
    /** Creates a new instance of LinearNeuron */
    public LinearNeuron(int nin) {
        super(nin);
    }

    public double x(double xin[]) {
        double a=b;
        for(int i=0;i<w.length;i++)
            a+=xin[i]*w[i];
        return a;
    }

    public void fw(Neuron in[]) {
        double a=b;
        for(int i=0;i<w.length;i++)
            a+=in[i].out*w[i];
        out = a;
    }

    public void bp(Neuron[] in) {
        for(int i=0; i<w.length;i++) {
             // backprop
            in[i].delta += delta*w[i];
            g[i] += delta*in[i].out;
        }
     
        db += delta;
    }
}

