#!/bin/bash

################################################################################
# Server exit codes: ###########################################################
# - 0: Shutdown      ###########################################################
# - 1: Error         ###########################################################
# - 2: Restart       ###########################################################
################################################################################

################################################################################
# Constants ####################################################################
################################################################################
APP_JAR="l2jlogin.jar"
APP_TITLE="L2J Login Server"
JVM_ARGS_DEFAULT_PATH="config/default/Server_JVM_Args.txt"
APP_ARGS_DEFAULT_PATH="config/default/Server_APP_Args.txt"
JVM_ARGS_OVERWRITE_PATH="config/Server_JVM_Args.txt"
APP_ARGS_OVERWRITE_PATH="config/Server_APP_Args.txt"
LOGDIR_PATH="log/"

err=2
while [ $err == 2 ]; do
	err=0
	JVM_ARGS_PATH="$JVM_ARGS_DEFAULT_PATH"
	APP_ARGS_PATH="$APP_ARGS_DEFAULT_PATH"

	# Determine if the overwrite files are available
	[ -f "$JVM_ARGS_OVERWRITE_PATH" ] && JVM_ARGS_PATH="$JVM_ARGS_OVERWRITE_PATH"
	[ -f "$APP_ARGS_OVERWRITE_PATH" ] && APP_ARGS_PATH="$APP_ARGS_OVERWRITE_PATH"

	# Check for the arguments file to be available
	if [ ! -f "$JVM_ARGS_PATH" ]; then
		echo "$JVM_ARGS_PATH is missing. Aborting launch."
		echo ""
		break
	elif [ ! -f "$APP_ARGS_PATH" ]; then
		echo "$APP_ARGS_PATH is missing. Aborting launch."
		echo ""
		break
	fi
	
	# Print used argument files and read them
	echo "JVM arguments file: $JVM_ARGS_PATH"
	echo "APP arguments file: $APP_ARGS_PATH"
	echo ""
	readarray -t JVM_ARGS < "$JVM_ARGS_PATH"
	readarray -t APP_ARGS < "$APP_ARGS_PATH"

	# Construct and print the command line
	APP_CMD_LN="java ${JVM_ARGS[*]//[$'\r\n']} -jar $APP_JAR ${APP_ARGS[*]//[$'\r\n']}"
	echo "Startup command line: $APP_CMD_LN"
	echo ""

	# rem Delete old *.lck files and archive old logs
	[ -f "log/*.lck" ] && rm log/*.lck
	for LOGFILE_PATH in $LOGDIR_PATH*; do
		[ "$LOGFILE_PATH" == "$LOGDIR_PATH*" ] && continue
		LOGFILE_NAME="${LOGFILE_PATH#*/}"
		[[ "$LOGFILE_NAME" == [0-9]* ]] && continue
		mv "$LOGFILE_PATH" "$LOGDIR_PATH`date +%Y-%m-%d_%H-%M-%S`_$LOGFILE_NAME"
	done

	# Execute the command line
	echo "Starting $APP_TITLE."
	echo ""
	$APP_CMD_LN

	err=$?
	[ $err == 2 ] && echo "Admin Restarted $APP_TITLE."

	sleep 10;
done

[ $err == 1 ] && echo "$APP_TITLE terminated abnormally!"
echo "$APP_TITLE terminated."