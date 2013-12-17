package server;

import java.io.*;
import java.net.*;

import sensorReader.SensorReader;
import sharedDatas.SharedDatas;

import analyzer.Analyser;


public class ServerThread extends Thread {

    private Socket socket;
    private SharedDatas datiCondivisi;
    private SensorReader letturaDatiSensore = null;
    
    private boolean running = true;

    public ServerThread(Socket s, SharedDatas datiCondivisi, SensorReader letturaDatiSensore) { //costruttore del serverThread
        this.socket = s;
        this.datiCondivisi = datiCondivisi;
        this.letturaDatiSensore = letturaDatiSensore;
    }

	@Override
    public void run() {     
        try{                                  
            BufferedReader is = new BufferedReader( new InputStreamReader(socket.getInputStream())); //variabile che prende l'imput dello stream
            PrintStream os    = new PrintStream( new BufferedOutputStream(socket.getOutputStream()),true); //var che prende l'output dello stream
              
            
            //ad ogni numero stampa una cosa diversa
            String requestMessage = null;
            int valinserito = -1;
            
            while(running){
            	requestMessage = is.readLine();
            	if(requestMessage != null){
		        	
		        	System.out.println("Request message: " + requestMessage); //messagio mandato dal client       
		            valinserito = Integer.parseInt(requestMessage);
		            
		            running = false;
		            
		            switch(valinserito){	            
		            	case 1: 
		            		os.println( datiCondivisi.getMedieGiornaliere30gg());
		            	break;
		            	
		            	case 2: 
		            		os.println( datiCondivisi.getClassificazione() );
		            	break;
		            	
		            	case 3: 
		            		os.println( datiCondivisi.getOreOn() );
		            	break;
		            	
		            	case 4: 
		            		os.println( datiCondivisi.getPotenzaAttuale());
		            	break;
		            	
		            	case 5: 
		            		os.println( datiCondivisi.getMedieMezzore() );
		            	break;
		            	
		            	case 6: 
		            		os.println( datiCondivisi.getConfronto() );
		            	break;
		            	
		            	case 7: 
		            		os.println( datiCondivisi.getConfrontoMediePesate());
		            	break;
		            	
		            	case 8: 
		            		letturaDatiSensore.writeOnSerial("1");
		            		os.println( datiCondivisi.getPotenzaAttuale());
		            	break;
		            		
		            	case 9:   
		            		letturaDatiSensore.writeOnSerial("0");
		            		os.println( datiCondivisi.getPotenzaAttuale());
		            	break;
		            		
		            	case 0: 
		            		running = false;
		            	break;
		            	
		            	case 10: 
		            		os.println( datiCondivisi.getPotenzaAttuale());
		            	break;
		            	
		            	// Invio classificazioni
		            	case 21: 
		            		Analyser.insertLabel(1);
		            	break;
		            	
		            	case 22: 
		            		Analyser.insertLabel(2);
		            	break;
		            	
		            	case 23: 
		            		Analyser.insertLabel(3);
		            	break;
		            	
		            	case 24: 
		            		Analyser.insertLabel(4);
		            	break;
		            	
		            	case 20: 
		            		Analyser.insertLabel(0);
		            	break;
		            	
		            	default: System.out.println("Wrong request message: " +requestMessage);
		                break;
		                		            
		            }
            	}            
            }    
            socket.close();
            is.close();
            os.close();
            System.out.println("Client connection closed!! :)  ");
            
        } catch (IOException e){
            System.err.println(e);
        } finally {                    
        	if (socket.isConnected()) {  
        		try {
        			socket.close();          
        		} 
        		catch (IOException e2) {
        			System.err.println("Error closing socket!");
        		}
        	}
        }
    }
}