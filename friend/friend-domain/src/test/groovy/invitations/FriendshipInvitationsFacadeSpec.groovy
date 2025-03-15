package invitations


import com.sedeo.friend.db.FriendshipInvitationRepository
import com.sedeo.friend.db.FriendshipRepository
import com.sedeo.friend.facade.FriendshipInvitationsFacade
import com.sedeo.friend.facade.Friendships
import com.sedeo.friend.model.FriendshipInvitation
import com.sedeo.friend.model.InvitationStatus
import com.sedeo.friend.model.error.FriendError
import io.vavr.control.Either
import spock.lang.Specification
import spock.lang.Unroll

import static com.sedeo.friend.model.InvitationStatus.PENDING

class FriendshipInvitationsFacadeSpec extends Specification {

    def friendshipInvitationRepository = Mock(FriendshipInvitationRepository)
    def friendshipRepository = Mock(FriendshipRepository)
    def friendships = Mock(Friendships)
    def facade = new FriendshipInvitationsFacade(friendshipInvitationRepository, friendshipRepository, friendships)

    def "should create a friendship invitation when none exists"() {
        given: "two users who are not already friends and have no pending invitations"
        def inviterId = UUID.randomUUID()
        def inviteeId = UUID.randomUUID()
        def invitation = new FriendshipInvitation(UUID.randomUUID(), inviterId, inviteeId, PENDING)

        and:
        "friendshipRepository returns false that the friendship exists, returns Either.right(null) when trying" +
                " to find friendship and returns invitation when saving friendship"
        1 * friendshipRepository.friendshipExists(inviterId, inviteeId) >> false
        1 * friendshipInvitationRepository.findByUserIds(inviterId, inviteeId, PENDING) >> Either.right(null)
        1 * friendshipInvitationRepository.save(_) >> Either.right(invitation)

        when: "trying to create a friendship invitation"
        def result = facade.createFriendshipInvitation(inviterId, inviteeId)

        then: "a new invitation is created and returned"
        result.isRight()
        result.get() == invitation
    }

    def "should not create an invitation if users are already friends"() {
        given: "two users who are already friends"
        def inviterId = UUID.randomUUID()
        def inviteeId = UUID.randomUUID()

        and: "repository returns true that friendship exists"
        1 * friendshipRepository.friendshipExists(inviterId, inviteeId) >> true
        0 * friendshipInvitationRepository.findByUserIds(_, _, _)
        0 * friendshipInvitationRepository.save(_)

        when: "trying to create a friendship invitation"
        def result = facade.createFriendshipInvitation(inviterId, inviteeId)

        then: "friendship is created"
        result.isLeft()
        result.getLeft() instanceof FriendError.FriendsAlreadyExist
    }

    def "should not create an invitation if one is already pending"() {
        given: "a pending invitation already exists"
        def inviterId = UUID.randomUUID()
        def inviteeId = UUID.randomUUID()

        and: "repository returns false that friendship exists and user invitation is already pending"
        1 * friendshipRepository.friendshipExists(inviterId, inviteeId) >> false
        1 * friendshipInvitationRepository.findByUserIds(inviterId, inviteeId, PENDING) >> Either.right(new FriendshipInvitation(UUID.randomUUID(), inviterId, inviteeId, PENDING))
        0 * friendshipInvitationRepository.save(_)

        when: "trying to create a friendship invitation"
        def result = facade.createFriendshipInvitation(inviterId, inviteeId)

        then: "An error is returned indicating a pending invitation already exists"
        result.isLeft()
        result.getLeft() instanceof FriendError.FriendInvitationIsPending
    }

    @Unroll
    def "should return error when invitation does not exist or user is not the invitee"() {
        given: "invitedUserId"
        def invitedUserId = UUID.randomUUID()

        and: "repository returns an invitation or null"
        1 * friendshipInvitationRepository.findById(invitationId, PENDING) >> Either.right(invitation)
        0 * friendshipInvitationRepository.update(_)

        when: "trying to change friendship invitation status"
        def result = facade.changeFriendshipInvitationStatus(invitationId, invitedUserId, InvitationStatus.ACCEPTED)

        then: "an error is returned indicating the invitation was not found or status change is not allowed"
        result.isLeft()
        result.getLeft() == expectedError

        where:
        wrongUserId       | invitationId      | invitation                                                                      | expectedError
        UUID.randomUUID() | UUID.randomUUID() | null                                                                            | new FriendError.FriendshipInvitationNotFound()
        UUID.randomUUID() | UUID.randomUUID() | new FriendshipInvitation(invitationId, wrongUserId, UUID.randomUUID(), PENDING) | new FriendError.FriendInvitationStatusChangeNotAllowed()
    }

    def "should accept a friendship invitation and create a friendship"() {
        given: "a valid invitation and invitee"
        def invitationId = UUID.randomUUID()
        def inviter = UUID.randomUUID()
        def invitee = UUID.randomUUID()
        def invitation = new FriendshipInvitation(invitationId, inviter, invitee, PENDING)
        def updatedInvitation = new FriendshipInvitation(invitationId, inviter, invitee, InvitationStatus.ACCEPTED)

        and: "repository returns the invitation, updates it and creates the friendship"
        1 * friendshipInvitationRepository.findById(invitationId, PENDING) >> Either.right(invitation)
        1 * friendshipInvitationRepository.update(_) >> Either.right(updatedInvitation)
        1 * friendships.createFriendship(invitee, inviter) >> Either.right(null)

        when: "the invitee accepts the invitation"
        def result = facade.changeFriendshipInvitationStatus(invitationId, invitee, InvitationStatus.ACCEPTED)

        then: "The invitation status is updated and a friendship is created"
        result.isRight()
        result.get() == updatedInvitation
    }

    def "should decline a friendship invitation"() {
        given: "a valid invitation and invitee"
        def invitationId = UUID.randomUUID()
        def inviter = UUID.randomUUID()
        def invitee = UUID.randomUUID()
        def invitation = new FriendshipInvitation(invitationId, inviter, invitee, PENDING)
        def updatedInvitation = new FriendshipInvitation(invitationId, inviter, invitee, InvitationStatus.REJECTED)

        and: "repository returns the invitation, updates it but does not create a friendship"
        1 * friendshipInvitationRepository.findById(invitationId, PENDING) >> Either.right(invitation)
        1 * friendshipInvitationRepository.update(_) >> Either.right(updatedInvitation)
        0 * friendships.createFriendship(_, _)

        when: "the invitee tries to decline the invitation"
        def result = facade.changeFriendshipInvitationStatus(invitationId, invitee, InvitationStatus.REJECTED)

        then: "the invitation status is updated to declined"
        result.isRight()
        result.get() == updatedInvitation
    }
}