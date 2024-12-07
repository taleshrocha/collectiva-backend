package br.ufrn.imd.collectiva_backend.service;

import br.ufrn.imd.collectiva_backend.dto.FileDownloadResponseDTO;
import br.ufrn.imd.collectiva_backend.dto.FileResponseDTO;
import br.ufrn.imd.collectiva_backend.dto.FileUploadDTO;
import br.ufrn.imd.collectiva_backend.mappers.FileMapper;
import br.ufrn.imd.collectiva_backend.model.File;
import br.ufrn.imd.collectiva_backend.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    @Mock
    private FileRepository repository;

    @Mock
    private FileSystemManagementService fileManagementService;

    @Mock
    private FileMapper fileDtoMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpload() {
        MultipartFile multipartFile = new MockMultipartFile("file", "test-file.txt", "text/plain", "Hello, World!".getBytes());
        FileUploadDTO fileUploadDto = new FileUploadDTO(multipartFile, "Test description");

        String uploadPath = "/uploads";
        String uploadedFilePath = "/uploads/test-file.txt";

        File savedFile = new File();
        savedFile.setId(1L);
        savedFile.setName("test-file.txt");
        savedFile.setPath(uploadedFilePath);
        savedFile.setMimeType("text/plain");
        savedFile.setDescription("Test description");
        savedFile.setSize(12L);

        when(fileManagementService.upload(multipartFile, uploadPath)).thenReturn(uploadedFilePath);
        when(repository.save(any(File.class))).thenReturn(savedFile);

        FileResponseDTO responseDto = fileService.upload(fileUploadDto);

        assertThat(responseDto.id()).isEqualTo(1L);
        assertThat(responseDto.name()).isEqualTo("test-file.txt");
        assertThat(responseDto.description()).isEqualTo("Test description");
    }

    @Test
    void testDownload() {
        Long fileId = 1L;
        File fileEntity = new File();
        fileEntity.setId(fileId);
        fileEntity.setName("test-file.txt");
        fileEntity.setPath("/uploads/test-file.txt");

        when(repository.findById(fileId)).thenReturn(Optional.of(fileEntity));
        UrlResource urlResource = mock(UrlResource.class);
        when(fileManagementService.download(fileEntity.getPath())).thenReturn(new FileDownloadResponseDTO("test-file.txt", urlResource));

        FileDownloadResponseDTO response = fileService.download(fileId);

        assertThat(response.fileName()).isEqualTo("test-file.txt");
        assertThat(response.resource()).isEqualTo(urlResource);
    }

    @Test
    void testDeleteById() {
        Long fileId = 1L;
        File fileEntity = new File();
        fileEntity.setId(fileId);
        fileEntity.setName("test-file.txt");
        fileEntity.setPath("/uploads/test-file.txt");

        when(repository.findById(fileId)).thenReturn(Optional.of(fileEntity));

        fileService.deleteById(fileId);

        verify(repository).findById(fileId);
        verify(repository).deleteById(fileId);
    }

    @Test
    void testFindAll() {
        Pageable pageable = Pageable.unpaged();

        File fileEntity = new File();
        fileEntity.setId(1L);
        fileEntity.setName("test-file.txt");
        fileEntity.setPath("/uploads/test-file.txt");
        fileEntity.setMimeType("text/plain");
        fileEntity.setDescription("Test description");
        fileEntity.setSize(12L);

        Page<File> fileEntityPage = new PageImpl<>(Collections.singletonList(fileEntity));
        FileResponseDTO dtoRes = new FileResponseDTO(1L, "test-file.txt", "Test description");

        when(repository.findAll(pageable)).thenReturn(fileEntityPage);
        when(fileDtoMapper.toDTO(fileEntityPage.getContent())).thenReturn(Collections.singletonList(dtoRes));

        Page<FileResponseDTO> responseDtoPage = fileService.findAll(pageable);

        assertThat(responseDtoPage.getTotalElements()).isEqualTo(1);
        assertThat(responseDtoPage.getContent()).hasSize(1);
        assertThat(responseDtoPage.getContent().get(0).id()).isEqualTo(1L);
        assertThat(responseDtoPage.getContent().get(0).name()).isEqualTo("test-file.txt");
        assertThat(responseDtoPage.getContent().get(0).description()).isEqualTo("Test description");
    }
}
