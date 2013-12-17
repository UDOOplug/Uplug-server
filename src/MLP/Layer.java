/*
 * Layer.java
 *
 * Implementazione di un Layer di neuroni dello stesso tipo 
 *
 */

package MLP;

import java.io.Serializable;


public class Layer implements Serializable {
  
    Neuron n[];
 
    public Layer(int neurons, Class neuronModel, int inputs)
      throws Exception {
        n = new Neuron[neurons];
        java.lang.reflect.Constructor nc =
                neuronModel.getConstructor(new Class[] {int.class});
        for(int i=0;i<neurons;i++)
            n[i] = (Neuron)nc.newInstance(new Integer(inputs));
    }
    
   
    public void forward(Layer inputLayer) {
        Neuron[] inputN = inputLayer.n;
        for(int i=0;i<n.length;i++)
            n[i].fw(inputN);
    }


    public void backward(Layer inputLayer) {
        Neuron[] inputN = inputLayer.n;
        for(int i=0;i<n.length;i++)
            n[i].bp(inputN);
    }
    
    public void resetGW() {
         for(int i=0;i<n.length;i++)
            n[i].resetGW();
    }
 
    
    public void newG() {
         for(int i=0;i<n.length;i++)
            n[i].newG();
    }
    
    
    public void updateW(double eta) {
        for(int i=0;i<n.length;i++)
            n[i].updateW(eta);
    }
    
    
    public void resetDelta() {
        for(int i=0;i<n.length;i++)
            n[i].delta=0;
    }
    
}

