package com.sedeo.settlement.repository.db.modelmapper;

import com.sedeo.settlement.repository.db.model.ParticipantEntity;
import com.sedeo.domain.settlement.model.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ParticipantMapper {

    ParticipantMapper INSTANCE = Mappers.getMapper(ParticipantMapper.class);

    Participant participantEntityToParticipant(ParticipantEntity participantEntity);

    @Mapping(source = "settlementStatus", target = "status")
    List<Participant> participantEntitiesToParticipants(List<ParticipantEntity> participantEntities);
}
