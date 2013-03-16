package com.pgis.bus.data.helpers;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class MyEncoder {
	int dataBits = 8;
	int codeBits = 6;
	String keyString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	char pad = '=';
	boolean arrayData = false;
	int max, group;
	ArrayList<Integer> mask = new ArrayList<Integer>();

	// pseudo-constructor

	public MyEncoder() {
		int i, mag, prev;

		// options
		// pad = options.pad || '';

		// bitmasks
		mag = Math.max(dataBits, codeBits);
		prev = 0;

		for (i = 0; i < mag; i += 1) {
			mask.add(prev);
			prev += prev + 1;
		}
		this.max = prev;

		// ouput code characters in multiples of this number
		group = dataBits / gcd(dataBits, codeBits);
	}

	public int getDataBits() {
		return dataBits;
	}

	public void setDataBits(int dataBits) {
		this.dataBits = dataBits;
	}

	public int getCodeBits() {
		return codeBits;
	}

	public void setCodeBits(int codeBits) {
		this.codeBits = codeBits;
	}

	public String getKeyString() {
		return keyString;
	}

	public void setKeyString(String keyString) {
		this.keyString = keyString;
	}

	public char getPad() {
		return pad;
	}

	public void setPad(char pad) {
		this.pad = pad;
	}

	public boolean isArrayData() {
		return arrayData;
	}

	public void setArrayData(boolean arrayData) {
		this.arrayData = arrayData;
	}

	// greatest common divisor
	private int gcd(int a, int b) {
		int t;
		while (b != 0) {
			t = b;
			b = a % b;
			a = t;
		}
		return a;
	};

	// append a byte to the output

	private char write(int n, boolean decoding) {
	
		if (!decoding) {
			return keyString.charAt(n);
		}  else {
			return (char) n;

		}
	}

	// the re-coder
	private String translate(String input, int bitsIn, int bitsOut,
			boolean decoding) throws Exception {
		int i, len, byteIn, buffer, size;
		StringBuilder output = new StringBuilder(input.length());
		char chr;

		buffer = 0;
		size = 0;

		len = input.length();
		for (i = 0; i < len; i += 1) {
			// the new size the buffer will be after adding these bits
			size += bitsIn;

			// read a character
			if (decoding) {
				// decode it
				chr = input.charAt(i);
				byteIn = keyString.indexOf(chr);
				if (chr == pad) {
					break;
				} else if (byteIn < 0) {
					throw new Exception(keyString);
				}
			} else {
				if (arrayData) {
					byteIn = (int) input.charAt(i);
				} else {
					byteIn = (int) input.codePointAt(i);
				}
				if ((byteIn | max) != max) {
					throw new Exception(keyString);
				}
			}

			// shift the buffer to the left and add the new bits
			buffer = (buffer << bitsIn) | byteIn;

			// as long as there's enough in the buffer for another output...
			while (size >= bitsOut) {
				// the new size the buffer will be after an output
				size -= bitsOut;

				// output the part that lies to the left of that number of bits
				// by shifting the them to the right
				output.append(write(buffer >> size, decoding));
				

				// remove the bits we wrote from the buffer
				// by applying a mask with the new size
				buffer &= mask.get(size);
			}
		}

		// If we're encoding and there's input left over, pad the output.
		// Otherwise, leave the extra bits off, 'cause they themselves are
		// padding
		if (!decoding && size > 0) {

			// flush the buffer
			output.append(write(buffer << (bitsOut - size), decoding));

			// add padding keyString for the remainder of the group
			len = output.length() % group;
			for (i = 0; i < len; i += 1) {
				output.append(pad);
			}
		}

		// string!
		return (arrayData && decoding) ? output.toString() : output.toString();
	}

	/**
	 * Encode. Input and output are strings.
	 */
	public String encode(String input) {
		try {
			return translate(input, dataBits, codeBits, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Decode. Input and output are strings.
	 */
	public String decode(String input) {
		try {
			return translate(input, codeBits, dataBits, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
