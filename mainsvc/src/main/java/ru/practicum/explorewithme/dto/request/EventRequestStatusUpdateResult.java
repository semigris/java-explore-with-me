package ru.practicum.explorewithme.dto.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.dto.request.ParticipationRequestDto;

import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateResult {

    /** Список подтвержденных заявок */
    private List<ParticipationRequestDto> confirmedRequests;

    /** Список отклоненных заявок */
    private List<ParticipationRequestDto> rejectedRequests;
}

