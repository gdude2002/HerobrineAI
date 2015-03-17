Compiling the plugin
====================

As this plugin relies on CraftBukkit, the build script has to download and compile the latest version of it. This can
take quite some time, but you may skip it if you feel that you have a late enough version compiled already.

Building everything
-------------------

* Clone the repository and `cd` into the folder you added it to.
* Run `gradlew`.
* Your JAR will be in `build/libs`.

Building without rebuilding CraftBukkit
---------------------------------------

* Clone the repository and `cd` into the folder you added it to.
* Run `gradlew clean build fatJar`.
* Your JAR will be in `build/libs`.