# News-Reader
This application is using a news API to show a list of top stories from USA. The application contins the following libraries and architectural patterns which can boost up the speed of writing a code from scratch.

Clean Code Principals (Usecases Exempted)
Separation of Concerns
MVVM pattern with LiveData and flows
Hilt Dagger2.x
Recycler View (Pagination library of Jetpack to be used later)
Retrofit Networking Library
Room Databse with Flows
Single source of truth concept is used (Local DB is the source for data for this application)
Work Manager is used to handle the network request in background. (SOneTime request is used but Periodic request can aslo be used without changing many things in view model)
View bindings
utilities for handling network status code and for handling the responses are provided.
The main purpose of this code is to provide a simplest,easiest and shortest way to start an app from scratch.
