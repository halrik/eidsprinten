# Eidsprinten

## Run locally

Replace <heroku_postgres_password> placeholder in application-local.properties for the postgres database. Get the password from https://data.heroku.com/datastores/fb929b62-7b97-4a58-8c06-55b362cea849#administration. DO NOT COMMIT

Start EidsprintenApplication and go to:

* http://localhost:8080/eidsprinten/swagger-ui/4.15.5/index.html
* http://localhost:8080/eidsprinten/

Remember to enable `spring.jpa.hibernate.ddl-auto=create` in application-local.properties the first time application is started.

## Deployed on heroku

* https://eidsprinten.herokuapp.com/eidsprinten/
* https://eidsprinten.herokuapp.com/eidsprinten/swagger-ui/4.15.5/index.html