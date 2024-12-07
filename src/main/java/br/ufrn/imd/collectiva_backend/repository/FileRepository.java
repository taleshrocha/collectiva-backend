package br.ufrn.imd.collectiva_backend.repository;

import br.ufrn.imd.collectiva_backend.dto.FileInformationDTO;
import br.ufrn.imd.collectiva_backend.model.File;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends GenericRepository<File> {
    @Query("SELECT new br.ufrn.imd.collectiva_backend.dto.FileInformationDTO(f.id, f.name, f.description) FROM File f WHERE f.id IN :ids")
    List<FileInformationDTO> findNamesAndDescriptionsByIds(@Param("ids") List<Long> ids);


}
