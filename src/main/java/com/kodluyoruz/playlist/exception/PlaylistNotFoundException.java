package com.kodluyoruz.playlist.exception;

public class PlaylistNotFoundException extends RuntimeException
{
	public PlaylistNotFoundException(String message)
	{
		super(message);
	}
}
