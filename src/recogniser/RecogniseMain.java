package recogniser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import MLP.DataSet;
import MLP.MLP;


public class RecogniseMain {
		
	static ArrayList<Float>   array_dati; //ci salvo dati potenza
	static ArrayList<Float>   array_dati2; //ci salvo la seconda colonna quella mediata
	static ArrayList<Integer> array_ora; //ci salvo la stringa della data
	static ArrayList<Integer> array_minuti;//ci salvo i minuti

	 
	public static void riconoscimi() throws ClassNotFoundException, IOException {
		leggiDatiDaFileCsv(); 
		
		RefrigeratorRic refrigeratorRic = new RefrigeratorRic();
		boolean siTrattaDiUnFrigo = refrigeratorRic.recognizeFrige(array_dati, array_ora);
		System.out.println(siTrattaDiUnFrigo);
		
		WashingmachineRic washingmachineRic=new WashingmachineRic();
		boolean siTrattaDiUnaLavatrice=washingmachineRic.recognizeWashingmachine(array_dati, array_ora,array_dati2);
		System.out.println(siTrattaDiUnaLavatrice);
		
		TvlcdRic tvlcdRic=new TvlcdRic();
		boolean siTrattaDiUnTvlcd=tvlcdRic.recognizeTVLCD(array_dati, array_ora,array_dati2);
		System.out.println(siTrattaDiUnTvlcd);
		
		DishwasherRic dishwasherRic=new DishwasherRic();
		boolean siTrattaDiUnaLavastoviglie=dishwasherRic.recogniseDishwasher(array_dati, array_ora,array_dati2);
		System.out.println(siTrattaDiUnaLavastoviglie);
		
		stampaRisultato(siTrattaDiUnFrigo,siTrattaDiUnaLavatrice,siTrattaDiUnTvlcd,siTrattaDiUnaLavastoviglie);			
	}
	 
	
	public static int recognise(ArrayList<Integer> array_ora,
		ArrayList<Float> array_dati,
		ArrayList<Float> array_dati2) throws ClassNotFoundException, IOException {
		
		RefrigeratorRic refrigeratorRic = new RefrigeratorRic();
		boolean siTrattaDiUnFrigo = refrigeratorRic.recognizeFrige(array_dati, array_ora);
		System.out.println(siTrattaDiUnFrigo);
		
		WashingmachineRic washingmachineRic=new WashingmachineRic();
		boolean siTrattaDiUnaLavatrice=washingmachineRic.recognizeWashingmachine(array_dati, array_ora,array_dati2);
		System.out.println(siTrattaDiUnaLavatrice);
		
		TvlcdRic tvlcdRic=new TvlcdRic();
		boolean siTrattaDiUnTvlcd=tvlcdRic.recognizeTVLCD(array_dati, array_ora,array_dati2);
		System.out.println(siTrattaDiUnTvlcd);
		
		DishwasherRic dishwasherRic=new DishwasherRic();
		boolean siTrattaDiUnaLavastoviglie=dishwasherRic.recogniseDishwasher(array_dati, array_ora,array_dati2);
		System.out.println(siTrattaDiUnaLavastoviglie);
		
		return stampaRisultato(siTrattaDiUnFrigo,siTrattaDiUnaLavatrice,siTrattaDiUnTvlcd,siTrattaDiUnaLavastoviglie);			
	}
	
	 public static int stampaRisultato(boolean Frigo,boolean Lavatrice,boolean Tvlcd,boolean Lavastoviglie){
		if (Frigo && !Lavatrice && !Tvlcd && !Lavastoviglie){
			System.out.println("Si tratta di un FRIGORIFERO");
			return 1;
		}
		else if(!Frigo && Lavatrice && !Tvlcd && !Lavastoviglie){
			System.out.println("Si tratta di una lavatrice");
			return 2;
		}
		else if(!Frigo && !Lavatrice && Tvlcd && !Lavastoviglie){
			System.out.println("Si tratta di una TV-LCD");
			return 3;
		}
		else if(!Frigo && !Lavatrice && !Tvlcd && Lavastoviglie){
			System.out.println("Si tratta di una Lavastoviglie");
			return 4;
		}
		else if(!Frigo && !Lavatrice && !Tvlcd && !Lavastoviglie){
			System.out.println("L'apparecchio non è nessuno di quelli che la rete riconosce");
			return 0;
		}
		else{
			System.out.println("La rete è indecisa tra due o più apparecchi :-(");
			return -1;
		}		
	 }
	
	public static void leggiDatiDaFileCsv() throws IOException{
		
	    Scanner scanner = new Scanner(System.in);
		System.out.println("Ricordati di cambiare il nome del file in uscita");
		System.out.println("/home/erika/Scrivania/dati_addestramento/Refrigerator/prova");
		System.out.print("Inserisci percorso directory: ");
		String input = scanner.nextLine();
		
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
		else{
			System.out.printf("%s does not exist",input);
		}
		
		
		for(int w=0;w<nomi_file.size();w++){
			System.out.println(nomi_file.get(w));
						
			//l'arrai contenente i dati della potenza è array_dati		
			String filename=input+"/"+nomi_file.get(w);
							
			array_dati   = new ArrayList<Float>(); //ci salvo dati potenza
			array_dati2  = new ArrayList<Float>(); //ci salvo la seconda colonna quella mediata
			array_ora    = new ArrayList<Integer>(); //ci salvo la stringa della data
			array_minuti = new ArrayList<Integer>();//ci salvo i minuti
					
			File f = new File(filename);
	        BufferedReader in = new BufferedReader(
	                            new InputStreamReader(
	                            new FileInputStream(f)));
	       
	        String lin="";
	        String vs[];
	        String temp1;
	        float temp2;
	        float temp3;
	        
	        while(lin!=null){
	    	
	        	lin = in.readLine();
	            try {
					lin=lin.trim();
				} catch (Exception e) {
					break;
				}
	                     
	            /* separa vettore di fetaure e vettore target */            
	            vs = lin.split(";");
		        int lunghezza_stringa = vs.length;
		        
		        temp1=vs[0];
		     	String[] data_ora; 
		        data_ora=temp1.split(" ");
		        String ora;
		        ora=data_ora[1];
		         
		        String[] ora_min_sec;
		         
		        ora_min_sec=ora.split(":");
		        int ore;
		        int minuti;
		        ore=Integer.parseInt(ora_min_sec[0]);
		        minuti=Integer.parseInt(ora_min_sec[1]);
		        array_ora.add(ore);
		        array_minuti.add(minuti);
		        		         
		        try {		        	
					temp2 = Float.parseFloat(vs[1]);
					array_dati.add(temp2);
				} catch (NumberFormatException e) {
					System.out.println("Errore conversione string --> float");
				}
		        
		        try{
		        	temp3=Float.parseFloat(vs[2]);
		        	array_dati2.add(temp3);
		        }catch (NumberFormatException e) {
					System.out.println("Errore conversione string --> float");
				}
			}
		} //FINE LETTURA DATI DA FILE				
	}	
	

    
    public static void printSingleResult(double[] pattern, MLP net) {
        
        // elabora l'ingresso i-esimo con l'MLP
        double outx[] = net.runMLP(pattern);
        
        for(int i=0; i<outx.length; i++)
        	System.out.print(outx[i]+ " --- ");                  
    }
}
