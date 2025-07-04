package com.newsfeed.listener.service;

import common.dao.InformationDao;
import common.repository.InformationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.newsfeed.listener.utils.HtmlCleaner;

import common.dto.InformationDto;
import reactor.core.publisher.Mono;

@Service
public class StoreInfoService {

    private final InformationRepository informationRepository;
    private final DtoToDaoMapper infoMapper;

    private static final Logger logger = LoggerFactory.getLogger(StoreInfoService.class);

    public StoreInfoService(@Qualifier("redisInformationRepository") InformationRepository informationRepository, DtoToDaoMapper infoMapper) {
        this.informationRepository = informationRepository;
        this.infoMapper = infoMapper;
    }

    @Async
    public void storeInfoAsync(InformationDto info) {
        try {
            logger.info("[*] Processing info id: {}", info.getInfoId());

            InformationDao dao = this.infoMapper.toDao(info);

            informationRepository.save(dao)
                .doOnSuccess(v -> logger.info("Saved info id: {}", dao.getInfoId()))
                .doOnError(e -> logger.error("Error saving info id: {}", dao.getInfoId(), e))
                .then(Mono.fromRunnable(() -> {
                    sendNewInfoMsg(info);
                    String cleanedContent = HtmlCleaner.cleanHtml(info.getContent());
                    sendDigTopicAction(info.getInfoId(), info.getTitle() + " " + cleanedContent);
                }));

        } catch (Exception e) {
            logger.error("Error processing info", e);
        }
    }

    private void sendNewInfoMsg(InformationDto info) {
        System.out.println("[>] New Info Msg Sent: " + info.getInfoId());
    }

    private void sendDigTopicAction(String infoId, String content) {
        System.out.println("[>] Dig Topic Action: " + infoId + " - " + content);
    }
}
