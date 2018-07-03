package net.nearbyservices.shared;


public class Validator {

	  public static boolean isBlank(String value) {
		  return (value == null || "".equals(value.trim()));
	  }
}
