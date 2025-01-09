package cohort46.gracebakeryapi;

import cohort46.gracebakeryapi.helperclasses.GlobalVariables;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class GraceBakeryApiApplication {
	private static boolean Init() {
		Path path = Paths.get(GlobalVariables.getImagesPath());
		try {
			Files.createDirectories(path);
			System.out.println("folder for images is created in: " + path);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("error: " + e.getMessage());
			return false;
		}
	};

	public static void main(String[] args) {
		if(Init()) {
			SpringApplication.run(GraceBakeryApiApplication.class, args);
		}
	}

}
