package cohort46.gracebakeryapi.helperclasses;

import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@NoArgsConstructor
public class updateFileLink {

    public static Boolean deleteImageFile(String name) {
        Path path = Paths.get(name);
        try {
            // Проверяем, существует ли файл
            if (Files.exists(path)) {
                Files.delete(path);
                System.out.println("The file:  " +  path + "is deleted");
                return true;
            } else {
                System.out.println("The file:  " +  path + "is not exist");
                return false;
            }
        } catch (IOException e) {
            System.out.println("error, the file: " +  path + "is not deleted...  " + e.getMessage());
            return false;
        }
    }

    public static String update(String newLink, String lastLink) {
        Path newpath = Paths.get(newLink);
        if ( (!newLink.equals(lastLink)) && (Files.exists(newpath)) ) {
            deleteImageFile(lastLink);
            return newLink;
        }
        else {
            return lastLink;
        }
    }
}
