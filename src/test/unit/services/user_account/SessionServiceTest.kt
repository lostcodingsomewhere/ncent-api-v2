package test.unit.services.user_account

import framework.models.idValue
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import io.kotlintest.Description
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import kotlinserverless.framework.services.SOAResultType
import main.daos.ApiCred
import main.daos.UserAccount
import main.services.user_account.EndSessionService
import main.services.user_account.StartSessionService
import main.services.user_account.ValidateSessionService

@ExtendWith(MockKExtension::class)
class SessionServiceTest : WordSpec() {
    private lateinit var startService: StartSessionService
    private lateinit var endService: EndSessionService
    private lateinit var validateService: ValidateSessionService
    private lateinit var userAccount: UserAccount
    private lateinit var apiCred: ApiCred
    private val secretKey = "SomeSecretKey"

    override fun beforeTest(description: Description): Unit {
        startService = StartSessionService()
        endService = EndSessionService()
        validateService = ValidateSessionService()
        userAccount = mockk()
        apiCred = userAccount.apiCreds._value as ApiCred
        apiCred.encryptedSecretKey = ApiCred.encryptSecretKey(secretKey)
    }

    init {
        "Start session service" should {
            "return success session when using a valid api key pair" {
                val result = startService.execute(userAccount.idValue, apiCred.apiKey, secretKey)
                result.result shouldBe SOAResultType.SUCCESS
            }

            "return success and the same session if a session is still open" {
                var result = startService.execute(userAccount.idValue, apiCred.apiKey, secretKey)
                result.result shouldBe SOAResultType.SUCCESS
                val session = result.data
                result = startService.execute(userAccount.idValue, apiCred.apiKey, secretKey)
                result.result shouldBe SOAResultType.SUCCESS
                result.data shouldBe session
            }

            "return failure when using an invalid api key pair" {
                val result = startService.execute(userAccount.idValue, apiCred.apiKey, "WRONGSECRET")
                result.result shouldBe SOAResultType.FAILURE
            }
        }

        "End session service" should {
            "return success when passing a valid session" {
                val session = startService.execute(userAccount.idValue, apiCred.apiKey, secretKey).data!!
                val result = endService.execute(userAccount.idValue, session.sessionKey)
                result.result shouldBe SOAResultType.SUCCESS
            }

            "return failure when passing an invalid session" {
                val result = endService.execute(userAccount.idValue, "BADSESSIONKEY")
                result.result shouldBe SOAResultType.FAILURE
            }

            "return failure when passing an expired session" {
                // TODO figure out timecop
            }
        }

        "Validate session service" should {
            "return success when passing a valid session" {
                val session = startService.execute(userAccount.idValue, apiCred.apiKey, secretKey).data!!
                val result = validateService.execute(userAccount.idValue, session.sessionKey)
                result.result shouldBe SOAResultType.SUCCESS
            }

            "return failure when passing an invalid session" {
                val result = validateService.execute(userAccount.idValue, "BADSESSIONKEY")
                result.result shouldBe SOAResultType.FAILURE
            }

            "return failure when passing an expired session" {
                // TODO figure out timecop
            }
        }
    }
}