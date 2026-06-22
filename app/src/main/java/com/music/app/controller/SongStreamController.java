package com.music.app.controller;


import com.music.app.service.SongStreamService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stream")
public class SongStreamController {

    private final SongStreamService songStreamService;

    public SongStreamController(SongStreamService songStreamService) {
        this.songStreamService = songStreamService;
    }

    /**
     * GET /api/stream/{id}
     *
     * Devuelve el audio de una canción en streaming con soporte de Range requests.
     * Requiere autenticación JWT.
     *
     * El cliente envía: Range: bytes=0-
     * El servidor responde: 206 Partial Content con el fragmento solicitado.
     *
     * Esto permite al cliente saltar a cualquier punto de la canción.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResourceRegion> stream(
            @PathVariable Long id,
            @RequestHeader HttpHeaders headers,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Obtiene el recurso de audio y calcula la región a servir según el Range header
        SongStreamService.StreamContext ctx = songStreamService.resolveStream(id, headers);

        return ResponseEntity
                .status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaType.parseMediaType(ctx.mimeType()))
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(ctx.region().getCount()))
                .body(ctx.region());
    }
    /**
     * Utilizo el v2 para la aplicación con JavaFX ya que el reproductor no acepta ResourceRegion como respuesta del endpoint
     */
    @GetMapping("/v2/{id}")
    public ResponseEntity<Resource> stream2(@PathVariable Long id) {

        Resource resource = songStreamService.loadSimpleAsResource(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .body(resource);
    }
}