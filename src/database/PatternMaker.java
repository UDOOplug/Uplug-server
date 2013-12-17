package database;

import java.sql.SQLException;
import java.util.ArrayList;

public class PatternMaker {
   
    private String hour_begin;
    private String hour_end;
    private int numberOfSample; // insert the number of sample
    private int timeInterval; //in minute
    private MySQL mySQL = null;
   
   
    public PatternMaker(){      
        try {
            mySQL = new MySQL("erika", "presaerika"); //user e password
        } catch (Exception e1) {       
            e1.printStackTrace();
        }      
    }

   
    
    
    
    public float[] getPatternArray(String inizio, String fine, int numberOfSample){
        this.hour_begin=inizio;
        this.hour_end=fine;
        this.numberOfSample=numberOfSample;
        float builtPattern[] = null;      
               
        String query_time_interval;
        query_time_interval="SELECT PotenzaUno FROM PresaIntel WHERE TimeStamp >= ( '";
        query_time_interval+=hour_begin;
        query_time_interval+="' ) AND TimeStamp <= ( '";
        query_time_interval+=hour_end;
        query_time_interval+="' )";
       
        System.out.println(query_time_interval);        
        String timeIntervalPower =" ";
       
        try {
            timeIntervalPower=mySQL.getDataTimeInterval(query_time_interval);       
            System.out.println(timeIntervalPower);
           
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        ArrayList<Float> power= new ArrayList<Float>();       
        String[] string_power=timeIntervalPower.split("\n");
              
        float temp;
       
        for(int i=0;i<string_power.length;i++){           
            temp=0;
            temp=Float.parseFloat(string_power[i]);
            power.add(temp);
        }
       
        for(int j=0;j<power.size();j++){     
            System.out.println(power.get(j));
        }
         
        float length_array=power.size();
        int length_sample;
        length_sample=(int)(length_array / numberOfSample);
        int index;
        float[] sum=new float[numberOfSample];
       
        for(int j=0;j<numberOfSample; j++){           
        	for(int i=0; i<length_sample; i++){
        		index= i+j*length_sample;
        		sum[j]+= power.get(index);
        		index=0;
        	}
        }
       
        builtPattern = new float[numberOfSample];
       
        for(int k=0;k<sum.length; k++){           
            builtPattern[k]=(sum[k] / length_sample);
        }
       
        System.out.println("--- averages pattern: ");
        for(int i=0; i<builtPattern.length;i++){
            System.out.println(builtPattern[i]);
        }
       
        return builtPattern;
    }
    
  
    
    
    

    public float[] getPatternConOrario(String start, String finish, int timeInterval){
       
        this.hour_begin=start;
        this.hour_end=finish;
        this.timeInterval=timeInterval; // in minuti
        float pattern[] = null;
        String query_TimeInterval;
        query_TimeInterval="SELECT PotenzaUno FROM PresaIntel WHERE TimeStamp >= ( '";
        query_TimeInterval+=hour_begin;
        query_TimeInterval+="' ) AND TimeStamp <= ( '";
        query_TimeInterval+=hour_end;
        query_TimeInterval+="' )";
       
        System.out.println(query_TimeInterval);
       
        String timeinterval_power =" ";
       
        try {
            timeinterval_power=mySQL.getDataTimeInterval(query_TimeInterval);       
            System.out.println(timeinterval_power);
           
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Float> potenzauno = new ArrayList<Float>();       
        String[] string_power = timeinterval_power.split("\n");
              
        float temp;
       
        for(int i=0;i<string_power.length;i++){           
            temp=0;
            temp=Float.parseFloat(string_power[i]);
            potenzauno.add(temp);
        }
       
        for(int j=0;j<potenzauno.size();j++){          
            System.out.println(potenzauno.get(j));
        }
       
        int length_array = potenzauno.size();
    
        // We divide the time interval in blocks of five second, 
        // because arduino is programmed to read data every five second.
        int length_sigle_interval=(timeInterval*60)/5;
        int n_sample=(length_array / length_sigle_interval);
        if(n_sample<1){          
            System.out.println("error, you have insert a time interval too big.");
        }

        int index;
        float[] sum=new float[n_sample];
       
        for(int j=0;j<n_sample; j++){           
        	for(int i=0; i<length_sigle_interval; i++){
        		index= i+j*length_sigle_interval;
        		sum[j]+= potenzauno.get(index);
        		index=0;
        	}
        }
       
        pattern=new float[n_sample];
       
        for(int k=0;k<sum.length; k++){           
            pattern[k]=(sum[k] / length_sigle_interval);
        }
       
        System.out.println("averages pattern on time intervals: " + timeInterval + " sec.");
        for(int i=0; i<pattern.length;i++){
            System.out.println(pattern[i]);
        }
           
        return pattern;
    }

}