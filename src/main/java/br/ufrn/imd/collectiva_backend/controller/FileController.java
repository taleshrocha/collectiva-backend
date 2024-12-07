package br.ufrn.imd.collectiva_backend.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.imd.collectiva_backend.dto.ApiResponseDTO;
import br.ufrn.imd.collectiva_backend.dto.FileDownloadResponseDTO;
import br.ufrn.imd.collectiva_backend.dto.FileInformationDTO;
import br.ufrn.imd.collectiva_backend.dto.FileResponseDTO;
import br.ufrn.imd.collectiva_backend.dto.FileUploadDTO;
import br.ufrn.imd.collectiva_backend.service.FileService;
import br.ufrn.imd.collectiva_backend.utils.exception.BusinessException;
import br.ufrn.imd.collectiva_backend.utils.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/file")
@Validated
public class FileController {

    private final FileService service;

    public FileController(FileService service) {
        this.service = service;
    }

    /**
     * Retrieves a paginated list of FileResponseDTO objects.
     *
     * @param pageable The pageable configuration for controlling pagination.
     * @return A ResponseEntity containing an ApiResponseDTO with a Page of
     *         FileResponseDTO objects.
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<FileResponseDTO>>> getAll(@ParameterObject Pageable pageable) {

        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                "Success: Entity located successfully.",
                service.findAll(pageable),
                null));
    }

    /**
     * Uploads a file based on the provided FileUploadDTO.
     *
     * @param dto The validated FileUploadDTO containing information about the file
     *            to be uploaded.
     * @return A ResponseEntity containing an ApiResponseDTO with a FileResponseDTO
     *         representing the uploaded file's information.
     */
    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<ApiResponseDTO<FileResponseDTO>> upload(
            @Valid @ModelAttribute FileUploadDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO<>(
                true,
                "Success: File uploaded successfully!",
                service.upload(dto),
                null));
    }

    /**
     * Retrieves and serves a file for inline viewing based on the provided ID.
     *
     * @param id      The unique identifier of the file to be served inline.
     * @param request The HttpServletRequest associated with the request.
     * @return A ResponseEntity containing the file as a Resource for inline
     *         viewing.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Resource> inlineFile(@PathVariable Long id, HttpServletRequest request) {
        return createFileResponse(request, service.download(id), "inline");
    }

    /**
     * Retrieves and serves a file for inline viewing based on the provided ID.
     *
     * @param id      The unique identifier of the file to be served inline.
     * @param name    The name of the file to be served inline.
     * @param request The HttpServletRequest associated with the request.
     * @return A ResponseEntity containing the file as a Resource for inline
     *         viewing.
     */
    @GetMapping("/{id}/{name}")
    public ResponseEntity<Resource> inlineFileInformation(@PathVariable Long id, @PathVariable String name,
            HttpServletRequest request) {
        return createFileResponse(request, service.download(id), "inline");
    }

    /**
     * Retrieves and serves a file for download based on the provided ID.
     *
     * @param id      The unique identifier of the file to be downloaded.
     * @param request The HttpServletRequest associated with the request.
     * @return A ResponseEntity containing the file as a Resource for download.
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id, HttpServletRequest request) {
        return createFileResponse(request, service.download(id), "attachment");
    }

    /**
     * Deletes a file based on the provided ID.
     *
     * @param id The unique identifier of the file to be deleted.
     * @return A ResponseEntity containing an ApiResponseDTO with a FileResponseDTO
     *         representing the deleted file's information.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponseDTO<FileResponseDTO>> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                "Success: Entity has been successfully removed.",
                null,
                null));
    }

    /**
     * Handles a POST request to retrieve information for a list of files based on
     * their IDs.
     * <p>
     * This endpoint processes a list of unique file IDs and returns a list of
     * FileInformationDTOs,
     * each containing detailed information about the corresponding file. The method
     * is useful
     * for bulk retrieval of file data when you have multiple file IDs and need to
     * gather
     * information for each one.
     *
     * @param listId A List of Long values representing the unique identifiers of
     *               the files.
     *               This list is passed in the body of the POST request.
     * @return A ResponseEntity containing an ApiResponseDTO with a List of
     *         FileInformationDTOs.
     *         Each FileInformationDTO in the list provides detailed information
     *         about one of
     *         the files identified by the IDs in the provided list. If an ID does
     *         not correspond
     *         to an existing file, it will not be included in the response.
     */
    @PostMapping("/find-information")
    public ResponseEntity<ApiResponseDTO<List<FileInformationDTO>>> findInformation(@RequestBody List<Long> listId) {
        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                "Success: Entity located successfully.",
                service.findInformation(listId),
                null));
    }

    private ResponseEntity<Resource> createFileResponse(HttpServletRequest request,
            FileDownloadResponseDTO response, String contentDisposition) {
        if (response.resource() == null) {
            throw new ResourceNotFoundException("Arquivo não encontrado");
        }

        File file;
        try {
            file = response.resource().getFile();
        } catch (IOException e) {
            throw new BusinessException("Falha na leitura do arquivo. O arquivo não está disponível no momento.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String contentType = request.getServletContext().getMimeType(file.getAbsolutePath());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        contentDisposition + "; filename=\"" + response.resource().getFilename() + "\"")
                .header(HttpHeaders.PRAGMA, response.resource().getFilename())
                .header(HttpHeaders.EXPIRES, "0")
                .header(HttpHeaders.CACHE_CONTROL, "must-revalidate")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()))
                .body(response.resource());
    }
}
