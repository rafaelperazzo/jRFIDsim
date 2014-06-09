#GRAPHICS GENERATION
set encoding utf8 #encoding
unset format
unset label
set key left top
set xlabel "Number of tags"
set ylabel "System efficiency"
set autoscale
set mxtics
set mytics

set grid xtics ytics lt 2 lw 0.5 lc rgb "#BEBEBE"
largura_linha=3
largura_ic=2
#Tabela de cores: http://gucky.uni-muenster.de/cgi-bin/rgbtab-en
#Linhas e pontos: http://sparky.rice.edu/gnuplot.html

set style line 1 lc rgb 'black' lt 1 lw 3  pt 7 ps 0
set style line 2 lc rgb 'blue' lt 2 lw 3 pt 5 ps 0
set style line 3 lc rgb "red" lt 1 lw 2 pt 9 ps 0
set style line 4 lc rgb "orange" lt 2 lw 2 pt 4 ps 0
set style line 5 lc rgb "yellow" lt 9 lw 2 pt 6 ps 0

set terminal postscript eps
#######################################################
# SYSTEM EFFICIENCY
#######################################################
set output "plot.sef.eps"
set border linewidth 3
set label 1 "Confidence Interval (CI) 90%" at 500,0.48
set label 2 "1000 iterations" at 500,0.47
#set label 3 "Standard deviation interval(SD) [0.003,0.05]" at 500,0.4
#set label 2 "C1G2" at 50,1750
#set label 3 "MQAIT" at 50,1650
set format y "%.2f"
set xrange [0:16000]
set yrange [0.20:0.50]
set xtics 0,500,15000 rotate by 45 offset -0.8,-2
set ytics 0.02
set key default
plot '01_SEF.MOTA.4.15000.txt' using 1:2:3:4 with linespoint ls 1 title "Out Algorithm", '01_SEF.MOTA.4.15000.txt' using 1:2:3:4 with errorbars ls 1 notitle, '01_SEF.C1G2.4.15000.txt' using 1:2:3:4 with linespoint ls 2 title "C1G2", '01_SEF.C1G2.4.15000.txt' using 1:2:3:4 with errorbars ls 2 notitle, '01_SEF.LOWER.128.15000.txt' using 1:2:3:4 with linespoint ls 3 title "Lower Bound", '01_SEF.LOWER.128.15000.txt' using 1:2:3:4 with errorbars ls 3 notitle, '01_SEF.SCHOUTE.128.15000.txt' using 1:2:3:4 with linespoint ls 4 title "Schoute", '01_SEF.SCHOUTE.128.15000.txt' using 1:2:3:4 with errorbars ls 4 notitle, '01_SEF.EOMLEE.128.15000.txt' using 1:2:3:4 with linespoint ls 5 title "Eom-Lee", '01_SEF.EOMLEE.128.15000.txt' using 1:2:3:4 with errorbars ls 5 notitle

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
set output "plot.total.eps"
set border linewidth 3
set label 1 "Confidence Interval (CI) 90%" at 500,31000
#set label 2 "Q Algorithm - Our simulation (1000 iteractions)" at 50,1750
#set label 3 "Q Algorithm - Published paper simulation[6] (Figure 7, Page 7)(approximate values)" at 50,1650
set xrange [0:16000]
set yrange [0:33000]
set xtics 0,500,15000 rotate by 45 offset -0.8,-2
set ytics 2000
set key default
plot '02_TOTAL.MOTA.4.15000.txt' using 1:2:3:4 with linespoint ls 1 title "NEDFSA Algorithm - Our simulation", '02_TOTAL.MOTA.4.15000.txt' using 1:2:3:4 with errorbars ls 1 notitle, '02_TOTAL.C1G2.4.15000.txt' using 1:2:3:4 with linespoint ls 2 title "C1G2", '02_TOTAL.C1G2.4.15000.txt' using 1:2:3:4 with errorbars ls 2 notitle, '02_TOTAL.LOWER.128.15000.txt' using 1:2:3:4 with linespoint ls 3 title "Lower Bound", '02_TOTAL.LOWER.128.15000.txt' using 1:2:3:4 with errorbars ls 3 notitle, '02_TOTAL.SCHOUTE.128.15000.txt' using 1:2:3:4 with linespoint ls 4 title "Schoute", '02_TOTAL.SCHOUTE.128.15000.txt' using 1:2:3:4 with errorbars ls 4 notitle, '02_TOTAL.EOMLEE.128.15000.txt' using 1:2:3:4 with linespoint ls 5 title "Eom-Lee", '02_TOTAL.EOMLEE.128.15000.txt' using 1:2:3:4 with errorbars ls 5 notitle

