package com.newsfeed.listener.service;

import org.springframework.stereotype.Component;
import common.dao.InformationDao;
import common.dao.News;
import common.dao.Story;
import common.dto.InformationDto;
import common.dto.RawNews;
import common.dto.RawStory;

@Component
public class DtoToDaoMapper {

    public InformationDao toDao(InformationDto dto) {
        if (dto instanceof RawNews) {
            RawNews newsDto = (RawNews) dto;
            return new News(
                newsDto.getTitle(),
                newsDto.getInfoId(),
                newsDto.getContent(),
                newsDto.getSource(),
                newsDto.getDt(),
                newsDto.getLink(),
                newsDto.getCat()
            );
        } else if (dto instanceof RawStory) {
            RawStory storyDto = (RawStory) dto;
            return new Story(
                storyDto.getTitle(),
                storyDto.getInfoId(),
                storyDto.getContent(),
                storyDto.getSource(),
                storyDto.getLink(),
                storyDto.getCat()
            );
        }
        throw new IllegalArgumentException("Unsupported DTO type: " + dto.getClass());
    }
}
