package gtfs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * Cette classe va t�l�charger le .zip GTFS, puis le d�zipper dans un dossier.
 * @author JackPhillips
 *
 */
public class DownloadAndDezip {
	private static Logger logger = Logger.getLogger("gtfs.DownloadAndDezip");

	public static void downloadAndDezip(String url){
		//Telechargement.
        //String url = "http://data.keolis-rennes.com/fileadmin/OpenDataFiles/GTFS/GTFS-20150120.zip";
        logger.info("Downloading GTFS files...");
        Download.getFile(url);
        logger.info("...GTFS files downloaded.");
        logger.info("\t File name : "+Download.fileName);
        logger.info("\t File length : "+ Download.fileLength+ " octets");
        
		//"Dezippage".
		//System.out.println("\n");
		//File zipfile = new File("GTFS-20150120.zip");
		File zipfile = new File(Download.fileName);
        File folder = new File("GTFS_Files_Folder");
        logger.info("Dezipping GTFS files...");
        try {
			Dezipping.unzip(zipfile, folder);
		} catch (FileNotFoundException e) {
			logger.fatal("File not found !", e);
		} catch (IOException e) {
			logger.fatal("IO Exception", e);
		}
        logger.info("...GTFS files dezipped.");
	}
}
