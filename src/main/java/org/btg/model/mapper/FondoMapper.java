package org.btg.model.mapper;

import org.btg.model.documents.Fondo;
import org.btg.model.dto.FondoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FondoMapper {
    FondoDTO toDTO(Fondo document);
    Fondo toDocument(FondoDTO dto);
}
