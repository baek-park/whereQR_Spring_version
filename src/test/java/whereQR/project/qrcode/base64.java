package whereQR.project.qrcode;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class base64 {

    private static String inputFilePath = "test.png";
    private static String outputFilePath = "test.png";

    @Test
    public void fileToBase64StringConversion() throws IOException {

        //given
        ClassLoader classLoader = getClass().getClassLoader();
        File inputFile = new File(classLoader.getResource(inputFilePath).getFile());

        //image to byte -> encoding
        byte[] fileContent = FileUtils.readFileToByteArray(inputFile);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);

        //when
        //encoded string to byte -> decoding
        File outputFile = inputFile;
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        FileUtils.writeByteArrayToFile(outputFile, decodedBytes);

        //then
        Assertions.assertTrue(FileUtils.contentEquals(inputFile, outputFile));

    }
}
