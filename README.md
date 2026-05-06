# MCFDoc_Information

MCFD is a simple documentation generator for Minecraft datapacks.

It aims to be

1. Easily integratable
2. useful even on commentless datapacks
3. customisable

 - [How to use](#how-to-use)
 - [Annotations](#annotations)
    - [Parameters](#param)
    - [Return](#return)
    - [Hidden](#hidden)
    - [Deprecated](#deprecated)
    - [See](#see)
    - [Custom Annotations](#custom-annotations)
 - [Parameter Types](#parameter-types)
    - [Custom Types](#custom-types)
 - [Using HTML](#using-html)
    - [All](#all)
    - [Safe](#safe)
    - [None](#none)
  - [Making the Document](#making-the-document)
     - [Required Flags](#required-flags)
     - [Optional Flags](#optional-flags)
     - [Errors](#errors)
  - [Downloads](#downloads)  

## How to use

By default, all datapacks with a function directory are valid.

To add a description to a function file, add a standard comment at the top:

~~~mcfunction
# Broadcasts a chat message

$tellraw @a "[Broadcast] $(message)"
~~~

## Annotations

As is fairly common for documentation generators, annotations (words prefixed with the @ symbol) are used to add special information.

MCFD provides a reasonable selection of annotations, which can all be seen in detail in [Annotations.md](./ANNOTATIONS.md). The key ones are as follows:

### <font style="font-family: 'Courier New', monospace;">@param</font>

The parameter annotation declares that a macro argument is required. It must be followed by a name and a type.

```mcfunction
# Logs a message with a specified colour
#
# @param col: String
```

The colon after the name is optional. A message can also be provided after the type.

In the resulting MCFDoc the parameters will be listed, and the function name will be displayed with them, for example:

 <code>give_example(item: <span style="color:#EE1111">String</span>, count: <span style="color:#EE1111">Int</span>)</code>

 See [Parameter Types](#parameter-types) about how types work.

### <font style="font-family: 'Courier New', monospace;">@return</font>

The return annotation declares what the function should return

```mcfunction
# Attempts to give weakness
#
# @return 1 if the target was invalid
```

### <font style="font-family: 'Courier New', monospace;">@hidden</font>

The hidden annotation will hide a function from the generated document.

Hidden functions can be 'unhidden' if you use the `showhidden=true` flag

### <font style="font-family: 'Courier New', monospace;">@deprecated</font>

The deprecated annotation is intended to mark that a function should not be used. 

It will be noted next to the function name in the generated document.

### <font style="font-family: 'Courier New', monospace;">@see</font>

The see annotation is intended to point the user in the direction of other relevant functions.

```mcfunction
# Clears menu items from the user
#
# @see example:menu/test
```

### Custom Annotations

You can use custom annotations by setting the `undeftags` flag to true. The first letter of the annotation name will be capitalised when displayed in the generated document.

By default, using an unrecognised annotation will cause an error, but notify you that you can use the flag.


## Parameter Types

By default, MCFDoc only recognises 7 types:

`Int`, `String`, `Bool`, `Float`, `Short`, `Long` and `Array`

You can make any of these types an array by adding the `[]` suffix. Any number of dimensions is allowed.

### Custom Types

In order to provide other types, you must make a file named `types` with no extension in the data directory of your datapack.

Inside this file you can declare types with the following syntax:

~~~
#<type name>
type description

#<type2 name>
type 2 description
~~~

You can write comments in the types file, by prefixing the line with `!`.

There is no limit to the number of types that can be defined.

For example

~~~
#BlockPos
An object with an x, y, and z value
~~~

will define a new type called `BlockPos` which can be used:

~~~
# @param position: BlockPos
# @param snake: BlockPos[]
~~~

Custom types will be listed similarly to functions at the top of the generated document.

## Using HTML

As the content you put in comments is converted to static html, you can use html elements.

There are 3 different levels which customise what elements can, and cannot, be used. 

It defaults to `SAFE` but can be changed with the flag `html=<ALL|SAFE|NONE>`

### <font style="font-family: 'Courier New', monospace;">None</font>

By setting html to `NONE`, all html elements will be translated into text equivalents, including line breaks (`<br>`)

### <font style="font-family: 'Courier New', monospace;">Safe</font>

By setting html to `SAFE`, a selection of safe formatting elements can be used:

`<br>`, `<strong>`,  `<i>`, `<small>`, `<big>`, `<ins>` and `<del>`

### <font style="font-family: 'Courier New', monospace;">All</font>

By setting html to `ALL`, all elements are permitted

An example use of this would be to make a popup:

~~~mcfunction
# <script> alert("This datapack is by ThisIsNotMyName314"); </script>
~~~

If you don't know what the comments in the datapack contain, there is a risk that unchecked, potentially harmful, code could be executed in your browser when you open the generated MCFDoc.

## Making the Document

To create a document you must execute the Jar with flags to indicate where the datapack is and where you want the output.

For example:

`java -jar MCFDoc-1.2.0.jar <flags>`

### Required flags

You must provide the `dir` flag, which points to the datapack directory, and the `out` flag which points to the output file.

For example (with Unix style paths):

`... dir=/home/user/Documents/datapack out=/home/user/Documents/output.html`

### Optional flags

`html=<ALL|SAFE|NONE>` to configure what html elements are permitted, defaults to `SAFE`

`overwrite=<true|false>` to configure whether the output can overwrite a file, defaults to `false`

`author=...` to set the name of the datapack author

`description=...` to set a description for the datapack

`version=...` to specify a version for the datapack

`legacy=<true|false>` to configure whether to search for a `functions` directory instead of `function` (the name was changed in [1.21 snapshot 24w21a](https://minecraft.wiki/w/Java_Edition_24w21a)), defaults to `false`

`undeftags=<true|false>` to configure whether to permit unrecognised annotations, defaults to `false`

`prefixtypes=<true|false>` to configure whether the types should be infront of the function name in the generated document, defaults to `false`

`showhidden=<true|false>` to configure whether to display hidden functions, defaults to `false`

String values can be encased in quotes. Escape codes shouldn't be used.

### Errors

If any errors occur you will be notified with a message indicating what went wrong.

Parser errors have a `[<function path>]` suffix to indicate which function caused the error.

## Downloads

Jar files for versions are stored in the [versions directory](./versions)

Alternatively you could clone and then compile the source code.