import org.mapstruct.Mapper
import org.mapstruct.Mapping

data class Address(val street: String, val city: String, val municipality: String?, val zipCode: Int)
data class OrderItem(val name: String, val quantity: Int, val price: Double)
data class Customer(val name: String, val address: Address)
data class Order(val id: Int, val customer: Customer, val items: List<OrderItem>)
data class OrderDTO(val id: Int, val customerName: String, val street: String, val city: String, val municipality: String?, val zipCode: Int, val totalPrice: Double)

// AS-IS Mapping function
fun mapOrderToDTO(order: Order): OrderDTO {
    // add null check for order.items
    val totalPrice = order.items.sumOf { it.price * it.quantity }

    val municipality = order.customer.address.municipality ?: "N/A"
    return OrderDTO(
        id = order.id,
        customerName = order.customer.name,
        street = order.customer.address.street,
        city = order.customer.address.city,
        zipCode = order.customer.address.zipCode,
        municipality = municipality,
        totalPrice = totalPrice
    )
}

@Mapper
interface OrderMapper {
    @Mapping(target = "customerName", source = "customer.name")
    @Mapping(target = ".", source = "customer.address")
    @Mapping(target = "municipality", source = "customer.address.municipality", defaultValue = "N/A")
    @Mapping(target = "totalPrice", expression = "java(calculateTotalPrice(order))")
    fun orderToOrderDTO(order: Order?): OrderDTO?

    fun calculateTotalPrice(order: Order): Double {
        return order.items.sumOf { it.price * it.quantity }
    }
}
fun main() {
    println("Hello World!")
}

