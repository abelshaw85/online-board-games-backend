# Online Board Games

This is the frontend repository for an online board game application. The frontend is made using Angular and associated libraries whilst the backend is made using Spring Boot.
The site allows players to compete online in games such as Chess, Shogi and Draughts/Checkers. Games are updated live using Websockets when the opposing player makes a move.

You can visit the frontend repo here: https://github.com/abelshaw85/online-board-games

A demo of this application can be found here: https://abelshaw85.github.io/online-board-games/

## Installation

Rename the application-demo.properties to application.properties and change the properties to your own. Your user database will need the following tables:
**users**
* username (varchar)
* password (varchar, at least 68 chars for BCrypted strings)
* enabled (boolean/tinyint)

**roles**
* id (int)
* name (varchar)
Populate this with your required roles, only ROLE_USER is used in this application.

**users_roles**
* role_id (int)
* user_name (varchar)
Will need FK enforcement to above tables.

## Usage

Run OnlineBoardGameApplication.java.

## Credits

For the most part the code is my own, though there are some exceptions when dealing with new concepts where the code is mostly unchanged:

* Java Guides: https://www.javaguides.net/2019/06/spring-boot-angular-8-websocket-example-tutorial.html was used heavily when creating the Websocket service.
* Java Brains: https://www.youtube.com/watch?v=X80nJ5T7YpE used for managing JWTs, mostly similar on the backend.
