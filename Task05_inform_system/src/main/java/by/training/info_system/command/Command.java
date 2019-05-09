package by.training.info_system.command;

import by.training.info_system.command.client.RequestAttribute;
import by.training.info_system.entity.Car;
import by.training.info_system.entity.Order;
import by.training.info_system.resource.page.JspPage;
import by.training.info_system.service.CarService;
import by.training.info_system.service.OrderService;
import by.training.info_system.service.ServiceFactory;
import by.training.info_system.validator.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public abstract class Command {

    ServiceFactory factory;


    void setFactory(ServiceFactory factory) {
        this.factory = factory;
    }

    public abstract JspPage execute(HttpServletRequest request, HttpServletResponse response);

    void putAttrInRequest(HttpServletRequest request, RequestAttribute attribute,
                          Object o) {
        request.setAttribute(attribute.getValue(), o);
    }

    void loadCars(HttpServletRequest request) {
        CarService service = factory.getService(CarService.class).orElseThrow();
        List<Car> cars = service.loadCars().orElseGet(ArrayList::new);
        putAttrInRequest(request, RequestAttribute.CARS, cars);
    }

    void loadOrders(HttpServletRequest request) {
        OrderService service = factory.getService(OrderService.class).orElseThrow();
        List<Order> orders = service.findAllOrders().orElseGet(ArrayList::new);
        if (!orders.isEmpty()) {
            putAttrInRequest(request, RequestAttribute.ORDERS, orders);
        } else {
            putAttrInRequest(request, RequestAttribute.INFO,
                    "Orders list is empty.");
        }
    }

    boolean validate(Validator validator, Object object) {
        return validator.validate(object);
    }
}
