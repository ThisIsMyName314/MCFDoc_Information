#
# Broadcasts a message to all players
# @param message: String the message to send
#

$tellraw @a [{text:"[Broadcast]", color:"green"}, {text:"$(message)", color:"white"}]