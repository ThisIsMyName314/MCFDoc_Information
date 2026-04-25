## Contributing

If you would like to contribute:

1. **Fork this project**

    Open the source folder in an IDE or open the files you want to change in a text editor

    It doesn't use any fancy tools like Gradle and so it doesn't require much setting up.

3. **Change what you want**

    Try and make your changes specific, if you want to make a large scale change, perhaps open an issue to suggest your idea.

4. **Make a Pull Request**

    Make sure your commits mention in detail what your changes do

### Issues

If you find a bug or a potential problem, feel free to make an issue on GitHub.

Alternatively you could make a pull request with a fix if you prefer.


### Guidelines

 - Try and follow the style of the code I've written
 - Make sure the final code that you make a pull request for is descriptive.

### Exceptions

If the code you write can throw exceptions, mention it in a JavaDoc comment, so that it can't be missed.

Don't use `System.exit()` as it can make debugging confusing. The Parser and similar classes all use exceptions and tend to have an `error` method to do so.


### External Libraries

MCFDoc should be a completely standalone Jar.

Don't use any external libraries!!