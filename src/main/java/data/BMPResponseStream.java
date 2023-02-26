package data;

import httpserver.ResponseStream;

import java.io.*;

public class BMPResponseStream extends ResponseStream {
    private final BmpInput image;

    public BMPResponseStream(File file, Shape imageShape, Rectangle fragment) throws IOException {
        this.image = new BmpInput(new RandomAccessFile(file, "r"), imageShape, fragment);
    }

    @Override
    public int read() throws IOException {
        return image.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return image.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return image.read(b, off, len);
    }

    @Override
    public void close() throws IOException {
        image.close();
        super.close();
    }

    @Override
    public long responseLength() {
        return image.size();
    }
}
