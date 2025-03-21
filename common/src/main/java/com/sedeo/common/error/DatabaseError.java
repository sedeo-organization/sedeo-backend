package com.sedeo.common.error;

public interface DatabaseError extends GeneralError {

    String DATABASE_READ_WAS_UNSUCCESSFUL = "Database read was unsuccessful";
    String DATABASE_WRITE_WAS_UNSUCCESSFUL = "Database write was unsuccessful";

    record DatabaseReadUnsuccessfulError(Error error) implements DatabaseError {
        public DatabaseReadUnsuccessfulError(Throwable throwable) {
            this(new Error(DATABASE_READ_WAS_UNSUCCESSFUL, throwable));
        }
    }

    record DatabaseWriteUnsuccessfulError(Error error) implements DatabaseError {
        public DatabaseWriteUnsuccessfulError(Throwable throwable) {
            this(new Error(DATABASE_WRITE_WAS_UNSUCCESSFUL, throwable));
        }
    }
}
