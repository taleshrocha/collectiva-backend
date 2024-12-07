package br.ufrn.imd.collectiva_backend.mappers;

import br.ufrn.imd.collectiva_backend.dto.FileResponseDTO;
import br.ufrn.imd.collectiva_backend.model.File;
import br.ufrn.imd.collectiva_backend.utils.Parser;
import org.springframework.stereotype.Component;


@Component
public class FileMapper implements DTOMapper<File, FileResponseDTO> {

    @Override
    public FileResponseDTO toDTO(File entity) {
        return new FileResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription());
    }

    @Override
    public File toEntity(FileResponseDTO fileResponseDto) {
        return Parser.parse(fileResponseDto, File.class);
    }
}
