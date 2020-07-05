[![Build Status](https://dev.azure.com/max-personal/starwars-challenge/_apis/build/status/maxalves.starwars-planets-api?branchName=master)](https://dev.azure.com/max-personal/starwars-challenge/_build/latest?definitionId=2&branchName=master)

# Star Wars API #
> Restful API built with Java and Spring Framework.
> 
> Responses implement HAL specification (hal+json).

More information about HAL:
* [HAL Specification](http://stateless.co/hal_specification.html)

More information about HATEOAS:
* [Richardson maturity model](https://martinfowler.com/articles/richardsonMaturityModel.html)
* [HATEOAS wikipedia](https://en.wikipedia.org/wiki/HATEOAS)

## Requirements
> * [Docker](https://docs.docker.com/get-docker/) 
> * [Docker Compose](https://docs.docker.com/compose/install/)

## How to run
> docker-compose up

## Settings
- **This step IS NOT required!**
> You can change application port on `docker-compose.yml` file.
```docker-compose
ports:
  - 8080:8080 <- CHANGE IT HERE
```

> Application Settings are located on `/src/main/resources` and by default a development enviroment is loaded, change it as you wish


## Endpoints
1. Add Planet - Request
> http://localhost:8080/v1/planets
```json
    {
    	"name": "Tatooine",
    	"terrain": "temperate",
    	"climate": "ocean"
    }
```
> Add Planet - Response
```json
{
    "id": "{planetId}",
    "name": "Tatooine",
    "climate": "ocean",
    "terrain": "temperate",
    "_links": {
        "self": {
            "href": "http://localhost:8080/v1/planets/{planetId}"
        },
        "planets": {
            "href": "http://localhost:8080/v1/planets"
        }
    }
}

```

2. Get Planet - Request
> http://localhost:8080/v1/planets/{planetId}

> Get Planet - Response
```json
{
    "id": "{planetId}",
    "name": "Tatooine",
    "climate": "ocean",
    "terrain": "temperate",
    "_links": {
        "self": {
            "href": "http://localhost:8080/v1/planets/{planetId}"
        },
        "planets": {
            "href": "http://localhost:8080/v1/planets"
        }
    },
    "film_apparitions": 5
}
```

3. Delete Planet - Request
> http://localhost:8080/planets/{planetId}

> Delete Planet - Response
```json
{
    "id": "{planetId}",
    "name": "Tatooine",
    "climate": "ocean",
    "terrain": "temperate",
    "_links": {
        "planets": {
            "href": "http://localhost:8080/v1/planets"
        }
    }
}
```

4. Get all planets - Request
> * http://localhost:8080/v1/planets
> * http://localhost:8080/v1/planets?page=1&size=1  (paging)

> Get all planets - Response
```json
{
    "_embedded": {
        "items": [
            {
                "id": "{planetId}",
                "name": "Tatooine",
                "climate": "ocean",
                "terrain": "temperate",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/v1/planets/{planetId}"
                    },
                    "planets": {
                        "href": "http://localhost:8080/v1/planets"
                    }
                },
                "film_apparitions": 10
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/v1/planets?page=1&size=1"
        },
        "next": {
            "href": "http://localhost:8080/v1/planets?page=2&size=1"
        }
    }
}
```

# Tests
> Tests run during CI build you can check the latest runs here
`
https://dev.azure.com/max-personal/starwars-challenge/_build
`