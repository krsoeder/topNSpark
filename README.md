# topNSpark


This is a simple project that will read in a gz file from a remote server and run a simple top n per day calculation over that data.

There docker folder contains a docker file used to stand up a standalone master server, as well as a list of commands to run this project on the container that you create with that docker file. There is a prebuilt jar for those that do not have maven installed locally to enable testing of this project without building it. There are also sample results at the top level of this project to demonstrate expected output files. Users can retrieve their own results from the docker container by following the instructions in docker/dockerCommands. Docker is required to be running locally for this to work. 
