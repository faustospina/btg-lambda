package org.btg.model.mapper;

import org.btg.model.documents.Cliente;
import org.btg.model.dto.ClienteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClienteMapper {
    ClienteDTO toDTO(Cliente document);
    Cliente toDocument(ClienteDTO dto);
}
