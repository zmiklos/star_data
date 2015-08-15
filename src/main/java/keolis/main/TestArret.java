package keolis.main;

import java.io.IOException;
import java.net.URISyntaxException;

import keolis.api.RequeteArretBus;
import keolis.mongoDB.ArretBusParser;
import keolis.mongoDB.ClientMongoDB;
import keolis.util.ArretBusAttribut;

import org.apache.http.HttpException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import api.ClientREST;

public class TestArret {
	private static Logger logger = Logger.getLogger("keolis.main.TestArret");

	public static void main(String[] args) throws URISyntaxException, HttpException, IOException {
		
		long start =  System.currentTimeMillis();
		// Set up a simple configuration that logs on the console.
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO); //will not show debug messages
        
		RequeteArretBus requeteArretBus = new RequeteArretBus();
		
		String routeNumber = "0004"; // C4
		String stopNumber = "1160"; // Tournebride
		String directionBool = "0"; // Beauregard
		
		requeteArretBus.addParametre("mode","stopline");
		requeteArretBus.addParametre("route][",routeNumber);
		requeteArretBus.addParametre("direction][",directionBool);
		requeteArretBus.addParametre("stop][",stopNumber);
		
		ClientREST clientREST = new ClientREST();
		ClientMongoDB mongoClient = ClientMongoDB.getInstance();
		
		clientREST.setRequete(requeteArretBus);
		
		mongoClient.setDB("star");
		DB db = mongoClient.getDB(); 

		DBCollection collecArretBus = db.getCollection("arretbus");
		BasicDBObject basicDBObject = ArretBusParser.parser(clientREST.execute());

		//On construit ici un objet ne contenant que les donnees : "prevu" et "vehicule" (nos attributs a tester)
		DBObject searchIfArretBusAlreadyExist = collecArretBus.findOne(
				new BasicDBObject()
				.append(ArretBusAttribut.PREVU, basicDBObject.get(ArretBusAttribut.PREVU))//on vérifie la date
				.append(ArretBusAttribut.NUM_VEHICULE, basicDBObject.get(ArretBusAttribut.NUM_VEHICULE))//on vérifie le numéro du vehicule
		);

		//si on a bien parse notre objet et qu'il n'existe pas deja, on l'insere
		if(basicDBObject != null){
			if (searchIfArretBusAlreadyExist != null) {

				//variable temporaire qui représentera notre nouvelle ligne a update
				BasicDBObject UpdatedBasicDBObject = basicDBObject;
				
				//modification des nouvelles donnees
				UpdatedBasicDBObject.put(ArretBusAttribut.DATE_REQUETE,basicDBObject.get(ArretBusAttribut.DATE_REQUETE));
				UpdatedBasicDBObject.put(ArretBusAttribut.REEL,basicDBObject.get(ArretBusAttribut.REEL));
				UpdatedBasicDBObject.put(ArretBusAttribut.PRECISION,basicDBObject.get(ArretBusAttribut.PRECISION));
				
				//on modifie la date prévue seulementde notre collection
				collecArretBus.update(searchIfArretBusAlreadyExist, UpdatedBasicDBObject);
				
			} else {//on a bien parse notre objet et qu'il n'existe pas deja, on l'insere
				collecArretBus.insert(basicDBObject);
				logger.info(basicDBObject);
			} 
		}
		mongoClient.close();
		logger.info("Durée : " + (long) ( System.currentTimeMillis() - start)/1000);
	}
}
