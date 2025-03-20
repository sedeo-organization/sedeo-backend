package friends

import com.sedeo.domain.friend.db.FriendRepository
import com.sedeo.domain.friend.facade.FriendsFacade
import com.sedeo.domain.friend.model.Friend
import com.sedeo.domain.friend.model.error.FriendError
import io.vavr.control.Either
import spock.lang.Specification

class FriendsFacadeSpec extends Specification {

    FriendRepository friendRepository = Mock()
    FriendsFacade friendsFacade = new FriendsFacade(friendRepository)

    def "fetchUsersPotentialFriends should return potential friends"() {
        given: "a userId and search phrase"
        UUID userId = UUID.randomUUID()
        String searchPhrase = "John"

        and: "the repository returns a list of potential friends"
        List<Friend> friendsList = [new Friend(UUID.randomUUID(), "John", "Doe", "512770856")]
        friendRepository.findUsersPotentialFriends(userId, searchPhrase) >> Either.right(friendsList)

        when: "trying to fetch users potential friends"
        def result = friendsFacade.fetchUsersPotentialFriends(userId, searchPhrase)

        then: "the result should contain John only"
        result.isRight()
        result.get() == friendsList
    }

    def "createFriend should return an error if the friend already exists"() {
        given: "a friend"
        Friend friend = new Friend(UUID.randomUUID(), "Jane", "Doe", "347643189")

        and: "the repository returns that the friend already exists"
        friendRepository.friendExistsById(friend.userId()) >> true

        when: "trying to create a new friend"
        def result = friendsFacade.createFriend(friend)

        then: "the result should be a FriendAlreadyExists error"
        result.isLeft()
        result.getLeft() instanceof FriendError.FriendAlreadyExists
    }

    def "createFriend should save the friend if they don't already exist"() {
        given: "a friend"
        Friend friend = new Friend(UUID.randomUUID(), "Jane", "Doe", "875345198")

        and: "the repository returns that the friend does not exist"
        friendRepository.friendExistsById(friend.userId()) >> false
        friendRepository.save(friend) >> Either.right(friend)

        when: "trying to create a new friend"
        def result = friendsFacade.createFriend(friend)

        then: "the friend should be saved successfully"
        result.isRight()
        result.get() == friend
    }

    def "fetchFriends should return an empty list if no userIds are provided"() {
        given: "an empty list of userIds"
        List<UUID> userIds = []

        when: "trying to fetch friends"
        def result = friendsFacade.fetchFriends(userIds)

        then: "the result should be an empty list"
        result.isRight()
        result.get().isEmpty()
    }

    def "fetchFriends should return friends for valid userIds"() {
        given: "a list of userIds"
        List<UUID> userIds = [UUID.randomUUID(), UUID.randomUUID()]

        and: "the repository returns a list of friends"
        List<Friend> friendsList = [new Friend(UUID.randomUUID(), "John", "Doe", "578333567")]
        friendRepository.findFriendsByIds(userIds) >> Either.right(friendsList)

        when: "trying to fetch friends"
        def result = friendsFacade.fetchFriends(userIds)

        then: "the result should contain the list of friends"
        result.isRight()
        result.get() == friendsList
    }
}

