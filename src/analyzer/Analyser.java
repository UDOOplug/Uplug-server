package analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

import main.CoreMain;
import recogniser.PreProcessing;
import recogniser.RecogniseMain;
import sharedDatas.SharedDatas;
import database.MySQL;

public class Analyser {
	
	public final static boolean DEBUG = true;	
	private int debughour             = 0;
	private int debugminute           = 0;
	private int debugsecond           = 0;
	
	ArrayList<Float> average_day        = new ArrayList<Float>();
	ArrayList<Float> hour_turn_on       = new ArrayList<Float>(); 
	ArrayList<Float> average_half_hour  = new ArrayList<Float>(); 

    ArrayList<Float>   array_power_day  = new ArrayList<Float>(); 
    ArrayList<Integer> array_hour_day   = new ArrayList<Integer>();
    ArrayList<Integer> array_minute_day = new ArrayList<Integer>();
    ArrayList<Float>   array_halfhour   = new ArrayList<Float>(); 
    
    int previousHour             = -1;
    int previousMinute           = -1;
    int number_data_half_hour    = 0;
    double temporaryPowerAverage = 0;
     
    private SharedDatas dataShared = null;      
    private static MySQL mySQL = null;
    
	public Analyser( SharedDatas datiCondivisi ){
		super();
		
		this.dataShared = datiCondivisi;
	
		System.out.print("Opening database connection... ");		
		try {
			mySQL = new MySQL(CoreMain.databaseUser, CoreMain.databasePassword); //user e password
			System.out.println("COMPLETED");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		//ScannerAverage();			
		previousHour = getHour();
		previousMinute = getMinute();
				
		for(int i=0; i<48; i++){
			float d=0;
			array_halfhour.add(d);
		}		
	}
	
	public void manageNewReadedPower(Float power){
		
		// Save new power in shared 
		dataShared.currentPower(power);
		
		// save power in mysql database
		try {
			mySQL.insertDataFromSerialPort(power, 0.0f);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//debug();
		if(DEBUG)
			TimeTestDebug();
		
		int currentMinute  = getMinute();
		int currentHour    = getHour();
		
		array_power_day.add(power);
		array_hour_day.add(currentHour);
		array_minute_day.add(currentMinute);
		
		temporaryPowerAverage += power;
		number_data_half_hour++;
		
		if( (previousMinute == 29 && currentMinute == 30) ||
				(previousMinute == 59 && currentMinute == 00)){
					
			manageChangeHalfHour(currentHour, currentMinute);
		}
		
		if( previousHour == 23 && currentHour == 00 ){					
			manageChangeDay();
		}
		
		previousMinute = currentMinute;
		previousHour = currentHour;
					
	}
	/*
	 * Inizialize half hour array
	 */
	private void manageChangeHalfHour(int hour, int minute){
		int indexHalfHour = hour * 2;
		if(minute >= 30 && minute <= 59)
			indexHalfHour++;
		
		float average =(float) temporaryPowerAverage / number_data_half_hour;
		
		array_halfhour.set(indexHalfHour, average);
		
		temporaryPowerAverage = 0;
		number_data_half_hour = 0;
	}
	
	private void manageChangeDay(){		

		float averageTodayPower = PreProcessing.AverageOfToday(array_power_day);
		float hourTurnOnToday   =(float) PreProcessing.HourTurnOn(array_power_day);
		
		average_day.add(averageTodayPower);		
		dataShared.setAverage30days(average_day);
		
		if(average_day.size()==0){
			System.out.println("There isn't average of previou day...");
		}
		else{
			int compare = comparePowerToday(average_day, averageTodayPower);
			dataShared.setComperisonAverage(compare);
			
			ArrayList<Float> comparisonWeightedAverage = CompareWeightedAverage(average_day,array_power_day);
			dataShared.setComparisonWeightedaverage(comparisonWeightedAverage);			
			
			if(average_day.size() == 30){
				average_day.remove(0); 
			}
		}
						
		
		try {
			dataShared.setClassification(RecogniseMain.recognise(array_hour_day, array_power_day, array_power_day));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		hour_turn_on.add(hourTurnOnToday);
		
		if(hour_turn_on.size() == 30)
			hour_turn_on.remove(0);
		
		dataShared.setHourTurnOn(hour_turn_on);
	
		for(int i=0;i<48;i++){			
			average_half_hour.set(i, average_half_hour.get(i)+ array_halfhour.get(i).floatValue()/2);
		}
		
		dataShared.setAverageHalfHour(average_half_hour);		
		
		for(int i=0;i<48;i++){
			float d=0;
			array_halfhour.set(i, d);
		}
		
		for(int i=0;i<array_power_day.size();i++){			
			array_power_day.set( i , (float) 0.0);
		}
		
	}
	

	public static void insertLabel(int label){
		try {
			mySQL.insertLabel(label);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public int comparePowerToday(ArrayList<Float> averages,float average_power_today	){
				
		if(averages.size() < 1)
			return -1;
					
		if(averages.get(averages.size()-1) > average_power_today){
			System.out.println("Good! You have save energy...");
			return 1;
		}
		if(averages.get(averages.size()-1) < average_power_today){
			System.out.println("Bad! You haven't save energy");
			return 2;
		}
	
		return -1;
	}
	
	
	public ArrayList<Float> CompareWeightedAverage(ArrayList<Float> averages, ArrayList<Float> array_power){
		ArrayList<Float> compareWeightedAverage = new ArrayList<Float>();
		float averageToday=PreProcessing.AverageOfToday(array_power);
		float weighted_average_of5days;
		float result;
		
		if(averages.size() > 5){
			int size=averages.size();
			weighted_average_of5days=(averages.get(size-1)*40+
					              averages.get(size-2)*30+
					              averages.get(size-3)*20+
					              averages.get(size-4)*10)/(float)100;
			
			System.out.println("the daily average is :"+ array_power);
			
			if(weighted_average_of5days < averageToday){
				System.out.println("Today you have consumpt more than the weight average of the last 5 days.");
				result=1;
			} else{
				System.out.println("Today you have consumpt less than the weight average of the last 5 days.");
				result=2;
			}	
		}else{			
			System.out.println("There aren't enought data, You need five day of monitoring...");
			weighted_average_of5days=0;
			result=0;
		}
					
		compareWeightedAverage.add(weighted_average_of5days);
		compareWeightedAverage.add(averageToday);
		compareWeightedAverage.add(result);
		
		return compareWeightedAverage;
	}
	
	
	public void ComparisonVectorAverages(ArrayList<Float> halfHour_average, ArrayList<Float> array_halfHour){
	
		int count  = 0;
		int count2 = 0;
		
		for(int i=0;i<48;i++){
			if(halfHour_average.get(i)<array_halfHour.get(i)){
				count++;
			}else{ 
				count2++;
			}
		}
	}
	
	
	//this metod allows to use the previous test data that we have in our database
	public ArrayList<ArrayList<Float>> ScannerAverage() throws IOException {
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Upload previous month test data ...");		
		
		String input;
		if(CoreMain.useDefaultPath)			
			input = "data/Vettori_medie_frigo";		
		else
			input = scanner.nextLine();
		
		File name = new File(input);
        ArrayList<ArrayList<Float>> arrayReturn = new ArrayList<ArrayList<Float>>();
		
		
		BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                            new FileInputStream(name)));
       
        String lin="";
        String vs[];
        float temp2;
              
        for(int i=0;i<3;i++){
    	
        	lin = in.readLine();
            try {
				lin=lin.trim();
			} catch (Exception e) {
				break;
			}
                                   
            vs = lin.split(",");
	        int lengthString = vs.length;
	        
		    if(i==0){
			    for(int j=0;j<vs.length;j++){
			 	    try{ 		   
			 		    temp2=Float.parseFloat(vs[j]);
			 	        average_day.add(temp2);
			 	        
			 	    } catch (NumberFormatException e) {
			 		   System.out.println("Error in string to float conversion");
			        }
			    }
		    }	         
	    	if(i==1){
	    		for(int j=0;j<vs.length;j++){
		    	    try{
		    			 temp2=Float.parseFloat(vs[j]);
		    		     hour_turn_on.add(temp2);
		    		} catch (NumberFormatException e) {
		    			 System.out.println("Error in string to float conversion");
		    		}
		    	}
		    }   
	    	if(i==2){
		    	for(int j=0;j<vs.length;j++){
		    		try{
		    			temp2=Float.parseFloat(vs[j]);
		    		    average_half_hour.add(temp2);
		    		} catch (NumberFormatException e) {
		    			 System.out.println("Error in string to float conversion");
		    		}
		    	}
		    }   
        }
        
		arrayReturn.add(average_day);
		arrayReturn.add(hour_turn_on);
		arrayReturn.add(average_half_hour);
		
		System.out.println("COMPLETED - days: "+average_day.size());
		
		dataShared.setAverage30days(average_day);
		dataShared.setHourTurnOn(hour_turn_on);
		dataShared.setAverageHalfHour(average_half_hour);
        
        return arrayReturn;
	}
	
	private int getHour(){
		if(DEBUG) return debughour;
		
		Calendar calendar = new GregorianCalendar();
		String time;
		int hours = calendar.get(Calendar.HOUR);
		  
		return hours;
	}
	
	private int getMinute(){
		if(DEBUG) return debugminute;
				
		Calendar calendar = new GregorianCalendar();
		String time;
		int minute = calendar.get(Calendar.MINUTE);
					
		return minute;
	}
		
	private void TimeTestDebug(){
		debugsecond++;
		
		if(debugsecond == 60){
			debugsecond = 0;
			debugminute++;
		}
		
		if(debugminute == 60){
			debugminute = 0;
			debughour++;
			System.out.println("cambiooo");
		}
		
		if(debughour == 24){
			debughour = 0;			
		}				
	}
		
}
