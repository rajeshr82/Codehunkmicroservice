package com.apps.codehunk.api.users.data;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.apps.codehunk.api.users.ui.model.AlbumResponseModel;

import feign.FeignException;
import feign.hystrix.FallbackFactory;

//@FeignClient(name = "albums-ws",fallback = AlbumsFallBack.class)
@FeignClient(name = "albums-ws",fallbackFactory = AlbumsFallbackFactory.class)
public interface AlbumsServiceClient {

	@GetMapping("/users/{id}/albums")
	public List<AlbumResponseModel> getAlbums(@PathVariable String id);
}

@Component
class AlbumsFallbackFactory implements FallbackFactory<AlbumsServiceClient>{

	public AlbumsServiceClient create(Throwable cause) {
		return new AlbumsServiceClientCallback(cause);
	}
}

class AlbumsServiceClientCallback implements AlbumsServiceClient{
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Throwable cause;
	public AlbumsServiceClientCallback(Throwable cause) {
		this.cause = cause;
	}

	@Override
	public List<AlbumResponseModel> getAlbums(String id) {
		if(cause instanceof FeignException &&((FeignException)cause).status()==404) {
			logger.error("404 error took place when getAlbuns was called with userId: "+
				id+".Error message: "+
				cause.getLocalizedMessage());
		}else {
			logger.error("Error Occured while calling getAlbums::"+cause.getLocalizedMessage());
		}
		return Collections.<AlbumResponseModel>emptyList();
	}
	
}


/*
 Fallback empty list
 * @Component
 
class AlbumsFallBack implements AlbumsServiceClient{

	@Override
	public List<AlbumResponseModel> getAlbums(String id) {
		// TODO Auto-generated method stub
		return Collections.<AlbumResponseModel>emptyList();
	}
	
}*/