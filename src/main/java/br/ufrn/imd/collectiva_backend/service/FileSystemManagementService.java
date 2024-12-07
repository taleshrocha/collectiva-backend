package br.ufrn.imd.collectiva_backend.service;

import br.ufrn.imd.collectiva_backend.dto.FileDownloadResponseDTO;
import br.ufrn.imd.collectiva_backend.utils.FileUtil;
import br.ufrn.imd.collectiva_backend.utils.exception.FileOperationException;
import br.ufrn.imd.collectiva_backend.utils.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class FileSystemManagementService {

    private static final Logger logger = LoggerFactory.getLogger(FileSystemManagementService.class);
    private final FileUtil fileUtil;

    public FileSystemManagementService(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    /**
     * Performs the upload of a file to the specified location.
     *
     * @param uploadFile The file to be uploaded.
     * @param path       The path to which the file will be uploaded.
     * @return A representation of the upload result.
     */
    public String upload(MultipartFile uploadFile, String path) {
        try {
            Path directory = Path.of(path);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            String originalFileName = Objects.requireNonNull(uploadFile.getOriginalFilename());
            String fileName = originalFileName;
            Path filePath = directory.resolve(fileName);

            while (Files.exists(filePath)) {
                String fileExtension;
                int lastDotIndex = originalFileName.lastIndexOf(".");
                if (lastDotIndex > 0) {
                    fileExtension = originalFileName.substring(lastDotIndex);
                    fileName = originalFileName.substring(0, lastDotIndex) + "-" + System.currentTimeMillis()
                            + fileExtension;
                } else {
                    fileName = originalFileName + "-" + System.currentTimeMillis();
                }
                filePath = directory.resolve(fileName);
            }

            fileUtil.copyFile(uploadFile, filePath);
            logger.info("O arquivo foi registrado: {}", filePath);
            return filePath.toString();
        } catch (IOException e) {
            logger.error("Falha ao salvar arquivo no path: {}", path);
            throw new FileOperationException("Falha ao salvar arquivo!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Downloads a file from the specified path.
     *
     * @param filePath The path to the file to be downloaded.
     * @return A `FileDownloadResponse` object representing the downloaded file.
     */
    public FileDownloadResponseDTO download(String filePath) {
        try {
            Path path = Path.of(filePath);
            if (!Files.exists(path)) {
                logger.warn("Arquivo não encontrado: {}", filePath);
                throw new ResourceNotFoundException("Arquivo não encontrado");
            }

            var resource = fileUtil.createUrlResource(path.toUri());
            return new FileDownloadResponseDTO(resource.getFilename(), resource);

        } catch (IOException e) {
            logger.error("Erro ao tentar fazer download do arquivo: {}", filePath);
            throw new FileOperationException("Erro ao tentar fazer download do arquivo!",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Removes a file with the specified path and filename.
     *
     * @param path     The path of the directory where the file is located.
     * @param filename The name of the file to be removed.
     */
    public void remove(String path, String filename) {
        Path filePath = Paths.get(path, filename);

        if (Files.exists(filePath)) {
            try {
                fileUtil.deleteFile(filePath);
            } catch (IOException e) {
                logger.error("Erro ao tentar remover o arquivo. FilePath: {}", filePath);
                throw new FileOperationException("Erro ao tentar remover arquivo!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.info("Arquivo removido: {}", filePath);
        } else {
            logger.warn("Arquivo não encontrado: {}", filePath);
            throw new ResourceNotFoundException("Arquivo não encontrado!");
        }
    }
}
