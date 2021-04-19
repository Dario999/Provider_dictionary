Go to application.properties and configure the datasource url for you mysql database but leave the 
&rewriteBatchedStatements=true&characterEncoding=UTF-8 on the end of the url.
Change the username and password for the database login.


To load the initial data call POST method on localhost:8080/api/manage/import/load.It might take around 2-3 minutes 
to save all the entities in the database but i will do the job asynchronous in the background.

Full documentation in Dictionary Service Dario.pdf
