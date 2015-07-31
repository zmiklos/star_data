package meteo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.json.JSONObject;

public class MeteoREST{
	private static Logger logger = Logger.getLogger("meteo.MeteoREST");
	private URI domain ;

	public MeteoREST() throws URISyntaxException {
		this.domain = new URI("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20%28select%20woeid%20from%20geo.places%281%29%20where%20text%3D%22Rennes%22%29&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");
	}

	public URI getDomain() {
		return domain;
	}
	public void setDomain(URI domain) {
		this.domain = domain;
	}
	public  JSONObject execute() throws URISyntaxException, HttpException, IOException{

		String requeteHTTP = "";
		HttpClient client = HttpClientBuilder.create().build();
		//construction de la requete 
		requeteHTTP = this.getDomain().toString();
		logger.debug(requeteHTTP);
		HttpGet request = new HttpGet(requeteHTTP);
		HttpResponse response = client.execute(request);
		BufferedReader br =  new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		String jsonStringLine = br.readLine();
		JSONObject jsonObject  = new JSONObject(jsonStringLine);
		return jsonObject;
	}

}
