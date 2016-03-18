.. image:: https://cdn.crate.io/web/2.0/img/crate-avatar_100x100.png
   :width: 100px
   :height: 100px
   :alt: Crate.IO
   :target: https://crate.io

=================================
Crate Commoncrawl Importer Plugin
=================================

This plugin is used to fast import data from `Common Crawl`_ into
Crate_. The plugin extends a custom schema ``ccrawl`` to the copy 
from command which allows to import gzipped common crawl data to
a table.

The table should have the following schema::

    create table if not exists commoncrawl (
      ssl boolean primary key, -- http/https
      authority string primary key, -- xyz.hello.com:123
      path string primary key, -- /a?d=1#hello
      date timestamp primary key,
      week_partition as date_trunc('week', date) primary key,
      ctype string,
      clen int,
      content string INDEX using fulltext with (max_token_length = 40)
    );

Then the data could be imported, for example::

    copy commoncrawl from 'ccrawl://aws-publicdatasets.s3.amazonaws.com/common-crawl/crawl-data/CC-MAIN-2015-35/segments/1440644064538.31/wet/CC-MAIN-20150827025424-00162-ip-10-171-96-226.ec2.internal.warc.wet.gz'


.. warning::

    note that the schema ccrawl must be used and not the original schema which is http.


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
