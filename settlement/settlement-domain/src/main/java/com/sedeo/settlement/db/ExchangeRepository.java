package com.sedeo.settlement.db;

import com.sedeo.settlement.model.Exchange;

import java.util.List;
import java.util.UUID;

public interface ExchangeRepository {

    Object save(List<Exchange> exchanges, UUID groupId, UUID settlementId);
}
