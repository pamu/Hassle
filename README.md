# Hassle
Making activities of IIT Mandi hassle free

## Problem
__________

Dynamic Mess food timetable updating system in the hostels of IIT. The user updates the timetable and the updates reach 
the subscribers of the service seamlessly. The database source for the application is the project at 
http://github.com/pamu/BigHassle

BigHassle rest service exposes three rest urls 

1) http://bighassle.herokuapp.com/menu which gives the complete menu

2) http://bighassle.herokuapp.com/day?day=0 which gives complete day schedule [day takes values from 0 to 6]

3) http://bighassle.herokuapp.com/slot?day=0&slot=0 which gives complete slot information [slot can be breakfast or lunch
or dinner. slot takes values 0 to 3]

The app queries the service for the data from the above source.
