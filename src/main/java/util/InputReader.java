package util;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class InputReader {

    public static List<String> readInputFile(String filepath) {
        List<String> lines = Collections.emptyList();
        try {
            Path resourcePath = Paths.get(ClassLoader.getSystemResource(filepath).toURI());
            lines = Files.readAllLines(Paths.get(resourcePath.toUri()), StandardCharsets.UTF_8);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static List<String> readInputFileChunks(String filepath) {
        return readInputFile(filepath)
                .stream()
                .collect(CustomCollectors.collectChunks(StringUtils::isBlank));
    }
}
