
# Games Gallery 
Simple game gallery app where you can view games with their photos and ratings. favorite them as well

#### How to run/install
This Application has no difficulties in installing and running, clone the project, build and run the app directly on your phone or emulator. 
###### 1 Games
The screen where all games are listed with their basic info
###### 2 Favorite Games
The games that you added to your favorite list.
###### 3 Game Detail
This is the detail section of the game you click on. you can see description and metattric score

### Structure

**The app has the following packages:**

* `model:` package holds all database/logic related classes.
    -`response:` contains the main response from the remote server
* `ui:` package holds ui classes such as custom views and callbacks.
    -`adapter:` has recyclerviews' adapters and viewpager adapter.
    -`pagination:` custom interface and helper class for pagination
	-`view:` fragments, mainactivity and viewholder views	
	-viewmodel class for the whole app.
* `util:` contains utilitiy classes for date conversion and observable extensions.
* `api:` contains all the networking related classes like the retrofit instance, and the endpoint interface
* `data:` contains data sources and firebase push notifications service and the repository layer


### Library References:
1. RxJava2/RxAndroid: [https://github.com/ReactiveX/RxAndroid]
2. firebase libs
3. retrofit, and retrofit call adapter
4. Uses Rx,Room, Databinding, Lifecycler, Navigation Component and Material Libraries.

### IMPORTANT NOTE:
I was intending to fully use room for local favorite games operations. I also used the pagination jetpack library at the beginning, however I didnt have the time to fully design the architecture based on those components, So I went on and wrote my own logic for pagination and ended up using sharedprefs for a faster solution even though I do not prefer using it at all. 
I also wanted to integrate Android Hilt for dependency injection. but due to my very busy state at my current work I truly didnt have the time to focus on this task completely.
I only had 4-5 hours to work on this. and this is what i could come up with during these short hours
You can see in my code there are data sources and room classes that are not being used at all. 
for that reason I'd apprecaite it if you take this into consideration while checking out my code. 




