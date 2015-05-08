package com.mattiasbergstrom.reseplanerare;

import java.util.Date;

/**
 * Callback for when searching for a route.
 * @author mattiasbergstrom
 *
 */
public interface SearchRouteCallback {
	public void searchRoute(String from, String to, Date date);
}
