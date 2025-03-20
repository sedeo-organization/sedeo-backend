package friendships

import com.sedeo.domain.friend.db.FriendshipRepository
import com.sedeo.domain.friend.facade.FriendshipsFacade
import com.sedeo.domain.friend.model.Friendship
import com.sedeo.domain.friend.model.error.FriendError
import io.vavr.control.Either
import spock.lang.Specification

class FriendshipsFacadeSpec extends Specification {

    def friendshipRepository = Mock(FriendshipRepository)
    def friendshipsFacade = new FriendshipsFacade(friendshipRepository)

    def "createFriendship should create a new friendship when it does not exist"() {
        given: "two ids of users who are not friends yet"
        def userId1 = UUID.randomUUID()
        def userId2 = UUID.randomUUID()
        def friendship = Friendship.withRandomId(userId1, userId2)

        and: "the repository returns false that the friendship already exists and returns friendship upon saving"
        1 * friendshipRepository.friendshipExists(userId1, userId2) >> false
        1 * friendshipRepository.save(_) >> Either.right(friendship)

        when: "trying to create a friendship"
        def result = friendshipsFacade.createFriendship(userId1, userId2)

        then: "friendship should be created successfully"
        result.isRight()
        result.get() == friendship
    }

    def "createFriendship should return error when friendship already exists"() {
        given: "two ids of users who are already friends"
        def userId1 = UUID.randomUUID()
        def userId2 = UUID.randomUUID()

        and: "the repository returns true that the friendship already exists and does not save it"
        1 * friendshipRepository.friendshipExists(userId1, userId2) >> true
        0 * friendshipRepository.save(_)

        when: "trying to create a friendship"
        def result = friendshipsFacade.createFriendship(userId1, userId2)

        then: "an error should be returned"
        result.isLeft()
        result.getLeft() instanceof FriendError.FriendsAlreadyExist
    }

    def "fetchUsersFriendships should fetch user's friendships successfully"() {
        given: "id of a user with existing friendships"
        def userId = UUID.randomUUID()
        def friendship1 = Friendship.withRandomId(userId, UUID.randomUUID())
        def friendship2 = Friendship.withRandomId(userId, UUID.randomUUID())
        def friendships = [friendship1, friendship2]

        and: "the repository returns users friendships"
        1 * friendshipRepository.findUsersFriendships(userId) >> Either.right(friendships)

        when: "trying to fetch users friendships"
        def result = friendshipsFacade.fetchUsersFriendships(userId)

        then: "users friendships should be returned correctly"
        result.isRight()
        result.get() == friendships
    }
}
