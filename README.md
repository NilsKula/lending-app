#Lending Application

Simple micro-lending rest api. In which user is able to register, apply for loan, extend loans and view them.
WebSecurity is used for authentications and PasswordEncoder for correct client information storing.
Submissions for loans are evaluated by three criteria:
-can't apply for max amount at night time
-client can't apply for max loan amount if in past 24h period there have already been two cases for this IP
-loan constraints

To start application locally, use docker and enter command in terminal
`docker run -p 5432:5432 -e POSTGRES_USER=nils -e POSTGRES_PASSWORD=nils -e POSTGRES_DB=lending_app postgres`

##Customer API

**Get constraints for loans**
*Request:*

```GET http://localhost:8080/api/constraints```

Status code: ```200 OK```


**Register**
*Request:*

```POST http://localhost:8080/api/register```

Status code: ```200 OK```

Request body:
```json
{
  "email": "example@example.com",
  "password": "Password123"
}
```
**Sign In**

*Request:*

```POST http://localhost:8080/api/sign-in```

Request body:
```json
{
  "email": "example@example.com",
  "password": "Password123"
}
```

Status code: ```200 OK```

**Sign Out**

*Request:*

```POST http://localhost:8080/api/sign-out```

*Response:*

Status code: ```200 OK```

**Get loans**

*Request:*

```GET http://localhost:8080/api/loans```

*Response:*

Status code: ```200 OK```

Response body:
```json

[
    {
        "id": "4907-4510",
        "status": "OPEN",
        "created": "2019-05-09",
        "dueDate": "2019-06-08",
        "principal": 400,
        "interest": 40,
        "total": 440,
        "extensions": []
    }
]

```

**Apply for loan**

*Request:*

```POST http://localhost:8080/api/loans/apply```

Request body:
```json
{
  "amount": 500.0,
  "days": 30
}
```

*Response:*

Status code: ```200 OK```

Response body:
```json
{
    "status": "APPROVED"
}
```

**Extend loan**

*Request:*

```POST http://localhost:8080/api/loans/{loan-id}/extend```

Where query parameters are:

  - *days* - amount of days

*Response:*

Status code: ```200 OK```

Response body:
```json
{
    "id": "4907-4510",
    "status": "OPEN",
    "created": "2019-05-09",
    "dueDate": "2019-06-22",
    "principal": 400,
    "interest": 60.53,
    "total": 460.53,
    "extensions": [
        {
            "created": "2019-05-09",
            "days": 14,
            "interest": 20.53
        }
    ]
}
```