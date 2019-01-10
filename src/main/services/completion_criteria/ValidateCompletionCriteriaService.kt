package main.services.completion_criteria

import kotlinserverless.framework.services.SOAResult
import kotlinserverless.framework.services.SOAResultType
import main.daos.CompletionCriteria
import main.daos.UserAccount

/**
 * For the time being, just validate the caller is the criteria address
 */
object ValidateCompletionCriteriaService: SOAServiceInterface<Boolean> {
    override fun execute(caller: UserAccount, params: Map<String, String>?) : SOAResult<Boolean> {
        val cc = CompletionCriteria.findById(params!!["completion_criteria_id"]!!.toInt())!!
        // TODO -- eventually move this logic to the completion criteria
        // TODO -- in future there will be different completion criteria types, and
        // TODO -- the object should decide if it is valid or not
        return SOAResult(SOAResultType.SUCCESS, null,caller.cryptoKeyPair.publicKey == cc.address)
    }
}