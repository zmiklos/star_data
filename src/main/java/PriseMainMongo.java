import java.util.Arrays;
import java.util.List;

import keolis.mongoDB.ClientMongoDB;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;


public class PriseMainMongo {
	private static Logger logger = Logger.getLogger("PriseMainMongo");
	
	public static void main(String[] args) {
		// Set up a simple configuration that logs on the console.
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO); //will not show debug messages
        
    	ClientMongoDB mongoClient = ClientMongoDB.getInstance();
    	//MongoClient mongoClient = new MongoClient(); //  connecting to a local instance on the default port
		
    	mongoClient.setDB("Demo");
    	DB database = mongoClient.getDB();
    	DBCollection collection = database.getCollection("people");
    	
    	// Creating a document
    	List<Integer> books = Arrays.asList(27464, 747854);
    	DBObject person = new BasicDBObject("_id", "jo")
    	                            .append("name", "Jo Bloggs")
    	                            .append("address", new BasicDBObject("street", "123 Fake St")
    	                                                         .append("city", "Faketon")
    	                                                         .append("state", "MA")
    	                                                         .append("zip", 12345))
    	                            .append("books", books);
    	collection.drop();
    	collection.insert(person);
    	
    	DBObject query = new BasicDBObject("_id", "jo");
    	DBCursor cursor = collection.find(query);
    	//DBObject jo = cursor.one();

    	//System.out.println( "Hello "+ (String)jo.get("name") );
    	logger.info("Hello "+ (String)cursor.one().get("name"));
    	
    	cursor.close();
    	mongoClient.close();
	}

}
