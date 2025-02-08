package net.adam85w.ddd.boundedcontextcanvas.management.diagram;

import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.zip.Deflater;

@Component
public final class DiagramEncoder {

    public String encode(String decoded) {
        return new String(Base64.getUrlEncoder().encode(compress(decoded.getBytes())));
    }

    private byte[] compress(byte[] source) {
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        deflater.setInput(source);
        deflater.finish();

        byte[] buffer = new byte[2048];
        int compressedLength = deflater.deflate(buffer);
        byte[] result = new byte[compressedLength];
        System.arraycopy(buffer, 0, result, 0, compressedLength);
        return result;
    }
}
