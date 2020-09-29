package com.kodluyoruz.playlist.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Playlist
{
	private String id;
	private String name;
	private String description;
	private int followersCount;
	private List<Track> tracks;
	private String userId;

	public Playlist()
	{
		this.id = UUID.randomUUID().toString();
		this.name = "";
		this.description = "";
		this.followersCount = 0;
		this.tracks = new ArrayList<Track>();
		this.userId = "";
	}

	public Playlist(String name, String description, List<Track> tracks, String userId)
	{
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.description = description;
		this.followersCount = 0;
		this.tracks = tracks;
		this.userId = userId;
	}
}
