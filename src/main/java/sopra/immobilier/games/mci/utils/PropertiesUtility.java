/**
 * 
 */
package sopra.immobilier.games.mci.utils;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author dpont & adolghier
 * 
 */
public class PropertiesUtility {

	private static PropertiesUtility instance;
	protected static Log LOGGER = LogFactory.getLog(PropertiesUtility.class);

	private PropertiesUtility() {
	}

	public static PropertiesUtility getInstance() {
		if (instance == null) {
			instance = new PropertiesUtility();
		}
		return instance;
	}

	public Properties read(String strRepertoireBin, String strPathFichier) throws Exception {
		
		strRepertoireBin = strRepertoireBin.substring(0, strRepertoireBin.lastIndexOf("/"));
		File fileRepertoireBin = new File(new URI(strRepertoireBin));
		File fileProperties = new File(fileRepertoireBin, strPathFichier);
		Properties properties = new Properties();
		properties.load(new FileInputStream(fileProperties));
		return properties;
	}

	public Integer getIntProperty(Properties properties, String property, Integer defaultValue) {
		Integer intVal = Integer.parseInt(properties.getProperty(property));
		return (intVal != null) ? intVal : defaultValue;
	}

	public Integer getIntProperty(Properties properties, String property) {
		Integer intVal = null;
		String strVal = properties.getProperty(property);
		if (strVal == null) {
			LOGGER.info("Paramètre inconnu [" + property + "] dans " + properties);
			return null;
		}

		try {
			intVal = Integer.parseInt(properties.getProperty(property));
		} catch (Exception e) {
			LOGGER.info("Format du paramètre [" + property + "]  invalide : [" + strVal + "]");
			return null;
		}
		return intVal;
	}

}
