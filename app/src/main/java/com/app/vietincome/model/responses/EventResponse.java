package com.app.vietincome.model.responses;

import com.app.vietincome.model.Event;
import com.app.vietincome.model.Metadata;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EventResponse {

	@SerializedName("_metadata")
	@Expose
	private Metadata metadata;
	@SerializedName("records")
	@Expose
	private ArrayList<Event> events;

	public ArrayList<Event> getEvents() {
		return events;
	}

	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
}
