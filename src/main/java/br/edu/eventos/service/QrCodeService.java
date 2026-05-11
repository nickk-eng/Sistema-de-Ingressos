package br.edu.eventos.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.OutputStream;

public class QrCodeService {
    public void escreverPng(String conteudo, OutputStream outputStream) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(conteudo, BarcodeFormat.QR_CODE, 260, 260);
            MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);
        } catch (Exception ex) {
            throw new IllegalStateException("Nao foi possivel gerar o QR Code.", ex);
        }
    }
}
