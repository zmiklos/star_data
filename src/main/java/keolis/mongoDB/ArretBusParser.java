package keolis.mongoDB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import keolis.util.ArretBusAttribut;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;

public class ArretBusParser {
	private static Logger logger = Logger.getLogger("keolis.mongoDB.ArretBusParser");

	public static BasicDBObject parser(JSONObject jsonObject) {
		logger.info("jsonObject : "+jsonObject);
		JSONObject json;
		JSONObject departures;
		JSONArray departure;
		JSONObject value;

		try{
			json = jsonObject.getJSONObject("stopline");
			departures = json.getJSONObject("departures");
			//departures = jsonObject.getJSONObject("departures");
			//System.out.println("departures 2:"+departures.toString());
			departure = departures.getJSONArray("departure");
			value = departure.getJSONObject(0);
		}
		catch (JSONException je){
			return null;
		}

		BasicDBObject arretBusMongo = new BasicDBObject();
		//arretBusMongo.append(ArretBusAttribut.DATE_REQUETE, jsonObject.getJSONObject("@attributes").getString("localdatetime"));
		arretBusMongo.append(ArretBusAttribut.ARRET,jsonObject.getJSONObject("stopline").getString("stop"));
		arretBusMongo.append(ArretBusAttribut.LIGNE,jsonObject.getJSONObject("stopline").getString("route"));
		arretBusMongo.append(ArretBusAttribut.DIRECTION, jsonObject.getJSONObject("stopline").getString("direction"));
		arretBusMongo.append(ArretBusAttribut.EN_TETE, value.getJSONObject("@attributes").getString("headsign"));
		arretBusMongo.append(ArretBusAttribut.NUM_VEHICULE, value.getJSONObject("@attributes").getString("vehicle"));
		arretBusMongo.append(ArretBusAttribut.PRECISION, value.getJSONObject("@attributes").getString("accurate"));
		arretBusMongo.append(ArretBusAttribut.PREVU, value.getJSONObject("@attributes").getString("expected"));
		arretBusMongo.append(ArretBusAttribut.REEL, value.getString("content"));

		try {

			SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss");
			//conversion des strings dates en format Date
			Date dateExpected 	= sdf.parse(value.getJSONObject("@attributes").getString("expected"));
			Date dateReel 	= sdf.parse(value.getString("content"));
			//soustraction des deux dates
			long diff_TR = dateExpected.getTime() - dateReel.getTime();

			arretBusMongo.append(ArretBusAttribut.DIFF_TR, Long.toString(diff_TR/1000));

		} catch (JSONException e) {
			logger.fatal("Erreur du JSON", e);
		} catch (ParseException e) {
			logger.fatal("Erreur de PARSE", e);
		}

		return arretBusMongo;

	}
	/**
	 * Le parseur getbusdeparture en mode "line"
	 * @param jsonObject
	 * @return collection de basicDBObject
	 */
	public static Collection<BasicDBObject> ligneParser(JSONObject jsonObject){
		List<BasicDBObject> basicDBObjects =  new ArrayList<BasicDBObject>();
		JSONArray stopLineJSONArray ;
		try {
			 stopLineJSONArray = jsonObject.getJSONArray("stopline");
		} catch (Exception e) {
			return null;
		}
		if(stopLineJSONArray.length()!=0){
			
			for (int j = 0; j < stopLineJSONArray.length(); j++) {
				JSONObject stopline = stopLineJSONArray.getJSONObject(j);
				JSONObject departures;
				JSONArray departure;
				JSONObject value;
				try{
					departures = stopline.getJSONObject("departures");
					departure = departures.getJSONArray("departure");
					value = departure.getJSONObject(0);
				}
				catch (JSONException je){
					return null;
				}

				BasicDBObject arretBusMongo = new BasicDBObject();
				arretBusMongo.append(ArretBusAttribut.DATE_REQUETE, jsonObject.getJSONObject("@attributes").getString("localdatetime"));
				arretBusMongo.append(ArretBusAttribut.ARRET,stopline.getString("stop"));
				logger.info("arret : "+ stopline.getString("stop"));
				arretBusMongo.append(ArretBusAttribut.LIGNE,stopline.getString("route"));
				arretBusMongo.append(ArretBusAttribut.DIRECTION, stopline.getString("direction"));
				arretBusMongo.append(ArretBusAttribut.EN_TETE, value.getJSONObject("@attributes").getString("headsign"));
				arretBusMongo.append(ArretBusAttribut.NUM_VEHICULE, value.getJSONObject("@attributes").getString("vehicle"));
				arretBusMongo.append(ArretBusAttribut.PRECISION, value.getJSONObject("@attributes").getString("accurate"));
				arretBusMongo.append(ArretBusAttribut.PREVU, value.getJSONObject("@attributes").getString("expected"));
				arretBusMongo.append(ArretBusAttribut.REEL, value.getString("content"));

				try {

					SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss");
					//conversion des strings dates en format Date
					Date dateExpected 	= sdf.parse(value.getJSONObject("@attributes").getString("expected"));
					Date dateReel 	= sdf.parse(value.getString("content"));
					//soustraction des deux dates
					long diff_TR = dateExpected.getTime() - dateReel.getTime();

					arretBusMongo.append(ArretBusAttribut.DIFF_TR, Long.toString(diff_TR/1000));
					basicDBObjects.add(arretBusMongo);
				} catch (JSONException e) {
					logger.fatal("Erreur du JSON", e);
				} catch (ParseException e) {
					logger.fatal("Erreur de PARSE", e);
				}
			}
			return basicDBObjects;
		}

		return null;

	}}