@ECHO OFF
REM Run JSQLIDE
SET COMMAND=java -cp ..\lib\jsqlide.jar;..\lib\xerces.jar;..\lib\mysql_comp.jar;..\lib\jdbc6.5-1.2.jar;%CLASSPATH% com.hackerdude.devtools.db.sqlide.sqlide
echo Running %COMMAND%
%COMMAND%
