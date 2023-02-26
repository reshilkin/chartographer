package data;

import java.io.*;
import java.util.Arrays;

public class BMP {
    public static void createBMP(String path, Shape imageShape) throws IOException {
        File bmp = new File(path);
        bmp.createNewFile();
        try (OutputStream os = new FileOutputStream(bmp)) {
            os.write(createHeaders(imageShape));
            writePixels(os, imageShape);
        }
    }

    public static byte[] createHeaders(Shape imageShape) {
        long dataSize = ((24L * imageShape.width + 31) / 32) * 4 * imageShape.height;
        byte[] header = createBMPHeader(dataSize, 14 + 40);
        byte[] dib = createBITMAPINFO(dataSize, imageShape);
        byte[] res = Arrays.copyOf(header, header.length + dib.length);
        System.arraycopy(dib, 0, res, header.length, dib.length);
        return res;
    }

    public static byte[] createBMPHeader(long dataSize, long offset) {
        byte[] res = new byte[14];
        res[0] = 0x42;
        res[1] = 0x4d;
        writeNum(res, 2, dataSize + offset, 4);
        writeNum(res, 10, offset, 4);
        return res;
    }

    public static void readHeader(InputStream is) throws IOException {
        is.skip(10);
        long skip = readNum(is, 4);
        is.skip(skip - 14);
    }

    private static byte[] createBITMAPINFO(long dataSize, Shape imageShape) {
        byte[] res = new byte[40];
        writeNum(res, 0, 40, 4);
        writeNum(res, 4, imageShape.width, 4);
        writeNum(res, 8, imageShape.height, 4);
        writeNum(res, 12, 1, 2);
        writeNum(res, 14, 24, 2);
        writeNum(res, 20, dataSize, 4);
        return res;
    }

    private static void writePixels(OutputStream os, Shape imageShape) throws IOException {
        for (int i = 0; i < imageShape.height; i++) {
            os.write(createRow(imageShape.width));
        }
    }

    private static byte[] createRow(int width) {
        return new byte[(width * 24 + 31) / 32 * 4];
    }

    private static void writeNum(byte[] arr, int offset, long num, int bytes) {
        for (int i = 0; i < bytes; i++) {
            arr[offset + i] = (byte) (num & 0xff);
            num >>= 8;
        }
    }

    private static long readNum(InputStream is, int bytes) throws IOException {
        long res = 0;
        long t = 1;
        for (int i = 0; i < bytes; i++, t <<= 8) {
            res += is.read() * t;
        }
        return res;
    }
}
