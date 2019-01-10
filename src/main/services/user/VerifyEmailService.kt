package main.services.user

import kotlinserverless.framework.services.SOAResult
import main.daos.UserAccount

/**
 *
 */
object VerifyEmailService: SOAServiceInterface<Boolean> {
    override fun execute(caller: UserAccount, params: Map<String, String>?) : SOAResult<Boolean> {
        throw NotImplementedError()
    }
}