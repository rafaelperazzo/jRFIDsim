#GRAPHICS GENERATION
set encoding utf8 #encoding
unset label
set key left top
set xlabel "Number of tags"
set ylabel "System efficiency"
set autoscale
set mxtics
set mytics
#set format y "%g %%"
set grid xtics ytics lt 2 lw 0.5 lc rgb "#BEBEBE"
largura_linha=3
largura_ic=2
#Tabela de cores: http://gucky.uni-muenster.de/cgi-bin/rgbtab-en
#Linhas e pontos: http://sparky.rice.edu/gnuplot.html
#Identification time
set style line 1 lc rgb 'black' lt 1 lw 3  pt 7 ps 1
set style line 2 lc rgb '#3B3B3B' lt 2 lw 3 pt 5 ps 1
#System efficiency
set style line 3 lc rgb "#3B3B3B" lt 1 lw 2 pt 9 ps 1
set style line 4 lc rgb "black" lt 2 lw 2 pt 4 ps 1
set style line 5 lc rgb "#0A0A0A" lt 9 lw 2 pt 6 ps 1
set style line 11 lc rgb "#1F1F1F" lt 6 lw 2 pt 8 ps 1
set style line 12 lc rgb "black" lt 8 lw 2 pt 3 ps 1

#Outros
set style line 6 lc rgb 'blue' lt 1 lw 2 pt 7 ps 0
set style line 7 lc rgb 'red' lt 1 lw 2 pt 6 ps 0
set style line 8 lc rgb "#006400" lt 1 lw 2 pt 5 ps 0
set style line 9 lc rgb "orange" lt 1 lw 2 pt 4 ps 0
set style line 10 lc rgb "#8B8B00" lt 1 lw 2 pt 3 ps 0
set terminal postscript eps
#######################################################
# SYSTEM EFFICIENCY
#######################################################
set output "efficiency.eps"
set border linewidth 3
set label 1 "Confidence Interval (CI) 90%" at 100,0.44
set label 2 "1000 iterations" at 100,0.43
#set label 3 "Standard deviation interval(SD) [0.003,0.05]" at 100,0.4
#set label 2 "C1G2" at 50,1750
#set label 3 "MQAIT" at 50,1650
set format y "%.4f"
set xrange [0:5100]
set yrange [0.30:0.45]
set xtics 0,500,5100
set ytics 0.01
set key default
plot '01_SEF.MOTA.4.12000.txt' using 1:2:3:4 with linespoint ls 1 title "Our NEDFSA System Efficiency simulation results", '01_SEF.MOTA.4.12000.txt' using 1:2:3:4 with errorbars ls 1 notitle,
'01_SEF.C1G2.4.12000.txt' using 1:2:3:4 with linespoint ls 2 title "C1G2", '01_SEF.C1G2.4.12000.txt' using 1:2:3:4 with errorbars ls 1 notitle,
'01_SEF.LOWER.128.12000.txt' using 1:2:3:4 with linespoint ls 3 title "Lower Bound", '01_SEF.LOWER.128.12000.txt' using 1:2:3:4 with errorbars ls 1 notitle,
'01_SEF.SCHOUTE.128.12000.txt' using 1:2:3:4 with linespoint ls 4 title "Schoute", '01_SEF.SCHOUTE.128.12000.txt' using 1:2:3:4 with errorbars ls 1 notitle,
'01_SEF.EOMLEE.128.12000.txt' using 1:2:3:4 with linespoint ls 5 title "Eom-Lee", '01_SEF.EOMLEE.128.12000.txt' using 1:2:3:4 with errorbars ls 1 notitle

##########################################################
#TOTAL SLOTS COUNTER
##########################################################
unset format
unset label
set key left top
set xlabel "Number of tags"
set ylabel "Mean identification time (slots)"
set autoscale
set mxtics
set mytics
set output "total_slots.eps"
set border linewidth 3
set label 1 "Confidence Interval (CI) 90%" at 250,7000
#set label 2 "Q Algorithm - Our simulation (1000 iteractions)" at 50,1750
#set label 3 "Q Algorithm - Published paper simulation[6] (Figure 7, Page 7)(approximate values)" at 50,1650
set xrange [0:5100]
set yrange [0:7500]
set xtics 0,500,5100
set ytics 500
set key default
plot '02_TOTAL.MOTA.4.12000.txt' using 1:2:3:4 with linespoint ls 1 title "NEDFSA Algorithm - Our simulation", '02_TOTAL.MOTA.4.12000.txt' using 1:2:3:4 with errorbars ls 1 notitle,
'01_TOTAL.C1G2.4.12000.txt' using 1:2:3:4 with linespoint ls 2 title "C1G2", '01_TOTAL.C1G2.4.12000.txt' using 1:2:3:4 with errorbars ls 1 notitle,
'01_TOTAL.LOWER.128.12000.txt' using 1:2:3:4 with linespoint ls 3 title "Lower Bound", '01_TOTAL.LOWER.128.12000.txt' using 1:2:3:4 with errorbars ls 1 notitle,
'01_TOTAL.SCHOUTE.128.12000.txt' using 1:2:3:4 with linespoint ls 4 title "Schoute", '01_TOTAL.SCHOUTE.128.12000.txt' using 1:2:3:4 with errorbars ls 1 notitle,
'01_TOTAL.EOMLEE.128.12000.txt' using 1:2:3:4 with linespoint ls 5 title "Eom-Lee", '01_TOTAL.EOMLEE.128.12000.txt' using 1:2:3:4 with errorbars ls 1 notitle

