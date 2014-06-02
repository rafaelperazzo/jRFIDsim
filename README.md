jRFIDsim
========

RFID anticollision Simulator

This project allows RFID researchers to test various anti-collision algorithms such as:

- Schoute
- Lower Bound
- Eom-Lee
- C1G2
- NEDFSA

================
Requirements
================

Oracle JAVA 7
http://www.java.com/pt_BR/download/manual.jsp



================
How to Use
================

		 java -jar <initialNumberOfTags> <finalNumberOfTags> <step> <confidenceLevel> <numberOfIterations> <method> <deleteStatusFile> <initialFrameSize> <type>

  Where: 
  
		 method: 1 - Schoute; 2-LOWER; 3-Eom-Lee; 4-Mota; 5-C1G2
		 deleteStatusFile: 1- Yes; 0-No
		 initialFrameSize: 128, 64, 256 for non Q based algorithms or 4,5,6,... for Q based Algorithms
		 type: all|no|test -> all: runs all methods; no -> run only the selected method; test: test estimation method: real number of tags x estimated number of tags 		 

		 Example: java -jar simulator7.jar 100 5000 100 90 1000 2 1 128 all
		 
		 The application will simulate using the number of tags from 100 to 5000, steps of 100, 90% Confidence Level, 1000 iterations, deleting the current status file, initial frame size 128. all methods
		 
		 
================
Credits
================
Rafael Perazzo Barbosa Mota
perazzo@ime.usp.br
http://www.ime.usp.br/~perazzo
