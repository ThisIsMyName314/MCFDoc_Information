## MCFDoc_Information

MCFDoc is a more basic version of JavaDoc which is designed to help
document datapacks thoroughly.

MCFDoc is short for MCFunction Documentation (and named to be similar to JavaDoc)

### Making a function MCFDoc compatible

To create an MCFDoc valid function add a comment at the top of the .mcfunction file, above all of the commands. 

For example, in this file called give_example.mcfunction:

~~~
# Gives the executor the specified item

$give @s $(item) $(count)
~~~

This will result in a function entry in the MCFDoc with the description 'Gives the executor the specified item'


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

 There are 5 valid types: `Int`, `Float`, `String`, `Bool` and `Other`. They are case sensitive and the MCFDoc will not generate if you give an invalid one.

 ##### Other tags

 There are a selection of other tags, though none are fancy like the parameter tag. 

 > The format for all other tags is `@<tag name> <description>`
 
 The list is:
`author`, `return`, `see`, `version`, `deprecated`, `since` and `example`

The description message can run over multiple lines.

MCFDoc is very simple as it's aimed to be easily integratable to functions.

### HTML usage in MCFDoc comments

As the content you put in the names and descriptions inside the MCFDoc comments is converted to HTML elements, it is possible to use HTML tags and create some customisation.

When compiling, there are 3 options for HTML sanitation:

> This defaults to `SAFE`, and can be changed with the flag `html=<NONE|SAFE|ALL>`

- `NONE`: This converts all HTML sensitive characters into their text form, preventing any use of HTML in comments

- `SAFE` allows only for a small selection of HTML elements to be kept. <br>These are `<br>`, `<strong>`,  `<i>`, `<small>`, `<big>`, `<ins>` and `<del>`.

- `ALL` prevents all sanitation from ocurring

> By setting html to ALL, you can add JavaScript to comments, which will then be kept in the resulting HTML file. This does mean that it is possible to smuggle malicious code in comments, and so you should only use MCFDoc on your own datapacks. 

### Creating an MCFDoc

To generate an MCFDoc you use the `/mcfdoc generate` command from the chat

There are 2 essential flags that have to be used:

`dir` which specifies the absolute path to the datapack, for example
/home/user/Documents/datapack on Linux

`out` which specifies the output path (including the file to write to), for example /home/user/Documents/output.html on Linux

`html` is optional and defaults to SAFE

`overwrite` is optional and enables you to allow overwriting when creating the output file and defaults to false

`author` is optional and lets you specify the author name to include at the top of the generated file

`description` is optional and lets you specify a description for the datapack

`legacy` is an optional boolean flag, when set to true it will search for a 'functions' folder, not a 'function' folder (this name change was caused in the [1.21 snapshot 24w21a](https://minecraft.wiki/w/Java_Edition_24w21))

### Examples

Linux:

`/mcfdoc generate dir=/home/user/Documents/datapack out=/home/user/Documents/output.html`

MacOS:

`/mcfdoc generate dir=/Users/user/Documents/datapack out=/Users/user/Documents/output.html`

Windows:

`/mcfdoc generate dir=C:\Users\User\Documents\datapack out=C:\Users\User\Documents\output.html`

### Errors

You will be notified in chat if there are errors

There is a possibility that you may try and access a file that the process running Minecraft does not have access to. If using Prism Launcher on Linux, installed as a Flatpak, Minecraft is sandboxed and so you have very limited file access. 

If this is the case, you can grant full filesystem access with the command (from the terminal)<br>
`sudo flatpak override org.prismlauncher.PrismLauncher --filesystem=host`<br>

You must then restart Prism Launcher (and Minecraft)



