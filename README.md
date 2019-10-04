# Readme

For this task, I had two objectives: to learn how to use Kotlin and to write robust, future proof code.

## Part A
This section will provide an overview on how the first set of tasks were solved.

### Layout
Changing the layout was a matter of working with the constraint layout and assigning constraints correctly between the views. 

### Validation
To complete the validation task meant simply editing some of the boolean logic already existing in the class. I created a couple of variables to store memory of whether an input was valid. This then means that if two values were incorrect, two error messages will be displayed accordingly, rather than for just the first error encountered. 

### Animation
The animation was solved by studying the Lottie read me. By setting the max frame to the end of the first animation section, I was able to check that the first section was completed by returning a double using the animatedFraction method from Lottie. Then I could simply set the new max and min frames and set the repeat count to infinite. 


## Part B
This section will explore the issues and factors that make up the solution to the second part of the task.

### Design
To make a coherent application I attempted to use a consistent theme throughout, basing this off the design of the first screen. Therefore, I reused the ‘moneybox’ throughout all of the screens. Similarly, I used the button in the drawable section for each button in the project. I also created a file in the drawable section entitled ‘divider’ to create consistent spacing between elements throughout all of the screens. 

### Network requests
Very quickly I had code written down that would make the requests and return a body of data. I used the programme ‘Postman’ to quickly test the responses to my inputted request headers without having to write code. I then translated this into Kotlin code and used gson to pass the results into an object. However, there was a couple of issues with the code that needed fixing.

Firstly, the code was duplicated for each screen and request. An AsyncTask was initially used to make HTTP requests for each class. The first step was to create a NetworkTask class which would handle all of the HTTP requests. Default headers were used and specific data for each request was passed in. The problem came when trying to pass the results back to the class that made the request. The conventional way is to override the method onPostExecute() in the AsyncTask class. However, I would have to pass in the class that called the class, and this seemed a little clumsy. Further to this, during my research I read about coroutines. This seemed like a good solution to the problem as I could return a value directly, and it seemed like an interesting new area for me to explore. See reference no. 1 below for the video I used to learn from. I used the withContext method because I wanted the app to be blocked until the result of the network task be completed. This is needed, for example, because the login information provided by the user has to be validated by a network request before a new intent can be started.

The result was processed by creating classes in each individual class that made a request. The processing was done using gson (reference no.2) to make the response into an object which I could then access easily. 

Secondly, I wanted the code to be future proof, so that it could easily be extended if more functionality was to be added. Therefore, if additional requests need to be added, the code caters for this. The RequestMethod enum simply needs to be extended to allow for additional HTTP request types. 

### Structure of data classes
A transaction data class was employed. The transaction data class contains a number of other data classes (accountInfo, networkInfo and loginInfo) so that data can be passed between screens in a clean way, rather than passing them individually. On the creation of a new screen, data is retrieved from this class and stored in local variables to be used and manipulated as it wishes. Then, before creating a new screen, the data is then wrapped up in transaction data to be passed on. As I have some experience as an ASP .Net developer, this life-cycle for a page format is something I feel comfortable working with. See reference no.3 for details on a page life-cycle.

## Evaluation of objectives
### Learning Kotlin
Having previously written apps in Java before, I wanted to experiment with something new. During general research, I’ve read that Kotlin is highly regarded and some people see it as the future of app design, and therefore this seemed like a good opportunity to expand my skill set. 

One of the main advantages I found working with Kotlin is the reduction of boiler-plate code for certain tasks. Namely, when using parcelable objects to store data, Kotlin has a handy ‘@Parcelize’ feature. In java, there is a lot of code needed to make an object parcelable, however Kotlin makes it very simple. This is only one example of how Kotlin helped me with the app. Initially I thought that Kotlin would hinder my project, and although it did mean it took longer to complete, there were aspects to it that actually made the project more manageable. I look forward to working with Kotlin in the future and learning more about it. 

### Future proof code 
One aspect that makes the code future proof is the introduction of the class ‘TransactionData’. Inside this class contains other data classes that are used to transport data across the different screens. This was designed with the future additions in mind as to add a new data class, TransactionData just needs to be updated with the new data class. The benefit of this is that the code does inside the screens does not have to be updated significantly when adding extra data classes.  

## Conclusion
Overall, I found this to be a very good exercise to learn Kotlin and a little bit more about app design. The two objectives I set for myself were, in my opinion, met. Looking forward, implementing some unit testing would be beneficial to the project as well as generally improving and gaining experience with Kotlin

References:
https://www.youtube.com/watch?v=jYuK1qzFrJg
https://github.com/google/gson
https://docs.microsoft.com/en-us/previous-versions/aspnet/ms178472(v=vs.100)?redirectedfrom=MSDN
https://square.github.io/okhttp/

