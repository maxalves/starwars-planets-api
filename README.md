# Star Wars API #
> Spring Boot hal+json Restful Hateoas API for Star Wars planets management, built as a code challenge.

More information about hal:
* [Hal Specification](http://stateless.co/hal_specification.html)

More information about Hateoas
* [Richardson maturity model](https://martinfowler.com/articles/richardsonMaturityModel.html)
* [Hateoas wikipedia](https://en.wikipedia.org/wiki/HATEOAS)

## Requirements
> * [Docker](https://docs.docker.com/get-docker/) 
> * [Docker Compose](https://docs.docker.com/compose/install/)

## How to run
> docker-compose up

## Settings
- **This step IS NOT required!**
> Docker exposes access to the API through port 8080, you can change that on Dockerfile
```js
Dockerfile

.
EXPOSE 8080 <- CHANGE IT HERE IF YOU WANT TO
.
```

> Application Settings are located on `/src/main/resources` and by default a development enviroment is loaded, change it as you wish


## Endpoints
1. Add Planet
> http://localhost:8080/v1/planets
```json 
    {
    	"name": "Tatooine",
    	"terrain": "temperate",
    	"climate": "ocean"
    }
```
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

2. Get Planet
> http://localhost:8080/v1/planets/{planetId}
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

3. Delete Planet
> http://localhost:8080/planets/{planetId}
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

4. Get all planets
> http://localhost:8080/v1/planets
```json
{
    "_embedded": {
        "items": [
            {
                "id": "{planet1Id}",
                "name": "Earth",
                "climate": "ocean",
                "terrain": "temperate",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/v1/planets/{planet1Id}"
                    },
                    "planets": {
                        "href": "http://localhost:8080/v1/planets"
                    }
                },
                "film_apparitions": 0
            },
            {
                "id": "{planet2Id}",
                "name": "Tatooine",
                "climate": "ocean",
                "terrain": "temperate",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/v1/planets/{planet2Id}"
                    },
                    "planets": {
                        "href": "http://localhost:8080/v1/planets"
                    }
                },
                "film_apparitions": 5
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/v1/planets?page=1&size=20"
        }
    }
}
```
