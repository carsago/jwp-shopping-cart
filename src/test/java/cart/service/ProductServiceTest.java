package cart.service;

import static cart.fixture.ProductRequestFixture.PRODUCT_REQUEST_A;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import cart.dao.ProductDao;
import cart.dto.request.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @Nested
    @DisplayName("상품 저장할 때")
    class SaveProduct {

        @DisplayName("정상적으로 성공한다.")
        @Test
        void save_success() {
            //given
            long expected = 1L;
            when(productDao.save(any()))
                    .thenReturn(expected);
            //when
            Long actual = productService.save(PRODUCT_REQUEST_A);
            //then
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "123456789012345678901"})
        @DisplayName("상품 이름의 길이가 1자 이상 20자 이하가 아닌 경우 예외가 발생한다.")
        void save_fail_by_name_length(String wrongName) {
            //given
            ProductRequest request = new ProductRequest(wrongName, 1000, "imageUrL");
            //when && then
            assertThatThrownBy(() -> productService.save(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상품의 이름은 1자 이상, 20자 이하입니다.");
        }

        @ParameterizedTest
        @ValueSource(ints = {1050, 1150})
        @DisplayName("상품 가격이 100원 단위가 아닌경우 예외가 발생한다")
        void save_fail_by_unit_of_price(int wrongPrice) {
            //given
            ProductRequest request = new ProductRequest("name", wrongPrice, "imageUrL");
            //when && then
            assertThatThrownBy(() -> productService.save(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상품의 가격 단위는 100원 단위입니다.");
        }

        @ParameterizedTest
        @ValueSource(ints = {999, 0, -1000})
        @DisplayName("상품의 가격이 1000원 미만이면 예외가 발생한다.")
        void success_fail_by_range_of_price(int wrongPrice) {
            //given
            ProductRequest request = new ProductRequest("name", wrongPrice, "imageUrL");
            //when && then
            assertThatThrownBy(() -> productService.save(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상품의 최소 가격은 1000원 이상입니다.");
        }

    }

    @Nested
    @DisplayName("상품 수정할 때")
    class UpdateProduct {

        @DisplayName("정상적으로 수정한다.")
        @Test
        void update_success() {
            //when && then
            assertThatNoException().isThrownBy(() -> productService.updateById(1L, PRODUCT_REQUEST_A));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "123456789012345678901"})
        @DisplayName("상품 이름의 길이가 1자 이상 20자 이하가 아닌 경우 예외가 발생한다.")
        void update_fail_by_name_length(String wrongName) {
            //given
            ProductRequest request = new ProductRequest(wrongName, 1000, "imageUrL");
            //when && then
            assertThatThrownBy(() -> productService.updateById(1L, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상품의 이름은 1자 이상, 20자 이하입니다.");
        }

        @ParameterizedTest
        @ValueSource(ints = {1050, 1150})
        @DisplayName("상품 가격이 100원 단위가 아닌경우 예외가 발생한다")
        void update_fail_by_unit_of_price(int wrongPrice) {
            //given
            ProductRequest request = new ProductRequest("name", wrongPrice, "imageUrL");
            //when && then
            assertThatThrownBy(() -> productService.updateById(1L, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상품의 가격 단위는 100원 단위입니다.");
        }

        @ParameterizedTest
        @ValueSource(ints = {999, 0, -1000})
        @DisplayName("상품의 가격이 1000원 미만이면 예외가 발생한다.")
        void update_fail_by_range_of_price(int wrongPrice) {
            //given
            ProductRequest request = new ProductRequest("name", wrongPrice, "imageUrL");
            //when && then
            assertThatThrownBy(() -> productService.updateById(1L, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상품의 최소 가격은 1000원 이상입니다.");
        }
    }

    @Nested
    @DisplayName("상품 삭제할 때")
    class DeleteProduct {

        @Test
        @DisplayName("정상적으로 삭제한다.")
        void delete_success() {
            assertThatNoException().isThrownBy(() -> productService.deleteById(1L));
        }
    }
}
