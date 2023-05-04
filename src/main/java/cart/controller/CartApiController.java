package cart.controller;

import cart.annotation.Login;
import cart.dto.request.CartProductRequest;
import cart.dto.request.LoginRequest;
import cart.dto.response.CartProductResponse;
import cart.service.CartProductService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartApiController {

    private final CartProductService cartProductService;

    public CartApiController(CartProductService cartProductService) {
        this.cartProductService = cartProductService;
    }

    @GetMapping
    public ResponseEntity<List<CartProductResponse>> findCartProductsByMember(@Login LoginRequest loginRequest) {
        List<CartProductResponse> cartProducts = cartProductService.findAllByMemberEmail(loginRequest.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(cartProducts);
    }

    @PostMapping
    public ResponseEntity<Void> addCartProduct(@Login LoginRequest loginRequest,
                                               @RequestBody CartProductRequest cartProductRequest) {
        cartProductService.save(loginRequest.getEmail(), cartProductRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{cartProductId}")
    public ResponseEntity<Void> deleteCartProduct(@Login LoginRequest loginRequest,
                                                  @PathVariable Long cartProductId) {
        cartProductService.deleteById(cartProductId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}