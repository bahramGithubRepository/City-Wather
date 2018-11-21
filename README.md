# City-Weather
This is an android application displaying the weather based on the searched city. The application enables you to customize a list of previously searched cities. By default, the weather information of the first city in the list would be displayed on the main page of the application (has been changed to a user selected item in ver 1.1 ).



## Instalation

Either uses Android Studio (http://developer.android.com/sdk/index.html) or build from the command line: ./gradlew build

## Scope

The weather information of each city in this version of the application doesn't include the wind speed and direction. 


## Version

1.1
## Added Features 
1- In this version, Room Persistence Library and ViewModel Architecture added in the application. These features increase performance and flexibility of the application.

![viewmodel](https://user-images.githubusercontent.com/26363869/48663146-01b8a800-ea8c-11e8-9e01-755a5b8985aa.png)

For more information about View model [click here]( https://developer.android.com/topic/libraries/architecture/viewmodel#java) and about Room Persistence [click here](https://developer.android.com/topic/libraries/architecture/room)

2- The selected item by a user in the list will be chosen as a default item instead of the first city of the list.
## Future work

In the next version of the application, I would like to include the complementary information provided by the REST API in this application. It would also be interesting to include the "Weather history" and "Forcast weather" provided by other REST APIs available at apixu.com. 

(This future work has been added in version 1.1 )I also think we can improve the UI lifecycle by utilizing ViewModel found at (https://developer.android.com/topic/libraries/architecture/viewmodel). 
