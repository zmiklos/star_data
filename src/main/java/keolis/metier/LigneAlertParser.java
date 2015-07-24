package keolis.metier;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;

public class LigneAlertParser {

	public static Collection<LigneAlert> parser(JSONObject jsonObject){
		Collection<LigneAlert> ligneAlerts = new ArrayList<LigneAlert>();
		if(jsonObject != null){
			JSONArray alerts = jsonObject.getJSONArray("alert");
			if(alerts.length() != 0){
				for (int i = 0; i < alerts.length(); i++) {
					ligneAlerts.add(LigneAlertParser.alertParse(alerts.getJSONObject(i)));

				}
			}
			return ligneAlerts;
		}
		return null;
	}

	private static LigneAlert alertParse(JSONObject jsonObject) {
		
		if(jsonObject != null){
			LigneAlert ligneAlert = new LigneAlert();
			ligneAlert.setTitle(jsonObject.getString("title"));
			ligneAlert.setStartTime(jsonObject.getString("starttime"));
			ligneAlert.setEndTime(jsonObject.getString("endtime"));
			ligneAlert.setMajordisturbance(jsonObject.getString("majordisturbance"));
			ligneAlert.setDetail(jsonObject.getString("detail"));
			JSONObject lines;
			String [] lignes ;
			try {

				lines = jsonObject.getJSONObject("lines");
				JSONArray itemLines = lines.optJSONArray("line");
				if(itemLines != null){
					lignes = new String[itemLines.length()];
					for (int i = 0; i < itemLines.length(); i++) {
						lignes[i] = (String) itemLines.get(i);
						
						//System.out.println(itemLines.get(i));
					}
					ligneAlert.setLignes(lignes);
				}else{
					String line  = lines.getString("line");
					//System.out.println(line);
					lignes = new String[1];
					lignes[0] = line;
					ligneAlert.setLignes(lignes);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return ligneAlert;
		}
		return null ;
	}

}
