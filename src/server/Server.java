package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import sensorReader.SensorReader;
import sharedDatas.SharedDatas;


//creo un server che può far connettere più client (questo non va più toccato,le cose le devo fare in thread)
public class Server extends Thread{
    
    private int port;
    private ServerSocket ss;
    private SharedDatas datiCondivisi = null;
    private SensorReader letturaDatiSensore = null;
    
    public Server(int porta, SharedDatas datiCondivisi, SensorReader letturaDatiSensore) {          // costruttore del server
        this.port = porta;
        this.datiCondivisi = datiCondivisi;
        this.letturaDatiSensore = letturaDatiSensore;
    }
      
    @Override
    public void run() {
    	
        try {
            ss = new ServerSocket(port);    // crea la server socket sulla porta     
            System.out.println("Server avviato su porta " + this.port);     
            
            while(true) {
                System.out.println("Attesa connessioni...");
                Socket s = ss.accept();                  // resta in ascolto di connessioni
               
                InetAddress clientip = s.getInetAddress();  //prende l'indirizzo del client che si è collegato                    //prende dalla socket l'ip di 
                System.out.println(clientip.getHostAddress() + " collegato");   //chi si è collegato

                ServerThread gestoreClient = new ServerThread(s, datiCondivisi, letturaDatiSensore);   // crea un serverThread per gestire il client che si
                gestoreClient.start();  // è collegato
            }
                
        } catch(IOException e) {        // gestisce eccezioni delle creazioni della socket
            System.err.println("Eccezione in server : " + e);   
        }  finally {  
            if (ss != null && !ss.isClosed()) {  
	            try {
	              ss.close();          
	            } catch (IOException e2) {
	              System.err.println("Errore in close() !"+e2);
	            }
            }
        }                                                                                                   
    }
           
      
//    public static void main(String[] args) throws IOException {
//        ServerEsempio s = new ServerEsempio(3333);                //crea oggetto server sulla porta 3333
//        s.avvioServer();                            //richiama il metodo avvioServer
//    }
}

