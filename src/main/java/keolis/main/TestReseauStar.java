package keolis.main;

import java.io.IOException;
import java.net.URISyntaxException;

import keolis.mongoDB.ClientMongoDB;
import meteo.MeteoREST;

import org.apache.http.HttpException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import api.ClientREST;

public class TestReseauStar {
	private static Logger logger = Logger.getLogger("keolis.main.TestReseauStar");

	public static void main(String[] args) throws URISyntaxException, HttpException, IOException {
		long start =  System.currentTimeMillis();
		// Set up a simple configuration that logs on the console.
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO); //will not show debug messages
        
		ReseauStar reseauStar = new ReseauStar(new ClientREST(), ClientMongoDB.getInstance(), new MeteoREST());
		
		reseauStar.execute();
		logger.info("Durée : " + (long) ( System.currentTimeMillis() - start)/1000);
	}

}
