function Set-WindowTitle($title)
{
    $host.UI.RawUI.WindowTitle = $title
}

$SCRIPT_DIRECTORY = Split-Path -Parent $MyInvocation.MyCommand.Definition
Set-Location $SCRIPT_DIRECTORY

################################################################################
# Server exit codes: ###########################################################
# - 0: Shutdown      ###########################################################
# - 1: Error         ###########################################################
# - 2: Restart       ###########################################################
################################################################################

################################################################################
# Constants ####################################################################
################################################################################
$APP_JAR="l2jlogin.jar"
$APP_TITLE="L2J Login Server"
$JVM_ARGS_DEFAULT_PATH="config\default\Server_JVM_Args.txt"
$APP_ARGS_DEFAULT_PATH="config\default\Server_APP_Args.txt"
$JVM_ARGS_OVERWRITE_PATH="config\Server_JVM_Args.txt"
$APP_ARGS_OVERWRITE_PATH="config\Server_APP_Args.txt"
$LOGDIR_PATH="log\"

Set-WindowTitle "$APP_TITLE Console"

################################################################################
# :restart_loop                                                               ##
# When the server exits with code 2, the scripts comes back here to restart.  ##
################################################################################
do
{
    $JVM_ARGS_PATH=$JVM_ARGS_DEFAULT_PATH
    $APP_ARGS_PATH=$APP_ARGS_DEFAULT_PATH

    # Determine if the overwrite files are available
    if (Test-Path $JVM_ARGS_OVERWRITE_PATH) { $JVM_ARGS_PATH=$JVM_ARGS_OVERWRITE_PATH }
    if (Test-Path $APP_ARGS_OVERWRITE_PATH) { $APP_ARGS_PATH=$APP_ARGS_OVERWRITE_PATH }

    # Check for the arguments file to be available
    if (-Not (Test-Path $JVM_ARGS_PATH))
    {
	    Write-Host "$JVM_ARGS_PATH is missing. Aborting launch."
	    Write-Host ""
	    return
    }
    elseif (-Not (Test-Path $APP_ARGS_PATH))
    {
	    Write-Host "$APP_ARGS_PATH is missing. Aborting launch."
	    Write-Host ""
	    return
    }

    # Print used argument files and read them
    Write-Host "JVM arguments file: $JVM_ARGS_PATH"
    Write-Host "APP arguments file: $APP_ARGS_PATH"
    Write-Host ""
    $JVM_ARGS=""
    $APP_ARGS=""
    $LINES = Get-Content $JVM_ARGS_PATH
    foreach ($LINE in $LINES) { $JVM_ARGS = "$JVM_ARGS $LINE" }
    $LINES = Get-Content $APP_ARGS_PATH
    foreach ($LINE in $LINES) { $APP_ARGS = "$APP_ARGS $LINE" }

    # Construct the command line
    $APP_CMD_LN = "java $JVM_ARGS -jar $APP_JAR $APP_ARGS"

    # Print the command line to execute
    Write-Host "Startup command line: $APP_CMD_LN"
    Write-Host ""

    # Delete old *.lck files and archive old logs
    if (Test-Path "$LOGDIR_PATH*.lck") { Remove-Item "$LOGDIR_PATH*" -Include "*.lck" }

    $LOCALDT = Get-Date -Format yyyy-MM-dd_HH-mm-ss
    if (Test-Path "$LOGDIR_PATH*") { $FILES = Get-Item $LOGDIR_PATH | where Name -match "^[^0-9]" }
    foreach ($FILE in $FILES)
    {
        $FILE_NAME_NEW = "$LOCALDT´_$FILE"
        $FILE_PATH_OLD = "$LOGDIR_PATH´_$FILE"
        Rename-Item -NewName $FILE_NAME_NEW -Path $FILE_PATH_OLD
    }

    # Execute the command line
    Write-Host "Starting $APP_TITLE."
    Write-Host ""
    Invoke-Expression $APP_CMD_LN

    if ($LASTEXITCODE -eq 2)
    {
	    Write-Host ""
	    Write-Host "Admin Restarted $APP_TITLE."
	    Write-Host ""
    }
}
while ($LASTEXITCODE -eq 2)

if ($LASTEXITCODE -eq 1)
{
	Write-Host ""
	Write-Host "$APP_TITLE terminated abnormally!"
	Write-Host ""
}

Write-Host ""
Write-Host "$APP_TITLE Terminated."
Write-Host ""