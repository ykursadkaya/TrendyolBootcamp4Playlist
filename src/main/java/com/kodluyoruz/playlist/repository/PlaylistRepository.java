package com.kodluyoruz.playlist.repository;

import com.kodluyoruz.playlist.model.Playlist;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository
{
	void insert(Playlist playlist);

	Optional<Playlist> findById(String playlistId);

	List<Playlist> findAllByUserId(String userId);

	void update(Playlist playlist);

	void delete(String playlistId);
}
