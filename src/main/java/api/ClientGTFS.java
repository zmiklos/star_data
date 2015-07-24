package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.input.BOMInputStream;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ClientGTFS implements Client {
	private static Logger logger = Logger.getLogger("api.ClientGTFS");

	private String domain;

	public ClientGTFS() throws URISyntaxException{
		this.domain = "http://data.keolis-rennes.com/fileadmin/OpenDataFiles/GTFS/feed";
	}

	
	/**
	 * connexion a l'url
	 * @param pUrlFeed URL du flux
	 * @return doc 
	 */
	public static Document urlConnection(String pUrlFeed){
		Document doc=null;

		try {
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    
		    String newUrlFeed=pUrlFeed.trim().replaceAll(" ", "%20");
		    
			URL url = new URL(newUrlFeed);

			HttpURLConnection urlconnection=(HttpURLConnection) url.openConnection();
			
			urlconnection.addRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
			urlconnection.connect();
			
			InputStream inputStream= urlconnection.getInputStream();
			
			BOMInputStream bomIn = new BOMInputStream(inputStream);
			BufferedReader reader = new BufferedReader(new InputStreamReader(bomIn,"UTF-8"));
			InputSource is = new InputSource(reader);
			is.setEncoding("UTF-8");
			doc = builder.parse(is);
			
			bomIn.close();

		}catch (ParserConfigurationException e) {
			logger.fatal("parseException", e);
		} catch (MalformedURLException e) {
			logger.fatal("malFormedException : url non conforme", e);
		} catch (IOException e) {
			logger.fatal("IOEXception.", e);
		} catch (SAXException e) {
			logger.fatal("saxException : Format XML non valide", e);
		}

		return doc;
	}
	
	/**
	 * pour recuperer l'URL de l'archive gtfs a telecharger
	 * @return l'url de l'archive en String
	 */
	public String getUrlGtfsData(){
		String urlGtfsData="";
		//connexion a l'url
		Document doc=urlConnection(domain);
		if(doc!=null){
			//recuperation de tous les "entry"
			NodeList listNodesEntry= doc.getElementsByTagName("entry");
			int nodesEntrySize=listNodesEntry.getLength();
			
			//recuperer le dernier "entry"
			Node lastEntryNode =  listNodesEntry.item(nodesEntrySize-1);
			
			NodeList listNodesEntryChildren=lastEntryNode.getChildNodes();
			int listNodesEntryChildrenSize=listNodesEntryChildren.getLength();
			
			List<Node> nodeLinkList=new ArrayList<Node>();
			
			for(int i=0;i<listNodesEntryChildrenSize;i++){
				if(listNodesEntryChildren.item(i).getNodeName().equalsIgnoreCase("link")){			
					nodeLinkList.add(listNodesEntryChildren.item(i));
				}
			}

			for (Node node : nodeLinkList) {
				
				NamedNodeMap namednodeMap=node.getAttributes();	
				Node nodeEnclosure=namednodeMap.getNamedItem("rel");
				if(nodeEnclosure.getNodeValue().equalsIgnoreCase("enclosure")){
					Node nodeHref=namednodeMap.getNamedItem("href");
					urlGtfsData=nodeHref.getNodeValue();
				}
			}				
		}
		return urlGtfsData;
	}
	
	
}
