package uk.co.osiris.vrs.vessel;

/**
 * Validates IMO ship identification numbers.
 * <p>
 * IMO numbers are 7 digits; the first six are key digits, the 7th is a check digit.
 * The check digit is the rightmost digit of: (d1×7) + (d2×6) + (d3×5) + (d4×4) + (d5×3) + (d6×2)
 * where d1 is the leftmost digit.
 */
public final class ImoNumberValidator {

	private static final int[] FACTORS = {7, 6, 5, 4, 3, 2};

	private ImoNumberValidator() {
	}

	/**
	 * Validates an IMO number. Must be 7 digits, not start with zero, and have a valid check digit.
	 */
	public static boolean isValid(String imoNumber) {
		if (imoNumber == null || imoNumber.length() != 7) {
			return false;
		}
		if (imoNumber.charAt(0) == '0') {
			return false;
		}
		int[] digits = new int[7];
		for (int i = 0; i < 7; i++) {
			char c = imoNumber.charAt(i);
			if (!Character.isDigit(c)) {
				return false;
			}
			digits[i] = c - '0';
		}
		int checksum = 0;
		for (int i = 0; i < 6; i++) {
			checksum += digits[i] * FACTORS[i];
		}
		int expectedCheckDigit = checksum % 10;
		return digits[6] == expectedCheckDigit;
	}
}
