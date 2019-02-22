package main.services.user_account

import framework.services.DaoService
import kotlinserverless.framework.services.SOAResult
import kotlinserverless.framework.services.SOAResultType
import main.daos.*

/**
 * This service will be used to generate a full User Account
 */
object GenerateUserAccountService {
    fun execute(uemail: String, ufirstname: String, ulastname: String) : SOAResult<NewUserAccount> {
        val apiCredResult = GenerateApiCredsService.execute()
        if(apiCredResult.result != SOAResultType.SUCCESS)
            return SOAResult(apiCredResult.result, apiCredResult.message, null)
        val apiCredNamespace: ApiCredNamespace = apiCredResult.data!!

        return DaoService.execute {
            val user = User.new {
                email = uemail
                firstname = ufirstname
                lastname = ulastname
            }
            val apiCred = ApiCred.new {
                apiKey = apiCredNamespace.apiKey
                secretKey = apiCredNamespace.secretKey
            }

            val userAccount = UserAccount.new {
                userMetadata = user
                apiCreds = apiCred
            }

            return@execute NewUserAccount(
                userAccount,
                apiCredNamespace.secretKey
            )
        }
    }
}
