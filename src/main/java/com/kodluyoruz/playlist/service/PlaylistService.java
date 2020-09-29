package com.kodluyoruz.playlist.service;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.kodluyoruz.playlist.exception.PlaylistNotFoundException;
import com.kodluyoruz.playlist.exception.TrackNotFoundException;
import com.kodluyoruz.playlist.model.Playlist;
import com.kodluyoruz.playlist.model.Track;
import com.kodluyoruz.playlist.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService
{
	@Qualifier("couchbaseRepository")
	private final PlaylistRepository playlistRepository;

	public PlaylistService(PlaylistRepository playlistRepository)
	{
		this.playlistRepository = playlistRepository;
	}

	public void create(Playlist playlist)
	{
		playlistRepository.insert(playlist);
	}

	public Playlist findById(String playlistId) throws PlaylistNotFoundException
	{
		Optional<Playlist> playlist = playlistRepository.findById(playlistId);
		if (playlist.isEmpty())
		{
			throw new PlaylistNotFoundException(String.format("Playlist ID: %s not found!", playlistId));
		}

		return playlist.get();
	}

	public List<Playlist> findAllByUserId(String userId)
	{
		return playlistRepository.findAllByUserId(userId);
	}

	public void addTracks(String playlistId, List<Track> tracks) throws PlaylistNotFoundException
	{
		Playlist playlist = findById(playlistId);
		playlist.getTracks().addAll(tracks);
		playlistRepository.update(playlist);
	}

	public void deleteTrack(String playlistId, Track track) throws PlaylistNotFoundException, TrackNotFoundException
	{
		Playlist playlist = findById(playlistId);
		if (!playlist.getTracks().remove(track))
		{
			throw new TrackNotFoundException(String.format("Track name: %s not found on playlist!", track.getName()));
		}
		playlistRepository.update(playlist);
	}

	public void delete(String playlistId) throws PlaylistNotFoundException
	{
		try
		{
			playlistRepository.delete(playlistId);
		}
		catch (DocumentNotFoundException e)
		{
			throw new PlaylistNotFoundException(String.format("Playlist ID: %s not found!", playlistId));
		}
	}
}
