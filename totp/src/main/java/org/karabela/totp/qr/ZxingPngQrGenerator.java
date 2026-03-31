package org.karabela.totp.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.karabela.totp.exceptions.QrGenerationException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * QR code generator that produces PNG images using the ZXing library.
 */
public final class ZxingPngQrGenerator implements QrGenerator {

    private final Writer writer;
    private final int imageSize;

    public ZxingPngQrGenerator() {
        this(new QRCodeWriter(), 350);
    }

    public ZxingPngQrGenerator(int imageSize) {
        this(new QRCodeWriter(), imageSize);
    }

    public ZxingPngQrGenerator(Writer writer, int imageSize) {
        this.writer = writer;
        this.imageSize = imageSize;
    }

    public int getImageSize() {
        return imageSize;
    }

    @Override
    public String getImageMimeType() {
        return "image/png";
    }

    @Override
    public byte[] generate(QrData data) throws QrGenerationException {
        try {
            BitMatrix bitMatrix = writer.encode(data.getUri(), BarcodeFormat.QR_CODE, imageSize, imageSize);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            return pngOutputStream.toByteArray();
        } catch (WriterException | IOException e) {
            throw new QrGenerationException("Failed to generate QR code. See nested exception.", e);
        }
    }
}
