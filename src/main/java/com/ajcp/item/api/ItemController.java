package com.ajcp.item.api;

import com.ajcp.item.model.entity.Item;
import com.ajcp.item.model.service.ItemService;
import com.ajcp.library.common.model.entity.Product;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RefreshScope
@RestController
@RequestMapping("${path.items}")
@AllArgsConstructor
public class ItemController {

    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    private Environment env;

    private final CircuitBreakerFactory cbFactory;

    //@Qualifier("feignService")
    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<?> getItems(@RequestParam(name = "name", required = false) String name,
                                      @RequestHeader(name = "token-request", required = false) String token) {
        log.info(String.format("[name]: %s", name));
        log.info(String.format("[token]: %s", token));
        return new ResponseEntity<List<Item>>(itemService.findAll(), HttpStatus.OK);
    }

    //@HystrixCommand(fallbackMethod = "alternativeContMethod")
    @GetMapping("/{id}/quantity/{quantity}")
    public ResponseEntity<?> getItem(@PathVariable Long id, @PathVariable Integer quantity) {
        return cbFactory.create("items")
                .run(() -> new ResponseEntity<Item>(itemService.findById(id, quantity), HttpStatus.OK),
                        e -> alternativeContMethod(id, quantity, e));
    }

    @CircuitBreaker(name = "items", fallbackMethod = "alternativeContMethod")
    @GetMapping("view/{id}/quantity/{quantity}")
    public ResponseEntity<?> getItemV2(@PathVariable Long id, @PathVariable Integer quantity) {
        return new ResponseEntity<Item>(itemService.findById(id, quantity), HttpStatus.OK);
    }

    @CircuitBreaker(name = "items", fallbackMethod = "alternativeContMethodV2")
    @TimeLimiter(name = "items")
    @GetMapping("view2/{id}/quantity/{quantity}")
    public CompletableFuture<ResponseEntity<?>> getItemV3(@PathVariable Long id, @PathVariable Integer quantity) {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<Item>(itemService.findById(id, quantity), HttpStatus.OK));
        // return ResponseEntity<Item>(itemService.findById(id, quantity), HttpStatus.OK);
    }

    public ResponseEntity<?> alternativeContMethod(Long id, Integer quantity, Throwable e) {
        log.info("[Error]: " + e.getMessage());
        log.error("[Error]: " + e.getMessage());
        Item item = new Item();
        Product product = new Product();

        item.setQuantity(quantity);
        product.setId(id);
        product.setName("Camara SONY");
        product.setPrice(new BigDecimal(500.00));
        item.setProduct(product);
        return new ResponseEntity<Item>(item, HttpStatus.OK);
    }

    public CompletableFuture<ResponseEntity<?>> alternativeContMethodV2(Long id, Integer quantity, Throwable e) {
        log.info("[Error]: " + e.getMessage());
        log.error("[Error]: " + e.getMessage());
        Item item = new Item();
        Product product = new Product();

        item.setQuantity(quantity);
        product.setId(id);
        product.setName("Camara SONY");
        product.setPrice(new BigDecimal(500.00));
        item.setProduct(product);
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<Item>(item, HttpStatus.OK));
    }

    @GetMapping("/get-configuration")
    public ResponseEntity<?> getConfiguration(@Value("${server.port}") String port,
                                              @Value("${configuration.text}") String text) {
        Map<String, Object> json = new HashMap<>();
        json.put("text", text);
        json.put("port", port);
        log.info("[text]: " + text);
        log.info("[port]: " + port);
        if (env.getActiveProfiles().length>0 && env.getActiveProfiles()[0].equals("dev")) {
            json.put("autor.nombre", env.getProperty("configuration.autor.nombre"));
            json.put("autor.email", env.getProperty("configuration.autor.email"));
        }
        return new ResponseEntity<Map<String, Object>>(json, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Product> registerProduct(@RequestBody Product product) {
        return new ResponseEntity<Product>(itemService.save(product), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, Product product) {
        return new ResponseEntity<Product>(itemService.update(id, product), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        itemService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
