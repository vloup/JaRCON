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

Configuration:
	0-	Before starting anything, be sure you have added
		the java's bin folder into your $PATH. This step
		is automatic on Linux, MacOS (as far as I know) and
		on all BSDs.

	1-	Next to the JaRCON.jar file, you will need to create
		a file named config.properties in the .jarcon/
		subfolder. The initial content of the file can be
		empty.

	2-	Have fun using JaRCON.jar with this following command:
			'java -jar JaRCON.jar'
		You can use the --help option to see some more steps
		about adding/querying/removing server.

	3-	If you are as lazy as me, you may do an alias in your
		~/.bashrc
		Just add this lines into your .bashrc and restart bash:
			'alias rcon="java -jar JaRCON.jar"'
			
Copyright:
	I haven't put some clear copyright on it. I do not think
	this script is rather finished now, I will decide later.

Credits:
	- Barto
