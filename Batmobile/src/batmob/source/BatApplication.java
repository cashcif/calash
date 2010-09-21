package batmob.source;

import java.util.List;

import org.openintents.intents.WikitudePOI;

import android.app.Application;

/**
 * The application object which holds information about all objects needed to be
 * exchanged in the application
 */
public class BatApplication extends Application {

	/** the POIs */
	private List<WikitudePOI> pois;

	public List<WikitudePOI> getPois() {
		return pois;
	}

	public void setPois(List<WikitudePOI> pois) {
		this.pois = pois;
	}

}
