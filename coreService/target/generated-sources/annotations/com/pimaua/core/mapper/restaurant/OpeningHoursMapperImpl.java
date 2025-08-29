package com.pimaua.core.mapper.restaurant;

import com.pimaua.core.dto.restaurant.OpeningHoursRequestDto;
import com.pimaua.core.dto.restaurant.OpeningHoursResponseDto;
import com.pimaua.core.entity.restaurant.OpeningHours;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-29T12:41:27+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class OpeningHoursMapperImpl implements OpeningHoursMapper {

    @Override
    public OpeningHours toEntity(OpeningHoursRequestDto openingHoursRequestDto) {
        if ( openingHoursRequestDto == null ) {
            return null;
        }

        OpeningHours.OpeningHoursBuilder openingHours = OpeningHours.builder();

        openingHours.dayOfWeek( openingHoursRequestDto.getDayOfWeek() );
        openingHours.opensAt( openingHoursRequestDto.getOpensAt() );
        openingHours.closesAt( openingHoursRequestDto.getClosesAt() );
        openingHours.is24Hours( openingHoursRequestDto.getIs24Hours() );

        return openingHours.build();
    }

    @Override
    public OpeningHoursResponseDto toDto(OpeningHours openingHours) {
        if ( openingHours == null ) {
            return null;
        }

        OpeningHoursResponseDto.OpeningHoursResponseDtoBuilder openingHoursResponseDto = OpeningHoursResponseDto.builder();

        openingHoursResponseDto.id( openingHours.getId() );
        openingHoursResponseDto.dayOfWeek( openingHours.getDayOfWeek() );
        openingHoursResponseDto.opensAt( openingHours.getOpensAt() );
        openingHoursResponseDto.closesAt( openingHours.getClosesAt() );
        openingHoursResponseDto.is24Hours( openingHours.getIs24Hours() );
        openingHoursResponseDto.updatedAt( openingHours.getUpdatedAt() );

        return openingHoursResponseDto.build();
    }

    @Override
    public void updateEntity(OpeningHours openingHours, OpeningHoursRequestDto openingHoursRequestDto) {
        if ( openingHoursRequestDto == null ) {
            return;
        }

        if ( openingHoursRequestDto.getDayOfWeek() != null ) {
            openingHours.setDayOfWeek( openingHoursRequestDto.getDayOfWeek() );
        }
        if ( openingHoursRequestDto.getOpensAt() != null ) {
            openingHours.setOpensAt( openingHoursRequestDto.getOpensAt() );
        }
        if ( openingHoursRequestDto.getClosesAt() != null ) {
            openingHours.setClosesAt( openingHoursRequestDto.getClosesAt() );
        }
        if ( openingHoursRequestDto.getIs24Hours() != null ) {
            openingHours.setIs24Hours( openingHoursRequestDto.getIs24Hours() );
        }
    }

    @Override
    public List<OpeningHoursResponseDto> toListDto(List<OpeningHours> openingHoursList) {
        if ( openingHoursList == null ) {
            return null;
        }

        List<OpeningHoursResponseDto> list = new ArrayList<OpeningHoursResponseDto>( openingHoursList.size() );
        for ( OpeningHours openingHours : openingHoursList ) {
            list.add( toDto( openingHours ) );
        }

        return list;
    }
}
