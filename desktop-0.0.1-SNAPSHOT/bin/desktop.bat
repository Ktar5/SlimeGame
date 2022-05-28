@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  desktop startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and DESKTOP_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\slime-0.0.1-SNAPSHOT.jar;%APP_HOME%\lib\tools-0.0.1-SNAPSHOT.jar;%APP_HOME%\lib\core-0.0.1-SNAPSHOT.jar;%APP_HOME%\lib\steamworks4j-1.8.0.jar;%APP_HOME%\lib\tileeditor-1.0-SNAPSHOT.jar;%APP_HOME%\lib\gameengine-1.0.jar;%APP_HOME%\lib\gdx-controllers-lwjgl3-1.9.11.jar;%APP_HOME%\lib\gdx-backend-lwjgl3-1.9.11.jar;%APP_HOME%\lib\gdx-platform-1.9.11-natives-desktop.jar;%APP_HOME%\lib\gdx-freetype-platform-1.9.11-natives-desktop.jar;%APP_HOME%\lib\gdx-controllerutils-mapping-0.3.0.jar;%APP_HOME%\lib\gdx-controllerutils-scene2d-0.3.0.jar;%APP_HOME%\lib\gdx-controllers-advanced-0.3.0-sources.jar;%APP_HOME%\lib\gdx-controllers-advanced-0.3.0.jar;%APP_HOME%\lib\mongodb-driver-sync-3.10.1.jar;%APP_HOME%\lib\gson-2.8.6.jar;%APP_HOME%\lib\gdx-vfx-effects-0.4.1.jar;%APP_HOME%\lib\gdx-vfx-core-0.4.1.jar;%APP_HOME%\lib\libgdx-utils-0.13.4.jar;%APP_HOME%\lib\json-20180130.jar;%APP_HOME%\lib\common-1.0-SNAPSHOT.jar;%APP_HOME%\lib\vis-ui-1.4.2.jar;%APP_HOME%\lib\gdx-freetype-1.9.11.jar;%APP_HOME%\lib\gdx-controllers-1.9.11.jar;%APP_HOME%\lib\gdx-1.9.11.jar;%APP_HOME%\lib\lwjgl-glfw-3.2.3.jar;%APP_HOME%\lib\lwjgl-glfw-3.2.3-natives-windows.jar;%APP_HOME%\lib\lwjgl-glfw-3.2.3-natives-windows-x86.jar;%APP_HOME%\lib\lwjgl-glfw-3.2.3-natives-linux.jar;%APP_HOME%\lib\lwjgl-glfw-3.2.3-natives-linux-arm32.jar;%APP_HOME%\lib\lwjgl-glfw-3.2.3-natives-linux-arm64.jar;%APP_HOME%\lib\lwjgl-glfw-3.2.3-natives-macos.jar;%APP_HOME%\lib\lwjgl-jemalloc-3.2.3.jar;%APP_HOME%\lib\lwjgl-jemalloc-3.2.3-natives-windows.jar;%APP_HOME%\lib\lwjgl-jemalloc-3.2.3-natives-windows-x86.jar;%APP_HOME%\lib\lwjgl-jemalloc-3.2.3-natives-linux.jar;%APP_HOME%\lib\lwjgl-jemalloc-3.2.3-natives-linux-arm32.jar;%APP_HOME%\lib\lwjgl-jemalloc-3.2.3-natives-linux-arm64.jar;%APP_HOME%\lib\lwjgl-jemalloc-3.2.3-natives-macos.jar;%APP_HOME%\lib\lwjgl-opengl-3.2.3.jar;%APP_HOME%\lib\lwjgl-opengl-3.2.3-natives-windows.jar;%APP_HOME%\lib\lwjgl-opengl-3.2.3-natives-windows-x86.jar;%APP_HOME%\lib\lwjgl-opengl-3.2.3-natives-linux.jar;%APP_HOME%\lib\lwjgl-opengl-3.2.3-natives-linux-arm32.jar;%APP_HOME%\lib\lwjgl-opengl-3.2.3-natives-linux-arm64.jar;%APP_HOME%\lib\lwjgl-opengl-3.2.3-natives-macos.jar;%APP_HOME%\lib\lwjgl-openal-3.2.3.jar;%APP_HOME%\lib\lwjgl-openal-3.2.3-natives-windows.jar;%APP_HOME%\lib\lwjgl-openal-3.2.3-natives-windows-x86.jar;%APP_HOME%\lib\lwjgl-openal-3.2.3-natives-linux.jar;%APP_HOME%\lib\lwjgl-openal-3.2.3-natives-linux-arm32.jar;%APP_HOME%\lib\lwjgl-openal-3.2.3-natives-linux-arm64.jar;%APP_HOME%\lib\lwjgl-openal-3.2.3-natives-macos.jar;%APP_HOME%\lib\lwjgl-3.2.3.jar;%APP_HOME%\lib\lwjgl-3.2.3-natives-windows.jar;%APP_HOME%\lib\lwjgl-3.2.3-natives-windows-x86.jar;%APP_HOME%\lib\lwjgl-3.2.3-natives-linux.jar;%APP_HOME%\lib\lwjgl-3.2.3-natives-linux-arm32.jar;%APP_HOME%\lib\lwjgl-3.2.3-natives-linux-arm64.jar;%APP_HOME%\lib\lwjgl-3.2.3-natives-macos.jar;%APP_HOME%\lib\jlayer-1.0.1-gdx.jar;%APP_HOME%\lib\jorbis-0.0.17.jar;%APP_HOME%\lib\mbassador-1.3.1.jar;%APP_HOME%\lib\mongodb-driver-core-3.10.1.jar;%APP_HOME%\lib\bson-3.10.1.jar;%APP_HOME%\lib\annotations-1.0-SNAPSHOT.jar;%APP_HOME%\lib\gdx-collections-1.9.5.jar;%APP_HOME%\lib\tinylog-impl-2.0.0-M4.2.jar;%APP_HOME%\lib\jul-tinylog-2.0.0-M4.2.jar;%APP_HOME%\lib\tinylog-api-2.0.0-M4.2.jar;%APP_HOME%\lib\lombok-1.16.20.jar;%APP_HOME%\lib\commons-lang3-3.7.jar

@rem Execute desktop
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %DESKTOP_OPTS%  -classpath "%CLASSPATH%" com.ktar5.slime.desktop.DesktopLauncher %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable DESKTOP_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%DESKTOP_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
