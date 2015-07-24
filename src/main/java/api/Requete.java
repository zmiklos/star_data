package api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class  Requete {
	private Collection<Parametre> parametres;
	public abstract String getCommand();
	public abstract String getVersion();
	public abstract String getRequete();

	public Requete(){
		this.parametres = new ArrayList<Parametre>();
	}
	public String getParametre() {
		String params = "";
		if(this.parametres.size()!=0){
			Iterator<Parametre> itParametre = this.parametres.iterator();
			while (itParametre.hasNext()){
				Parametre parametre = itParametre.next();
				params += "&param["+ parametre.getKey()+"]="+ parametre.getValue();
			}
			return params;	
		}
		return null;
	}
	public void addParametre(String key, String value){
		this.parametres.add(new Parametre(key, value));
	}
	public boolean removeParam(String key){
		return true;
	}
}
