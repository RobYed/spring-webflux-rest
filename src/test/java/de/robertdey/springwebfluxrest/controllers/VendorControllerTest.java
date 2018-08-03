package de.robertdey.springwebfluxrest.controllers;

import de.robertdey.springwebfluxrest.domain.Vendor;
import de.robertdey.springwebfluxrest.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

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

        webTestClient.get().uri(VendorController.API_PATH + "/")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void getById() {
        BDDMockito.given(vendorRepository.findById("1234"))
                .willReturn(Mono.just(Vendor.builder().firstName("Susi").lastName("Strolch").build()));

        webTestClient.get().uri(VendorController.API_PATH + "/1234")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    public void create() {
        BDDMockito.given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendorToSaveMono = Mono.just(Vendor.builder().firstName("Bob").lastName("Sinclair").build());

        webTestClient.post().uri(VendorController.API_PATH + "/")
                .body(vendorToSaveMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void update() {
        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToSaveMono = Mono.just(Vendor.builder().firstName("Bob").lastName("Sinclair").build());

        webTestClient.put().uri(VendorController.API_PATH + "/1234")
                .body(vendorToSaveMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
}