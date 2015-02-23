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
Validation
================		 
This simulator was validated comparing the results with the paper: 

[WU, 2013] Haifeng Wu; Yu Zeng; Jihua Feng; Yu Gu, "Binary Tree Slotted ALOHA for Passive RFID Tag Anticollision," Parallel and Distributed Systems, IEEE Transactions on , vol.24, no.1, pp.19,31, Jan. 2013
doi: 10.1109/TPDS.2012.120
URL: http://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=6178250&isnumber=6365207

The results are available at: 
http://www.ime.usp.br/~perazzo/jRFIDsim_validation/1.png
http://www.ime.usp.br/~perazzo/jRFIDsim_validation/2.png

		 
================
Credits
================
Rafael Perazzo Barbosa Mota
perazzo@ime.usp.br
http://www.ime.usp.br/~perazzo
