package org.btg.model.mapper;

import org.btg.model.documents.Fondo;
import org.btg.model.documents.Transaccion;
import org.btg.model.dto.FondoDTO;
import org.btg.model.dto.TransaccionDTO;
import org.btg.model.dto.TransaccionesResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransaccionMapper {
    TransaccionDTO toDTO(Transaccion document);
    Transaccion toDocument(TransaccionDTO dto);

    TransaccionesResponse reporte(Transaccion transaccion, Fondo fondo);
}
