package meteo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DateMeteoManager {
	private static final List<String> DATE_FORMAT_REGEXPS = new ArrayList<String>() {{
        add("^\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}$");
	}};
	
	/**
	 * recupere la date la plus pertinente depuis une liste de String 
	 * @param plistDates liste de String
	 * @return la date la plus pertinente
	 */
	public static String getMeteoDate(String[] plistDates){
		List<String> listDates=getListAllDates(plistDates);
		String date=getAppropriateDate(listDates);
		return date;
	}
	/**
	 * permet d'avoir la date la plus pertinente
	 * @param pListDates liste de dates en String
	 * @return la date la plus pertinente
	 */
	public static String getAppropriateDate(List<String> pListDates){
		String dateMeteo="";
		Date dateNow=getTodaysDate(true);
		long dateNowTime=dateNow.getTime();
		Date dateZero;
		if(pListDates.size()>0){
			dateZero=convertStringToDate(pListDates.get(0));
			long dateZeroTime=dateZero.getTime();
			float diffZero=Math.abs(dateNowTime-dateZeroTime);
			for(int i=1;i<pListDates.size();i++){
				Date date=convertStringToDate(pListDates.get(i));
				long dateTime=date.getTime();
				float diffTime=Math.abs(dateTime-dateNowTime);
				if(diffTime<diffZero){
					diffZero=diffTime;
					dateZero=date;
				}
			}
			dateMeteo=formatDate(dateZero, "yyyy-MM-dd HH:mm:ss");
		}
		
		return dateMeteo;
	}
	/**
	 * retourne la liste des dates dans une liste de String
	 * @param pListStrings lists de String
	 * @return liste de dates en String
	 */
	public static List<String> getListAllDates(String[] pListStrings){
		List<String> listAllDates=new ArrayList<String>();
		for (String string : pListStrings) {
			Boolean isDate=getIfStringIsDate(string);
			if(isDate){
				listAllDates.add(string);
			}
		}
		
		return listAllDates;
	}
	/**
     * permet de si un string est une date ou pas
     * @param pText le texte a explorer
     * @return la date en chaine de caractere si elle existe, sinon, retourne une chaine de caractere vide
     */
    public static Boolean getIfStringIsDate(String pText){
    	Boolean isDate=false;
    	if(pText!=null){
	        for (String regexp : DATE_FORMAT_REGEXPS) {
	            if (pText.toLowerCase().matches(regexp)) {
	            	isDate=true;
	            }
	        }
        }
        return isDate; // Unknown format.
    }
    /**
     * pour recuperer la date du jour
     * @param pIsCurrentTime true si on retourne l'heure exacte, et false si on met l'heure a 00:00:00
     * @return la date du jour
     */
    public static Date getTodaysDate(Boolean pIsCurrentTime){
    	Calendar cal=Calendar.getInstance();
    	if(pIsCurrentTime==false){
    		cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE),0,0,0);
    	}
    	return cal.getTime();
    }
    
    /**
	 * pour convertir une date 
	 * @param pDateString
	 * @return date en format Date de java
	 */
	public static Date convertStringToDate(String pDateString){
		Date dateconverted=null;
		try {
			SimpleDateFormat dateFormatEN = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			dateFormatEN.setLenient(true);
			dateconverted = dateFormatEN.parse(pDateString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateconverted;

	}
	/**
     * formate une date en String via un pattern
     * @param pDate Date a formater
     * @param pFormatDate format de la date
     * @return date en chaine de carateres suivant le format entre
     */
    public static String formatDate(Date pDate, String pFormatDate){
    	SimpleDateFormat format=new SimpleDateFormat(pFormatDate,Locale.ENGLISH);
    	String date=format.format(pDate);
    	return date;
    }
}
