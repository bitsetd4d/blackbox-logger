# Black Box Logger
Inspired by an aircraft 'black box' flight recorder, this is a logger that
collects log output (in memory) and only actually writes it if asked to.

The use case that prompted the creation of this is running a 
suite of Cucumber functional tests where we are only interested in the output
if a test actually fails. At this point the Hook can tell the logger to
write its output, or if a test passes to discard the logging.

To start holding onto logs, you call:

`BlackBoxRegistry.getBlackBoxBroadcast().holdLogging();`

Then to decide what to do with that held output you either call:

`BlackBoxRegistry.getBlackBoxBroadcast().flushHeldLogging()`

or 

`BlackBoxRegistry.getBlackBoxBroadcast().forgetHeldLogging()`

### Improvements
The log items are entirely held in memory. An improvement would be do limit the number
of lines held to some defined threshold.
