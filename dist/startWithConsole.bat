@echo off
set PATH="C:\SDK\jdk1.6.0_24\bin";%PATH%
java -cp ./libs/*;packetsamurai.jar packetsamurai.PacketSamurai
exit