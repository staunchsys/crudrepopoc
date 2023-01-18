
## Summary
1. By default all the entity would be hard deleted.
2. deleted_on is added at generic entity level. Each entity has this column. 
3. Provided a declarative way to mark the entity for the soft delete. The entity should be annotated as a SoftDeleteEnabled.
4. The generic CRUD is defined to support any new entity.
5. Provided a generic way 'listByFilter' to enable search on each entity on given fields. i.e. for a product by default it would return all the records soft deleted and active one. To retrieve only active records need to add a filter deleted_on = (null).  

The above approach is preferable when few of the entity has hard delete feature and most of the entity has soft delete. 

The other approach is do not have deleted_on as part of abstract entity but have one more abstract soft delete entity which extends the abstract entity and has only one attribute deleted_on, all the entity which needs soft delete would extend the abstract soft delete entity, i.e. product extends abstract soft delete entity.

pls note, in given solution, this two is not considered as a strict requirement.
1. default all deleted entities are excluded from result*.
2. if a flag in the query is set to true ex: includeDeleted, all softdelete elements should be returned.

by defult the list api returns all the records, to retrieve the soft deleted records, the deletedOn=(not_null) filter should be applied. To retrieve non soft deleted records the deletedOn=(null) filer should be applied

# Setup details
## Mysql and Schema setup
	1. Install Mysql version 8.0
	2. Create a schema poc_db
	3. Update application.properties file in a project with the credential of mysql user having read/write access for the schema poc_db.
		3.1 `spring.datasource.username`
		3.2 `spring.datasource.password`
## Download maven dependencies
	1. Go to the Base folder of the project.
	2. Run command mvn eclipse:eclipse from the command line.
## Swagger URL for CRUD test
	1. http://localhost:8080/swagger-ui/index.html

## Tech. stack used
- Java 11
- Maven 4
- Mysql 8.0.*
- Spring Boot 2.6.*
- REST APIs
- Swagger 3.0.0
- Data JPA
- Lombok
