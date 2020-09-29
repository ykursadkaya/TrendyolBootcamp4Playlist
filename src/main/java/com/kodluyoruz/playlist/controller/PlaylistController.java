package com.kodluyoruz.playlist.controller;

import com.couchbase.client.core.error.CouchbaseException;
import com.kodluyoruz.playlist.exception.PlaylistNotFoundException;
import com.kodluyoruz.playlist.exception.TrackNotFoundException;
import com.kodluyoruz.playlist.model.Playlist;
import com.kodluyoruz.playlist.model.Track;
import com.kodluyoruz.playlist.service.PlaylistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/playlists")
public class PlaylistController
{
	private final PlaylistService playlistService;

	public PlaylistController(PlaylistService playlistService)
	{
		this.playlistService = playlistService;
	}

	@PostMapping
	public ResponseEntity<Void> create(@RequestBody Playlist playlist)
	{
		try
		{
			playlistService.create(playlist);
			URI location = URI.create(String.format("/playlists/%s", playlist.getId()));
			return ResponseEntity.created(location).build();
		}
		catch (CouchbaseException e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Playlist> getPlaylist(@PathVariable("id") String playlistId)
	{
		try
		{
			Playlist playlist = playlistService.findById(playlistId);
			return ResponseEntity.ok(playlist);
		}
		catch (PlaylistNotFoundException e)
		{
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping
	public ResponseEntity<List<Playlist>> getUsersAllPlaylists(@RequestParam("user-id") String userId)
	{
		try
		{
			List<Playlist> playlists = playlistService.findAllByUserId(userId);
			return ResponseEntity.ok(playlists);
		}
		catch (CouchbaseException e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity deletePlaylist(@PathVariable("id") String playlistId)
	{
		try
		{
			playlistService.delete(playlistId);
			return ResponseEntity.noContent().build();
		}
		catch (PlaylistNotFoundException e)
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@PostMapping("/{id}/tracks")
	public ResponseEntity addTracksToPlaylist(@PathVariable("id") String playlistId, @RequestBody List<Track> tracks)
	{
		try
		{
			playlistService.addTracks(playlistId, tracks);
			return ResponseEntity.ok().build();
		}
		catch (PlaylistNotFoundException e)
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@DeleteMapping("/{id}/tracks")
	public ResponseEntity deleteTrackFromPlaylist(@PathVariable("id") String playlistId, @RequestBody Track track)
	{
		try
		{
			playlistService.deleteTrack(playlistId, track);
			return ResponseEntity.ok().build();
		}
		catch (PlaylistNotFoundException | TrackNotFoundException e)
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
}
