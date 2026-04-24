#
# Logs a warning to all players with tag 'dev'
# 
# @param msg: String the message to send
# @see example:helper/log (deprecated alternative)
#

$tellraw @a[tag=dev] "[WARN] $(msg)"