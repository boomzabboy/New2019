@echo off
setlocal enabledelayedexpansion

REM ################################################################################
REM # Server exit codes: ###########################################################
REM # - 0: Shutdown      ###########################################################
REM # - 1: Error         ###########################################################
REM # - 2: Restart       ###########################################################
REM ################################################################################

REM ################################################################################
REM # Constants ####################################################################
REM ################################################################################
set APP_JAR=l2jlogin.jar
set APP_TITLE=L2J Login Server
set JVM_ARGS_DEFAULT_PATH=config\default\Server_JVM_Args.txt
set APP_ARGS_DEFAULT_PATH=config\default\Server_APP_Args.txt
set JVM_ARGS_OVERWRITE_PATH=config\Server_JVM_Args.txt
set APP_ARGS_OVERWRITE_PATH=config\Server_APP_Args.txt
set LOGDIR_PATH=log\

title %APP_TITLE% Console

REM ################################################################################
REM # :restart_loop                                                               ##
REM # When the server exits with code 2, the scripts comes back here to restart.  ##
REM ################################################################################
:restart_loop
set JVM_ARGS_PATH=%JVM_ARGS_DEFAULT_PATH%
set APP_ARGS_PATH=%APP_ARGS_DEFAULT_PATH%

rem Determine if the overwrite files are available
if exist %JVM_ARGS_OVERWRITE_PATH% set JVM_ARGS_PATH=%JVM_ARGS_OVERWRITE_PATH%
if exist %APP_ARGS_OVERWRITE_PATH% set APP_ARGS_PATH=%APP_ARGS_OVERWRITE_PATH%

rem Check for the arguments file to be available
if not exist %JVM_ARGS_PATH% (
	echo %JVM_ARGS_PATH% is missing. Aborting launch.
	echo.
	goto end
) else if not exist %APP_ARGS_PATH% (
	echo %APP_ARGS_PATH% is missing. Aborting launch.
	echo.
	goto end
)

rem Print used argument files and read them
echo JVM arguments file: %JVM_ARGS_PATH%
echo APP arguments file: %APP_ARGS_PATH%
echo.
set JVM_ARGS=
set APP_ARGS=
for /f "delims=" %%F in (%JVM_ARGS_PATH%) do set JVM_ARGS=!JVM_ARGS! %%F
for /f "delims=" %%F in (%APP_ARGS_PATH%) do set APP_ARGS=!APP_ARGS! %%F

rem Construct the command line
set APP_CMD_LN=java %JVM_ARGS% -jar %APP_JAR% %APP_ARGS%

rem Print the command line to execute
echo Startup command line: %APP_CMD_LN%
echo.

rem Delete old *.lck files and archive old logs
for /F "skip=1 delims=" %%T in ('wmic os get localdatetime /FORMAT:table') do (
	if not defined LOCALDT (
		set LOCALDT=%%T
		set LOCALDT=!LOCALDT:~0,4!-!LOCALDT:~4,2!-!LOCALDT:~6,2!_!LOCALDT:~8,2!-!LOCALDT:~10,2!-!LOCALDT:~12,2!
	)
)

if exist %LOGDIR_PATH%*.lck del /F /Q log\*.lck>nul

for /f %%F in ('dir %LOGDIR_PATH% /b ^| findstr /r "^[^0-9]"') do (
	set LOGFILE_NAME_OLD=%%F
	set LOGFILE_NAME_NEW=%LOCALDT%_!LOGFILE_NAME_OLD!
	set LOGFILE_PATH_OLD=%LOGDIR_PATH%!LOGFILE_NAME_OLD!
	rename !LOGFILE_PATH_OLD! !LOGFILE_NAME_NEW!
)

rem Execute the command line
echo Starting %APP_TITLE%.
echo.
%APP_CMD_LN%

if ERRORLEVEL 2 (
	echo.
	echo Admin Restarted %APP_TITLE%.
	echo.
	goto restart_loop
)
REM ## :restart_loop ###############################################################
REM ################################################################################

if ERRORLEVEL 1 (
	echo.
	echo %APP_TITLE% terminated abnormally!
	echo.
)

echo.
echo %APP_TITLE% Terminated.
echo.
pause