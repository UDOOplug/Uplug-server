package sensorReader;


import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

import main.CoreMain;

import analyzer.Analyser;


public class SensorReader extends Thread {
	
	static Analyser analyser = null; //puntatore ad analizzatore
	boolean running = true;
	float readedPower;
	int timeInterval = 1000;
	private OutputStream out = null; 
	private InputStream in = null;
	
	public SensorReader(Analyser analizzatore) throws IOException{
		this.analyser = analizzatore;	
		
		if(CoreMain.simulateFirstMonth)
			readDataFromCsvFile();
		
		if(Analyser.DEBUG)
			timeInterval = 1;
		else
			timeInterval = 1000;
	}
	
	
	
	@Override
	public void run() {
		super.run();
		
		while(running){			
			readedPower = powerFromVector();
			sendPowerToAnalizer(readedPower);
						
			try {
				Thread.sleep(timeInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}
	
	private void stopThread(){
		this.running = false;
	}
	
	private static void sendPowerToAnalizer(Float potenza){
		System.out.print(".");
		analyser.manageNewReadedPower(potenza);
	}
	
	private float generateRandomPower(){
		float d = (float) (Math.random() * 100);		
		return d;
	}
	
	int indice=0;
	ArrayList<Float> dataArray=null;
	
	private float powerFromVector(){
		if(indice<dataArray.size()-1)
		indice++;
				
		return dataArray.get(indice);
	}

	
	private ArrayList<Float> readDataFromCsvFile() throws IOException{
		
		ArrayList<Float> dataArray2;
		ArrayList<Integer> hourArray;
		ArrayList<Integer> minuteArray;
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Simulating first month of data");
		String input;
		if(CoreMain.useDefaultPath)			
			input = "data/prova";		
		else
			input = scanner.nextLine();
		
		File name = new File(input);
		ArrayList<String> nomi_file=new ArrayList<String>();
		if (name.exists())
		{
			if (name.isDirectory())
			{
				String directory[] = name.list();
				//System.out.printf("\n\nDirectory contents:\n\n");
				
				for (String directoryName : directory)
				{
					//System.out.printf("%s\n\n",directoryName);
					nomi_file.add(directoryName);					
				}
			}
		}
		else
		{
			System.out.printf("%s does not exist",input);
		}
		
		
		for(int w=0;w<nomi_file.size();w++){
			System.out.println(nomi_file.get(w));
						
			//l'arrai contenente i dati della potenza è array_dati		
			String filename=input+"/"+nomi_file.get(w);
							
			dataArray   = new ArrayList<Float>();   // power data
			dataArray2  = new ArrayList<Float>();   // smothed power data
			hourArray   = new ArrayList<Integer>(); // date (hours) string
			minuteArray = new ArrayList<Integer>(); // date (minutes) string
					
			File f = new File(filename);
	        BufferedReader in = new BufferedReader(
	                            new InputStreamReader(
	                            new FileInputStream(f)));
	       
	        String lin="";
	        String vs[];
	        String temp1;
	        float  temp2;
	        float  temp3;
	        
	        while(lin!=null){	    	
	        	lin = in.readLine();
	            try {
					lin=lin.trim();
				} catch (Exception e) {
					break;
				}
	                            
	            vs = lin.split(";");
		        int stringLenght = vs.length;
		        
		        temp1=vs[0];
		     	String[] date_hour; 
		        date_hour=temp1.split(" ");
		        String hour;
		        hour=date_hour[1];
		         
		        String[] hours_minutes_seconds;
		         
		        hours_minutes_seconds=hour.split(":");
		        int hours;
		        int minutes;
		        hours=Integer.parseInt(hours_minutes_seconds[0]);
		        minutes=Integer.parseInt(hours_minutes_seconds[1]);
		        hourArray.add(hours);
		        minuteArray.add(minutes);
		        		         
		        try {		        	
					temp2 = Float.parseFloat(vs[1]);
					dataArray.add(temp2);
				} catch (NumberFormatException e) {
					System.out.println("Error converting string to float");
				}
		        
		        try{
		        	temp3=Float.parseFloat(vs[2]);
		        	dataArray2.add(temp3);
		        }catch (NumberFormatException e) {
					System.out.println("Error converting string to float");
				}
			}
		}
		
		return dataArray;
	}	

	
	
	public synchronized void writeOnSerial(String s){
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
		try {
			bw.write(s);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} 		
	}
	
	public void connect ( String portName ) throws Exception{
		
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() ) {
            System.out.println("Error: Port is currently in use");
        }
        else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort ){
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                InputStream in = serialPort.getInputStream();
                out = serialPort.getOutputStream();
                
                serialPort.addEventListener(new SerialReader(in));
                serialPort.notifyOnDataAvailable(true);
            }
            else {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
    }
	
	
	public static class SerialReader implements SerialPortEventListener {
	    private InputStream in;
	    private byte[] buffer = new byte[1024];
	        
        public SerialReader ( InputStream in ) {
            this.in = in;
        }
	        
        public void serialEvent(SerialPortEvent arg0) {
            int data;
          
            try{
                int len = 0;
                while ( ( data = in.read()) > -1 ){
                    if ( data == '\n' ) {
                        break;
                    }
                    buffer[len++] = (byte) data;
                }
                
                String readedString = new String(buffer,0,len);
                
                // String example <55.8#0.0>                
                String[] stringarray=readedString.split("#");
                stringarray[0]=stringarray[0].replace("<", "");
                stringarray[1]=stringarray[1].replace(">", "");
                
                float[] stringafloat = new float[3];
                
                stringafloat[0]=Float.parseFloat(stringarray[0]); 
           
                float power1=stringafloat[0];
	                
	            sendPowerToAnalizer(power1);	            
            
	            } catch ( IOException e5 ){
	                e5.printStackTrace();
	                System.exit(-1);
	            }  	
	    }
	}
 
}
