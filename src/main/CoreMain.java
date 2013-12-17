package main;

import java.io.IOException;
import java.util.Scanner;

import sensorReader.SensorReader;
import server.Server;
import sharedDatas.SharedDatas;
import analyzer.Analyser;

public class CoreMain {

	public static String trainedNetPath = "data/";
	
	public static String PORT_NAME = "/dev/ttyUSB0";
	
	public static int serverPort = 3333;
	
	public static boolean useDefaultPath = false;
	public static boolean simulateFirstMonth = false;
	
	private static SharedDatas sharedDatas; 
	
	private static Analyser analizer;
	
	private static SensorReader sensorReader;
	
	private static Server server;
	
	public final static String databaseUser     = "erika";
	public final static String databasePassword = "presaerika";
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		

		for(int i=0; i<args.length; i++){
			if(args[i].startsWith("-")){

				if(args[i].contains("h")){
					System.out.println(" ----- HELP -----");
					System.out.println(" Usage: ");
					System.out.println("  -d  All data needed by Uplug are loaded from default path");
					System.out.println("  -f  Simulates first month of datas taken from a fridge");
					System.out.println(" \n\n ");
					return;
				}
				if(args[i].contains("f")){
					simulateFirstMonth = true;
					System.out.println(" ----- SIMULATE FIRST MONTH OF FRIDGE POWER DATA -----");
					System.out.println(" -----   data path:   pattern_frigo.csv  -----");
				}
				
				if(args[i].contains("d")){
					useDefaultPath = true;
					System.out.println(" ----- USE DEFAULT PATHS TO LOAD DATAS -----");
				}
			}
		}


		System.out.print("Loading trained ANN parameters...");		
		String input;
		if(CoreMain.useDefaultPath)			
			input = "data/Vettori_medie_frigo";		
		else{
			Scanner scanner = new Scanner(System.in);
			input = scanner.nextLine();
		}
		System.out.println("COMPLETE");
			
		trainedNetPath = input;
		sharedDatas = new SharedDatas();		
		analizer = new Analyser( sharedDatas );	
				
		sensorReader = new SensorReader(analizer);		
		
		server = new Server(serverPort, sharedDatas, sensorReader);
		server.start();	
		
		System.out.print("Enabling SAM3X serial connection " + PORT_NAME + "...");	
		try {
			sensorReader.connect(PORT_NAME);
			System.out.println("COMPLETE");
		} catch (Exception e) {
			System.out.println("FAILED");
		}		
	}

}
