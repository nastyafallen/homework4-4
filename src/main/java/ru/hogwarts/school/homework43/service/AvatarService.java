package ru.hogwarts.school.homework43.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.homework43.exception.StudentNotFoundException;
import ru.hogwarts.school.homework43.model.Avatar;
import ru.hogwarts.school.homework43.model.Student;
import ru.hogwarts.school.homework43.repository.AvatarRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final StudentService studentService;

    @Value("/avatars")
    private String avatarsDir;

    @Autowired
    public AvatarService(AvatarRepository avatarRepository,
                         StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }

    public Pair<byte[], String> findAvatarFromDb(Long studentId) {
        Avatar avatar = avatarRepository.findAvatarByStudentId(studentId)
                .orElseThrow(StudentNotFoundException::new);
        return Pair.of(avatar.getData(), avatar.getMediaType());
    }

    public Pair<Resource, String> findAvatarFromFile(Long studentId) {
        Avatar avatar = avatarRepository.findAvatarByStudentId(studentId)
                .orElseThrow(StudentNotFoundException::new);
        return Pair.of(new FileSystemResource(avatar.getFilePath()), avatar.getMediaType());
    }

    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        Student student = studentService.getStudent(studentId);
        Path filePath = Paths.get(avatarsDir).resolve(
                student.getName() + "." + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                OutputStream outputStream = Files.newOutputStream(filePath);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream((int) avatarFile.getSize())) {
            avatarFile.getInputStream().transferTo(byteArrayOutputStream);
            byte[] avatarBytes = byteArrayOutputStream.toByteArray();

            outputStream.write(avatarBytes);

            Avatar avatar = avatarRepository.findAvatarByStudentId(studentId).orElse(new Avatar());
            avatar.setStudent(student);
            avatar.setFilePath(filePath.toString());
            avatar.setFileSize(avatarFile.getSize());
            avatar.setMediaType(avatarFile.getContentType());
            avatar.setData(avatarFile.getBytes());
            avatarRepository.save(avatar);
        }
    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public List<Avatar> findAllWithPagination(int page, int size) {
        return avatarRepository.findAll(PageRequest.of(page, size)).get()
                .collect(Collectors.toList());
    }
}
