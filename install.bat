@echo off
echo [INFO] build and install modules.
call mvn clean deploy -Dmaven.test.skip=true
pause