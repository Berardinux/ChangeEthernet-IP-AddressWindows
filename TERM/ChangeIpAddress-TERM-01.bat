@echo off
setlocal enabledelayedexpansion

:: Get the list of all Ethernet interfaces
powershell -Command "Get-NetAdapter | Where-Object { $_.InterfaceDescription -like '*Ethernet*' -and $_.InterfaceDescription -notlike '*Loopback*' } | Select-Object -Property Name,InterfaceDescription,ifIndex,MacAddress,LinkSpeed" > interfaces.txt

:: Check if the file is empty
for /f %%i in ('type interfaces.txt ^| find /c /v ""') do set lines=%%i
if %lines% lss 2 (
    echo No Ethernet interfaces found.
    del interfaces.txt
    pause
    exit /b
)

:: Display the list of interfaces
echo.
echo Select an Ethernet interface:
echo.

set count=0
for /f "tokens=*" %%i in (interfaces.txt) do (
    for /f "tokens=1 delims=: " %%j in ("%%i") do (
        if /i "%%j"=="Name" (
            set /a count+=1
            echo !count!. %%i
        ) else (
            echo    %%i
        )
    )
)

:: Prompt user to select an interface
:select_interface
set /p interface_choice=Enter the number of the Ethernet interface to use: 

:: Validate user input
if %interface_choice% lss 1 if %interface_choice% gtr %count% (
    echo Invalid choice. Please try again.
    goto select_interface
)

:: Get the selected interface name
set choice=%interface_choice%
set count=0
for /f "tokens=*" %%i in (interfaces.txt) do (
    for /f "tokens=1 delims=: " %%j in ("%%i") do (
        if /i "%%j"=="Name" (
            set /a count+=1
            if !count! equ %choice% (
                for /f "tokens=1,* delims=: " %%k in ("%%i") do set eth=%%l
            )
        )
    )
)

del interfaces.txt

if "%eth%"=="" (
    echo Ethernet interface not found.
    pause
    exit /b
)

call :Header
set /p choice=Type number to choose IP: 
if '%choice%'=='1' goto con1
if '%choice%'=='2' goto con2
if '%choice%'=='3' goto con3
if '%choice%'=='4' goto con4
if '%choice%'=='5' goto con5
if '%choice%'=='6' goto con6
if '%choice%'=='7' goto SYO
if '%choice%'=='8' goto DHCP
if '%choice%'=='9' goto end

:con1
call :Change_IP %eth% 192.168.0.15 255.255.255.0
goto end
:con2
call :Change_IP %eth% 192.168.1.14 255.255.255.0
goto end
:con3
call :Change_IP %eth% 192.168.2.10 255.255.255.0
goto end
:con4
call :Change_IP %eth% 10.1.3.10 255.255.255.0
goto end
:con5
call :Change_IP %eth% 10.255.25.10 255.255.255.0
goto end
:con6
call :Change_IP %eth% 169.254.10.10 255.255.255.0
goto end

:SYO
cls
set /p ADDR=Enter the IP address you would like: 
call :Validate_IP %ADDR%
if errorlevel 1 (
    echo Invalid IP address. Please try again.
    pause
    goto SYO
)
call :Change_IP %eth% %ADDR% 255.255.255.0
goto end

:DHCP
powershell -Command "Start-Process powershell -ArgumentList 'netsh interface ip set address name=%eth% source=dhcp' -Verb RunAs"
goto end

:Change_IP
powershell -Command "Start-Process powershell -ArgumentList 'netsh interface ip set address name=%1 source=static addr=%2 mask=%3' -Verb RunAs"
goto :eof

:Validate_IP
set ip=%1
powershell -Command "if ([System.Net.IPAddress]::TryParse('%ip%', [ref]([System.Net.IPAddress]::None))) { exit 0 } else { exit 1 }"
goto :eof

:Header
echo ###############################################################
echo ########################## IP Changer #########################
echo ###############################################################
echo #                                                             #
echo # 1. UPLC, PCM-5350            Change 192.168.0.15  Static IP #
echo # 2. SEL 3530, SEL 3555 Port 1 Change 192.168.1.14  Static IP #
echo # 3. SEL-3555 Port 2           Change 192.168.2.10  Static IP #
echo # 4. Doble 6150                Change 10.1.3.10     Static IP #
echo # 5. MOXA MB3280               Change 10.225.25.10  Static IP #
echo # 6. Megger MRCT CT Tester     Change 169.254.10.10 Static IP #
echo # 7. Set your own IP                                          #
echo # 8. DHCP                                                     #
echo # 9. Exit                                                     #
echo #                                                             #
echo ###############################################################
goto :eof

:end
