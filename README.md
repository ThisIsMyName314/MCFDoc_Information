## MCFDoc_Information

MCFDoc is a more basic version of JavaDoc which is designed to help
document datapacks thoroughly.

The name is short for MCFunction Documentation \(and named to be similar to JavaDoc\)

Overall it is very simple \(nothing close to JavaDoc or similar documentation generators\) as it's aimed to be easily integratable into functions or datapacks.  The main thing this was made for is to enable you to easily list all functions in one place, and visualise the macros you need to pass for each one.

### Making a function MCFDoc compatible

To create an MCFDoc-documented function add a comment at the top of the .mcfunction file, above all of the commands. 

For example, in this file called give_example.mcfunction:

~~~
# Gives the executor the specified item

$give @s $(item) $(count)
~~~

This will result in a function entry in the MCFDoc with the description 'Gives the executor the specified item'


#### Parameters

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

#### Types

By default there are 8 types, `Int`, `String`, `Bool`, `Float`, `Short`, `Long` and `Array`

For array arguments you can use the `[]` suffix, for example `Float[]`. Multidimensional arrays are also valid, for example `String[][]`.

You can also define custom types.

##### Custom Types

To add custom types, you must have a file named `types` in the data directory of your datapack.

To add a custom type, the syntax is the following:

~~~
#<type name>
type description

#<type2 name>
type 2 description
~~~

If you prefix a line with `!` it will not be parsed

There is no limit to the number of types that can be defined. 

Defined types can be used as arrays when used in parameters.

For example

~~~
#BlockPos
An object with an x, y, and z value
~~~

will define a new type called `BlockPos` which can be used:

~~~
# @param position: BlockPos the position to execute at
# @param snake: BlockPos[] the positions that need resetting
~~~

Custom types will also appear at the top of the generated MCFDoc page with their description.

 #### Other tags

 There are a selection of other tags, though none are fancy like the parameter tag. 

 > The format for all other tags is `@<tag name> <description>`
 
 The list is:
`author`, `return`, `see`, `version`, `deprecated`, `since` and `example`

The description message can run over multiple lines.

##### Custom Tags

By default, using an undefined tag will throw an error, but if you use the flag `undeftags=<true|false>` you are able to enable them.

They follow the same syntax:

~~~
# @customtag Hello world
~~~

### HTML usage in MCFDoc comments

As the content you put in the names and descriptions inside the MCFDoc comments is converted to HTML elements, it is possible to use HTML tags and create some customisation.

When compiling, there are 3 options for HTML sanitation:

> This defaults to `SAFE`, and can be changed with the flag `html=<NONE|SAFE|ALL>`

- `NONE`: This converts all HTML sensitive characters into their text form, preventing any use of HTML in comments

- `SAFE` allows only for a small selection of HTML elements to be kept. <br>These are `<br>`, `<strong>`,  `<i>`, `<small>`, `<big>`, `<ins>` and `<del>`.

- `ALL` prevents all sanitation from ocurring

NONE is not recommended as all linebreaks will appear in text form as \<br\>. 

> By setting html to ALL, you can add JavaScript to comments, which will then be kept in the resulting HTML file. This does mean that it is possible to smuggle malicious code in comments, and so you should only use MCFDoc on your own datapacks. 

An example use of this would be to make a popup

~~~
# Called when the world is either /reloaded or the world first loads
# <script> alert("This datapack is by ThisIsNotMyName314"); </script>

~~~

### Generating the MCFDoc

To generate an MCFDoc you use the `/mcfdoc generate` command from the chat if using the mod.

If you are using the standalone Jar, use `java -jar MCFDoc.jar <flags>` instead.

There are 2 essential flags that have to be used:

1. `dir` which specifies the absolute path to the datapack, for example
/home/user/Documents/datapack on Linux

2. `out` which specifies the output path (including the file to write to), for example /home/user/Documents/output.html on Linux

And 6 optional:

1. `html` is optional and defaults to SAFE

2. `overwrite` Enables you to allow overwriting when creating the output file. Defaults to false

3. `author` lets you specify the author name to include at the top of the generated file

4. `description` lets you specify a description for the datapack

5. `legacy` is a boolean flag. When set to true it will search for a 'functions' folder, not a 'function' folder (this name change was caused in the [1.21 snapshot 24w21a](https://minecraft.wiki/w/Java_Edition_24w21a))

6. `undeftags` is also a boolean flag, letting you specify whether custom tags are allowed. It defaults to false.

If you require spaces in the flag value, you can enclose it in double quotes

### Examples

Linux:

`/mcfdoc generate dir=/home/user/Documents/datapack out=/home/user/Documents/output.html`

MacOS:

`/mcfdoc generate dir=/Users/user/Documents/datapack out=/Users/user/Documents/output.html`

Windows:

`/mcfdoc generate dir=C:\Users\User\Documents\datapack out=C:\Users\User\Documents\output.html`

For the standalone Jar, replace `/mcfdoc generate` with `java -jar MCFDoc.jar` and run from the command line.

Attached in this repository is an example datapack and the generated MCFDoc for it.

### Errors

You will be notified if there are any errors.

Parser errors have a `[<function path>]` suffix to indicate which function caused the error

#### Errors in the mod:

There is a possibility that you may try and access a file that the process running Minecraft does not have access to. If using Prism Launcher on Linux, installed as a Flatpak, Minecraft is sandboxed and so you have very limited file access. 

If this is the case, you can grant full filesystem access with the command (from the terminal)<br>
`sudo flatpak override org.prismlauncher.PrismLauncher --filesystem=host`<br>

You must then restart Prism Launcher (and Minecraft)



