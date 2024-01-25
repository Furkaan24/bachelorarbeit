package de.gfai.mobile.data.qrc;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Daniel
 */
// Tutorial: http://zxing.org/w/docs/javadoc/index.html
public class QRCodeGenerator
{
  private QRCodeGenerator()
  {
  }

  public static BufferedImage createBufferedImage(String qrCodeData, int qrsize) throws UnsupportedEncodingException, WriterException
  {
    String contents = createContents(qrCodeData);
    BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, qrsize, qrsize, createHints());
    return MatrixToImageWriter.toBufferedImage(bitMatrix);
  }

  private static String createContents(String qrCodeData) throws UnsupportedEncodingException
  {
    String charset = "UTF-8";
    return new String(qrCodeData.getBytes(charset), charset);
  }

  private static Map<EncodeHintType, Object> createHints()
  {
    Map<EncodeHintType, Object> hints = new HashMap<>(2);
    hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
    hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
    hints.put(EncodeHintType.MARGIN, 0);
    return hints;
  }

}
