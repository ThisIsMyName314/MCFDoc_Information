## MCFDoc_Information

MCFDoc is a more basic version of JavaDoc which is designed to help
document datapacks thoroughly.

MCFDoc is short for MCFunction Documentation

### Making a function MCFDoc compatible

To create an MCFDoc valid function add a comment at the top of the .mcfunction file.

> This is not essential, and the function will be present in the MCFDoc with or without a comment at the start, but MCFDoc is for documentation and so this is a guide assuming that you want to make a well documented function.

For example, in this file called give_example.mcfunction:

~~~
# Gives the executor the specified item

$give @s $(item) $(count)
~~~

This will result in a function entry in the MCFDoc with the description 'Gives the executor the specified item'

<br>
##### Parameters

If you wanted to note to the user that the item and count arguments are required, you could add 2 parameter tags, as such:

> The general format for a parameter tag is: ```@param <name>: <type> <description>```

~~~
# Gives the executor the specified item
# @param item: String the id of the item to give
# @param count: Int the number of items to give

$give @s $(item) $(count)
~~~

This would result in the function being listed as:

 give_example(item: <span style="color:#EE1111">String</span>, count: <span style="color:#EE1111">Int</span>)

 With the two parameters listed with descriptions below the function description.

 > There are 5 valid types: Int, Float, String, Bool and Other. They are case sensitive and the MCFDoc will not generate if you give an invalid one.

 ##### Other tags

 There are a selection of other tags, though none are fancy like the parameter tag. 

 > The format for all other tags is `@<tag name> <description>`
 
 The list is:
`author`, `return`, `see`, `version`, `deprecated`, `since` and `example`

> The description can run over multiple lines of comments, but it must be present, otherwise the MCFDoc won't be created.

MCFDoc is very simple as it's aimed to be easily integratable to functions.

### HTML usage in MCFDoc comments

As the content you put in the names and descriptions inside the MCFDoc comments is converted to HTML elements, it is possible to use HTML tags and create some customisation.

When compiling, there are 3 options for HTML sanitation:

> This defaults to `SAFE`, and can be changed with the flag `html=<NONE|SAFE|ALL>`

- `NONE`: This converts all HTML sensitive characters into their text form, preventing any use of HTML in comments

- `SAFE` allows only for a small selection of HTML elements to be kept. <br>These are `<br>`, `<strong>`,  `<i>`, `<small>`, `<big>`, `<ins>` and `<del>`.

- `ALL` prevents all sanitation from ocurring

> If you use MCFDoc on an untrusted datapack and turn off sanitation with the `html=ALL` flag, there is a possibility that malicious JavaScript code could be executed when you open the generated page in your browser, and so it is only recommended to do so if you are the creator. 

More tags, options and 'safe' HTML tags may be added in the future, however, with these, most of the basic formatting can be done.

