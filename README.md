# ItunesSearchApplication

ItunesSearch
An android app that uses the Itunes Search API to find movies that has the word "star" in its title.

Java is used for the development of this application. 
For its architectural design pattern, I followed MVVM (Model-View-ViewModel) 
mainly because the UI components are separated from the business logic, which makes it easier to execute unit testing and also easier to understand the code. Although the only downside of this for me is that you have to code more because of the redundancy and transferring of data from one part of the architecture to another. Based on my experience in developing the project, I think that this is suitable for test-driven development.

Room and Glide were used for caching data and images.
