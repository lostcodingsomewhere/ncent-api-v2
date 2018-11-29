package main.services.user_account

import framework.models.idValue
import framework.services.DaoService
import kotlinserverless.framework.services.SOAResult
import kotlinserverless.framework.services.SOAResultType
import kotlinserverless.framework.services.SOAServiceInterface
import main.daos.*
import main.services.transaction.GenerateTransactionService

/**
 * This service will be used to generate a full User Account
 */
object GenerateUserAccountService: SOAServiceInterface<UserAccount> {
    override fun execute(caller: Int?, params: Map<String, String>?) : SOAResult<UserAccount> {
        val keyPairResult = GenerateCryptoKeyPairService.execute()
        if(keyPairResult.result != SOAResultType.SUCCESS)
            return SOAResult(keyPairResult.result, keyPairResult.message, null)
        val keyPairNamespace: CryptoKeyPairNamespace = keyPairResult.data!!

        val apiCredResult = GenerateApiCredsService.execute()
        if(apiCredResult.result != SOAResultType.SUCCESS)
            return SOAResult(apiCredResult.result, apiCredResult.message, null)
        val apiCredNamespace: ApiCredNamespace = apiCredResult.data!!

        val sessionResult = StartSessionService.execute()
        if(sessionResult.result != SOAResultType.SUCCESS)
            return SOAResult(apiCredResult.result, sessionResult.message, null)
        val sessionNamespace: SessionNamespace = sessionResult.data!!

        return DaoService.execute {
            val user = User.new {
                email = params!!["email"]!!
                firstname = params!!["firstname"]!!
                lastname = params!!["lastname"]!!
            }
            val keyPair = CryptoKeyPair.new {
                publicKey = keyPairNamespace.publicKey
                encryptedPrivateKey = keyPairNamespace.encryptedPrivateKey
            }
            val apiCred = ApiCred.new {
                apiKey = apiCredNamespace.apiKey
                encryptedSecretKey = apiCredNamespace.encryptedSecretKey
            }
            val newSession = Session.new {
                sessionKey = sessionNamespace.sessionKey
                expiration = sessionNamespace.expiration
            }

            val userAccount = UserAccount.new {
                userMetadata = user
                cryptoKeyPair = keyPair
                apiCreds = apiCred
                session = newSession
            }

            // TODO log or error result?
            val transactionResult = GenerateTransactionService.execute(
                userAccount!!.idValue,
                TransactionNamespace(
                    keyPairNamespace.publicKey,
                    null,
                    ActionNamespace(
                        ActionType.CREATE,
                        userAccount.idValue,
                        UserAccount::class.simpleName!!
                    ),
                    null, null
                ), null
            )

            return@execute userAccount
        }
    }
}
