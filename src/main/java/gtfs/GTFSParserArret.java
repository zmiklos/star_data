
package gtfs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import keolis.util.ArretAttribut;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class GTFSParserArret implements GTFSParser{

	private DBCollection collectionTxtToMongoDB; 

	public GTFSParserArret(DBCollection collectionTxtToMongoDB) {
		this.collectionTxtToMongoDB = collectionTxtToMongoDB;
	}

	public void execute(String path) {

		BufferedReader br = null;
		String line = "";
		String dbSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(path));
			br.readLine();
			while ((line = br.readLine()) != null) {

				String[] routesTab = line.split(dbSplitBy);
				BasicDBObject dbLine = new BasicDBObject(); 
				
				dbLine.append(ArretAttribut.STOP_ID, removeQuotes(routesTab[0]));
				dbLine.append(ArretAttribut.CODE, removeQuotes(routesTab[1]));
				dbLine.append(ArretAttribut.NAME, removeQuotes(routesTab[2]));
				dbLine.append(ArretAttribut.DESC, removeQuotes(routesTab[3]));
				dbLine.append(ArretAttribut.LAT, removeQuotes(routesTab[4]));
				dbLine.append(ArretAttribut.LON, removeQuotes(routesTab[5]));
				dbLine.append(ArretAttribut.ZONE_ID, removeQuotes(routesTab[6]));
				dbLine.append(ArretAttribut.URL, removeQuotes(routesTab[7]));
				dbLine.append(ArretAttribut.LOCATION_TYPE, removeQuotes(routesTab[8]));
				dbLine.append(ArretAttribut.PARENT_STATION, removeQuotes(routesTab[9]));
				dbLine.append(ArretAttribut.TIMEZONE, removeQuotes(routesTab[10]));
				dbLine.append(ArretAttribut.WHEELCHAIR_BOARDING, removeQuotes(routesTab[11]));				
					
				collectionTxtToMongoDB.insert(dbLine);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public String removeQuotes(String param){
		return param.substring(1, param.length()-1);
	}

	
}
