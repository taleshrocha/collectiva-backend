package br.ufrn.imd.collectiva_backend.service;

import br.ufrn.imd.collectiva_backend.dto.FileDownloadResponseDTO;
import br.ufrn.imd.collectiva_backend.dto.FileInformationDTO;
import br.ufrn.imd.collectiva_backend.dto.FileResponseDTO;
import br.ufrn.imd.collectiva_backend.dto.FileUploadDTO;
import br.ufrn.imd.collectiva_backend.mappers.FileMapper;
import br.ufrn.imd.collectiva_backend.model.File;
import br.ufrn.imd.collectiva_backend.repository.FileRepository;
import br.ufrn.imd.collectiva_backend.utils.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FileService {

    private final FileRepository repository;
    private final FileSystemManagementService fileSystemManagementService;

    private final FileMapper fileDTOMapper;

    @Value("${file.upload-path}")
    private String uploadPath;

    public FileService(FileRepository repository, FileSystemManagementService fileSystemManagementService,
                       FileMapper fileMapper) {
        this.repository = repository;
        this.fileSystemManagementService = fileSystemManagementService;
        this.fileDTOMapper = fileMapper;
    }

    /**
     * Uploads a file based on the provided fileUploadDTO.
     *
     * @param dto The fileUploadDTO containing information about the file
     *            to be uploaded.
     * @return A fileUploadDTO representing the uploaded file's information.
     */
    public FileResponseDTO upload(FileUploadDTO dto) {
        var path = fileSystemManagementService.upload(dto.file(), uploadPath);
        var fileToSave = new File();
        fileToSave.setName(dto.file().getOriginalFilename());
        fileToSave.setPath(path);
        fileToSave.setMimeType(dto.file().getContentType());
        fileToSave.setDescription(dto.description());
        fileToSave.setSize(dto.file().getSize());

        var fileSaved = repository.save(fileToSave);

        return new FileResponseDTO(fileSaved.getId(), fileSaved.getName(), fileSaved.getDescription());
    }

    /**
     * Downloads a file identified by the provided ID.
     *
     * @param id The unique identifier of the file to be downloaded.
     * @return A FileDownloadResponse containing the downloaded file's information.
     */
    public FileDownloadResponseDTO download(Long id) {
        var fileEntity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("O arquivo não foi encontrado!"));
        return fileSystemManagementService.download(fileEntity.getPath());
    }

    /**
     * Deletes a record identified by the provided ID.
     *
     * @param id The unique identifier of the record to be deleted.
     */
    public void deleteById(Long id) {
        var fileEntity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("O arquivo com não foi encontrado"));

        repository.deleteById(id);
        fileSystemManagementService.remove(uploadPath, fileEntity.getName());

    }

    /**
     * Retrieves a paginated list of fileUploadDTO objects.
     *
     * @param pageable The pageable configuration for controlling pagination.
     * @return A Page containing fileUploadDTO objects representing the result
     * set.
     */
    public Page<FileResponseDTO> findAll(Pageable pageable) {
        var entityPage = repository.findAll(pageable);
        return new PageImpl<>(fileDTOMapper.toDTO(entityPage.getContent()), pageable, entityPage.getTotalElements());
    }

    /**
     * Retrieves a list of FileInformationDTO objects based on the provided list of IDs.
     * This method is designed to fetch detailed information for a collection of files
     * identified by their unique IDs. It returns a list of FileInformationDTO, which
     * encapsulates the essential details of each file.
     *
     * @param listId A List of Long values representing the unique identifiers for the files.
     *               These IDs are used to query and retrieve the corresponding file information.
     * @return A List of FileInformationDTO objects containing detailed information
     * about each file identified by the IDs in the provided list. If a file ID
     * does not exist or is not found, it will not be included in the returned list.
     */
    public List<FileInformationDTO> findInformation(List<Long> listId) {
        return repository.findNamesAndDescriptionsByIds(listId);
    }
}
