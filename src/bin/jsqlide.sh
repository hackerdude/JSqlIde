#!/bin/bash

export JSQLIDE_HOME=..
java -cp $JSQLIDE_HOME/lib/jsqlide.jar:$JSQLIDE_HOME/lib/xerces.jar:$JSQLIDE_HOME/lib/mysql_comp.jar:$CLASSPATH com.hackerdude.apps.sqlide.SqlIdeApplication

