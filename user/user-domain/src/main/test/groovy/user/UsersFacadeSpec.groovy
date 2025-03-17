package user

import com.sedeo.user.db.PasswordResetTokenRepository
import com.sedeo.user.db.UserRepository
import com.sedeo.user.facade.UsersFacade
import com.sedeo.user.model.PasswordResetToken
import com.sedeo.user.model.TokenStatus
import com.sedeo.user.model.User
import com.sedeo.user.model.error.PasswordResetTokenError
import com.sedeo.user.model.error.UserError
import io.vavr.control.Either
import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

import java.time.LocalDateTime

class UsersFacadeSpec extends Specification {

    UserRepository userRepository = Mock()
    PasswordResetTokenRepository passwordResetTokenRepository = Mock()
    ApplicationEventPublisher applicationEventPublisher = Mock()
    UsersFacade usersFacade = new UsersFacade(userRepository, passwordResetTokenRepository, applicationEventPublisher)

    def "should fetch user by id"() {
        given: "a user"
        def usersId = UUID.randomUUID()
        def user = new User(usersId, "John", "Doe", "515863777", "test@example.com",
                "examplePassword", new BigDecimal("100.00"))

        and: "user is found in the repository"
        1 * userRepository.findUser(usersId) >> Either.right(user)

        when: "trying to fetch the user by id"
        def result = usersFacade.fetchUser(usersId)

        then: "the user is successfully retrieved"
        result.isRight()
        result.get() == user
    }

    def "should return error when user is not found by id"() {
        given: "non-existent users id is provided"
        def userId = UUID.randomUUID()

        and: "user is not found in the repository"
        1 * userRepository.findUser(userId) >> Either.right(null)

        when: "trying to fetch the user by id"
        def result = usersFacade.fetchUser(userId)

        then: "error that user was not found is returned"
        result.isLeft()
        result.getLeft() instanceof UserError.UserNotFoundError
    }

    def "should fetch user by email"() {
        given: "a user"
        def usersEmail = "example@gmail.com"
        def user = new User(UUID.randomUUID(), "John", "Doe", "515863777", usersEmail,
                "examplePassword", new BigDecimal("100.00"))

        and: "user is found in the repository"
        1 * userRepository.findUser(usersEmail) >> Either.right(user)

        when: "trying to fetch the user by email"
        def result = usersFacade.fetchUser(usersEmail)

        then: "the user is successfully retrieved"
        result.isRight()
        result.get() == user
    }

    def "should return error when user is not found by email"() {
        given: "non-existent users email is provided"
        def usersEmail = "someanotheremail@gmail.com"

        and: "user is not found in the repository"
        1 * userRepository.findUser(usersEmail) >> Either.right(null)

        when: "trying to fetch the user by email"
        def result = usersFacade.fetchUser(usersEmail)

        then: "error that user was not found is returned"
        result.isLeft()
        result.getLeft() instanceof UserError.UserNotFoundError
    }

    def "should add to user's account balance"() {
        given: "a user and positive amount to add"
        def userId = UUID.randomUUID()
        def amount = new BigDecimal("50.19")
        def user = new User(userId, "Adam", "Savage", "876349107", "yetanothermail@example.com",
                "hashedPassword", new BigDecimal("151.34"))

        and: "user exists in the repository and balance is updated"
        1 * userRepository.findUser(userId) >> Either.right(user)

        and: "users account balance is updated"
        User updatedUser = null
        1 * userRepository.updateUser(_) >> { User u ->
            updatedUser = u
            Either.right(u)
        }

        when: "trying to add amount to user's balance"
        def result = usersFacade.addToUsersAccountBalance(userId, amount)

        then: "the balance is updated successfully"
        result.isRight()
        result.get() == null
        updatedUser.accountBalance() == new BigDecimal('201.53')
    }

    def "should subtract from user's account balance"() {
        given: "a user and a positive amount to subtract are provided"
        def userId = UUID.randomUUID()
        def user = new User(userId, "Adam", "Corey", "767891234", "somethingis@gmail.com",
                "someHashedPassword123", new BigDecimal("125.31"))
        def amount = new BigDecimal("11.81")

        and: "user exists in the repository and balance is updated"
        1 * userRepository.findUser(userId) >> Either.right(user)

        and: "users account balance is updated"
        User updatedUser = null
        1 * userRepository.updateUser(_) >> { User u ->
            updatedUser = u
            Either.right(u)
        }

        when: "trying to subtract amount from user's balance"
        def result = usersFacade.subtractFromUsersAccountBalance(userId, amount)

        then: "the balance is updated successfully"
        result.isRight()
        result.get() == null
        updatedUser.accountBalance() == new BigDecimal('113.50')
    }

    def "should successfully use a valid and unused password reset token"() {
        given: "a valid and unused password reset token"
        def token = new PasswordResetToken(UUID.randomUUID(), UUID.randomUUID(), "July", "Flower",
                "flower@yahoo.com", LocalDateTime.now().plusMinutes(5), TokenStatus.UNUSED)

        when: "trying to use the token"
        def result = token.useToken()

        then: "the token is successfully marked as used"
        result.isRight()
        result.get().tokenStatus() == TokenStatus.USED
    }

    def "should fail to use an already used password reset token"() {
        given: "a password reset token that has already been used"
        def token = new PasswordResetToken(UUID.randomUUID(), UUID.randomUUID(), "July", "Flower",
                "flower@yahoo.com", LocalDateTime.now().plusMinutes(5), TokenStatus.USED)

        when: "trying to use the token"
        def result = token.useToken()

        then: "an error is returned indicating the token was already used"
        result.isLeft()
        result.getLeft() instanceof PasswordResetTokenError.TokenUsedAlready
    }

    def "should fail to use an expired password reset token"() {
        given: "an expired password reset token"
        def token = new PasswordResetToken(UUID.randomUUID(), UUID.randomUUID(), "July", "Flower",
                "flower@yahoo.com", LocalDateTime.now().minusMinutes(1), TokenStatus.UNUSED)

        when: "trying to use the token"
        def result = token.useToken()

        then: "an error is returned indicating the token has expired"
        result.isLeft()
        result.getLeft() instanceof PasswordResetTokenError.PasswordChangeTimePassed
    }

    def "should successfully reset user's password given that the token is valid"() {
        given: "a valid and unused password reset token and a user"
        def tokenId = UUID.randomUUID()
        def userId = UUID.randomUUID()
        def token = new PasswordResetToken(tokenId, userId, "July", "Flower", "flower@yahoo.com",
                LocalDateTime.now().plusHours(1), TokenStatus.UNUSED)
        def user = new User(userId, "Adam", "Corey", "767891234", "somethingis@gmail.com",
                "someHashedPassword123", new BigDecimal("125.31"))
        def newPassword = "newSecurePassword"

        and: "repository returns the valid token"
        1 * passwordResetTokenRepository.find(tokenId) >> Either.right(token)

        and: "the updated token is returned"
        1 * passwordResetTokenRepository.update(_) >> Either.right(token)

        and: "a user is found in the repository"
        1 * userRepository.findUser(userId) >> Either.right(user)

        and: "the user's password is updated in the repository"
        User updatedUser = null
        1 * userRepository.updateUser(_) >> { User u ->
            updatedUser = u.withNewPassword(newPassword)
            Either.right(updatedUser)
        }

        when: "attempting to reset the user's password"
        def result = usersFacade.changeUsersPassword(tokenId, newPassword)

        then: "the password is successfully updated"
        result.isRight()
        result.get() instanceof User
        updatedUser.password() == newPassword
    }
}
