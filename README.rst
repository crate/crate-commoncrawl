.. image:: https://cdn.crate.io/web/2.0/img/crate-avatar_100x100.png
   :width: 100px
   :height: 100px
   :alt: Crate.IO
   :target: https://crate.io

=================================
Crate Commoncrawl Importer Plugin
=================================

This plugin is used to fast import data from `Common Crawl`_ into
Crate_.

Build & Install
===============

In order to use it with an existing crate installation, one must build
a JAR and copy all related JAR's into crate's class path (usually at
``<CRATE_HOME>/lib``).

Build JAR
---------

::

   ./gradlew jar

Install JAR
-----------

Copy plugin's JAR to crate's plugins directory::

  cp build/libs/crate-commoncrawl-<version>.jar <CRATE_HOME>/plugins/

Run tests
=========

All test can be run by a single gradle task::

  ./gradlew test


Running Crate in your IDE
=========================

IntelliJ
--------

We recommend IntelliJ to develop crate-commoncrawl. Gradle can be used to generate project
files that can be opened in IntelliJ::

    ./gradlew compileJava idea

Run/Debug Configurations
------------------------

``gradlew idea`` will have created a Run/Debug configuration called ``Crate``.
This configuration can be used to launch and debug Crate from within IntelliJ.

The ``home`` directory will be set to ``<project_root>/sandbox/crate`` and the
configuration files for it can be found in
``<project_root>/sandbox/crate/config``.

Help & Contact
==============

Do you have any questions? Or suggestions? We would be very happy
to help you. So, feel free to swing by our support channel on Slack_.
Or for further information and official contact please
visit `https://crate.io/ <https://crate.io/>`_.


.. _Slack: https://crate.io/docs/support/slackin/
.. _Common Crawl: http://commoncrawl.org
.. _Crate: https://crate.io
