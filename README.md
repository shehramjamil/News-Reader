# News-Reader
This application is using a news API to show a list of top stories from USA. The application contins the following libraries and architectural patterns which can boost up the speed of writing a code from scratch.

1. Clean Code Principals (Usecases Exempted)
2. Separation of Concerns
3. MVVM pattern with LiveData and flows
4. Hilt Dagger2.x
5. Recycler View (Pagination library of Jetpack to be used later)
6. Retrofit Networking Library
7. Room Databse with Flows
8. Single source of truth concept is used (Local DB is the source for data for this application)
9. Work Manager is used to handle the network request in background. (SOneTime request is used but Periodic request can aslo be used without changing many things in view model)
9. View bindings
10. utilities for handling network status code and for handling the responses are provided.
The main purpose of this code is to provide a simplest,easiest and shortest way to start an app from scratch.
