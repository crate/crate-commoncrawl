===============
Developer Guide
===============

Run tests
=========

Tests can be run like so::

  ./gradlew test

IDE Integration
===============

IntelliJ
--------

We recommend that you use `IntelliJ IDEA`_ for development.

Gradle can be used to generate project files that can be opened in IntelliJ::

    $ ./gradlew idea

Run/Debug Configurations
------------------------

Running ``./gradlew idea`` creates a run/debug configuration called ``Crate``.
This configuration can be used to launch and debug CrateDB from within IntelliJ.

The ``home`` directory will be set to ``<PROJECT_ROOT>/sandbox/crate`` and the
configuration files can be found in the ``<PROJECT_ROOT>/sandbox/crate/config``
directory.

Here, ``<PROJECT_ROOT>`` is the root of your Git repository.

.. _IntelliJ IDEA: https://www.jetbrains.com/idea/
