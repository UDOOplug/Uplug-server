package MLP;


import java.io.*;
import java.util.ArrayList;

public class DataSet {
    
    public double x[][]; // feature vectors
    public double d[][]; // target vectors

    public DataSet() {}
    
    
    public void unlabeledFromArray(double fv[][]) {
            x = fv.clone();
    }

    public void labeledFromArray(double fv[][], double tg[][]) {
       x = fv.clone();
       d = tg.clone();
    }

    String VectorToString(double v[]) {
        int j;
        String s = "[";
        for(j=0;j<v.length-1;j++)
            s+= v[j]+",";
        s+= v[j]+"]";
        return s;
    }

    public String XiToString(int i) {
        return VectorToString(x[i]);
    }

    public String DiToString(int i) {
        return VectorToString(d[i]);
    }
    
    public double[][] computeNormalizeFactors() {
        double[][] factor = new double[2][x[0].length];
        int dim = x[0].length;
        
        for(int i=0;i<dim;i++) {
            for(int j=0;j<x.length;j++) {
                factor[0][i] += x[j][i];
                factor[1][i] += x[j][i]*x[j][i];
            }
            factor[0][i] /= x.length;
            factor[1][i] /= x.length;
            factor[1][i] -= factor[0][i]*factor[0][i];
            factor[1][i] = Math.sqrt(factor[1][i]);
        }
        return factor;
    }
    
    public void normalizeData(double[][] f) {
        int dim = f[0].length;
        
        for(int i=0;i<x.length;i++)
            for(int j=0;j<x[i].length;j++)
                x[i][j] = (x[i][j]-f[0][j])/f[1][j];
    }

    double[] getCSVvector(String s) {
        String[] vi = s.split(",");
        double[] v = new double[vi.length];
        
        for(int i=0;i<vi.length;i++)
           v[i]=Double.parseDouble(vi[i]); 
        
        return v;
    }
    
    
    public void readFromCSV(String filename) throws IOException {

        ArrayList tempx = new ArrayList();
        ArrayList tempd = new ArrayList();
        double[] xt;
        
        File f = new File(filename);
        BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                            new FileInputStream(f)));
       
        int xdim = 0;
        int ddim = 0;
        int ndat = 0;
        
        int line = 1;
        String lin = in.readLine();
        while(lin!=null){
            lin=lin.trim();
            if(lin.length()==0) {
                lin=in.readLine();
                line++;
                continue;
            }
            
            String vs[] = lin.split(";");
            
            try{
                xt = getCSVvector(vs[0]);
            } catch(NumberFormatException e) {
                throw new IOException("File: "+filename+" (line "+
                        line+"): Number Format Error");
            }
            
            if(xdim<1)
                xdim = xt.length;
            else if(xt.length!=xdim)
                throw new IOException("File: "+filename+" (line "+
                        line+"): feature vector dimension mismatch (expected "
                        +xdim+" found "+xt.length+")");
            
            tempx.add(xt);
            
            if(vs.length>1) {
                if((ndat>0)&&(ddim==0)) 
                    throw new IOException("File: "+filename+" (line "+
                        line+"): labeled data in unlabeled dataset!");
                

                try{
                    xt = getCSVvector(vs[1]);
                } catch(NumberFormatException e) {
                    throw new IOException("File: "+filename+" (line "+
                                        line+"): Number Format Error");
                }

                if(ddim<1)
                   ddim = xt.length;
                else if(xt.length!=ddim)
                   throw new IOException("File: "+filename+" (line "+
                        line+"): target vector dimension mismatch (expected "
                        +ddim+" found "+xt.length+")");
                
                tempd.add(xt);
            } else if(ddim>0) { 
                throw new IOException("File: "+filename+" (line "+
                        line+"): target vector missing");
            }
            ndat++;
            lin=in.readLine();
            line++;
        }
        
        x = new double[tempx.size()][];
        for(int i=0;i<x.length;i++)
            x[i]=(double [])tempx.get(i);
        
        if(ddim>0) {
            d = new double[tempd.size()][];
            for(int i=0;i<d.length;i++)
                d[i]=(double [])tempd.get(i);
        }
        
    }
}