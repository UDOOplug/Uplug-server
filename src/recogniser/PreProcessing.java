package recogniser;
import java.util.ArrayList;


public class PreProcessing {
	/*
	 * Prepara il pattern per il frigorifero%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	 */
	public static double[] PatternRefigerator(ArrayList<Float> array_dati, ArrayList<Integer> array_ora){
		double[] pattern=new double[15];
		double[] temp=MediePerOra(array_dati,array_ora);
						
			pattern[0]=AverageOfToday(array_dati);
			pattern[1]=VarianzaCampionaria(array_dati);
			pattern[2]=CicliOnOff(array_dati); 
			
		for(int i=0;i<12;i++){
			pattern[i+3]=temp[i];
		}	
		
		return pattern;
	}
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	
	/*
	 * Metodo per preparare il pattern per riconoscere la Washingmachine
	 */
	public static double[] PatternWashingmachine(ArrayList<Float> array_dati, ArrayList<Integer> array_ora,ArrayList<Float> array_dati2){
		double[] pattern=new double[5];
		
		pattern[0]=MaxPotenza(array_dati);
		pattern[1]=MediaConsumoOn(array_dati2);
		pattern[2]=HourTurnOn(array_dati2);
		pattern[3]=VarianzaFFT(array_dati);
	    pattern[4]=CicliOnOff(array_dati);
		//pattern[5]=MaxFourier;pattern[1]=MediaGiornaliera(array_dati);
		return pattern;
	}
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	public static double[] PatternDishwasher(ArrayList<Float> array_dati, ArrayList<Integer> array_ora,ArrayList<Float> array_dati2){
		double[] pattern=new double[5];
		
		/*pattern[0]=MaxPotenza(array_dati);
		pattern[1]=LavastoviglieMediaOn(array_dati);
		pattern[2]=TempoON(array_dati2);
		pattern[3]=VarianzaFFT(array_dati);
	    pattern[4]=SaliteLavastoviglie(array_dati2);
		*/
		//ho visto che con lo stesso della washingmachine funziona perfettamente
		pattern[0]=MaxPotenza(array_dati);
		pattern[1]=MediaConsumoOn(array_dati2);
		pattern[2]=HourTurnOn(array_dati2);
		pattern[3]=VarianzaFFT(array_dati);
	    pattern[4]=CicliOnOff(array_dati);
		
		return pattern;
	}
	
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	public static double[] PatternTVLCD(ArrayList<Float> array_dati, ArrayList<Integer> array_ora,ArrayList<Float> array_dati2){
		double[] pattern=new double[4];
		
		pattern[0]=AverageOfToday(array_dati);
		pattern[1]=MaxPotenza(array_dati);
		pattern[2]=VarianzaFFT(array_dati);
		pattern[3]=MediaConsumoOn(array_dati);
		return pattern;
	}
	
	
	
	
	
	
	
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	/*
	 * questo metodo dovrebbe ritornare le ore stimate di accenzione contanto i valori in cui la potenza
	* assorbita è >4 con count, poi fa la proporzione count:lungnezzaVettrore=x:24 cioè le ore del giorno
	 */
	
	public static double HourTurnOn(ArrayList<Float> array_dati2){
		double tempoOn=0;
		int count=0;
		for(int i=0;i<array_dati2.size();i++){
			if(array_dati2.get(i)>4){
				count++;
			}
		}
		tempoOn=(count*24)/(double)array_dati2.size();
		return tempoOn;
	}
	/*
	 * questo metodo restituisce il max della potenza
     */
	public static double MaxPotenza(ArrayList<Float> array_dati){
		double massimo=0;
		for(int i=0; i<=array_dati.size()-1; i++) {
		      if( array_dati.get(i)>massimo ) {
		        massimo=array_dati.get(i);
		      }
		    }
		return massimo;
	}
	
