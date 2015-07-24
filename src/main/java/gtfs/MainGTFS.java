package gtfs;

import java.net.URISyntaxException;

import keolis.mongoDB.ClientMongoDB;
import api.ClientGTFS;

import com.mongodb.DB;
import com.mongodb.DBCollection;

public class MainGTFS {

	public static void main(String[] args) {
		
		ClientGTFS clientGTFS;
		try {
			clientGTFS = new ClientGTFS();
			DownloadAndDezip.downloadAndDezip(clientGTFS.getUrlGtfsData());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ClientMongoDB clientMongoDB = ClientMongoDB.getInstance();
		clientMongoDB.setDB("star");
		DB dataBase = clientMongoDB.getDB();
		//création/utilisation/suppression de la table de nom "LigneDeBus"
		GTFSParser gtfsp; 
		
		DBCollection lignesDeBus = dataBase.getCollection("ligne");
		lignesDeBus.drop();
		gtfsp = new GTFSParserLigne(lignesDeBus);
		gtfsp.execute("GTFS_Files_Folder/routes.txt");
		gtfsp.execute("GTFS_Files_Folder/routes_additionals.txt");
		
		DBCollection arretDeBus = dataBase.getCollection("arret");
		arretDeBus.drop();
		gtfsp = new GTFSParserArret(arretDeBus);
		gtfsp.execute("GTFS_Files_Folder/stops.txt");
		
		clientMongoDB.close();
		
	}	
}
