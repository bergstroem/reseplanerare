package com.mattiasbergstrom.reseplanerare;

/**
 * Callback for when a favorite needs to be checked. (For example for adjusting the interface
 * depending on the favorite provided).
 * @author mattiasbergstrom
 *
 */
public interface FavoritesCheckCallback {
	public void checkFavorite(Favorite favorite);
}
