package cohort46.gracebakeryapi.bakery.image.service;

import cohort46.gracebakeryapi.bakery.bakeryoptional.dto.BakeryoptionalDto;
import cohort46.gracebakeryapi.bakery.image.controller.ImageController;
import cohort46.gracebakeryapi.bakery.product.dao.ProductRepository;
import cohort46.gracebakeryapi.bakery.image.dao.ImageRepository;
import cohort46.gracebakeryapi.bakery.image.dto.ImageDto;
import cohort46.gracebakeryapi.exception.FailedDependencyException;
import cohort46.gracebakeryapi.exception.ImageNotFoundException;
import cohort46.gracebakeryapi.exception.ProductNotFoundException;
import cohort46.gracebakeryapi.exception.ResourceNotFoundException;
import cohort46.gracebakeryapi.bakery.image.model.Image;
import cohort46.gracebakeryapi.bakery.product.model.Product;
import cohort46.gracebakeryapi.helperclasses.GlobalVariables;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Stream;
import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private ImageController imageController;

    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public ImageDto addImage(MultipartFile file, Long product_id) {
        Image image = new Image();
        if(product_id == 0) {
            image.setProduct(null);
        }
        else
        {
            image.setProduct(productRepository.findById(product_id).orElseThrow(() -> new ImageNotFoundException(product_id)));
        }
        image.setId(null);
        image.setImage(pushImageFile(file));
        image = imageRepository.save(image);
        if(image != null) {
            return modelMapper.map(image, ImageDto.class);
        }
        return null;
    }

    @Override
    public ImageDto findImageById(Long imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow(() -> new ImageNotFoundException(   imageId   ));
        return modelMapper.map(image, ImageDto.class);
    }

    @Transactional
    @Override
    public ImageDto deleteImage(Long imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow(() -> new ImageNotFoundException(   imageId   ));
        String temp = image.getImage();
        imageRepository.delete(image);
        deleteImageFile(temp);
        return modelMapper.map(image, ImageDto.class);
    }

    @Transactional
    @Override
    public ImageDto updateImage(MultipartFile file, Long image_id, Long product_id) {
        Image image = imageRepository.findById(image_id).orElseThrow(() -> new ImageNotFoundException( image_id  ));
        if (product_id != 0) {
            image.setProduct(productRepository.findById(product_id).orElseThrow(() -> new ProductNotFoundException( product_id  )));
        }
        if(!(file.isEmpty())){
            String temp = image.getImage();
            image.setImage(pushImageFile(file));
            deleteImageFile(temp);
        }
        return modelMapper.map(imageRepository.save(image), ImageDto.class);
    }

    @Transactional
    @Override
    public Image store(Image image) {
        return imageRepository.saveAndFlush(image);
    }

    @Transactional(readOnly = true)
    @Override()
    public Iterable<ImageDto> findByProductId(Long product_id) {
        productRepository.findById(product_id).orElseThrow(() -> new ProductNotFoundException(   product_id)   );
        return imageRepository.findByProductId( product_id, Sort.by("id") ).map(b -> modelMapper.map(b, ImageDto.class)).toList();
    }

    //@Transactional
    @Override
    public String pushImageFile(MultipartFile file) {
        String fileName = String.valueOf(System.currentTimeMillis())
                + String.valueOf(System.nanoTime())
                + String.valueOf(new Random().nextInt(1000) + 1000);
        String filePath = GlobalVariables.getImagesPath() + fileName + "." + FilenameUtils.getExtension(file.getOriginalFilename());

        try {
            // Сохраняем файл на сервере
            file.transferTo(new File(filePath))   ;
        } catch (IOException e) {
            throw new FailedDependencyException("file is not saved");
        }
        return filePath;
    }

    @Override
    public Boolean deleteImageFile(String name) {
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

    @Transactional
    @Override
    public Boolean updateImageFile(MultipartFile file, String name) {
        Path path = Paths.get(name);
        try {
            // Проверяем, существует ли файл
            if (Files.exists(path)) {
                Files.delete(path);

                try {
                    // Сохраняем файл на сервере
                    file.transferTo(new File(String.valueOf(path)))   ;
                } catch (IOException e) {
                    throw new FailedDependencyException("file is not saved");
                }

                System.out.println("The file:  " +  path + "is updated");
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

    @Transactional
    @Override
    public String updateImageFileLink(String newLink, String lastLink) {
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