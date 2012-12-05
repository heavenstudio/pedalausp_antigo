/*
 * Copyright (c) 1999-2009 Touch Tecnologia e Informatica Ltda.
 * Gomes de Carvalho, 1666, 3o. Andar, Vila Olimpia, Sao Paulo, SP, Brasil.
 * Todos os direitos reservados.
 * 
 * Este software e confidencial e de propriedade da Touch Tecnologia e
 * Informatica Ltda. (Informacao Confidencial). As informacoes contidas neste
 * arquivo nao podem ser publicadas, e seu uso esta limitado de acordo com os
 * termos do contrato de licenca.
 */

package br.com.muggler.contactless.smartcard;


import java.text.ParseException;


/**
 * @author muggler
 * @since
 */
public class ParsedATR {

    /**
     * Parses a string containing an ATR and returna an object that encapsulates
     * all of the ATR's fields. Valid formats for the atr string are:
     * <ul>
     * <li>3A70040188065A208010152</li>
     * <li>3B A7 00 40 18 80 65 A2 08 01 01 52</li>
     * <li>3B:A7:00:40:18:80:65:A2:08:01:01:52</li>
     * </ul>
     * 
     * @param atr
     *            a String containing an ATR
     * @return a {@link ParsedATR} object encapsulating the parsed ATR's fields
     * @since
     */
    public static final ParsedATR parseATR(final String atr) {
        return ATRParser.parseATR(atr);
    }

    private ParsedATR(int tS, int t0, int[] tA, int[] tB, int[] tC, int[] tD, int historicalBytesLength,
            int[] historicalBytes, int tCK, boolean hasChecksum, int protocolCount) {
        super();
        this.tS = tS;
        this.t0 = t0;
        this.tA = tA;
        this.tB = tB;
        this.tC = tC;
        this.tD = tD;
        this.numHistoricalBytes = historicalBytesLength;
        this.historicalBytes = historicalBytes;
        this.tCK = tCK;
        this.hasChecksum = hasChecksum;
        this.protocolCount = protocolCount;
    }

    @SuppressWarnings("unused")
    private static final int MAX_ATR_LENGTH = 33;
    @SuppressWarnings("unused")
    private static final int MAX_HIST_LENGTH = 15;
    private static final int IFACE_CHARS_SZ = 4;
    private static final int ATR_PROTOCOL_TYPE_T0 = 0;

    // Initial character. Indicates direct (0x3B) of reverse (0x3F) convention.
    private final int tS;
    // Format character. Indicates which interface bytes follow it.
    private final int t0;
    // Interface characters.
    private final int[] tA;
    private final int[] tB;
    private final int[] tC;
    private final int[] tD;
    // Number of historical bytes contained in the ATR;
    private final int numHistoricalBytes;
    // historical bytes, T1, T2, ...Tk
    private final int[] historicalBytes;

    // XOR checksum. XOR of all the bytes preceding it, from T0.
    private final int tCK;
    private final boolean hasChecksum;

    // number of protocol accepted
    private final int protocolCount;

    @Override
    public String toString() {
        final String tA, tB, tC, tD, hist;
        tA = intArrayToHexString(this.tA);
        tB = intArrayToHexString(this.tB);
        tC = intArrayToHexString(this.tC);
        tD = intArrayToHexString(this.tD);
        hist = intArrayToHexString(this.historicalBytes);
        return String
                .format(
                        "TS: %02X, T0: %02X, TA: %d bytes, %s, TB: %d bytes, %s, TC: %d bytes, %s, TD: %d bytes, %s, %d historical bytes: %s, %d protocols, has checksum: %s checksum: %02X",
                        this.tS, this.t0, this.tA.length, tA, this.tB.length, tB, this.tC.length, tC, this.tD.length,
                        tD, this.numHistoricalBytes, hist, this.protocolCount, (this.hasChecksum ? "yes" : "no"),
                        this.tCK);
    }

    private static String intArrayToHexString(int[] bArr) {
        final StringBuilder sb = new StringBuilder();
        for (int b : bArr) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }

    private static final class ATRParser {

