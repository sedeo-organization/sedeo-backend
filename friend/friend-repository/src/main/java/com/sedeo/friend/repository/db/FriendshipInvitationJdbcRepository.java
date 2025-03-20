package com.sedeo.friend.repository.db;

import com.sedeo.common.error.DatabaseError;
import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.friend.db.FriendshipInvitationRepository;
import com.sedeo.friend.repository.db.mapper.FriendshipInvitationMapper;
import com.sedeo.domain.friend.model.FriendshipInvitation;
import com.sedeo.domain.friend.model.InvitationStatus;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.UUID;

import static com.sedeo.friend.repository.db.queries.FriendshipInvitationQuery.*;
import static java.lang.Boolean.TRUE;
import static org.springframework.dao.support.DataAccessUtils.singleResult;

public class FriendshipInvitationJdbcRepository implements FriendshipInvitationRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(FriendshipJdbcRepository.class);
    private static final FriendshipInvitationMapper FRIENDSHIP_INVITATION_MAPPER = new FriendshipInvitationMapper();

    private final JdbcTemplate jdbcTemplate;

    public FriendshipInvitationJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Either<GeneralError, FriendshipInvitation> save(FriendshipInvitation friendshipInvitation) {
        return Try.of(() -> jdbcTemplate.update(SAVE_FRIENDSHIP_INVITATION,
                        friendshipInvitation.invitationId(),
                        friendshipInvitation.invitingUserId(),
                        friendshipInvitation.invitedUserId(),
                        friendshipInvitation.invitationStatus().toString()))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .onSuccess(success -> LOGGER.info("Friend invitation created for inviting user {} and recipient {}",
                        friendshipInvitation.invitingUserId(), friendshipInvitation.invitedUserId()))
                .toEither()
                .map(result -> friendshipInvitation)
                .mapLeft(DatabaseError.DatabaseWriteUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, FriendshipInvitation> update(FriendshipInvitation friendshipInvitation) {
        return Try.of(() -> jdbcTemplate.update(UPDATE_FRIENDSHIP_INVITATION,
                        friendshipInvitation.invitingUserId(),
                        friendshipInvitation.invitedUserId(),
                        friendshipInvitation.invitationStatus().toString(),
                        friendshipInvitation.invitingUserId(),
                        friendshipInvitation.invitedUserId()))
                .onFailure(exception -> LOGGER.error("Database write error occurred", exception))
                .toEither()
                .map(result -> friendshipInvitation)
                .mapLeft(DatabaseError.DatabaseWriteUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, FriendshipInvitation> findByUserIds(UUID invitingUserId, UUID invitedUserId, InvitationStatus invitationStatus) {
        return Try.of(() -> singleResult(jdbcTemplate.query(FRIENDSHIP_INVITATION_BY_USER_IDS, FRIENDSHIP_INVITATION_MAPPER,
                        invitingUserId, invitedUserId, invitationStatus.toString())))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseError.DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, List<FriendshipInvitation>> findUsersIncomingFriendshipInvitationsByUserId(UUID invitedUserId,
                                                                                                           InvitationStatus invitationStatus) {
        return Try.of(() -> jdbcTemplate.query(INCOMING_FRIENDSHIP_INVITATIONS_BY_USER_ID, FRIENDSHIP_INVITATION_MAPPER,
                        invitedUserId, invitationStatus.toString()))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseError.DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, FriendshipInvitation> findById(UUID invitationId, InvitationStatus invitationStatus) {
        return Try.of(() -> singleResult(jdbcTemplate.query(FRIENDSHIP_INVITATIONS_BY_ID, FRIENDSHIP_INVITATION_MAPPER,
                        invitationId, invitationStatus.toString())))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseError.DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Boolean existsByUserIds(UUID invitingUser, UUID invitedUserId) {
        return TRUE.equals(jdbcTemplate.queryForObject(FRIENDSHIP_INVITATION_EXISTS_BY_IDS, Boolean.class, invitingUser, invitedUserId));
    }
}
