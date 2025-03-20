package com.sedeo.settlement.repository.db.modelmapper;

import com.sedeo.settlement.repository.db.model.ExchangeEntity;
import com.sedeo.domain.settlement.model.Exchange;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ExchangeMapper {

    ExchangeMapper INSTANCE = Mappers.getMapper(ExchangeMapper.class);

    Exchange exchangeEntityToExchange(ExchangeEntity exchangeEntity);

    List<Exchange> exchangeEntityListToExchangeList(List<ExchangeEntity> exchangeEntities);
}
