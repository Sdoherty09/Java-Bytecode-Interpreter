public class TempConverter {
	public static void main(String[] args) {
		final int BASE = 32;
		final double CONVERSION_FACTOR = 9.0 / 5.0;
		
		double fahrenheitTemp;
		int celsiusTemp = 24;
		
		fahrenheitTemp = celsiusTemp * CONVERSION_FACTOR + BASE;
		
		System.out.println("Fahrenheit Equivalent " + fahrenheitTemp);
	}
}

//Java Software Solution - Foundations of Program Design - Ninth Edition - Lewis Loftus - Page 88