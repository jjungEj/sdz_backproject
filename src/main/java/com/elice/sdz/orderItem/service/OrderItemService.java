package com.elice.sdz.orderItem.service;

import com.elice.sdz.global.exception.CustomException;
import com.elice.sdz.global.exception.ErrorCode;
import com.elice.sdz.orderItem.dto.OrderItemDTO;
import com.elice.sdz.orderItem.entity.OrderItem;
import com.elice.sdz.orderItem.entity.OrderItemDetail;
import com.elice.sdz.orderItem.repository.OrderItemDetailRepository;
import com.elice.sdz.orderItem.repository.OrderItemRepository;
import com.elice.sdz.product.entity.Product;
import com.elice.sdz.product.repository.ProductRepository;
import com.elice.sdz.user.entity.Users;
import com.elice.sdz.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderItemDetailRepository orderItemDetailRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // 유저 조회 메서드
    private Users findUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    // 상품 조회 메서드
    private Product findByProductId(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    // 장바구니 조회 (DTO 반환)
    @Transactional
    public OrderItemDTO getOrderItems(String userId) {
        OrderItem orderItem = orderItemRepository.findByUserId(findUserById(userId))
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_ITEM_NOT_FOUND));

        return convertToDTO(orderItem);
    }

    // DTO 변환 메서드
    private OrderItemDTO convertToDTO(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setOrderItemId(orderItem.getId());

        List<OrderItemDTO.OrderItemDetailDTO> details = orderItem.getOrderItemDetails().stream()
                .map(detail -> {
                    OrderItemDTO.OrderItemDetailDTO detailDTO = new OrderItemDTO.OrderItemDetailDTO();
                    detailDTO.setProductId(detail.getProduct().getProductId());
                    detailDTO.setQuantity(detail.getQuantity());
                    detailDTO.setProductAmount(detail.getProductAmount());
                    return detailDTO;
                }).collect(Collectors.toList());

        orderItemDTO.setOrderItemDetails(details);
        return orderItemDTO;
    }

    // 장바구니 상품 추가
    @Transactional
    public void addOrderItem(String userId, Long productId, int quantity) {
        OrderItem orderItem = findOrCreateOrderItem(userId);
        Product addProduct = findByProductId(productId);

        Optional<OrderItemDetail> optionalOrderItemDetail = orderItemDetailRepository
                .findByOrderItemIdAndProduct(orderItem.getId(), addProduct);

        int currentQuantity = optionalOrderItemDetail.map(OrderItemDetail::getQuantity).orElse(0);
        if (currentQuantity + quantity > addProduct.getProductCount()) {
            throw new CustomException(ErrorCode.OUT_OF_STOCK); // 재고 초과 시 예외 발생
        }

        if (optionalOrderItemDetail.isPresent()) {
            // 동일한 물건이 있을 경우 수량 수정
            OrderItemDetail orderItemDetail = optionalOrderItemDetail.get();
            orderItemDetail.setQuantity(orderItemDetail.getQuantity() + quantity);
            orderItemDetailRepository.save(orderItemDetail);
            orderItem.setUpdatedAt(orderItemDetail.getUpdatedAt());
        } else {
            // 동일한 물건이 없을 경우 새로 추가
            OrderItemDetail orderItemDetail = new OrderItemDetail();
            orderItemDetail.setOrderItem(orderItem);
            orderItemDetail.setProduct(addProduct);
            orderItemDetail.setQuantity(quantity);
            orderItemDetailRepository.save(orderItemDetail);
            orderItem.setUpdatedAt(orderItemDetail.getUpdatedAt());
        }
    }

    // 장바구니 상품 삭제
    @Transactional
    public void deleteOrderItem(String userId, Long productId, int quantity) {
        OrderItem orderItem = orderItemRepository.findByUserId(findUserById(userId))
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_ITEM_NOT_FOUND));
        Product deldteProduct = findByProductId(productId);

        Optional<OrderItemDetail> optionalOrderItemDetail = orderItemDetailRepository
                .findByOrderItemIdAndProduct(orderItem.getId(), deldteProduct);

        if (optionalOrderItemDetail.isPresent()) {
            OrderItemDetail orderItemDetail = optionalOrderItemDetail.get();
            if (orderItemDetail.getQuantity() > quantity) {
                // 수량만 감소
                orderItemDetail.setQuantity(orderItemDetail.getQuantity() - quantity);
                orderItemDetailRepository.save(orderItemDetail);
            } else {
                // 수량이 일치하거나 적을 경우 완전히 삭제
                orderItem.getOrderItemDetails().remove(orderItemDetail); // 리스트에서 제거
                orderItemDetailRepository.delete(orderItemDetail); // DB에서 삭제
            }
            orderItem.setUpdatedAt(orderItemDetail.getUpdatedAt());
        }
    }

    // 장바구니 전체 삭제
    @Transactional
    public void clearOrderItems(String userId) {
        OrderItem orderItem = orderItemRepository.findByUserId(findUserById(userId))
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_ITEM_NOT_FOUND));
//        orderItemRepository.delete(orderItem);
        orderItem.getOrderItemDetails().clear();
        orderItemRepository.save(orderItem);
        orderItem.setUpdatedAt(LocalDateTime.now());
    }

    // 장바구니 생성 및 찾기
    private OrderItem findOrCreateOrderItem(String userId) {
        Optional<OrderItem> optionalOrderItem = orderItemRepository.findByUserId(findUserById(userId));
        if (optionalOrderItem.isEmpty()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setUserId(findUserById(userId));
            return orderItemRepository.save(orderItem);
        } else {
            return optionalOrderItem.get();
        }
    }
}
