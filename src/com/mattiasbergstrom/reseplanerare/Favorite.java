package com.mattiasbergstrom.reseplanerare;

import java.io.Serializable;

/**
 * Represents a user favorite search
 */
public class Favorite implements Serializable {
	
	private static final long serialVersionUID = -5111917433806344018L;
	private String from;
	private String to;
	private int fromId;
	private int toId;
	
	/**
	 * Overrides equals to compare IDs in the favorite
	 */
	@Override
	public boolean equals(Object o) {
		Favorite other = (Favorite) o;
		return (this.toId == other.toId && this.fromId == other.fromId);
	}
	
	/* Getters / setters */
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public int getFromId() {
		return fromId;
	}
	public void setFromId(int fromId) {
		this.fromId = fromId;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public int getToId() {
		return toId;
	}
	public void setToId(int toId) {
		this.toId = toId;
	}
}
