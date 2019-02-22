Table of Contents

1. [User APIs](#user-apis)
   + [Find One User](#find-one-user)
   + [GET (requestData object must pass data in as query parameters instead of request body) User Balances](#GET (requestData object must pass data in as query parameters instead of request body)-user-balances)
   + [Create User](#user-create)
   
# User Authentication

Most API calls will require user authentication. All that is required for this is the presence of an "Authorization" header
 in the request with a value that has the following format: "Basic {apiKey:secretKey}". This follows standard RESTful API best 
 practices, which are outlined in this [article](https://blog.restcase.com/restful-api-authentication-basics/).
   
# User APIs

The following APIs will interact with the UserAccountsController.

## Find One User

This API is a simple retrieval for a single user account

#### Route

/user

#### Method

GET (requestData object must pass data in as query parameters instead of request body)

#### Parameters

1. user
   * The user making the API call
   * Datatype: UserAccount
1. requestData
   * Datatype: Object
   * Contains the following fields:
      * secretKey - the caller's API secret key for verification
1. id
   * The ID of the user to be retrieved
   * Datatype: Int
   
#### Sample Response
```json
{
  "statusCode" : 200,
  "body" : {
    "createdAt" : "2019-01-23T12:24:40.428-08:00",
    "updatedAt" : "null",
    "deletedAt" : "null",
    "userMetadata" : {
      "createdAt" : "2019-01-23T12:24:40.424-08:00",
      "updatedAt" : "null",
      "deletedAt" : "null",
      "email" : "dev0@lostcoders.io",
      "firstname" : "dev0",
      "lastname" : "lostcoders0",
      "metadatas" : [ {
        "createdAt" : "2019-01-23T12:24:40.439-08:00",
        "updatedAt" : "null",
        "deletedAt" : "null",
        "key" : "test1key",
        "value" : "test1val"
      } ]
    },
    "apiCreds" : {
      "createdAt" : "2019-01-23T12:24:40.426-08:00",
      "updatedAt" : "null",
      "deletedAt" : "null",
      "apiKey" : "[B@de6e913"
    }
  },
  "headers" : {
    "X-Powered-By" : "AWS Lambda & Serverless"
  },
  "base64Encoded" : false
}
```

## Create User

This API is used for the creation of a new User instance

#### Route

/user

#### Method

POST 

#### Parameters 

1. user
   * The user making the API call
   * Datatype: UserAccount
1. requestData
   * Datatype: Object
   * Contains the following fields:
      * secretKey - the caller's API secret key for verification
      * body - a nested object containing the following user data fields (as Strings)
         * email 
         * firstname
         * lastname

#### Sample Response
```json
{
  "statusCode" : 200,
  "body" : {
    "value" : {
      "createdAt" : "2019-01-24T20:36:47.373Z",
      "updatedAt" : "null",
      "deletedAt" : "null",
      "userMetadata" : {
        "createdAt" : "2019-01-24T20:36:47.372Z",
        "updatedAt" : "null",
        "deletedAt" : "null",
        "email" : "dev@lostcoders.io",
        "firstname" : "dev",
        "lastname" : "lostcoders",
        "metadatas" : [ ]
      },
      "apiCreds" : {
        "createdAt" : "2019-01-24T20:36:47.372Z",
        "updatedAt" : "null",
        "deletedAt" : "null",
        "apiKey" : "[B@1673c486"
      }
    },
    "secretKey" : "[C@3c06f5c3"
  },
  "headers" : {
    "X-Powered-By" : "AWS Lambda & Serverless"
  },
  "base64Encoded" : false
}
```
