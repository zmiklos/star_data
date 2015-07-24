package keolis.main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Iterator;

import keolis.api.RequeteLigneAlert;
import keolis.metier.LigneAlert;
import keolis.metier.LigneAlertParser;
import keolis.mongoDB.ClientMongoDB;
import keolis.util.LigneAlertAttribut;
import keolis.util.LigneAttribut;
import keolis.util.NomCollectionMongoDB;

import org.apache.http.HttpException;

import api.ClientREST;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class MainLigneAlert {

	public static void mainLigneAlert() throws URISyntaxException, HttpException, IOException {
		
		RequeteLigneAlert requeteLigneAlert = new RequeteLigneAlert();
		requeteLigneAlert.addParametre("mode", "all");
		ClientREST clientREST = new ClientREST();
		clientREST.setRequete(requeteLigneAlert);
		Collection<LigneAlert> lignesAlert = LigneAlertParser.parser(clientREST.execute());
		if(lignesAlert != null){
			ClientMongoDB clientMongoDB = ClientMongoDB.getInstance();
			clientMongoDB.setDB("star");
			DB db = clientMongoDB.getDB();
			DBCollection collection = db.getCollection(NomCollectionMongoDB.LIGNEALERT);
			collection.drop();
			Iterator<LigneAlert> itLigneAlert = lignesAlert.iterator();
			while(itLigneAlert.hasNext()){
				
				BasicDBObject basicDBObject = new BasicDBObject();
				LigneAlert ligneAlert = itLigneAlert.next();
				
				basicDBObject.append(LigneAlertAttribut.TITLE, ligneAlert.getTitle());
				basicDBObject.append(LigneAlertAttribut.STARTIME, ligneAlert.getStartTime());
				basicDBObject.append(LigneAlertAttribut.ENDTIME, ligneAlert.getEndTime());
				basicDBObject.append(LigneAlertAttribut.MAJORDISTURBANCE, ligneAlert.getMajordisturbance());
				basicDBObject.append(LigneAlertAttribut.DETAIL, ligneAlert.getDetail());
				basicDBObject.append(LigneAlertAttribut.LINES, ligneAlert.getLignes() );
				if(ligneAlert.getLignes() != null){
					String []ligneLargeName = new String[ligneAlert.getLignes().length];
					DBCollection lignes = db.getCollection(NomCollectionMongoDB.LIGNE);
					for (int i = 0; i < ligneAlert.getLignes().length; i++) {
						DBObject ligneDbObject = lignes.findOne( new BasicDBObject().
								append("short_name", ligneAlert.getLignes()[i])
								);
						if (ligneDbObject != null) {
							ligneLargeName[i] = (String) ligneDbObject.get(LigneAttribut.LIGNE_ID);
							
						}
						else{
							ligneLargeName[i] = null;
						}
					}
					basicDBObject.append(LigneAttribut.LIGNE_ID, ligneLargeName);
				}
				
				collection.insert(basicDBObject);
			}
			
		}
		
	}

}
