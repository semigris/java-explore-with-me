package ru.practicum.explorewithme.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {

    /** Идентификаторы запросов на участие в событии текущего пользователя */
    private List<Long> requestIds;

    /** Новый статус запроса на участие в событии текущего пользователя */
    private String status;
}