cd ./data
@java -classpath ../lib/hsqldb.jar org.hsqldb.server.Server -port 9001 -database.0 file:mydb -dbname.0 mydb
