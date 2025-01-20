package cohort46.gracebakeryapi.helperclasses;

import lombok.Getter;
import org.modelmapper.internal.util.Lists;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class GlobalVariables {
    private static final GlobalVariables instance = new GlobalVariables();

    private GlobalVariables() {
        //statusList.add(new OrderStatus(OrdersStatusEnum.Cart, "Cart", "Карзина"));
    };

    @Getter  //src\main\resources\static
    //private static final String ImagesPath = System.getProperty("user.dir") + File.separator + "resources" + File.separator + "images" + File.separator;
    private static final String ImagesPath = "/" + File.separator + "home"+ File.separator + "images" + File.separator;
    @Getter
    private static final List<OrderStatus> statusList = List.of(
            new OrderStatus(OrdersStatusEnum.Cart, "Cart", "Карзина"),
            new OrderStatus(OrdersStatusEnum.Created, "Created", "Создан"),
            new OrderStatus(OrdersStatusEnum.CanceledByUser, "Canceled by User", "Отменен пользователем"),
            new OrderStatus(OrdersStatusEnum.Viewed, "Viewed", "Просмотрен"),
            new OrderStatus(OrdersStatusEnum.CanceledByAdmin, "Canceled by Administrator", "Отменен администратором"),
            new OrderStatus(OrdersStatusEnum.Agreed, "Has been agreed", "Согласован"),
            new OrderStatus(OrdersStatusEnum.Payed, "Payed", "Оплачен"),
            new OrderStatus(OrdersStatusEnum.InWork, "In work", "В работе"),
            new OrderStatus(OrdersStatusEnum.Ready, "Ready", "Готов"),
            new OrderStatus(OrdersStatusEnum.InDelivery, "In delivery", "В доставке"),
            new OrderStatus(OrdersStatusEnum.HandOver, "Hand over", "Вручен"),
            new OrderStatus(OrdersStatusEnum.HasProblem, "Has a problem", "Есть проблема"),
            new OrderStatus(OrdersStatusEnum.ProblemSolved, "problem is solved", "Проблема решена"),
            new OrderStatus(OrdersStatusEnum.Finished, "Finished", "Завершен")
    );

}
