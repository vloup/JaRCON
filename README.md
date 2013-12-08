          **           *******     ******    *******   ****     **
         /**          /**////**   **////**  **/////** /**/**   /**
         /**  ******  /**   /**  **    //  **     //**/**//**  /**
         /** //////** /*******  /**       /**      /**/** //** /**
         /**  ******* /**///**  /**       /**      /**/**  //**/**
     **  /** **////** /**  //** //**    **//**     ** /**   //****
    //***** //********/**   //** //******  //*******  /**    //***
     /////   //////// //     //   //////    ///////   //      /// 

JaRCON aims to implement a Quake 3 based admin protocol
in a CLI. It has the particularity to be able to deal
with multiple servers and with colored player names.

### Configuration:

1.	Before starting anything, be sure you have added
	the java's bin folder into your $PATH. This step
	is automatic on Linux, MacOS (as far as I know) and
	on all BSDs.

2.	If you are as lazy as me, you may do an alias in your
	`~/.bashrc`.
    Just add this lines into your .bashrc and restart bash:
	`alias rcon="java -jar JaRCON.jar"`.

3.  Start the JaRCON environment with `--firstRun` followed by
    `--new`, `--colors`, `--server`.
    Use the `--help` if you have any troubles.

4. Have fun querying the server you set earlier.

### Copyright:

    Copyright (c) 2013 Barto

    This file is part of JaRCON.

    JaRCON is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JaRCON is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JaRCON.  If not, see <http://www.gnu.org/licenses/>.
