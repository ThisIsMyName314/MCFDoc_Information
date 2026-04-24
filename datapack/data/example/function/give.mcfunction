#
# Gives the specified item to the executor
# @param id: String the item
# @param count: Int the number to give
# @param name: String the custom name for the item
#

$give @s $(id)[custom_name={text: "$(name)", italic: false}] $(count)
