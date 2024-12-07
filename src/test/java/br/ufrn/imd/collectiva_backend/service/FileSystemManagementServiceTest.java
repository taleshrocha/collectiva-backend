package br.ufrn.imd.collectiva_backend.service;

import br.ufrn.imd.collectiva_backend.dto.FileDownloadResponseDTO;
import br.ufrn.imd.collectiva_backend.utils.FileUtil;
import br.ufrn.imd.collectiva_backend.utils.exception.FileOperationException;
import br.ufrn.imd.collectiva_backend.utils.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class FileSystemManagementServiceTest {

    @InjectMocks
    private FileSystemManagementService fileService;

    @Mock
    FileUtil fileUtilMock;

    private Path testFilePath;
    String path = "test-path";

    @BeforeEach
    public void createTestFile() throws IOException {
        String fileName = "test-file.txt";

        Files.createDirectories(Paths.get(path));
        testFilePath = Paths.get(path, fileName);
        Files.createFile(testFilePath);
    }

    @AfterEach
    public void removeTestFile() throws IOException {
        Files.deleteIfExists(testFilePath);
        Files.deleteIfExists(Path.of(path));
    }

    @Test
    void testUpload() throws IOException {
        String fileName = "test-file-upload.txt";

        MultipartFile file = new MockMultipartFile("file", fileName, "text/plain", "Hello, World!".getBytes());

        String uploadedFilePath = fileService.upload(file, path);

        Path pathUpload = Path.of(path, fileName);
        assertThat(uploadedFilePath).isEqualTo(pathUpload.toString());
        Files.deleteIfExists(Path.of(path, fileName));
    }

    @Test
    void testDownload() throws IOException {
        String filePath = "test-path/test-file.txt";
        Path path = Path.of(filePath);

        when(fileUtilMock.createUrlResource(any())).thenReturn(new UrlResource(path.toUri()));
        FileDownloadResponseDTO response = fileService.download(filePath);

        assertThat(response.fileName()).isEqualTo("test-file.txt");
        assertThat(response.resource()).isNotNull();
    }

    @Test
    void testRemoveSuccess() throws IOException {
        String path = "test-path";
        String fileName = "test-file.txt";

        fileService.remove(path, fileName);

        Path expectedPath = Paths.get(path, fileName);
        verify(fileUtilMock).deleteFile(expectedPath);
    }

    @Test
    void testDownloadError() {
        String filePath = "non-existent-file.txt";

        assertThatThrownBy(() -> fileService.download(filePath))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testRemoveError() {
        String path = "non-existent-directory";
        String filename = "non-existent-file.txt";

        assertThatThrownBy(() -> fileService.remove(path, filename))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testRemoveIOException() throws IOException {

        FileSystemManagementService fileService = new FileSystemManagementService(fileUtilMock);

        String filename = "test-file.txt";
        Path filePath = Paths.get(path, filename);

        doThrow(new IOException("Simulated IOException")).when(fileUtilMock).deleteFile(filePath);

        assertThatThrownBy(() -> fileService.remove(path, filename))
                .isInstanceOf(FileOperationException.class)
                .hasMessage("Erro ao tentar remover arquivo!");
    }

    @Test
    void testUploadIOException() throws IOException {

        FileSystemManagementService fileService = new FileSystemManagementService(fileUtilMock);

        MultipartFile uploadFile = new MockMultipartFile("file", "test-file.txt", "text/plain",
                "Hello, World!".getBytes());
        doThrow(new IOException("Simulated IOException")).when(fileUtilMock).copyFile(isNotNull(), any());

        assertThatThrownBy(() -> fileService.upload(uploadFile, path))
                .isInstanceOf(FileOperationException.class)
                .hasMessage("Falha ao salvar arquivo!");

    }

    @Test
    void testDownloadWithIOException() throws IOException {

        FileSystemManagementService fileService = new FileSystemManagementService(fileUtilMock);

        when(fileUtilMock.createUrlResource(any(URI.class))).thenThrow(new IOException("Erro simulado"));

        var filePath = String.valueOf(testFilePath);

        assertThatThrownBy(() -> fileService.download(filePath))
                .isInstanceOf(FileOperationException.class)
                .hasMessage("Erro ao tentar fazer download do arquivo!");

    }
}
