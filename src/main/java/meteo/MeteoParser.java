package meteo;


import org.json.JSONObject;

import com.mongodb.BasicDBObject;

public class MeteoParser {
	public static BasicDBObject parser(JSONObject jsonObject) {
//		String[] names=JSONObject.getNames(jsonObject);
//		String dateJson=DateMeteoManager.getMeteoDate(names);
		JSONObject json;
		JSONObject tempEtEtat;
		double humidity;
		double temperature;
		String textEtat;
		try{
//			json = jsonObject.getJSONObject(dateJson);
//			temperature = json.getJSONObject("temperature");
			json=jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel");
			humidity=json.getJSONObject("atmosphere").getDouble("humidity");
			tempEtEtat=json.getJSONObject("item").getJSONObject("condition");
			temperature=tempEtEtat.getDouble("temp");
			textEtat=tempEtEtat.getString("text");
			
		}
		catch (Exception je){
			je.getStackTrace();
			return null;
		}

		BasicDBObject meteoMongo = new BasicDBObject();
//		meteoMongo.append("date", json.toString());
//		meteoMongo.append("temperature",MeteoUtils.convertCelsiusKelvin((temperature.getDouble("2m")), false)).toString();
//		meteoMongo.append("pluie",json.getDouble("pluie"));
//		meteoMongo.append("risque_neige", json.getString("risque_neige"));
		meteoMongo.append("temperature",MeteoUtils.convertCelsiusToOtherUnit(temperature, "f", false)).toString();
		meteoMongo.append("humidite",humidity).toString();
		meteoMongo.append("meteo", textEtat);
		return meteoMongo;
	}

}
