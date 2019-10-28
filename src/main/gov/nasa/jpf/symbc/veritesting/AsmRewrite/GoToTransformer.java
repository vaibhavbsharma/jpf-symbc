package GoToTransformation;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class GoToTransformer {


    public static void execute() throws IOException {

        File folder = new File("resources/old");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String currentName = listOfFiles[i].getName();
                if (currentName.contains("class")) {
                    Path pathRead = FileSystems.getDefault().getPath("resources/old/", currentName);

                    Path pathWrite = FileSystems.getDefault().getPath("resources/new", currentName);


                    byte[] classByteRead = Files.readAllBytes(pathRead);

                    byte[] classByteWrite = CollectGoTo.execute(classByteRead);


                    Files.write(pathWrite, classByteWrite);
                }
            }
        }

    }

    public static void main(String[] args) throws IOException {
        GoToTransformer.execute();
    }
}
