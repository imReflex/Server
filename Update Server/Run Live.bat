@echo off
title File Server
"C:\Program Files\Java\jdk1.7.0_60\bin\java.exe" -Xmx1400m -cp bin;lib/netty-3.2.jar; org.apollo.jagcached.FileServer 
pause