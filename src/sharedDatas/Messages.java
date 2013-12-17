package sharedDatas;

import java.util.ArrayList;


public class Messages {

	public static final String medieGiornaliereUltimi30Giorni  = "1";	
	public static final String classificazioneElettrodomestico = "2";
	public static final String OreOnUltimi30gg                 = "3";
	public static final String potenza                         = "4";
	public static final String medie_mezzore                   = "5";
	public static final String confronto                       = "6";
	public static final String confrontoMediePesate            = "7";
	public static final String on                              = "8";
	public static final String off                             = "9";
	public static final String consumoIstantaneo               = "10";

	
	
	public static ArrayList<Float> stringToFloatArraylist(String s){
		ArrayList<Float> arr = new ArrayList<Float>();
		
		s = s.replace("<","");
		s = s.replace(">","");
		
		String[] ss = s.split("");
		for(int i=0; i<ss.length ; i++){
			arr.add( Float.parseFloat(ss[i]) );
		}
		
		return arr;		
	}

	
	
	public static String floatArraylistToString(ArrayList<Float> ff){
		if(ff == null){
			System.out.println("null array");
			return "";
		}	
		
		String s = "<";
		
		for(int i=0; i<ff.size()-1; i++){
			s += ff.get(i);
			s += "#";
		}
		
		s += ff.get( ff.size() -1 );
		s += ">";
		
		return s;		
	}
	
	
	
	
}