	/*
	 * Il seguente metodo esegue una media del consumo di quando l'apparecchio è ACCESO
	 * per fare questo mettiamo di soglia la potenza deve essere maggiore di 4, sui valori già mediati
	 * cioè si prende array_dati2.
	 * Prende in input array_dati, array_dati2, array_ora.
	 */
	public static double MediaConsumoOn(ArrayList<Float> array_dati2){
		double mediaConsumoOn=0;
		//double mediadi5=0;
		int count=0;
		for(int i=0;i<array_dati2.size();i++){
						
			if(array_dati2.get(i)>4){
				mediaConsumoOn+=array_dati2.get(i);
				count++;
			}
			
		}
	mediaConsumoOn=mediaConsumoOn/count;
	return 	mediaConsumoOn;
	}
	/*
	 * Metodo che fa la media del consumo di quando è acceso, ottimizzato per la lavastoviglie
	 */
	public static double LavastoviglieMediaOn(ArrayList<Float> array_dati){
		double mediaConsumoOn=0;
		//double mediadi5=0;
		int count=0;
		for(int i=0;i<array_dati.size();i++){
						
			if(array_dati.get(i)>2){
				mediaConsumoOn+=array_dati.get(i);
				count++;
			}
			
		}
	mediaConsumoOn=mediaConsumoOn/count;
	return 	mediaConsumoOn;
	}
	
	
	/*
	 * conta il numero di cicli in cui passa da ON a OFF
	 */
	public static double CicliOnOff(ArrayList<Float> array_dati){
		
	double numero_cicli=0;	
	
		for(int i=0;i<array_dati.size()-1;i++){
			
			if(array_dati.get(i)<5 && array_dati.get(i+1)>30 ){ 
				numero_cicli++;
			}
		}
	return numero_cicli;	
	}
	/*
	 * metodo per riconoscere le salite nella lavastoviglie
	 */
	public static double SaliteLavastoviglie(ArrayList<Float> array_dati){
		
		double numero_cicli=0;	
		
			for(int i=0;i<array_dati.size()-2;i++){
				
				if(array_dati.get(i)==0 && array_dati.get(i+1)>1 && array_dati.get(i+2)>2 ){ 
					numero_cicli++;
				}
			}
		return numero_cicli;	
		}
	
	
	/*
	 * MEDIA GIORNALIERA
	 */
	
	public static float AverageOfToday(ArrayList<Float> array_dati){
		float media=0;
		for(int i=0;i<array_dati.size();i++){
			
			media+=array_dati.get(i);
			
		}
		media=media/(float)array_dati.size(); //mi torglie la virgola tanto non importa
		return media;
	}

	public static double MediaGiornalieraDouble(ArrayList<Double> array_dati){
		double media=0;
		for(int i=0;i<array_dati.size();i++){
			
			media+=array_dati.get(i);
			
		}
		media=media/(double)array_dati.size(); //mi torglie la virgola tanto non importa
		return media;
	}
	
	
	/*
	 * VARIANZA CAMPIONARIA
	 */
	
	public static double VarianzaCampionaria(ArrayList<Float> array_dati){
		double varianza=0;
		double mediaGiornaliera = AverageOfToday(array_dati);
		
		for(int i=0;i<array_dati.size();i++){
			varianza += Math.pow((array_dati.get(i)- mediaGiornaliera),2);
		}
		varianza=varianza/(double)array_dati.size();
		return varianza;
	}
	/*
	 * VARIANZIA SULLA FFT
	 */
	public static double VarianzaFFT(ArrayList<Float> array_dati){
		double varianza=0;
		double[] InterOn=new double[512];
		InterOn= EstrazioneIntON(array_dati);
		
		Complex[] InterOn_complex=new Complex[512];
		for(int i=0;i<512;i++){
        	InterOn_complex[i]=new Complex(InterOn[i],0); //li trasformo da reali a complessi
        }
		Complex[] trasformataF=FFT.fft(InterOn_complex);
		 double[] abs_trasformataF=new double[512];
		 //faccio il modulo della trasformata
		// System.out.println("Stampo la trasformata,così ci faccio il grafico");
		 for(int k=0;k<512;k++){
		abs_trasformataF[k]=trasformataF[k].abs();
		//System.out.println(abs_trasformataF[k]);
		 }
		 //ne faccio la media
		 double somma=0;
		 for(int i=0;i<512;i++){
			somma+= abs_trasformataF[i];
		 }
		 double media;
		 media=somma/(double)512;
		 //ne calcolo la varianza
		 for(int i=0;i<512;i++){
				varianza += Math.pow((abs_trasformataF[i]- media),2);
			}
			varianza=varianza/(double)512;
		 
		return varianza;
	}
	
	
	/*
	 * estrae 512 valori in cui è acceso
	 */
	public static double[] EstrazioneIntON(ArrayList<Float> array_dati){
		double[] InterOn=new double[512];
		for(int i=0;i<array_dati.size();i++){
			//se due a fila sono maggiori di una certa soglia
			if(array_dati.get(i)>10 && array_dati.get(i+1)>10 ){
				for(int j=0;j<512;j++){
					InterOn[j]=array_dati.get(i+j);
				}
				break;
			}
			
			
		}
		return InterOn;
	}
	
