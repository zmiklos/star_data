package keolis.mongoDB;

import java.net.UnknownHostException;

import keolis.util.ArretBusAttribut;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class ClientMongoDB {
	//private static String host = "localhost";
	private MongoClient mongoClient;
	private DB db;
	private static ClientMongoDB clientMongoDBstatic;
	
	private ClientMongoDB() throws UnknownHostException{
		// this.mongoClient = new MongoClient(new ServerAddress(host, port));
//		this.mongoClient = new MongoClient(new ServerAddress(host));
		this.mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
	}
	
	/**
	 * recuperation instance de ClientMongoDB en singleton
	 * @return instance de ClientMongoDB
	 */
	public static synchronized ClientMongoDB getInstance() {
		if (clientMongoDBstatic == null) {
			try {
				clientMongoDBstatic = new ClientMongoDB();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return clientMongoDBstatic;
	}
	public MongoClient getMongoClient(){
		return mongoClient;
	}
	public void close(){
		mongoClient.close();
	}
	public DB getDB() {
		return db;
	}
	public void setDB(String dBName){
		db = mongoClient.getDB(dBName);
	}
	public void insert(DBCollection collection, BasicDBObject dBbasicDBObject){
		DBCollection dBcollection = collection;


		//On construit ici un objet ne contenant que les donnes : "prevu" et "vehicule" (nos attributs a tester)
		DBObject searchIfArretBusAlreadyExist = dBcollection.findOne(
				new BasicDBObject()
				.append(ArretBusAttribut.PREVU, dBbasicDBObject.get(ArretBusAttribut.PREVU))//on vérifie la date
				.append(ArretBusAttribut.NUM_VEHICULE, dBbasicDBObject.get(ArretBusAttribut.NUM_VEHICULE))//on vérifie le numéro du vehicule
				);

		//si on a bien parse notre objet et qu'il n'existe pas deja, on l'insere
		if(dBbasicDBObject!=null){
			//
			if (searchIfArretBusAlreadyExist != null) {

				//variable temporaire qui représentera notre nouvelle ligne a update
				BasicDBObject UpdatedBasicDBObject = dBbasicDBObject;

				//modification des nouvelles donnees
				UpdatedBasicDBObject.put(ArretBusAttribut.DATE_REQUETE,dBbasicDBObject.get(ArretBusAttribut.DATE_REQUETE));
				UpdatedBasicDBObject.put(ArretBusAttribut.REEL,dBbasicDBObject.get(ArretBusAttribut.REEL));
				UpdatedBasicDBObject.put(ArretBusAttribut.PRECISION,dBbasicDBObject.get(ArretBusAttribut.PRECISION));

				//on modifie la date prévue seulementde notre collection
				dBcollection.update(searchIfArretBusAlreadyExist, UpdatedBasicDBObject);

			} else {//on a bien parse notre objet et qu'il n'existe pas deja, on l'insere
				dBcollection.insert(dBbasicDBObject);
			} 
		} 
	}
}