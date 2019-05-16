package by.training.info_system.command.manager;

import by.training.info_system.command.Command;
import by.training.info_system.command.client.RequestAttribute;
import by.training.info_system.command.client.RequestParameter;
import by.training.info_system.entity.Order;
import by.training.info_system.entity.status.OrderStatus;
import by.training.info_system.resource.message.RequestMessage;
import by.training.info_system.resource.page.JspPage;
import by.training.info_system.resource.page.PageEnum;
import by.training.info_system.resource.page.PageFactory;
import by.training.info_system.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExtendUserOrderCommand extends Command {
    @Override
    public JspPage execute(final HttpServletRequest request,
                           final HttpServletResponse response) {
        JspPage page = PageFactory.defineAndGet(PageEnum.ORDERS);

        Long orderId = Long.valueOf(request.getParameter("extend"));
        OrderService service = factory.getService(OrderService.class).orElseThrow();
        Order order = service.findOrderById(orderId).orElseThrow();
        order.setReturnDate(order.getReturnDate().plusDays(1L));
        order.setStatus(OrderStatus.EXTENDED);
        boolean isUpdated = service.updateOrder(order);
        appendTimeParam(page);
        String pageNum = findCurrentPage(request);
        appendRequestParameterWithoutEncoding(page, RequestParameter.PAGE, pageNum);
        if (isUpdated) {
            appendRequestParameter(page, RequestParameter.ATTRIBUTE,
                    RequestAttribute.INFO.toString());
            appendRequestParameter(page, RequestParameter.ORDER_ID,
                    orderId.toString());
            appendRequestParameter(page, RequestParameter.MESSAGE,
                    RequestMessage.EXTENDED_ORDER);
        } else {
            appendRequestParameter(page, RequestParameter.ATTRIBUTE,
                    RequestAttribute.INCORRECT_DATA.toString());
            appendRequestParameter(page, RequestParameter.MESSAGE,
                    RequestMessage.EXTEND_GOES_WRONG);
        }
        return page;
    }
}