	/* Questo metodo prende in input all'array list con tutti i dati dell file, ovvero con la potenza al secondo
	 * di questi seleziona solo i valori maggiori di 10, ovvero quando l'apparecchio si accende
	 * prende i primi 512 valori e su questi fa la trasformata di fuorier, di questi ne seleziona la metà
	 * e li raggruppa facendone la media e restituendo un vettore di pattern lungo m
	 * 
	 */
	public static double[] MedieSuFFT(ArrayList<Float> array_dati){
				
		int count=0;
        int n=512; //ora lo voglio lungo così, ma si può cambiare
        float[] valori_utili=new float[n];
        Complex[] valori_utili_complex=new Complex[n];
                        
        for(int k=0; k<array_dati.size();k++){
    		if(array_dati.get(k)>10){
    			for(int j=0;j<n;j++){ 
    				valori_utili[j]=array_dati.get(k+j);
    			}       			
    			break;
    		}	
        }
        for(int i=0;i<n;i++){
        	valori_utili_complex[i]=new Complex(valori_utili[i],0); //li trasformo da reali a complessi
        }
        
        Complex[] trasformataF=FFT.fft(valori_utili_complex);
        
        /*for(int i=0;i<n;i++){
        	//System.out.println(valori_utili[i]);
        	//System.out.println(valori_utili_complex[i]);
        	System.out.println(trasformataF[i].abs()); //ritorna l'il modulo
        }*/
        
       //devo dividere il vettore della trasfomata in due, tanto è simmetrico
       //Poi faccio la media dividendolo per 2^5 e mi vengono 8, che li metto nel pattern
       
        int intervallo=32; // quantità di valore da mediare
        //double intervallo=Math.pow(2,m);
        int mezzo_vettore=256;
        int m=mezzo_vettore/intervallo;
        double[] abs_trasformataF=new double[n];
        double[] somme_abs=new double[m];
        //riempio il nuovo vettore con il modulo dei complessi che c'è in trasformataF che è complex
        for(int k=0;k<mezzo_vettore;k++){
        	//così prendo solo i primi 256
        	abs_trasformataF[k]=trasformataF[k].abs();
        	//System.out.println(abs_trasformataF[k]);
        }
        System.out.println("pattern medie valore assoluto");
        for(int i=0;i<m;i++){
        	for(int j=0;j<32;j++){
        	somme_abs[i]+=abs_trasformataF[j+(j*i)];
        	}
        	somme_abs[i]=somme_abs[i]/m;
        	//System.out.println(somme_abs[i]); //vettore medie usato come patter
        }
		return somme_abs;
	}
	
	/*Il seguente metodo fa un pattern con le medie dei valori della potenza raccolti per ora 
	 * quindi il patter restituito è lungo 24, ma ogni singola media può essere fatta su un numero variabile di 
	 * valori, poiché il metodo fa la media per ogni singola ora non in base al numero di dati rilevati.
	 */
	public static double[] MediePerOra(ArrayList<Float> array_dati, ArrayList<Integer> array_ora){
		 float[] somme= new float[24];
	        int[] contatori=new int[24];
	        int indice_somme=0;
	        
	        for(int i=0;i<array_ora.size()-1;i++){
	        	
	        	indice_somme=array_ora.get(i);
	        	somme[indice_somme]+=array_dati.get(i);
	        	contatori[indice_somme] ++;	        	
	        }
	        
	        float[] array_medie=new float[24];
	        
	        for(int i=0;i<somme.length;i++){  
	        	
	        	if(contatori[i]==0){
	        		array_medie[i]=0;
	        		//System.out.println(array_medie[i]);
	        	}
	        	else{
	    	    array_medie[i]=somme[i]/contatori[i];
	    	    //System.out.println(array_medie[i]);
	    	    }
	        }
	        
	        //ora per fare l'array lungo solo 12 si rimedia due a due
	      
	        double[] array12_medie=new double[12]; 
	        
	        for(int i=0;i<array12_medie.length;i++){
	        	array12_medie[i]=(array_medie[2*i]+array_medie[2*i+1])/2;
	        }
		return array12_medie;
		
	}
	
	/*
	 * Questo metodo restituisce un pattern fatto con le medie fatte raggruppando per 3600 valori, ovvero un ora
	 * il problema è che alcune volte i file non hanno tutti lo stesso numero di dati
	 */
	public static float[] MediePerQuantitaValori(ArrayList<Float> array_dati){
		int ore_nel_file=(array_dati.size())/3600; 
        float[] somme= new float[ore_nel_file];
        int index;
        
        for(int j=0;j<ore_nel_file;j++){       
        	for(int i=0;i<3600;i++){       	
        		index=i+(j*3600);        	
        		somme[j]+=array_dati.get(index);      	       	
	        }        
        }
                     
        float[] array_medie=new float[ore_nel_file];
        
        for(int i=0;i<somme.length;i++){   	   
    	    array_medie[i]=somme[i]/3600;
    	    //System.out.println(array_medie[i]);
        }
        return array_medie;
	}
	
	

}
