package keolis.api;

import api.Requete;


public class RequeteArretBus extends Requete {

	private String version = "2.2";
	private String command = "getbusnextdepartures";
	public RequeteArretBus(String version, String command) {
		this.version = version;
		this.command = command;
	}

	public RequeteArretBus() {
		super();
	}
	public String getCommand() {
		return this.command;
	}

	public String getVersion() {
		return this.version;
	}

	public String getRequete() {
		return "&cmd="+ this.getCommand()+ "" + "&version="+ this.getVersion() + this.getParametre();
	}


}
