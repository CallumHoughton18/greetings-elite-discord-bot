# Halo Elite Greetings Discord Bot

![WORT WORT WORT](./src/main/resources/greetings.mp4)

Built using Kotlin, Kord, and Lava Player!

A 'built just for fun' discord bot that can join discord voice channels and play audio from the [blessed Halo Elite clip](https://twitter.com/i/status/1463205815641333760) to greet discord users.

## Add it to Your Server

The idea is you should self host this bot yourself, as it contains a docker-compose.yml file to allow you to easily throw it on a VPS. [But if you want to use
a demo example, you can invite it to your server here](https://discord.com/api/oauth2/authorize?client_id=912800510661259274&permissions=8&scope=bot). But there is no gurantee of any uptime.

## Quick Setup

The project is fairly simple, you should be able to just open the project in IntelliJ and run the `run` Gradle task to launch it. You will need to edit your configuration to include two environment variables which are:
* BOT_TOKEN={Discord bot token from developer portal via Discord}
* GREETINGS_AUDIO_LINK=https://www.youtube.com/watch?v=c_w2Nh-mxH4

The GREETINGS_AUDIO_LINK can really link to any video, but this points to an unlisted YouTube clip of the blessed Halo Elite clip mentioned above.

Running the shadowJar Gradle task will build a single .jar file that you run via the command line, this .jar should be located in the `build/libs` directory and end in '-all.jar'.

## Tests(?)

Lol tests in a 'just for fun' project??? If it runs on the Dev bot it ships 👀
