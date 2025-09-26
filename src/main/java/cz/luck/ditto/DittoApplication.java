package cz.luck.ditto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DittoApplication {

	public static void main(String[] args) {

		// Simple byte array to demonstrate the conversion.
		// { 26 (0x1A), -1 (0xFF), 0 (0x00) }
		byte[] data = { 0x1A, (byte) 0xCC, 0x00 };

		String hexString = toHexString(data);

		System.out.println("Original byte array: [" +
				String.join(", ", java.util.stream.IntStream.range(0, data.length)
						.mapToObj(i -> String.format("0x%02X", data[i]))
						.toArray(String[]::new)) + "]");
		System.out.println("Computed hex string: " + hexString);

		// Expected output: 1aff00
		// - 0x1A -> "1a"
		// - 0xFF -> "ff" (Java's byte is signed, but bitwise operations handle it correctly)
		// - 0x00 -> "00"

		SpringApplication.run(DittoApplication.class, args);
	}


	// A lookup table for converting a nibble (4 bits) to its hex character.
	private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

	/**
	 * Computes a hexadecimal string representation of a byte array.
	 *
	 * @param bytes The input byte array.
	 * @return The hex string, or null if the input is null.
	 */
	public static String toHexString(byte[] bytes) {
		// Defensive check: handle null or empty input.
		if (bytes == null || bytes.length == 0) {
			return null;
		}

		// The final hex string will be twice the length of the byte array.
		final char[] hexString = new char[bytes.length * 2];

		// Iterate through each byte in the input array.
		for (int i = 0; i < bytes.length; i++) {
			// Get the current byte.
			final byte b = bytes[i];

			// 1. Compute the high nibble (4 bits).
			// (b >> 4) shifts the top 4 bits to the bottom.
			// & 0x0F masks off the remaining top bits, leaving only the nibble value (0-15).
			final int highNibble = (b >> 4) & 0x0F;

			// 2. Compute the low nibble (4 bits).
			// & 0x0F masks the byte to get the bottom 4 bits.
			final int lowNibble = b & 0x0F;

			// Use the nibble values as indices to get the hex characters from our lookup table.
			// Place them in the correct positions in our result char array.
			hexString[i * 2] = HEX_CHARS[highNibble];
			hexString[i * 2 + 1] = HEX_CHARS[lowNibble];
		}

		// Create and return a new String from the char array.
		return new String(hexString);
	}

}
