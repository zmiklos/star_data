package keolis.main;

import java.io.IOException;
import java.net.URISyntaxException;

import keolis.api.RequeteArretBus;
import keolis.mongoDB.ArretBusLigneParser;
import keolis.mongoDB.ClientMongoDB;
import keolis.mongoDB.LigneParser;
import keolis.util.Direction;
import keolis.util.LigneAttribut;
import keolis.util.NomCollectionMongoDB;

import org.apache.http.HttpException;

import api.ClientREST;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class ReseauStar {
	
	private ClientREST clientREST;
	private ClientMongoDB clientMongoDB;
	private ArretBusLigneParser arretBusLigneParser;
	private String lignes[];

	public ReseauStar(ClientREST clientREST, ClientMongoDB clientMongoDB) {
		new MainArretBus(clientREST, clientMongoDB);
		this.clientREST = clientREST;
		this.clientMongoDB = clientMongoDB;
		this.arretBusLigneParser = new ArretBusLigneParser(clientREST, clientMongoDB);
	}

	public void execute() throws URISyntaxException, HttpException, IOException{
		
		this.lignes = ligneStar();
		if(this.lignes != null){
			for(int i = 0; i < this.lignes.length - 1; i++){
				for(int k = 0; k < Direction.directions.length; k++){
					this.arretBusLigneParser.execute(lignes[i], Direction.directions[k]);
				}
			}
		}
	}
	
	/**
	 * Get star routes from MongoDB after GTFS insertion
	 * @return string array that contains all star routes
	 */
	public String [] ligneStar() {
		
		clientMongoDB.setDB("star");
		DB dataBase = this.clientMongoDB.getDB();
		DBCollection collection = dataBase.getCollection(NomCollectionMongoDB.LIGNE);
		DBCursor dbCursor =  collection.find();
		int lengthDbCursor = dbCursor.count();

		if(lengthDbCursor == 0)
			return null;

		String []routes = new String[lengthDbCursor];
		int i = 0;
		while(dbCursor.hasNext()){
			DBObject obj = dbCursor.next();
			routes[i++] = (String) obj.get(LigneAttribut.LIGNE_ID);
		}
		dbCursor.close();
		return routes;
	}
	
	/**
	 * Constructs a line mode request, parses the result and returns stoplist of route
	 * @param route route id
	 * @param direction 
	 * @return string array that contains all stoplines ids of route
	 * @throws URISyntaxException
	 * @throws HttpException
	 * @throws IOException
	 */
	public String[] makeRequeteArretBus (String route, String direction) throws URISyntaxException, HttpException, IOException{
		
		RequeteArretBus requeteArretBus = new RequeteArretBus();
		requeteArretBus.addParametre("mode","line");
		requeteArretBus.addParametre("route",route);
		requeteArretBus.addParametre("direction",direction);
		this.clientREST.setRequete(requeteArretBus);
		return  LigneParser.parser(this.clientREST.execute());
	}
	
	
}
