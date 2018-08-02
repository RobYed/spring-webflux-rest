package de.robertdey.springwebfluxrest;

import de.robertdey.springwebfluxrest.domain.Category;
import de.robertdey.springwebfluxrest.domain.Vendor;
import de.robertdey.springwebfluxrest.repositories.CategoryRepository;
import de.robertdey.springwebfluxrest.repositories.VendorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    private final VendorRepository vendorRepository;

    @Override
    public void run(String... args) {
        System.out.println("now initializing data...");
        initCategories();
        initVendor();
    }

    private void initCategories() {
        categoryRepository
                .count()
                .filter(count -> count == 0)
                .subscribe(count -> categoryRepository.saveAll(getCategories()).subscribe());
    }

    private void initVendor() {
        vendorRepository
                .count()
                .filter(count -> count == 0)
                .subscribe(count -> vendorRepository.saveAll(getVendors()).subscribe());
    }

    private List<Category> getCategories() {
        List<Category> categories = new ArrayList<>(3);

        Category furniture = new Category();
        furniture.setDescription("Furniture");
        categories.add(furniture);

        Category tricks = new Category();
        tricks.setDescription("Magic Tricks");
        categories.add(tricks);

        Category ships = new Category();
        ships.setDescription("Space Ships");
        categories.add(ships);

        System.out.println("created the following categories:" + categories.toString());

        return categories;
    }

    private List<Vendor> getVendors() {
        List<Vendor> vendors = new ArrayList<>(2);

        Vendor parry = new Vendor();
        parry.setFirstName("Parry");
        parry.setLastName("Hotter");
        vendors.add(parry);

        Vendor san = new Vendor();
        san.setFirstName("San");
        san.setLastName("Holo");
        vendors.add(san);

        System.out.println("created the following vendors:" + vendors.toString());

        return vendors;
    }
}
