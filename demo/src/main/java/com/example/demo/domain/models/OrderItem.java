package domain.models;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;


@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id @GeneratedValue Long id;
    @ManyToOne Order order;
    @ManyToOne Product product;
    Integer quantity;
    BigDecimal priceAtOrder;
}