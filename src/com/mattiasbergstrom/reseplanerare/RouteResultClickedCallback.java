package com.mattiasbergstrom.reseplanerare;

import com.mattiasbergstrom.resrobot.Route;

/**
 * Callback for when a route result item has been clicked
 * @author mattiasbergstrom
 *
 */
public interface RouteResultClickedCallback {
	public void routeResultClicked(Route route);
}
