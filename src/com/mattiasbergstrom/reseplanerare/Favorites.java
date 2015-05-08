package com.mattiasbergstrom.reseplanerare;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;

import android.content.Context;
import android.util.Log;

/**
 * Represents user favorites. Can be serialized and saved to file.
 * @author mattiasbergstrom
 *
 */
public class Favorites implements Serializable {
	final static String FILENAME = "favorites";
	/**
	 * 
	 */
	private static final long serialVersionUID = -336717035733608438L;
	private LinkedList<Favorite> favorites;
	
	public Favorites() {
		favorites = new LinkedList<Favorite>();
	}
	
	/**
	 * Loads favorites from storage. Returns the favorites object, or empty favorites if no file exists
	 * @param context The application context
	 * @return The favorites
	 */
	public static Favorites load(Context context) {
		Favorites favorites = null;
		
		FileInputStream fis;
		try {
			fis = context.openFileInput(FILENAME);
			ObjectInputStream in = new ObjectInputStream(fis);
			
			favorites = (Favorites) in.readObject();
			
			in.close();
			fis.close();
		} catch (FileNotFoundException e) {
			Log.w("Favorites", "No favorites file found");
			favorites = new Favorites();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return favorites;
	}
	
	/**
	 * Saves the favorites
	 * @param context The application context
	 */
	public void save(Context context) {
		FileOutputStream fos;
		try {
			fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(this);
			out.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/* Getters / setters */
	
	public LinkedList<Favorite> getFavorites() {
		return favorites;
	}

	public void setFavorites(LinkedList<Favorite> favorites) {
		this.favorites = favorites;
	}
}
