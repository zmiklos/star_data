package keolis.main;

import java.io.IOException;
import java.net.URISyntaxException;

import keolis.api.RequeteArretBus;
import keolis.mongoDB.ArretBusParser;
import keolis.mongoDB.ClientMongoDB;
import keolis.util.ArretBusAttribut;

import org.apache.http.HttpException;

import api.ClientREST;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class MainArretBus {
	private ClientREST clientREST;
	private ClientMongoDB clientMongoDB;
	
	
	public MainArretBus(ClientREST clientREST, ClientMongoDB clientMongoDB) {
		this.clientREST = clientREST;
		this.clientMongoDB = clientMongoDB;
	}


	public ClientREST getClientREST() {
		return clientREST;
	}


	public void setClientREST(ClientREST clientREST) {
		this.clientREST = clientREST;
	}


	public ClientMongoDB getClientMongoDB() {
		return clientMongoDB;
	}


	public void setClientMongoDB(ClientMongoDB clientMongoDB) {
		this.clientMongoDB = clientMongoDB;
	}


	public  void execute(String routeNumber, String stopNumber, String directionBool) 
			throws URISyntaxException, HttpException, IOException{
		RequeteArretBus requeteArretBus = new RequeteArretBus();

		requeteArretBus.addParametre("mode","stopline");
		requeteArretBus.addParametre("route][",routeNumber);
		requeteArretBus.addParametre("direction][",directionBool);
		requeteArretBus.addParametre("stop][",stopNumber);

		
		this.clientREST.setRequete(requeteArretBus);
		this.clientMongoDB.setDB("star");

		//		MongoClient mongoClient = clientMongoDB.getMongoClient();
		DB dataBase = this.clientMongoDB.getDB();

		DBCollection collection = dataBase.getCollection("arretbus");
		BasicDBObject basicDBObject = ArretBusParser.parser(clientREST.execute());


		//On construit ici un objet ne contenant que les donnes : "prevu" et "vehicule" (nos attributs a tester)
		DBObject searchIfArretBusAlreadyExist = collection.findOne(
				new BasicDBObject()
				.append(ArretBusAttribut.PREVU, basicDBObject.get(ArretBusAttribut.PREVU))//on vérifie la date
				.append(ArretBusAttribut.NUM_VEHICULE, basicDBObject.get(ArretBusAttribut.NUM_VEHICULE))//on vérifie le numéro du vehicule
				);

		//si on a bien parse notre objet et qu'il n'existe pas deja, on l'insere
		if(basicDBObject!=null){
			//
			if (searchIfArretBusAlreadyExist != null) {

				//variable temporaire qui représentera notre nouvelle ligne a update
				BasicDBObject UpdatedBasicDBObject = basicDBObject;

				//modification des nouvelles donnees
				UpdatedBasicDBObject.put(ArretBusAttribut.DATE_REQUETE,basicDBObject.get(ArretBusAttribut.DATE_REQUETE));
				UpdatedBasicDBObject.put(ArretBusAttribut.REEL,basicDBObject.get(ArretBusAttribut.REEL));
				UpdatedBasicDBObject.put(ArretBusAttribut.PRECISION,basicDBObject.get(ArretBusAttribut.PRECISION));

				//on modifie la date prévue seulementde notre collection
				collection.update(searchIfArretBusAlreadyExist, UpdatedBasicDBObject);

			} else {//on a bien parse notre objet et qu'il n'existe pas deja, on l'insere
				collection.insert(basicDBObject);
			} 
		} 
	}
}
