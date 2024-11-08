package org.btg.model.mapper;

import org.btg.model.documents.Cliente;
import org.btg.model.dto.ClienteDTO;
import org.btg.model.dto.TipoTopic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClienteMapper {

    @Mapping(source = "preferenciaNotificacion", target = "preferenciaNotificacion", qualifiedByName = "stringToTipoTopic")
    ClienteDTO toDTO(Cliente document);

    @Mapping(source = "preferenciaNotificacion", target = "preferenciaNotificacion", qualifiedByName = "tipoTopicToString")
    Cliente toDocument(ClienteDTO dto);

    // Convertir de String a TipoTopic
    @Named("stringToTipoTopic")
    default TipoTopic stringToTipoTopic(String preferenciaNotificacion) {
        return TipoTopic.valueOf(preferenciaNotificacion.toLowerCase());
    }

    // Convertir de TipoTopic a String
    @Named("tipoTopicToString")
    default String tipoTopicToString(TipoTopic tipoTopic) {
        return tipoTopic.name();
    }
}
