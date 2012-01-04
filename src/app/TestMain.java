package app;

import java.text.DecimalFormat;

public class TestMain {
	public static void main(String[] args) {
		DecimalFormat df = new DecimalFormat("#.##");
		df.setMinimumFractionDigits(2);
		System.out.println(df.format(12.1));
		new java.util.Locale("RS");
	}
}
