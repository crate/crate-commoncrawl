=============================
CrateDB Common Crawl Importer
=============================

A plugin for importing `Common Crawl`_ data into CrateDB_.

The plugin adds the ``ccrawl`` URI scheme to the ``COPY FROM`` command, which then allows you to import compressed Common Crawl data to a table.

Prerequisites
=============

You must be using CrateDB 0.55 or newer.

A JDK needs to be installed.

On OS X, we recommend using `Oracle's Java`_. If you're using Linux, we
recommend OpenJDK_.

We recommend you use a recent Java 8 version.

Setup
=====

Clone the project::

    $ git clone git@github.com:crate/crate-crate-commoncrawl.git

Build the JAR file like so::

    $ ./gradlew jar

Copy the JAR file to CrateDB's plugins directory::

  cp build/libs/crate-commoncrawl-<version>.jar <CRATEDB_HOME>/plugins

Here, ``<CRATEDB_HOME>`` is the root of your CrateDB installation.

Use
===

You must create this table::

    CREATE TABLE IF NOT EXISTS commoncrawl (
      ssl BOOLEAN PRIMARY KEY, -- http/https
      authority STRING PRIMARY KEY, -- xyz.hello.com:123
      path STRING PRIMARY KEY, -- /a?d=1#hello
      date TIMESTAMP PRIMARY KEY,
      week_partition AS date_trunc('week', date),
      ctype STRING,
      clen INT,
      content string INDEX USING FULLTEXT WITH (max_token_length = 40)
    );

Then, data can be imported, like so::

    COPY commoncrawl FROM 'ccrawl://cr8.is/1WSiodP';

Note here that ``ccrawl://`` must be used instead of ``http://``.

Contributing
============

This project is primarily maintained by `Crate.io`_, but we welcome community
contributions!

See the `developer docs`_ and the `contribution docs`_ for more information.

Help
====

Looking for more help?

- Read `the project blog post`_
- Check `StackOverflow`_ for common problems
- Chat with us on `Slack`_
- Get `paid support`_

.. _Common Crawl: http://commoncrawl.org
.. _contribution docs: CONTRIBUTING.rst
.. _Crate.io: http://crate.io/
.. _CrateDB: https://github.com/crate/crate
.. _developer docs: DEVELOP.rst
.. _OpenJDK: http://openjdk.java.net/projects/jdk8/
.. _Oracle's Java: http://www.java.com/en/download/help/mac_install.xml
.. _paid support: https://crate.io/pricing/
.. _Slack: https://crate.io/docs/support/slackin/
.. _StackOverflow: https://stackoverflow.com/tags/crate
.. _the project blog post: https://crate.io/a/crate-commoncrawl
