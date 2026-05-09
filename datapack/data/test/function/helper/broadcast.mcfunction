#
# Broadcasts a message to all users with the 'Broadcast' prefix
# 
# @param message: String the message to send
# @param colour: String the colour of the message
#

$tellraw @a [{text:"Broadcast",color:$(colour)}, {text:"$(message)"}]