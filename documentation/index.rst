.. DataCafe documentation master file, created by
   sphinx-quickstart on Thursday July 21, 2016.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

*********************************************
Data Cafe - A Dynamic Data Warehousing System
*********************************************

Data Cafe can be used to construct data lakes in Hadoop HDFS from heterogeneous data sources, and query the data lakes efficiently by leveraging Apache Drill.

The steps are outlined below:


Data Cafe Data Sources
######################

Data cafe is implemented for, and tested with the origin data sources:

1. MongoDB.

2. MySQL.


Data lake in:

* Hadoop HDFS and Hive.



Configure Mongo
###############

* Start MongoDB.

 $ sudo systemctl start mongodb


* You may choose to configure MongoDB to start at the system start-up time.

 $ sudo systemctl enable mongodb


* For the evaluation, We first create the sample databases in Mongo if they do not exist already.

.. toctree::
   :maxdepth: 1

   src/main/resources/mongo/Configure-Sample-Mongo-Datasources.rst
   src/main/resources/mongo/PhysioNet-in-Mongo.rst


You may also have MySQL or other SQL and NoSQL data sources as the origin data sources.

.. toctree::
   :maxdepth: 1

   src/main/resources/Configure-MySql.rst

Configure and Execute Hadoop
############################


.. toctree::
   :maxdepth: 1

   src/main/resources/Configure-Hadoop.rst


* Start Hadoop NameNode daemon and DataNode daemon

 $HADOOP_HOME/sbin/start-dfs.sh

* Browse the web interface for the name node - http://localhost:50070/

* Once the execution is complete, you may stop the daemons with

 $HADOOP_HOME/sbin/stop-dfs.sh



Configure and Execute Hive
##########################

.. toctree::
   :maxdepth: 1

   src/main/resources/Configure-Hive.rst


* Run Hive Metastore and Hive

 $HIVE_HOME/bin/hive --service metastore &

* Start HiveServer2

 $HIVE_HOME/bin/hiveserver2



Configure Drill
###############

Make sure Java is installed in order to start Drill.

.. toctree::
   :maxdepth: 1

   src/main/resources/Configure-Drill.rst

   src/main/resources/Enable-Drill-Security.rst

* Launch Drill in Embedded mode

 $DRILL_HOME/bin/drill-embedded

* If Drill is secured, log in with the credentials.

 $DRILL_HOME/bin/drill-embedded -n username -p password

   or

 $DRILL_HOME/bin/sqlline

  !connect jdbc:drill:zk=local

   Enter the credentials now.


* Browse the web interface of Drill - http://localhost:8047/


* Make sure to set extractHeader element appropriately in the Drill storage according to your data sources.

       "extractHeader": true,



Build Data Cafe
###############

Data Cafe can be built using Apache Maven 3.x and Java 1.8.x or higher.

 $ mvn clean install

Built and tested with Apache Maven 3.1.1 and Oracle Java 1.8.0.


Execute Data Cafe
#################

You may execute Data Cafe by writing a server application on top of the Data Cafe Server as well as a client application
by extending the Data Cafe Client.

Server and client samples are provided in the datacafe-server-samples and datacafe-client-samples modules.

Make sure to include log4j2-test.xml into your class path to be able to configure and view the logs. Default log level is [WARN].

The samples described below are PhysioNet MIMIC-III data sources downloaded and configured as MongoDB data bases.


**Executor Sample in datacafe-server-samples module.**

Executor offers a server sample with Mongo as the origin data source and HDFS as the integrated data source.

To execute the Executor sample,

 $ java -classpath lib/datacafe-server-samples-1.0-SNAPSHOT.jar:lib/*:conf/ edu.emory.bmi.datacafe.server.samples.mongo.physionet.Executor


**ExecutorClient Sample in datacafe-client-samples module.**

ExecutorClient on the other hand, offers a sample client implementation with queries partially auto-generated.


To execute the ExecutorClient sample,

 $ java -classpath lib/datacafe-client-samples-1.0-SNAPSHOT.jar:lib/*:conf/ edu.emory.bmi.datacafe.client.samples.ExecutorClient


**HzServer in datacafe-server module.**

HzServer may be executed to create more Hazelcast members to join the Hazelcast cluster created by the Executor.

 $ java -classpath lib/datacafe-server-1.0-SNAPSHOT.jar:lib/*:conf/ edu.emory.bmi.datacafe.hazelcast.HzServer


**DatacafeEngine in datacafe-rest module.**

DatacafeEngine exposes the RESTful APIs to data lake creation and access. It exposes the Data Cafe server and client
methods as RESTful APIs.

 $ java -classpath lib/datacafe-rest-1.0-SNAPSHOT.jar:lib/*:conf/ edu.emory.bmi.datacafe.rest.DatacafeEngine



Dependencies
############

This project depends on the below major projects.

* Hazelcast

* MongoDB

* Apache Hadoop

* Apache Hive

* Apache Drill

* Apache Log4j2

* MySQL

* SparkJava

* Unirest

Current and Future Evaluations
##############################

Data Cafe has been evaluated with various preliminary data sources, and there are on going and future evaluations.


.. toctree::
   :maxdepth: 1

   src/main/resources/AWS-Execution.rst

   src/main/resources/Future-Work.rst



Using Data Cafe in your research
################################

Please cite the below, if you use Data Cafe in your research.

 [1] Kathiravelu, P. & Sharma, A. (2016). **A Dynamic Data Warehousing Platform for Creating and Accessing Biomedical Data Lakes**.
 In *Second International Workshop on Data Management and Analytics for Medicine and Healthcare (DMAH'16), co-located with 42 nd International Conference on Very Large Data Bases (VLDB 2016)*.
 Sep. 2016. LNCS. 18 pages. To Appear.

 [2] Kathiravelu, P., Kazerouni, A., & Sharma, A. (2016). **Data Café — A Platform For Creating Biomedical Data Lakes.**
 In *AMIA 2016 Joint Summits on Translational Science*. Mar. 2016.


