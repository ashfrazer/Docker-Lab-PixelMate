Pre-Requisites:
- Docker (https://www.docker.com/)

-------------------------------------

INSTRUCTIONS TO RUN PIXELMATE:

- Download pre-requisite, Docker.
- Run first_time_setup.bat.
	* This will install OCSF.jar into the Maven repository and start the Docker container which initializes and contains the MariaDB database.
	* NOTE: Although there is an init.sql file, there is no need to manually run it, as Docker will automatically execute it when building the container.
- Run server.bat.
	* This file will first build the project, then compile it, and then finally, run the unit and integration tests, whose results will be printed to the console.
	* IMPORTANT: Every time that you run server.bat, the project (including the database) are re-built, meaning that pre-existing users will be deleted (other than "test").
				 Because of this, you may want to only run server.bat one time. It is unnecessary to run it more than once, unless changes are made to the code.
- Run client.bat.
	* This file will execute the MainGUI class, which begins the game.
	* IMPORTANT: If you are wanting to play on ONE computer (e.g. for testing), then run client.bat TWICE.

-------------------------------------

IN-GAME:

- Upon building the Docker container, an account is automatically created with the following credentials:
	* username: test
	* password: 1234
- Either log in as the user "test" or create your own account. Password must be at least 6 characters.
	* WARNING: For security reasons, please do not use any passwords that you use for purposes outside of this game.
- Choose to Host a game or to Join an existing lobby (ONLY USERS IN THE SAME NETWORK CAN PLAY).
	* IF HOSTING: Enter the LAN IPv4 address of your computer to host the game on. MAKE SURE THAT YOUR FIREWALL WILL ALLOW A CONNECTION TO THE CORRESPONDING SOCKET.
	* IF JOINING: Enter the LAN IPv4 address of the host's computer. If having difficulty, ensure that their firewall will allow a connection to that socket.
	* PRO TIP: If on Windows, to locate your LAN IPv4 address, open command prompt and enter the command: `ipconfig`. Your LAN IPv4 address will be listed.
- If playing single-player, host a game on one instance and join on the other, using the same IPv4 address. There should be no conflict.
- Enjoy the game!

-------------------------------------

TROUBLESHOOTING:

- Q: I'm having issues with Docker-- how can I resolve this?
- A: Assuming that the issue is related to building the container for the first time, here are some steps to try:
		1) Open a terminal and navigate to the project directory.
		2) Run the following command: `docker-compose down`. This will stop any running containers.
		3) Run the following command: `docker ps`. This will display any running containers. Assuming you do not have any containers unrelated to PixelMate, this
		    should return an empty list. If not, then open Docker Desktop and manually stop and delete the container.
		4) Run the following command: `docker-compose up -d --build`. This will re-build the container and restart the database.

- Q: How can I run the unit and integration tests?
- A: When executing server.bat, these tests are automatically run, and their outputs are printed to the terminal. However, if you would like to run the tests individually,
	  open a terminal, navigate to the project folder, and run the following commands:
		* `mvnw test -Dtest=DatabaseIT`
		* `mvnw test -Dtest=KnightTest`
		
-------------------------------------

IMPORTANT FILE LOCATIONS AND INFORMATION (FOR GRADING PURPOSES):

- TESTING CLASSES:
	* INTEGRATION TEST (DatabaseIT.java): src/test/java/DatabaseIT.java
		`--> Note: Maven is managing the testing and recognizes integration tests as "__IT.java", so that is the reasoning for the file name, rather than "DatabaseTest.java".
	* UNIT TEST (KnightTest.java): src/test/java/KnightTest.java
- FINAL CLASS DIAGRAM
	* Located in "Project Documentation" folder in project root.
- BATCH FILES:
	* Due to Maven directory conflicts, the batch files are located in the project root, instead of a folder called batFiles. They must have easy access to multiple files, 
	   such as the wrapper, properties, and POM files. However for convenience, they (along with .sql file) are also included in a folder called batFiles, uploaded separately
	   to Blackboard.