        public static final ParsedATR parseATR(final String atr) {
            int[] tA = null;
            int[] tB = null;
            int[] tC = null;
            int[] tD = null;

            String cleanAtr = cleanUpString(atr);
            int[] nAtr = normalize(cleanAtr);

            final int tS = nAtr[0];
            final int t0 = nAtr[1];
            int tDi = t0;

            // number of historical bytes
            final int hbLength = t0 & 15;

            boolean hasTck = false;

            // protocol number
            int pn = 1;

            int i = 0;
            int[] tAtemp = new int[IFACE_CHARS_SZ];
            int[] tBtemp = new int[IFACE_CHARS_SZ];
            int[] tCtemp = new int[IFACE_CHARS_SZ];
            int[] tDtemp = new int[IFACE_CHARS_SZ];
            while (i < nAtr.length) {
                // check for interface bytes TAi
                if ((tDi | 0xEF) == 0xFF) {
                    i++;
                    tAtemp[pn] = nAtr[i];
                }
                // check for interface bytes TBi
                if ((tDi | 0xDF) == 0xFF) {
                    i++;
                    tBtemp[pn] = nAtr[i];
                }
                // check for interface bytes TCi
                if ((tDi | 0xBF) == 0xFF) {
                    i++;
                    tCtemp[pn] = nAtr[i];
                }
                // check for interface bytes TDi
                if ((tDi | 0x7F) == 0xFF) {
                    i++;
                    tDtemp[pn] = tDi = nAtr[i];
                    if ((tDi & 0x0F) != ATR_PROTOCOL_TYPE_T0) {
                        hasTck = true;
                    }
                    pn++;
                } else {
                    // tDi indicates whether more interface bytes follow; thus we must now break out of the loop.

                    // Store the interface bytes that were read.
                    tA = storeInterfaceBytes(tAtemp);
                    tB = storeInterfaceBytes(tBtemp);
                    tC = storeInterfaceBytes(tCtemp);
                    tD = storeInterfaceBytes(tDtemp);

                    // cya!
                    break;
                }
            }

            // store number of protocols
            final int numProts = pn;

            // store historical bytes
            final int[] hBytes = new int[hbLength];
            System.arraycopy(nAtr, i + 1, hBytes, 0, hbLength);

            // simple check
            if (hBytes.length < hbLength) {
                int missing = hbLength - hBytes.length;
                String t1, t2;
                if (missing > 1) {
                    t1 = "s";
                    t2 = "are";
                } else {
                    t1 = "";
                    t2 = "is";
                }
                throw new RuntimeException(String.format("ERROR! ATR is truncated: %d byte%s %s missing", missing, t1,
                        t2), new ParseException(cleanAtr, i + 1 + hbLength));
            }

            // Store checksum
            int tCk = 0;
            if (hasTck) {
                tCk = nAtr[nAtr.length - 1];
            }

            return new ParsedATR(tS, t0, tA, tB, tC, tD, hbLength, hBytes, tCk, hasTck, numProts);
        }

        private static int[] storeInterfaceBytes(int[] tiTemp) {
            int[] tA;
            tA = new int[tiTemp.length];
            System.arraycopy(tiTemp, 0, tA, 0, tiTemp.length);
            return tA;
        }

        private static String cleanUpString(final String atr) {
            return atr.replaceAll("[:\\s]", "");
        }

        private static int[] normalize(final String cleanAtr) {
            StringBuffer normalizing = new StringBuffer(cleanAtr.length());
            normalizing.append(cleanAtr);

            int[] atr = new int[cleanAtr.length() / 2];
            int i = 0;
            while (normalizing.length() >= 2) { // 2 string characters = 1 byte
                String byt = normalizing.substring(0, 2);
                normalizing.delete(0, 2);
                atr[i++] = Integer.parseInt(byt, 16);
            }

            if (normalizing.length() > 0) {
                throw new RuntimeException("Warning: an odd ATR string was passed for parsing. Original ATR: "
                        + cleanAtr + " remainder after normalization attempt: " + normalizing.toString(),
                        new ParseException(cleanAtr, 2 * i));
            }

            return atr;
        }

        // Test
        @SuppressWarnings("unused")
        public static void main(String[] args) {
            // String[] atrs =
            // { "3B:A7:00:40:18:80:65:A2:08:01:01:52", "3B A7 00 40 18 80 65 A2 08 01 01 52",
            // "3BA70040188065A208010152", "3B 8F 80 01 80 4F 0C A0 00 00 03 06 03 00 01 00 00 00 00 6A" };
            String[] atrs = { "3B 8F 80 01 80 4F 0C A0 00 00 03 06 03 00 01 00 00 00 00 6A" };

            for (String atr : atrs) {
                System.out.println(cleanUpString(atr));
            }

            for (String atr : atrs) {
                int[] norm = normalize(cleanUpString(atr));
                for (int i : norm) {
                    System.out.printf("%03X ", i);
                }
                System.out.println();
                for (int i : norm) {
                    System.out.printf("%03d ", i);
                }
                System.out.println();
            }

            for (String atr : atrs) {
                System.out.println(parseATR(atr));
            }

        }

    }

}
