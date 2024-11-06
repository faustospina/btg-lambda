package org.btg.model.mapper;

import org.btg.model.documents.Transaccion;
import org.btg.model.dto.TransaccionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransaccionMapper {
    TransaccionDTO toDTO(Transaccion document);
    Transaccion toDocument(TransaccionDTO dto);
}
