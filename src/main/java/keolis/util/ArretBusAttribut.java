package keolis.util;

public class ArretBusAttribut {
	//stopline[i] -> stop
	public static String ARRET = "id_arret";
	//stopline[i] -> route
	public static String LIGNE = "id_ligne";
	// : localdatetime
	public static String DATE_REQUETE = "date_requete";
	//stopline[i] -> direction
	public static String DIRECTION  = "direction";
	//stopline[i] -> departures -> departure[0] : headSign
	public static String EN_TETE = "en_tete";
	//stopline[i] -> departures -> departure[0] : excepted
	public static String PREVU = "prevu";
	//stopline[i] -> departures -> departure[0] 
	public static String REEL = "reel";
	//stopline[i] -> departures -> departure[0] : vehicle
	public static String NUM_VEHICULE = "num_vehicule";
	//stopline[i] -> departures -> departure[0] : accurate
	public static String PRECISION = "precision";
	//calcul
	public static String DIFF_TR = "diff_TR";
	
	public static String TRAVAUX = "travaux";
}
