package test

import com.beust.klaxon.JsonObject
import main.daos.*
import main.helpers.JsonHelper
import main.helpers.UserAccountHelper
import main.services.user_account.GenerateUserAccountService
import org.jetbrains.exposed.sql.transactions.transaction

object TestHelper {

    fun toJsonObject(value: Any): JsonObject {
        val string = JsonHelper.KLAX.toJsonString(value)
        val map = JsonHelper.KLAX.parse<Map<String, Any?>>(string)
        return JsonObject(map!!)
    }

    // Used to generate a request to the api, used for integration testing
    fun buildRequest(user: NewUserAccount?, path: String, httpMethod: String, body: Map<String, Any>? = null, queryParams: Map<String, Any?>? = null): Map<String, Any> {
        var request = mutableMapOf<String, Any>()
        request["path"] = path
        request["httpMethod"] = httpMethod
        if(body != null)
            request["body"] = body
        if(queryParams != null)
            request["queryStringParameters"] = queryParams
        if(user != null)
            request["headers"] = mapOf(Pair("Authorization", UserAccountHelper.getUserAuth(user)))
        return request
    }

    fun generateUserAccounts(count: Int = 1): List<NewUserAccount> {
        var newUserAccounts = mutableListOf<NewUserAccount>()
        for(i in 0..(count - 1)) {
            transaction {
                newUserAccounts.add(GenerateUserAccountService.execute(
                        "dev$i@lostcoders.io",
                        "dev$i",
                        "lostcoders$i"
                ).data!!)
            }
        }
        return newUserAccounts
    }
}