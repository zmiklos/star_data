package gtfs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;


/**
 * Cetten classe va telecharger le fichier .zip GTFS.
 * @author JackPhillips thanks to respawner.fr/
 * 
 *
 */
public class Download
{
	private static Logger logger = Logger.getLogger("gtfs.Download");
	
	static int fileLength;
	static String fileName;
	
	public static void getFile(String host)
	{
		InputStream input = null;
		FileOutputStream writeFile = null;
		
		URLConnection connection;
		
		
		try
		{
			URL url = new URL(host);
			connection = url.openConnection();
			fileLength = connection.getContentLength();

			if (fileLength == -1)
			{
				logger.error("Invalide URL or file.");
				return;
			}

			input = connection.getInputStream();
			fileName = url.getFile().substring(url.getFile().lastIndexOf('/') + 1);
			writeFile = new FileOutputStream(fileName);
			byte[] buffer = new byte[1024];
			int read;

			while ((read = input.read(buffer)) > 0)
				writeFile.write(buffer, 0, read);
			writeFile.flush();
		}
		catch (IOException e)
		{
			logger.fatal("Error while trying to download the file.", e);
		}
		finally
		{
			try
			{
				writeFile.close();
				input.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}