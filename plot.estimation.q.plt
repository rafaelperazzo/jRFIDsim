##########################################################
#Number of tags x Estimated number of tags
##########################################################
unset format
set style line 1 lc rgb 'black' lt 1 lw 2  pt 7 ps 0.7
set style line 20 lc rgb "#292929" lt 1
set style line 21 lc rgb "#878787" lt 1
set style line 22 lc rgb "#B8B8B8" lt 1
set style line 23 lc rgb "#E5E5E5" lt 1
set terminal postscript eps
set key left top
set xlabel "Number of tags" font ",20" offset 0,-1,0
set ylabel "Estimation number of tags" font ",20" offset -1,0,0
set label 1 "1000 iterations" font "Times,18" at 500,8000
#set label 2 "Mean difference: 37.1%" font "Times,18" at 500,85
#set label 3 "Simulator URL: https://github.com/rafaelperazzo/jRFIDsim" font "Times,12" at 6500,95
#set label 4 "8000" font "Times,18" at 8000,0.5
set autoscale
set mxtics
set mytics
set output "plot.estimation.q.eps"
set border linewidth 3
#set style data histogram
#set style histogram cluster gap 1
#set style fill solid 1.0
#set boxwidth 1.0
set xrange [0:12500]
#set format y "%g %%"
set yrange [0:9000]
set ytics 1000 font ",20" rotate by 0
set xtics 0,500,12000 font ",10" rotate by 45 offset -0.8,-1.2
set key font ",18" spacing 1.5
#unset xtics
plot '04_EST.4.12000.txt' using 1:5 with lines notitle ls 1
#, '04_EST.4.10000.txt' using 0:($6+0.05):($6) with labels font "Times,14" notitle


