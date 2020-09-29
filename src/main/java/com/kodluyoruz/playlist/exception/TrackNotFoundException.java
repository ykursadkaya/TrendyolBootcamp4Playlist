package com.kodluyoruz.playlist.exception;

public class TrackNotFoundException extends RuntimeException
{
	public TrackNotFoundException(String message)
	{
		super(message);
	}
}
