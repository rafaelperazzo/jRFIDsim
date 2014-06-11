package simulator.rfid.passive;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Util {

	public static void writePlotFile(int start, int end, int iterations, int steps, int confidence, double maxTotal) {
		String dados = "";
		dados = "set encoding utf8 #encoding\n";
		dados = dados + "unset format\n";
		dados = dados + "unset label\n";
		dados = dados + "set key left top\n";
		dados = dados + "set xlabel \"Number of tags\"\n";
		dados = dados + "set ylabel \"System efficiency\"\n";
		dados = dados + "set autoscale\n";
		dados = dados + "set mxtics\n";
		dados = dados + "set mytics\n";
		dados = dados + "set grid xtics ytics lt 2 lw 0.5 lc rgb \"#BEBEBE\"\n";
		dados = dados + "set style line 1 lc rgb 'black' lt 1 lw 3  pt 7 ps 0\n";
		dados = dados + "set style line 2 lc rgb 'blue' lt 2 lw 3 pt 5 ps 0\n";
		dados = dados + "set style line 3 lc rgb 'red' lt 1 lw 2 pt 9 ps 0\n";
		dados = dados + "set style line 4 lc rgb 'orange' lt 2 lw 2 pt 4 ps 0\n";
		dados = dados + "set style line 5 lc rgb 'yellow' lt 9 lw 2 pt 6 ps 0\n";
		dados = dados + "set terminal postscript eps\n";
		dados = dados + "set output \"plot.sef.eps\"\n";
		dados = dados + "set border linewidth 3\n";
		dados = dados + "set key default\n";
		dados = dados + "set label 1 \"Confidence Interval (CI) " + confidence  + "%\" at " + (start+steps) + ",0.48\n";
		dados = dados + "set label 2 \"" + iterations +" iterations\" at "+(start+steps) + ",0.47\n";
		dados = dados + "set format y \"%.2f\"\n";
		dados = dados + "set xrange [0:" + (end+steps) +"]\n";
		dados = dados + "set yrange [0.20:0.50]\n";
		dados = dados + "set xtics 0," + steps +"," + (end+steps) +" rotate by 45 offset -0.8,-2\n";
		dados = dados + "set ytics 0.02\n";
		dados = dados + "plot '01_SEF.MOTA.4." + end + ".txt' using 1:2:3:4 with linespoint ls 1 title \"Our Algorithm\", '01_SEF.MOTA.4." + end + ".txt' using 1:2:3:4 with errorbars ls 1 notitle,";
		dados = dados + "'01_SEF.C1G2.4." + end + ".txt' using 1:2:3:4 with linespoint ls 2 title \"C1G2\", '01_SEF.C1G2.4." + end + ".txt' using 1:2:3:4 with errorbars ls 2 notitle,";
		dados = dados + "'01_SEF.LOWER.128." + end + ".txt' using 1:2:3:4 with linespoint ls 3 title \"LOWER\", '01_SEF.LOWER.128." + end + ".txt' using 1:2:3:4 with errorbars ls 3 notitle,";
		dados = dados + "'01_SEF.SCHOUTE.128." + end + ".txt' using 1:2:3:4 with linespoint ls 4 title \"SCHOUTE\", '01_SEF.SCHOUTE.128." + end + ".txt' using 1:2:3:4 with errorbars ls 4 notitle,";
		dados = dados + "'01_SEF.EOMLEE.128." + end + ".txt' using 1:2:3:4 with linespoint ls 5 title \"EOMLEE\", '01_SEF.EOMLEE.128." + end + ".txt' using 1:2:3:4 with errorbars ls 5 notitle\n";
		dados = dados + "unset format\n";
		dados = dados + "unset label\n";
		dados = dados + "set key default\n";
		dados = dados + "set key left top\n";
		dados = dados + "set xlabel \"Number of tags\"\n";
		dados = dados + "set ylabel \"Mean identification time (slots)\"\n";
		dados = dados + "set autoscale\n";
		dados = dados + "set mxtics\n";
		dados = dados + "set mytics\n";
		dados = dados + "set output \"plot.total.eps\"\n";
		dados = dados + "set border linewidth 3\n";
		dados = dados + "set label 1 \"Confidence Interval (CI) " + confidence + "%\" at " +(start+steps) + "," + maxTotal + "\n";
		dados = dados + "set xrange [0:" + (end+steps) + "]\n";
		int overhead = (int)(maxTotal + (maxTotal*0.1));
		dados = dados + "set yrange [0:" +overhead + "]\n";
		dados = dados + "set xtics 0," +steps + "," + (end+steps) + " rotate by 45 offset -0.8,-2\n";
		dados = dados + "set ytics auto\n";
		dados = dados + "plot '02_TOTAL.MOTA.4." + end + ".txt' using 1:2:3:4 with linespoint ls 1 title \"Our Algorithm\", '02_TOTAL.MOTA.4." + end + ".txt' using 1:2:3:4 with errorbars ls 1 notitle,";
		dados = dados + "'02_TOTAL.C1G2.4." + end + ".txt' using 1:2:3:4 with linespoint ls 2 title \"C1G2\", '02_TOTAL.C1G2.4." + end + ".txt' using 1:2:3:4 with errorbars ls 2 notitle,";
		dados = dados + "'02_TOTAL.LOWER.128." + end + ".txt' using 1:2:3:4 with linespoint ls 3 title \"LOWER\", '02_TOTAL.LOWER.128." + end + ".txt' using 1:2:3:4 with errorbars ls 3 notitle,";
		dados = dados + "'02_TOTAL.SCHOUTE.128." + end + ".txt' using 1:2:3:4 with linespoint ls 4 title \"SCHOUTE\", '02_TOTAL.SCHOUTE.128." + end + ".txt' using 1:2:3:4 with errorbars ls 4 notitle,";
		dados = dados + "'02_TOTAL.EOMLEE.128." + end + ".txt' using 1:2:3:4 with linespoint ls 5 title \"EOMLEE\", '02_TOTAL.EOMLEE.128." + end + ".txt' using 1:2:3:4 with errorbars ls 5 notitle";
		dados = dados + "\n";
		File f = new File("plot.plt");
		try {
			FileUtils.writeStringToFile(f, dados);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
}
