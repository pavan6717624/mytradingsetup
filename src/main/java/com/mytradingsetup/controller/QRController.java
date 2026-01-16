package com.mytradingsetup.controller;

import java.io.ByteArrayOutputStream;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "QR")
public class QRController {

	@RequestMapping(value = "demo")
	public String demo() throws Exception {
		
		
		
		String html="<table border=1>";

		for (int i = 0; i < 6; i++) {
			html+="<tr>";
			for (int j = 0; j < 5; j++) {
				QRCodeWriter qrCodeWriter = new QRCodeWriter();
				
				String text="SHOP-"+(10000+(i*6)+j);

				BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 120, 120);

				ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
				MatrixToImageConfig con = new MatrixToImageConfig(MatrixToImageConfig.BLACK, MatrixToImageConfig.WHITE);

				MatrixToImageWriter.writeToStream(bitMatrix, "JPEG", pngOutputStream, con);
				byte[] pngData = pngOutputStream.toByteArray();
				// return pngData;

				String image = new String(Base64.encodeBase64(pngData), "UTF-8");
				html+="<td style='padding:5px'><center>"+"<img  src='data:image/jpeg;base64," + image + "' /><br/>"+text+"</center></td>";
			}
			html+="</tr>";
		}
		html+="</table>";
		return html;
	}
}
