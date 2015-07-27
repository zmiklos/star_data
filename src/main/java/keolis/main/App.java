package keolis.main;

import gtfs.DownloadAndDezip;
import gtfs.GTFSParser;
import gtfs.GTFSParserArret;
import gtfs.GTFSParserLigne;

import java.io.IOException;
import java.net.URISyntaxException;

import keolis.mongoDB.ClientMongoDB;

import org.apache.http.HttpException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import api.ClientGTFS;
import api.ClientREST;

import com.mongodb.DB;
import com.mongodb.DBCollection;

public class App 
{
	private static Logger logger = Logger.getLogger("keolis.main.App");
	
    public static void main( String[] args ) throws URISyntaxException, HttpException, IOException
    {   
    	long start =  System.currentTimeMillis();
    	
    	// Set up a simple configuration that logs on the console.
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO); //will not show debug messages
        
    	ClientMongoDB mongoClient = ClientMongoDB.getInstance();
    	
    	/***  Recupération données GTFS ***/  
    	mongoClient.setDB("star");
    	DB database = mongoClient.getDB();
    	ClientGTFS clientGTFS;
		try {
			clientGTFS = new ClientGTFS();
			DownloadAndDezip.downloadAndDezip(clientGTFS.getUrlGtfsData());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GTFSParser gtfsp; 
		
		DBCollection lignesDeBus = database.getCollection("ligne");
		lignesDeBus.drop();
		gtfsp = new GTFSParserLigne(lignesDeBus);
		gtfsp.execute("GTFS_Files_Folder/routes.txt");
		gtfsp.execute("GTFS_Files_Folder/routes_additionals.txt");
		
		DBCollection arretDeBus = database.getCollection("arret");
		arretDeBus.drop();
		gtfsp = new GTFSParserArret(arretDeBus);
		gtfsp.execute("GTFS_Files_Folder/stops.txt");
		
		ClientREST clientREST = new ClientREST();
		
		/***  Recupération données Réseau Star ***/
		MainLigneAlert.mainLigneAlert();
		ReseauStar reseauStar = new ReseauStar(clientREST, mongoClient);
		reseauStar.execute();
		
        mongoClient.close();
        logger.info("Durée : " + (long) ( System.currentTimeMillis() - start)/1000 + " secondes");
    }
}
