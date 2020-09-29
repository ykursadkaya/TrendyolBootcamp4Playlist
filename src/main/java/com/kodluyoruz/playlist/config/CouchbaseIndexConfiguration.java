package com.kodluyoruz.playlist.config;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.manager.query.CreatePrimaryQueryIndexOptions;
import com.couchbase.client.java.manager.query.CreateQueryIndexOptions;
import com.couchbase.client.java.manager.query.QueryIndexManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

import static com.couchbase.client.java.manager.query.CreatePrimaryQueryIndexOptions.*;
import static com.couchbase.client.java.manager.query.CreateQueryIndexOptions.*;

@Configuration
public class CouchbaseIndexConfiguration
{
	private final Cluster couchbaseCluster;
	private final CouchbaseProperties couchbaseProperties;

	public CouchbaseIndexConfiguration(Cluster couchbaseCluster, CouchbaseProperties couchbaseProperties)
	{
		this.couchbaseCluster = couchbaseCluster;
		this.couchbaseProperties = couchbaseProperties;
	}

	@Bean
	public void createIndexes()
	{
		QueryIndexManager queryIndexManager = couchbaseCluster.queryIndexes();
		queryIndexManager.createPrimaryIndex(couchbaseProperties.getBucketName(), createPrimaryQueryIndexOptions().ignoreIfExists(true));
		queryIndexManager.createIndex(couchbaseProperties.getBucketName(), "userIdx", Collections.singletonList("userId"), createQueryIndexOptions().ignoreIfExists(true));
	}
}
