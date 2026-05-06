package uk.co.osiris.vrs.vessel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImoNumberValidatorTest {

	@Test
	void validImoNumberFromExample() {
		assertTrue(ImoNumberValidator.isValid("9074729"));
	}

	@Test
	void validImoNumberCheckDigitCalculation() {
		assertTrue(ImoNumberValidator.isValid("1234567"));
	}

	@ParameterizedTest
	@ValueSource(strings = {"1234567", "9876543", "1000007"})
	void otherValidImoNumbers(String imo) {
		assertTrue(ImoNumberValidator.isValid(imo));
	}

	@Test
	void nullIsInvalid() {
		assertFalse(ImoNumberValidator.isValid(null));
	}

	@Test
	void wrongLengthIsInvalid() {
		assertFalse(ImoNumberValidator.isValid("123456"));
		assertFalse(ImoNumberValidator.isValid("12345678"));
	}

	@Test
	void leadingZeroIsInvalid() {
		assertFalse(ImoNumberValidator.isValid("0123456"));
	}

	@Test
	void invalidCheckDigitIsInvalid() {
		assertFalse(ImoNumberValidator.isValid("9074720"));
		assertFalse(ImoNumberValidator.isValid("9074728"));
	}

	@Test
	void nonDigitsAreInvalid() {
		assertFalse(ImoNumberValidator.isValid("907472a"));
		assertFalse(ImoNumberValidator.isValid("90747 9"));
	}
}
