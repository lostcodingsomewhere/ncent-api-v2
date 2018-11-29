package main.services.transaction

import framework.services.DaoService
import kotlinserverless.framework.services.SOAResult
import kotlinserverless.framework.services.SOAResultType
import kotlinserverless.framework.services.SOAServiceInterface
import main.daos.*
import org.jetbrains.exposed.dao.EntityID

/**
 * Retrieve transactions by filter, such as from/to
 * "to" is required for now. need at least one field required
 */
object GetTransactionsService: SOAServiceInterface<TransactionList> {
    override fun execute(caller: Int?, params: Map<String, String>?) : SOAResult<TransactionList> {
        var action: Action? = null
        if(params!!["data"] != null) {
            val actionResult = DaoService.execute {
                Action.find {
                    Actions.data eq Integer.valueOf(params!!["data"]!!)
                    Actions.type eq ActionType.valueOf(params!!["type"]!!)
                    Actions.dataType eq params!!["type"]!!
                }.first()
            }
            if(actionResult.result != SOAResultType.SUCCESS)
                return SOAResult(actionResult.result, actionResult.message, null)
            action = actionResult.data!!
        }

        val transactionsResult = DaoService.execute {
            Transaction.find {
                if(action != null)
                    Transactions.action eq action!!.id
                if(params!!["from"] != null)
                    Transactions.from eq params!!["from"]!!
                if(params!!["previousTransaction"] != null) {
                    Transactions.previousTransaction eq EntityID(
                            Integer.valueOf(params!!["previousTransaction"]!!),
                            Transactions
                    )
                }
                Transactions.to eq params!!["to"]!!
            }.distinct()
        }

        if(transactionsResult.result == SOAResultType.SUCCESS)
            return SOAResult(SOAResultType.SUCCESS, null, TransactionList(transactionsResult.data!!.distinct()))
        return SOAResult(transactionsResult.result, transactionsResult.message, null)
    }
}