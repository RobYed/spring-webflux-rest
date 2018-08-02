package de.robertdey.springwebfluxrest.controllers;

import de.robertdey.springwebfluxrest.domain.Vendor;
import de.robertdey.springwebfluxrest.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class VendorControllerTest {

    private VendorRepository vendorRepository;
    private VendorController vendorController;
    private WebTestClient webTestClient;

    @Before
    public void setUp() {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    public void list() {
        BDDMockito.given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("Tom").lastName("Cherry").build(),
                        Vendor.builder().firstName("Tim").lastName("Struppi").build()));

        webTestClient.get().uri("/api/v1/vendors/")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void getById() {
        BDDMockito.given(vendorRepository.findById("1234"))
                .willReturn(Mono.just(Vendor.builder().firstName("Susi").lastName("Strolch").build()));

        webTestClient.get().uri("/api/v1/vendors/1234")
                .exchange()
                .expectBody(Vendor.class);
    }
}