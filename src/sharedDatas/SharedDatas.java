package sharedDatas;

import java.util.ArrayList;

public class SharedDatas {
			
	private ArrayList<Float> medieGiornaliereUltimi30Giorni = null;	
	private ArrayList<Float> OreOnUltimi30gg = null;
	private ArrayList<Float> medie_mezzore = null;
	private ArrayList<Float> confrontoMediePesate = null;
	private int classificazioneElettrodomestico = 0;
	private int confronto = 0;
	private float potenza = 0;
	
	//metodi che mi accedono alle variabili in modo da evitare accavallamenti
	public synchronized void setAverage30days( ArrayList<Float> medieGiornaliereUltimi30Giorni ){
		this.medieGiornaliereUltimi30Giorni = medieGiornaliereUltimi30Giorni;
	}
	
	public synchronized void setClassification(int classificazioneElettrodomestico){
		this.classificazioneElettrodomestico = classificazioneElettrodomestico;
	}
	
	public synchronized void setHourTurnOn(ArrayList<Float> OreOnUltimi30gg){
		this.OreOnUltimi30gg = OreOnUltimi30gg;
	}
	
	public synchronized void currentPower(float potenza){
		this.potenza = potenza;
	}
	
	public synchronized void setAverageHalfHour(ArrayList<Float> medie_mezzore){
		this.medie_mezzore = medie_mezzore;
	}
	
	public synchronized void setComperisonAverage(int confronto){ 
		this.confronto = confronto;
	}
	
	public synchronized void setComparisonWeightedaverage(ArrayList<Float> confrontoMediePesate){
		this.confrontoMediePesate=confrontoMediePesate;
	}
	
		
	//metodi che vengono richiami dall'app android
	public synchronized String getMedieGiornaliere30gg() {
		return Messages.floatArraylistToString(this.medieGiornaliereUltimi30Giorni);
	}
	
	public synchronized String getClassificazione() {
		return "" + this.classificazioneElettrodomestico;
	}
	
	public synchronized String getOreOn() {
		return Messages.floatArraylistToString(this.OreOnUltimi30gg);
	}
    
	public synchronized String getPotenzaAttuale(){
		return "" + this.potenza;
	}
	
	public synchronized String getMedieMezzore(){
		return Messages.floatArraylistToString(this.medie_mezzore);
	}
	
	public synchronized String getConfronto(){
		return "" + this.confronto;
	}
	
	public synchronized String getConfrontoMediePesate(){
		if(confrontoMediePesate == null)
			return null;

		return Messages.floatArraylistToString(confrontoMediePesate);
	}
}
