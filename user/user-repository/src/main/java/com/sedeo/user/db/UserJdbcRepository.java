package com.sedeo.user.db;

import com.sedeo.common.error.DatabaseError.DatabaseWriteUnsuccessfulError;
import com.sedeo.common.error.DatabaseError.DatabaseReadUnsuccessfulError;
import com.sedeo.common.error.GeneralError;
import com.sedeo.user.db.mapper.FriendInvitationMapper;
import com.sedeo.user.db.mapper.UserMapper;
import com.sedeo.user.db.model.FriendInvitationEntity;
import com.sedeo.user.db.model.FriendshipEntity;
import com.sedeo.user.db.model.UserEntity;
import com.sedeo.user.db.queries.Query;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.sedeo.user.db.queries.Query.*;
import static java.lang.Boolean.TRUE;
import static org.springframework.dao.support.DataAccessUtils.singleResult;

public class UserJdbcRepository implements UserRepository {

    private static final UserMapper USER_MAPPER = new UserMapper();
    private static final FriendInvitationMapper FRIEND_INVITATION_MAPPER = new FriendInvitationMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(UserJdbcRepository.class);

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public UserJdbcRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public Either<GeneralError, UserEntity> findUser(UUID userId) {
        return Try.of(() -> singleResult(jdbcTemplate.query(Query.USER_BY_ID, USER_MAPPER, userId)))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, List<UserEntity>> findUsers(List<UUID> userIds) {
        SqlParameterSource parameters = new MapSqlParameterSource(Map.of(Query.USER_IDS_PARAMETER, userIds));
        return Try.of(() -> namedParameterJdbcOperations.query(Query.USERS_BY_IDS, parameters, USER_MAPPER))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, UserEntity> findUser(String email) {
        return Try.of(() -> singleResult(jdbcTemplate.query(USER_BY_EMAIL, USER_MAPPER, email)))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, List<UserEntity>> findUsersFriends(UUID userId) {
        return Try.of(() -> jdbcTemplate.query(Query.FRIENDS_BY_USER_IDS, USER_MAPPER, userId, userId, userId))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, List<UserEntity>> findFriendInvitationUsers(UUID userId, FriendInvitationEntity.InvitationStatus invitationStatus) {
        return Try.of(() -> jdbcTemplate.query(Query.FRIEND_INVITATIONS_BY_USER_ID, USER_MAPPER, userId, userId, invitationStatus.toString()))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, List<UserEntity>> findUsersPotentialFriends(UUID userId, String searchPhrase) {
        SqlParameterSource parameters = new MapSqlParameterSource(Map.of(Query.CURRENT_USER_ID_PARAMETER, userId,
                Query.SEARCH_PHRASE_PARAMETER, "%" + searchPhrase + "%"));
        return Try.of(() -> namedParameterJdbcOperations.query(Query.POTENTIAL_FRIENDS_BY_SEARCH_PHRASE, parameters, USER_MAPPER))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Boolean friendsExist(UUID firstUserId, UUID secondUserId) {
        return TRUE.equals(jdbcTemplate.queryForObject(Query.FRIENDS_EXIST_BY_IDS, Boolean.class, firstUserId, secondUserId, secondUserId, firstUserId));
    }

    @Override
    public Either<GeneralError, FriendInvitationEntity> findFriendInvitation(UUID invitingUserId, UUID requestedUserId, FriendInvitationEntity.InvitationStatus invitationStatus) {
        return Try.of(() -> singleResult(jdbcTemplate.query(Query.FRIEND_INVITATION_BY_USER_IDS, FRIEND_INVITATION_MAPPER,
                        invitingUserId, requestedUserId, invitationStatus.toString())))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, FriendInvitationEntity> saveFriendInvitation(FriendInvitationEntity friendInvitationEntity) {
        return Try.of(() -> jdbcTemplate.update(SAVE_FRIEND_INVITATION,
                        friendInvitationEntity.invitingUserId(),
                        friendInvitationEntity.requestedUserId(),
                        friendInvitationEntity.invitationStatus().toString()))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .onSuccess(success -> LOGGER.info("Friend invitation created for inviting user {} and recipient {}", friendInvitationEntity.invitingUserId(), friendInvitationEntity.requestedUserId()))
                .toEither()
                .map(result -> friendInvitationEntity)
                .mapLeft(DatabaseWriteUnsuccessfulError::new);
    }

    @Override
    public Boolean friendInvitationExists(UUID invitingUser, UUID requestedUserId) {
        return TRUE.equals(jdbcTemplate.queryForObject(Query.FRIEND_INVITATION_EXISTS_BY_IDS, Boolean.class, invitingUser, requestedUserId));
    }

    @Override
    public Either<GeneralError, FriendInvitationEntity> updateFriendInvitation(FriendInvitationEntity friendInvitationEntity) {
        return Try.of(() -> jdbcTemplate.update(UPDATE_FRIEND_INVITATION,
                        friendInvitationEntity.invitingUserId(),
                        friendInvitationEntity.requestedUserId(),
                        friendInvitationEntity.invitationStatus().toString(),
                        friendInvitationEntity.invitingUserId(),
                        friendInvitationEntity.requestedUserId()))
                .onFailure(exception -> LOGGER.error("Database write error occurred", exception))
                .toEither()
                .map(result -> friendInvitationEntity)
                .mapLeft(DatabaseWriteUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, FriendshipEntity> createFriendship(FriendshipEntity friendshipEntity) {
        return Try.of(() -> jdbcTemplate.update(SAVE_FRIENDSHIP,
                        friendshipEntity.firstUserId(),
                        friendshipEntity.secondUserId()))
                .onFailure(exception -> LOGGER.error("Database write error occurred", exception))
                .toEither()
                .map(result -> friendshipEntity)
                .mapLeft(DatabaseWriteUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, UserEntity> updateUser(UserEntity userEntity) {
        return Try.of(() -> jdbcTemplate.update(UPDATE_USER,
                        userEntity.firstName(),
                        userEntity.lastName(),
                        userEntity.phoneNumber(),
                        userEntity.email(),
                        userEntity.password(),
                        userEntity.accountBalance(),
                        userEntity.userId()))
                .onFailure(exception -> LOGGER.error("Database write error occurred", exception))
                .toEither()
                .map(result -> userEntity)
                .mapLeft(DatabaseWriteUnsuccessfulError::new);
    }

    @Override
    public Boolean userExists(String email, String phoneNumber) {
        return TRUE.equals(jdbcTemplate.queryForObject(Query.USER_EXISTS_BY_EMAIL_OR_PHONE_NUMBER, Boolean.class, email, phoneNumber));
    }

    @Override
    public Either<GeneralError, UserEntity> createUser(UserEntity userEntity) {
        return Try.of(() -> jdbcTemplate.update(SAVE_USER, userEntity.userId(), userEntity.firstName(), userEntity.lastName(),
                        userEntity.phoneNumber(), userEntity.email(), userEntity.password(), userEntity.accountBalance()))
                .onFailure(exception -> LOGGER.error("Database write error occurred", exception))
                .toEither()
                .map(result -> userEntity)
                .mapLeft(DatabaseWriteUnsuccessfulError::new);
    }
}
