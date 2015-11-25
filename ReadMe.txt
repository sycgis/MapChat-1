Tran Situ 
Dana Thomas 
Jason Wang 
Ananth Venkateswaran 
Arad Margalit 
Willa Zhao 

How to Run MapScreen Application:
---------------------------------
Someone must create a server in which the cients all connect to.
To do so, the server host will run Networking/Server.java
The host will then run GUI/MapClientWindow.java
The clients who wish to connect will also launch GUI/MapClientWindow.java
When prompted, they will enter the IP address for the server that the host had set up.


Functionality:
----------------------------------
Server(Functional):
	-Local Server
	-Server Hosting

Logging in(Functional):
	-Login - Searches Database
	-Register - Adds to Database
	-Try it Out - Sets User info to NULL/functions as Guest

Setting Preferences(Functional):
	-Set Personal Information
	-Slider Bars Store 

Current Location Prompt(Functional):
	-Enter Current Location
	-All locations globally work except New York City
	-API zooms to specified location and displays attractions as yellow, numbered dots

Sharing/Switching Personal Location(Functional):
	-Right Click on a Location on the map: given the options to Share Location or Change Location
		-Share Location allows other users to view your location on their maps
		-Change Location changes your view and frame of the map

Attraction(Functional):
	-Attractions displayed are those closest that are of the category you select on the NorthPanel, otherwise, it will display attraction strictly by location.
	-Get information by clicking on the attraction dot
	-See most recent review, see average rating
	-Add your own rating and testimonial - Updates in Database

Chat:
	-Chat with someone who is familiar with the area you are in with the highest matching score by clicking the left most button on the north panel of the map screen
	-Chat with someone of your choice on the map by clicking their name
	-Chat will not be sent if the other match is already in a different chat
	-Chat successfully disconnects
	*If two chats are accepted simultaneously, inconsistent population of correct chat client
	*When disconnecting, host is unable to see other clients when there are two chats open only.

User Information Screen(Functional):
	-Displays Current user information as well as app information
	-Has the logout button to remove the client thread from the server.


Other Notes:
----------------------------------
We accepted the recommendation of adding attractions after submitting our design document. However, due to time restrictions for the project, we were unable to add the feature of new attractions that weren't already included in the Google API. 



