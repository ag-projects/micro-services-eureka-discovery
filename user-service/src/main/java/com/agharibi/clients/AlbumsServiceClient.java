package com.agharibi.clients;

import com.agharibi.entities.AlbumResponseModel;
import feign.FeignException;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "albums-ws", fallbackFactory = AlbumsServiceClient.AlbumsFallbackFactory.class)
public interface AlbumsServiceClient {

    @GetMapping(path = "/users/{id}/albums")
    List<AlbumResponseModel> getAlbums(@PathVariable("id") String id);

    @Component
    class AlbumsFallbackFactory implements FallbackFactory<AlbumsServiceClient> {

        @Override
        public AlbumsServiceClient create(Throwable cause) {
            return new AlbumsServiceClientFallback(cause);
        }
    }

    class AlbumsServiceClientFallback implements AlbumsServiceClient {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        private final Throwable cause;

        public AlbumsServiceClientFallback(Throwable cause) {
            this.cause = cause;
        }

        @Override
        public List<AlbumResponseModel> getAlbums(String id) {

            if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
                logger.error("404 error took place when getAlbum was called with userId: " + id
                    + ". Error message: " + cause.getLocalizedMessage());
            } else {
                logger.error("Other error took place: " + cause.getLocalizedMessage());
            }
            return new ArrayList<>();
        }
    }
}
