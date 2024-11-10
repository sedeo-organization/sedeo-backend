package com.sedeo.settlement.db.modelmapper;

import com.sedeo.settlement.db.model.ExchangeEntity;
import com.sedeo.settlement.model.Exchange;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ExchangeMapper {

    ExchangeMapper INSTANCE = Mappers.getMapper(ExchangeMapper.class);

    Exchange exchangeEntityToExchange(ExchangeEntity ExchangeEntity);

    List<Exchange> exchangeEntityListToExchangeList(List<ExchangeEntity> exchangeEntities);
}
