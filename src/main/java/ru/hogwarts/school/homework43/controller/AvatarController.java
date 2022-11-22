package ru.hogwarts.school.homework43.controller;

import org.springframework.core.io.Resource;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.homework43.model.Avatar;
import ru.hogwarts.school.homework43.service.AvatarService;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/avatar")
@Validated
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{studentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId, @RequestParam MultipartFile avatar) throws IOException {
        try {
            avatarService.uploadAvatar(studentId, avatar);
            return ResponseEntity.ok("Сохранено!");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/{id}/avatar-from-db")
    public ResponseEntity<byte[]> downloadAvatarFromDb(@PathVariable Long id) {
        Pair<byte[], String> result = avatarService.findAvatarFromDb(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(result.getSecond()))
                .contentLength(result.getFirst().length)
                .body(result.getFirst());
    }

    @GetMapping(value = "/{id}/avatar-from-file")
    public ResponseEntity<Resource> downloadAvatarFromFile(@PathVariable Long id) {
        try {
            Pair<Resource, String> result = avatarService.findAvatarFromFile(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.parseMediaType(result.getSecond()))
                    .contentLength(result.getFirst().contentLength())
                    .body(result.getFirst());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/allAvatars")
    public ResponseEntity<List<Avatar>> findAllWithPagination(@RequestParam @Min(0) int page,
                                                @RequestParam @Min(0) int size) {
        return ResponseEntity.ok(avatarService.findAllWithPagination(page, size));
    }
}
