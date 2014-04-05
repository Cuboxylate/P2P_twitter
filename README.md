# P2P Twitter 

This project was a university assignment for my networks
course. The assignment specifications are included.

#### Objective
Create a P2P messaging client that allows the user to 
post statuses (with max length 140 chars) and displays
the statuses of the connected peers. Use a P2P model 
instead of the central server farm model used by the 
original Twitter. 

We successfully connected ~30 students together with 
their individual implementations.

#### Overview of program
Main() is hosted in P2PTwitter.java. It creates three
threads running the StatusUpdater, P2PServer and
P2PClient methods.

__Communication__ acts as a shared storage space for all of
the threads, storing the profiles of the local user and 
their peers. Peers are specified in the 
participants.properties file.

__StatusUpdater__ loops asking for the local user's status.
When one is accepted it prints all of the current statuses
the server has received.

__P2PServer__ listens to the created UDP datagram socket. When
it receives a status from a peer, it saves it locally 
against that peer's profile.

__P2PClient__ sends out the local user's status at random 
intervals between 1 and 3 seconds. It concatenates the 
string message with the local user's identifier, then
packages it within a datagram packet and sends it out to
all of the peers.

