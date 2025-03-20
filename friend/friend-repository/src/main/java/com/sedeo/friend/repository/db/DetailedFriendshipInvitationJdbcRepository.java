package com.sedeo.friend.repository.db;

import com.sedeo.common.error.DatabaseError;
import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.friend.db.DetailedFriendshipInvitationRepository;
import com.sedeo.friend.repository.db.mapper.DetailedFriendshipInvitationMapper;
import com.sedeo.domain.friend.model.DetailedFriendshipInvitation;
import com.sedeo.domain.friend.model.InvitationStatus;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.UUID;

import static com.sedeo.friend.repository.db.queries.DetailedFriendshipInvitationQuery.FIND_DETAILED_FRIENDSHIP_INVITATIONS;

public class DetailedFriendshipInvitationJdbcRepository implements DetailedFriendshipInvitationRepository {

    private static final DetailedFriendshipInvitationMapper DETAILED_FRIENDSHIP_INVITATION_MAPPER = new DetailedFriendshipInvitationMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(DetailedFriendshipInvitationJdbcRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public DetailedFriendshipInvitationJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Either<GeneralError, List<DetailedFriendshipInvitation>> find(UUID invitedUserId, InvitationStatus invitationStatus) {
        return Try.of(() -> jdbcTemplate.query(FIND_DETAILED_FRIENDSHIP_INVITATIONS, DETAILED_FRIENDSHIP_INVITATION_MAPPER, invitedUserId, invitationStatus.toString()))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseError.DatabaseReadUnsuccessfulError::new);
    }
}
