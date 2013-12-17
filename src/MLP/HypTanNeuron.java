package MLP;


public class HypTanNeuron extends Neuron {
       
   /** Creates a new instance of HypTanNeuron */
   public HypTanNeuron(int nin) {
       super(nin);

   }
   
   
   public double x(double xin[]) {
       double a=b;
       for(int i=0;i<w.length;i++)
           a+=xin[i]*w[i];
       return (Math.tanh(a));
   }


   public void fw(Neuron in[]) {
       double a=b;
       for(int i=0;i<w.length;i++)
           a+=in[i].out*w[i];
       out = Math.tanh(a);
   }

   public void bp(Neuron[] in) {
     
       delta *= (1-out*out); 
    
       for(int i=0; i<w.length;i++) {
           in[i].delta += delta*w[i];
           g[i] += delta*in[i].out;
       }
   
       db += delta;
   }
}

