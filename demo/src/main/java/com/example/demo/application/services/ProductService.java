package application.services;

import application.dto.CreateProductDto;
import domain.models.Product;
import domain.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepo;

    public ProductService(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    public Product create(CreateProductDto dto) {
        Product product = Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .build();

        return productRepo.save(product);
    }

    public List<Product> listAll() {
        return productRepo.findAll();
    }
}
