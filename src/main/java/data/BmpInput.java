package data;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class BmpInput extends BmpIO {
    private final byte[] ansHeaders;
    private int ansHeadersPos;
    private int rowPos;

    public BmpInput(RandomAccessFile image, Shape imageShape, Rectangle fragment) throws IOException {
        super(image, imageShape, fragment);
        ansHeaders = BMP.createHeaders(fragment);
        ansHeadersPos = 0;
        forcedReadRow();
    }

    public int read() throws IOException {
        if (ansHeadersPos < ansHeaders.length) {
            return ansHeaders[ansHeadersPos++];
        }
        readRow();
        return buffer[rowPos++];
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int resLen;
        len = Math.min(len, b.length - off);
        if (ansHeadersPos < ansHeaders.length) {
            resLen = Math.min(ansHeaders.length - ansHeadersPos, len);
            System.arraycopy(ansHeaders, ansHeadersPos, b, off, resLen);
            ansHeadersPos += resLen;
        } else {
            readRow();
            resLen = Math.min(buffer.length - rowPos, len);
            System.arraycopy(buffer, rowPos, b, off, resLen);
            rowPos += resLen;
        }
        return resLen;
    }

    private void forcedReadRow() throws IOException {
        rowPos = buffer.length;
        readRow();
    }

    private void readRow() throws IOException {
        if (rowPos != buffer.length) {
            return;
        }
        int last = 0;
        while (last != 3 * fragment.width) {
            int resLen = 0;
            if (curX < 0) {
                resLen = -curX;
            } else if (curY < 0 || curY >= height || curX == 3 * width) {
                resLen = 3 * fragment.width - last;
            } else if (curX < 3 * width) {
                resLen = Math.min(leftInFragmentRow(), width * 3 - curX);
            }
            if (curX < 0 || curY < 0 || curY >= height || curX == 3 * width) {
                Arrays.fill(buffer, last, last + resLen, (byte) 0);
            } else if (curX < 3 * width) {
                image.read(buffer, last, resLen);
            }
            last += resLen;
            curX += resLen;
            Arrays.fill(buffer, last, buffer.length, (byte) 0);
        }
        checkLine();
        rowPos = 0;
    }

    public long size() {
        return (long) buffer.length * fragment.height + ansHeaders.length;
    }
}
