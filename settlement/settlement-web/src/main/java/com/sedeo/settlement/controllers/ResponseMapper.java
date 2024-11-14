package com.sedeo.settlement.controllers;

import com.sedeo.common.error.ErrorResponse;
import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.model.error.SettlementGroupError;
import org.springframework.http.ResponseEntity;

public class ResponseMapper {

    private static final String UNEXPECTED_ERROR_OCCURRED = "Unexpected error occurred";

    public static ResponseEntity<ErrorResponse> mapError(GeneralError error) {
        if (error instanceof SettlementGroupError.UserNotAuthorized userNotAuthorized) {
            return ErrorResponse.notAuthorized(userNotAuthorized.message());
        } else if (error instanceof SettlementGroupError.SettlementNotFound settlementNotFound) {
            return ErrorResponse.notFound(settlementNotFound.message());
        } else if (error instanceof SettlementGroupError.SettlementValuesIncorrect settlementValuesIncorrect) {
            return ErrorResponse.conflict(settlementValuesIncorrect.message());
        } else if (error instanceof SettlementGroupError.SettlementExchangeDirectionIncorrect settlementExchangeDirectionIncorrect) {
            return ErrorResponse.conflict(settlementExchangeDirectionIncorrect.message());
        } else if (error instanceof SettlementGroupError.SettlementAlreadyExists settlementAlreadyExists) {
            return ErrorResponse.conflict(settlementAlreadyExists.message());
        } else if (error instanceof SettlementGroupError.SettlementGroupAlreadyExists settlementGroupAlreadyExists) {
            return ErrorResponse.conflict(settlementGroupAlreadyExists.message());
        } else if (error instanceof SettlementGroupError.ExchangeDoesNotExist exchangeDoesNotExist) {
            return ErrorResponse.notFound(exchangeDoesNotExist.message());
        } else if (error instanceof SettlementGroupError.ExchangeStatusChangeNotAllowed exchangeStatusChangeNotAllowed) {
            return ErrorResponse.conflict(exchangeStatusChangeNotAllowed.message());
        } else {
            return ErrorResponse.databaseError(UNEXPECTED_ERROR_OCCURRED);
        }
    }
}
