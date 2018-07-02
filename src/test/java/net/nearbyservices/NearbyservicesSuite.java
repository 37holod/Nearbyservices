package net.nearbyservices;

import net.nearbyservices.client.NearbyservicesTest;
import com.google.gwt.junit.tools.GWTTestSuite;
import junit.framework.Test;
import junit.framework.TestSuite;

public class NearbyservicesSuite extends GWTTestSuite {
	public static Test suite() {
		TestSuite suite = new TestSuite("Tests for Nearbyservices");
		suite.addTestSuite(NearbyservicesTest.class);
		return suite;
	}
}
