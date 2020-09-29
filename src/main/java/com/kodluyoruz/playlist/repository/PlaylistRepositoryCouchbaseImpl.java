package com.kodluyoruz.playlist.repository;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryResult;
import com.kodluyoruz.playlist.model.Playlist;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.couchbase.client.java.query.QueryOptions.*;

@Repository("couchbaseRepository")
public class PlaylistRepositoryCouchbaseImpl implements PlaylistRepository
{
	private final Cluster couchbaseCluster;
	private final Collection playlistCollection;

	public PlaylistRepositoryCouchbaseImpl(Cluster couchbaseCluster, Collection playlistCollection)
	{
		this.couchbaseCluster = couchbaseCluster;
		this.playlistCollection = playlistCollection;
	}

	@Override
	public void insert(Playlist playlist)
	{
		playlistCollection.insert(playlist.getId(), playlist);
	}

	@Override
	public Optional<Playlist> findById(String playlistId)
	{
		try
		{
			GetResult getResult = playlistCollection.get(playlistId);
			Playlist playlist = getResult.contentAs(Playlist.class);
			return Optional.of(playlist);
		}
		catch (DocumentNotFoundException e)
		{
			return Optional.empty();
		}
	}

	@Override
	public List<Playlist> findAllByUserId(String userId)
	{
		String statement = "SELECT `playlist`.* FROM `playlist` WHERE userId=$userId";
		QueryOptions queryOptions = queryOptions().parameters(JsonObject.create().put("userId", userId));
		QueryResult queryResult = couchbaseCluster.query(statement, queryOptions);

		return queryResult.rowsAs(Playlist.class);
	}

	@Override
	public void update(Playlist playlist)
	{
		playlistCollection.replace(playlist.getId(), playlist);
	}

	@Override
	public void delete(String playlistId)
	{
		playlistCollection.remove(playlistId);
	}
}
