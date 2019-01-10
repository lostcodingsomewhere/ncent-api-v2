package main.services.completion_criteria

import kotlinserverless.framework.services.SOAResult
import kotlinserverless.framework.services.SOAResultType
import main.daos.*
import main.services.reward.GenerateRewardService

/**
 * Generate a new completion criteria
 */
object GenerateCompletionCriteriaService: SOAServiceInterface<CompletionCriteria> {
    override fun execute(caller: UserAccount, d: Any, params: Map<String, String>?) : SOAResult<CompletionCriteria> {
        val completionCriteriaNamespace = d as CompletionCriteriaNamespace
        val rewardResult = GenerateRewardService.execute(completionCriteriaNamespace.rewardNamespace, params)
        if(rewardResult.result != SOAResultType.SUCCESS)
            return SOAResult(SOAResultType.FAILURE, rewardResult.message)

        val completionAddress = if(completionCriteriaNamespace.address != null) {
            completionCriteriaNamespace.address
        } else {
            caller.cryptoKeyPair.publicKey
        }

        // TODO generate a transaction

        return if(completionCriteriaNamespace.preReqChallengeIds.any()) {
            val prereqChallenges = Challenge.find {
                Challenges.id inList completionCriteriaNamespace.preReqChallengeIds!!
            }
            SOAResult(SOAResultType.SUCCESS, null, CompletionCriteria.new {
                address = completionAddress
                reward = rewardResult.data!!
                prereq = prereqChallenges
            })
        } else {
            SOAResult(SOAResultType.SUCCESS, null, CompletionCriteria.new {
                address = completionAddress
                reward = rewardResult.data!!
            })
        }
    }
}