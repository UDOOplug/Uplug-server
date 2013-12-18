package sharedDatas;

import java.util.ArrayList;


public class Messages {

	public static final String last30daysAvg          = "1";	
	public static final String annResult              = "2";
	public static final String last30DayTurnùOnTime   = "3";
	public static final String power                  = "4";
	public static final String halfhourAvg            = "5";
	public static final String comparisonAvg          = "6";
	public static final String comparisonWeigthAvg    = "7";
	public static final String on                     = "8";
	public static final String off                    = "9";
	public static final String instantConsumption     = "10";

	
	
	public static ArrayList<Float> stringToFloatArraylist(String s){
		ArrayList<Float> arr = new ArrayList<Float>();
		
		s = s.replace("<","");
		s = s.replace(">","");
		
		String[] ss = s.split("#");
		for(int i=0; i<ss.length ; i++){
			try {
				arr.add( Float.parseFloat(ss[i]) );
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		return arr;		
	}

	
	
	public static String floatArraylistToString(ArrayList<Float> ff){
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
