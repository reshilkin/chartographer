package data;

import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class BmpIO {
    protected final byte[] buffer;
    protected final RandomAccessFile image;
    protected final long offset;
    protected final int width;
    protected final int height;
    protected final Rectangle fragment;
    protected int curX;
    protected int curY;

    protected BmpIO(RandomAccessFile image, Shape imageShape, Rectangle fragment) throws IOException {
        buffer = new byte[(fragment.width * 24 + 31) / 32 * 4];
        this.image = image;
        this.width = imageShape.width;
        this.height = imageShape.height;
        this.offset = readOffset();
        this.fragment = fragment;
        curX = fragment.x * 3;
        curY = fragment.y;
        setPointer(curX, curY);
    }

    protected boolean checkLine() throws IOException {
        if (leftInFragmentRow() == 0) {
            curX = fragment.x * 3;
            curY++;
            if (curY == fragment.y + fragment.height) {
                return false;
            }
            setPointer(curX, curY);
        }
        return true;
    }

    protected void setPointer(int x, int y) throws IOException {
        if (x < 3 * width && y < height) {
            image.seek(offset + (24L * width + 31) / 32 * 4 * Math.max(y, 0) + Math.max(x, 0));
        }
    }

    private long readNum(int bytes) throws IOException {
        long res = 0;
        long t = 1;
        for (int i = 0; i < bytes; i++, t <<= 8) {
            res += image.readByte() * t;
        }
        return res;
    }

    protected long readOffset() throws IOException {
        image.seek(10);
        return readNum(4);
    }

    protected int leftInFragmentRow() {
        return (fragment.x + fragment.width) * 3 - curX;
    }

    protected void close() throws IOException {
        image.close();
    }
}
