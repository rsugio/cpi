@echo off
echo.
echo.
echo Don't shoot the piano player, it's proof of concept !
echo.
echo.

set jarexe="C:\Program Files\Java\jdk1.8.0_162\bin\jar.exe"
%jarexe% cvfm ../main/resources/mapping/rsugiobean-0.0.1.jar META-INF/MANIFEST.MF rsugiobean/*
rem META-INF/MANIFEST.MF