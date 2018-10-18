package main.services.transaction

import kotlinserverless.framework.services.SOAResult
import kotlinserverless.framework.services.SOAServiceInterface
import main.daos.Transaction

/**
 * Retrieve transactions by filter, such as from/to
 */
class GetTransactionsService: SOAServiceInterface<Transaction> {
    override fun execute(caller: Int?, params: Map<String, String>?) : SOAResult<List<Transaction>> {
        throw NotImplementedError()
    }
}