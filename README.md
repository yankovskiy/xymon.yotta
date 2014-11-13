xymon.yotta
===========
Xymon-extension for monitoring Axus Yotta arrays. 

Supported device:
* YB-16S3EF8
* Y3-16S6SF8p
* Y3-24S6DF8

Features:
* Monitoring disks status (for all enclosures).

Requirements:
* Monitoring is carried out via the http protocol (your firewall must allow traffic from monitoring-client to the array on the 80 tcp port)
* JRE 1.7 (may be works on other, not tested)
* *nix server(client monitoring) with bash in /bin/bash (need for send data to xymon server)

You can get jar file [here](https://www.dropbox.com/s/cy2794r2grcutve/yottamon.jar?dl=0).

Read [wiki](https://github.com/yankovskiy/xymon.yotta/wiki) for more info.
