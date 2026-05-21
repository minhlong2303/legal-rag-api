package com.ragapi.config;

import com.ragapi.entity.ConsultationRequest;
import com.ragapi.entity.ConsultationStatus;
import com.ragapi.service.ConsultationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class ConsultationScheduler {

    private final ConsultationService
            consultationService;

    /**
     * Check mỗi 10 giây
     */
    @Scheduled(fixedDelay = 10000)
    public void offerConsultationsScheduledTask() {

        try {

            log.debug(
                    "Running consultation scheduler..."
            );

            List<ConsultationRequest>
                    pendingRequests =
                    consultationService
                            .getPendingConsultationRequests();

            for (ConsultationRequest request
                    : pendingRequests) {

                try {

                    if (shouldOfferConsultation(
                            request
                    )) {

                        consultationService
                                .offerConsultation(
                                        request.getId()
                                );

                        log.info(
                                "Consultation offered: {}",
                                request.getId()
                        );
                    }

                } catch (Exception ex) {

                    log.error(
                            "Error processing request: {}",
                            request.getId(),
                            ex
                    );
                }
            }

        } catch (Exception ex) {

            log.error(
                    "Scheduler execution failed",
                    ex
            );
        }
    }

    /**
     * Check đủ điều kiện offer consultant
     */
    private boolean shouldOfferConsultation(
            ConsultationRequest request
    ) {

        if (request == null) {
            return false;
        }

        /**
         * Đã offer rồi
         */
        if (ConsultationStatus.OFFERED.equals(request.getStatus())) {
            return false;
        }

        /**
         * Sai trạng thái
         */
        if (!ConsultationStatus.PENDING_OFFER.equals(request.getStatus())) {

            return false;
        }

        /**
         * Chưa đủ 30 giây
         */
        long seconds =
                Duration.between(
                        request.getCreatedAt(),
                        LocalDateTime.now()
                ).getSeconds();

        return seconds >= 30;
    }
}

