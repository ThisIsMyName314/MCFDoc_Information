# MCFD Annotations

In order to differentiate parts of a comment, annotations are used.

Any word prefixed with the `@` symbol will be interpreted as an annotation. 
In order to prevent this happening, use the `&#64;` html code instead. By default, unrecognised annotations will throw an error unless the `undeftags=true` flag is included.

- [Default Annotations](#Default-Annotations)
    - [`@param`](#Annotation-param)
    - [`@return`](#Annotation-return)
    - [`@deprecated`](#Annotation-deprecated)
    - [`@hidden`](#Annotation-hidden)
    - [`@see`](#Annotation-see)
    - [`@example`](#Annotation-example)
    - [`@author`](#Annotation-author)
    - [`@version`](#Annotation-version)
    - [`@since`](#Annotation-since)
- [Custom Annotations](#Custom-Annotations)

## Default Annotations

### Annotation `@param`

Declares that a macro argument is expected, followed by a name and a type.

```mcfunction
# Clears the hotbar of the specified user 
#
# @param name String the player's username
```

### Annotation `@return`

Declares what the function will return

```mcfunction
# @return 1 if an error occurs, otherwise 0
```

### Annotation `@deprecated`

Declares that a function is deprecated. Deprecated functions will have a red message next to the name in the generated document.

```
# Broadcasts a message to all users
# 
# @deprecated
```

### Annotation `@hidden`

Hides a function from the generated document. Intended for functions which you do not want readers of the documentation to use or see.

```
# Checks for leftover markers
# 
# @hidden
```

### Annotation `@see`

Intended to make traversing the functions easier, by providing a link to functions which may be relevant.

```
# Broadcasts a message to all nearby admins
#
# @see example:broadcast/all
```

### Annotation `@example`

Provides an example of how to use the given function.

```mcfunction
# Makes pigs within n blocks fly
#
# @example example:fly/pigs {radius:5}
```

### Annotation `@author`

Declares who the author of a function is. Intended for datapacks with multiple contributors.

```mcfunction
# Test function
#
# @author Steve
```

### Annotation `@version`

Declares the version of the function

```mcfunction
# Init function
#
# @version v1.0
```

### Annotation `@since`

Intended for library datapacks, to provide information regarding what versions the function applies

```mcfunction
# Logs a debugging message
#
# @since v6.7
```

## Custom Annotations

For customisation, and as it is not possible to hardcode all useful annotations, you can write your own.

Any word prefixed with the `@` symbol is valid, however to allow this you may need to add the flag `undeftags=true`

### Exaple Annotation `@warn`

```mcfunction
# Kills all markers
#
# @warn likely to break the game
```

<font color="#111111">Documentation format inspired by IMP Spec https://github.com/Arcensoth/imp-spec</font>