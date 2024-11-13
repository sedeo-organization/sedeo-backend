package com.sedeo.settlement.model.error;

import com.sedeo.common.error.DomainError;

public interface SettlementGroupError extends DomainError {

    String SETTLEMENT_GROUP_WAS_NOT_FOUND = "Settlement group was not found";
    String USER_IS_NOT_AUTHORIZED_TO_ACCESS_THAT_RESOURCE = "User is not authorized to access that resource";
    String SETTLEMENT_WAS_NOT_FOUND = "Settlement was not found";
    String VALUES_IN_SETTLEMENT_EXCHANGES_AND_SETTLEMENT_ARE_DIFFERENT = "Values in settlement exchanges and settlement are different";
    String SETTLEMENT_EXCHANGE_CANNOT_INCLUDE_THE_SAME_USERS = "Settlement exchange cannot include the same users";
    String SETTLEMENT_GROUP_ALREADY_EXISTS = "Settlement group already exists";
    String SETTLEMENT_ALREADY_EXISTS = "Settlement already exists";

    record SettlementGroupNotFound(String message) implements SettlementGroupError {
        public SettlementGroupNotFound() {
            this(SETTLEMENT_GROUP_WAS_NOT_FOUND);
        }
    }

    record SettlementNotFound(String message) implements SettlementGroupError {
        public SettlementNotFound() {
            this(SETTLEMENT_WAS_NOT_FOUND);
        }
    }

    record UserNotAuthorized(String message) implements SettlementGroupError {
        public UserNotAuthorized() {
            this(USER_IS_NOT_AUTHORIZED_TO_ACCESS_THAT_RESOURCE);
        }
    }

    record SettlementValuesIncorrect(String message) implements SettlementGroupError {
        public SettlementValuesIncorrect() {
            this(VALUES_IN_SETTLEMENT_EXCHANGES_AND_SETTLEMENT_ARE_DIFFERENT);
        }
    }

    record SettlementExchangeDirectionIncorrect(String message) implements SettlementGroupError {
        public SettlementExchangeDirectionIncorrect() {
            this(SETTLEMENT_EXCHANGE_CANNOT_INCLUDE_THE_SAME_USERS);
        }
    }

    record SettlementGroupAlreadyExists(String message) implements SettlementGroupError {
        public SettlementGroupAlreadyExists() {
            this(SETTLEMENT_GROUP_ALREADY_EXISTS);
        }
    }

    record SettlementAlreadyExists(String message) implements SettlementGroupError {
        public SettlementAlreadyExists() {
            this(SETTLEMENT_ALREADY_EXISTS);
        }
    }
}
