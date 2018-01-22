# ClientServerJavaPractice

Practing client and server communication using SOLID principles
of software design.

Proxy Design: Client side has a Server Proxy that inhertis from a
Server Interface. Share all commands. 

Command Pattern: Second design pattern uses the commands. Only one
handler in the server which calls execute on the command. CommandInterface
implemented by each of the commands and execute function of command
calls function on the target class in the server.

