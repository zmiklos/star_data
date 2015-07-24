package keolis.api;

import api.Requete;

public class RequeteLigneAlert extends Requete {
	private String command = "getlinesalerts";
	private String version =  "2.0";
	@Override
	public String getCommand() {
		return this.command;
	}

	@Override
	public String getVersion() {
		return this.version;
	}

	@Override
	public String getRequete() {
		return "&cmd="+ this.getCommand()+ "" + "&version="+ this.getVersion() + this.getParametre();
	}

}
