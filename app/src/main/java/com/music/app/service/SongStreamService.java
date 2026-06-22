package com.music.app.service;

import com.music.app.model.Song;
import com.music.app.repository.SongRepository;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;

@Service
public class SongStreamService {

    private static final long CHUNK_SIZE = 1024 * 1024;

    private final SongRepository songRepository;

    public SongStreamService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    /**
     * Resuelve qué fragmento del archivo de audio debe servirse,
     * basándose en el header Range enviado por el cliente.
     *
     * Si no hay header Range, devuelve el primer chunk desde el inicio.
     */
    public StreamContext resolveStream(Long songId, HttpHeaders headers) {
        Song song = songRepository.findById(songId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Canción no encontrada"));

        Resource resource = loadResource(song);
        long contentLength = getContentLength(resource);

        ResourceRegion region = buildRegion(resource, contentLength, headers);
        String mimeType = resolveMimeType(song);

        return new StreamContext(region, mimeType);
    }


    private Resource loadResource(Song song) {
        Resource resource = new FileSystemResource(song.getFilePath());

        if (!resource.exists() || !resource.isReadable()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Archivo de audio no encontrado en el servidor");
        }

        return resource;
    }

    private long getContentLength(Resource resource) {
        try {
            return resource.contentLength();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al leer el archivo de audio");
        }
    }

    /**
     * Calcula la región del archivo a servir según el header Range.
     *
     * Ejemplo de Range header: "bytes=0-"       → desde el inicio
     *                          "bytes=1048576-" → desde el segundo megabyte (seeking)
     *                          Sin header       → primer chunk desde cero
     */
    private ResourceRegion buildRegion(Resource resource, long contentLength, HttpHeaders headers) {
        List<HttpRange> ranges = headers.getRange();

        if (ranges.isEmpty()) {
            // Sin Range header: devuelve el primer chunk
            long rangeLength = Math.min(CHUNK_SIZE, contentLength);
            return new ResourceRegion(resource, 0, rangeLength);
        }

        // Con Range header: respeta la posición que pide el cliente
        HttpRange range = ranges.get(0);
        long start = range.getRangeStart(contentLength);
        long end   = range.getRangeEnd(contentLength);


        System.out.println(">>> contentLength: " + contentLength);
        System.out.println(">>> start: " + start + " end: " + end); // 👈

        // Limita el chunk al tamaño máximo para no enviar el archivo completo
        long rangeLength = Math.min(CHUNK_SIZE, end - start + 1);

        return new ResourceRegion(resource, start, rangeLength);
    }

    private String resolveMimeType(Song song) {
        if (song.getMimeType() != null && !song.getMimeType().isBlank()) {
            return song.getMimeType();
        }
        return "audio/mpeg";
    }


    public record StreamContext(ResourceRegion region, String mimeType) {}

    /**
     * Metodo publico para utilizar con la aplicación JavaFX
     *
     *
     */
    public Resource loadSimpleAsResource(Long id) {

        Song song = songRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Resource resource = new FileSystemResource(song.getFilePath());

        if (!resource.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return resource;
    }


}