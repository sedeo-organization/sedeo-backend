package com.sedeo.friend.repository.db;

import com.sedeo.common.error.DatabaseError;
import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.friend.db.FriendshipRepository;
import com.sedeo.friend.repository.db.mapper.FriendshipMapper;
import com.sedeo.domain.friend.model.Friendship;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.UUID;

import static com.sedeo.friend.repository.db.queries.FriendshipQuery.*;
import static java.lang.Boolean.TRUE;

public class FriendshipJdbcRepository implements FriendshipRepository {

    private static final FriendshipMapper FRIENDSHIP_MAPPER = new FriendshipMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(FriendshipJdbcRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public FriendshipJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Boolean friendshipExists(UUID firstUserId, UUID secondUserId) {
        return TRUE.equals(jdbcTemplate.queryForObject(FRIENDSHIP_EXISTS_BY_IDS, Boolean.class, firstUserId, secondUserId, secondUserId, firstUserId));
    }

    @Override
    public Either<GeneralError, Friendship> save(Friendship friendship) {
        return Try.of(() -> jdbcTemplate.update(SAVE_FRIENDSHIP,
                        friendship.friendshipId(),
                        friendship.firstUserId(),
                        friendship.secondUserId()))
                .onFailure(exception -> LOGGER.error("Database write error occurred", exception))
                .toEither()
                .map(result -> friendship)
                .mapLeft(DatabaseError.DatabaseWriteUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, List<Friendship>> findUsersFriendships(UUID userId) {
        return Try.of(() -> jdbcTemplate.query(FIND_FRIENDSHIPS_BY_USER_ID, FRIENDSHIP_MAPPER, userId, userId))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseError.DatabaseReadUnsuccessfulError::new);
    }

}
