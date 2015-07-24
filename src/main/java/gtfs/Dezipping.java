package gtfs;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * Cetten classe va dezipper le fichier .zip GTFS.
 * @author JackPhillips thanks to patatos.over-blog.com
 *
 */
public class Dezipping {

	  /**
     * d�compresse le fichier zip dans le r�pertoire donn�
     * @param folder le r�pertoire o� les fichiers seront extraits
     * @param zipfile le fichier zip � d�compresser
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void unzip(File zipfile, File folder) throws FileNotFoundException, IOException{

        // cr�ation de la ZipInputStream qui va servir � lire les donn�es du fichier zip
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(
                        new FileInputStream(zipfile.getCanonicalFile())));

        // extractions des entr�es du fichiers zip (i.e. le contenu du zip)
        ZipEntry ze = null;
        try {
            while((ze = zis.getNextEntry()) != null){

                // Pour chaque entr�e, on cr�e un fichier
                // dans le r�pertoire de sortie "folder"
                File f = new File(folder.getCanonicalPath(), ze.getName());
           
                // Si l'entr�e est un r�pertoire,
                // on le cr�e dans le r�pertoire de sortie
                // et on passe � l'entr�e suivante (continue)
                if (ze.isDirectory()) {
                    f.mkdirs();
                    continue;
                }
               
                // L'entr�e est un fichier, on cr�e une OutputStream
                // pour �crire le contenu du nouveau fichier
                f.getParentFile().mkdirs();
                OutputStream fos = new BufferedOutputStream(
                        new FileOutputStream(f));
           
                // On �crit le contenu du nouveau fichier
                // qu'on lit � partir de la ZipInputStream
                // au moyen d'un buffer (byte[])
                try {
                    try {
                        final byte[] buf = new byte[8192];
                        int bytesRead;
                        while (-1 != (bytesRead = zis.read(buf)))
                            fos.write(buf, 0, bytesRead);
                    }
                    finally {
                        fos.close();
                    }
                }
                catch (final IOException ioe) {
                    // en cas d'erreur on efface le fichier
                    f.delete();
                    throw ioe;
                }
            }
        }
        finally {
            // fermeture de la ZipInputStream
            zis.close();
        }
    }
    

}
